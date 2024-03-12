package edu.java.scrapper.services;

import edu.java.models.dtos.AddLinkRequest;
import edu.java.models.dtos.LinkResponse;
import edu.java.models.dtos.ListLinksResponse;
import edu.java.models.dtos.RemoveLinkRequest;
import edu.java.models.dtos.TgChatDto;
import edu.java.models.utils.State;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.exceptions.ChatAlreadyRegisteredException;
import edu.java.scrapper.exceptions.ChatNotFoundException;
import edu.java.scrapper.exceptions.LinkAlreadyTrackedException;
import edu.java.scrapper.exceptions.LinkNotFoundException;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.model.TgChat;
import edu.java.scrapper.repositories.JdbcChatLinkRepository;
import edu.java.scrapper.repositories.JdbcChatRepository;
import edu.java.scrapper.repositories.JdbcLinkRepository;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class JdbcChatLinkServiceTest extends IntegrationEnvironment {

    private final JdbcChatLinkService service;
    private final JdbcChatRepository chatRepository;
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    @Autowired
    public JdbcChatLinkServiceTest(
        JdbcChatLinkService service,
        JdbcChatRepository chatRepository,
        JdbcLinkRepository linkRepository,
        JdbcChatLinkRepository chatLinkRepository
    ) {
        this.service = service;
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
    }

    @Test
    public void testChatRegistration() {
        //given
        long id = 1L;

        //when
        service.register(id);

        //then
        assertThat(chatRepository.existsById(id)).isTrue();
    }

    @Test
    public void testSecondChatRegistrationWithError() {
        //given
        long id = 2L;

        //when
        chatRepository.save(new TgChat(id));

        //then
        assertThatExceptionOfType(ChatAlreadyRegisteredException.class)
            .isThrownBy(() -> service.register(id));
    }

    @Test
    public void testUnregister() {
        //given
        long id = 3L;

        //when
        chatRepository.save(new TgChat(id));
        service.unregister(id);

        //then
        assertThat(chatRepository.existsById(id)).isFalse();
    }

    @Test
    public void testUnregisterWithMissingChat() {
        //given
        long id = 4L;

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.unregister(id));
    }

    @Test
    public void testGetLinksByChatId() {
        //given
        long chatId = 5L;
        URI url = URI.create("https://github.com/new/repo");
        long linkId = linkRepository.save(new Link(url));

        //when
        chatRepository.save(new TgChat(chatId));
        chatLinkRepository.addLink(chatId, linkId);

        //then
        assertThat(service.getLinksById(chatId))
            .isEqualTo(new ResponseEntity<>(new ListLinksResponse(List.of(
                new LinkResponse(linkId, url))), HttpStatus.OK)
            );
    }

    @Test
    public void testGetLinksByChatIdWithMissingChat() {
        //given
        long id = 6L;

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.getLinksById(id));
    }

    @Test
    public void testAddLink() {
        //given
        long chatId = 7L;
        String urlStr = "https://stackoverflow/questions/5";
        AddLinkRequest addLinkRequest = new AddLinkRequest(urlStr);

        //when
        chatRepository.save(new TgChat(chatId));
        service.addLink(chatId, addLinkRequest);

        //then
        Optional<Link> foundLink = linkRepository.findByUrl(URI.create(urlStr));
        assertThat(foundLink).isPresent();
        assertThat(chatLinkRepository.existsByChatAndLinkId(chatId, foundLink.get().getId())).isTrue();
    }

    @Test
    public void testAddLinkWithMissingChat() {
        //given
        long id = 8L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://stackoverflow/questions/6");

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.addLink(id, addLinkRequest));
    }

    @Test
    public void testAddLinkWhenLinkAlreadyTracked() {
        //given
        long chatId = 9L;
        String urlStr = "https://stackoverflow/questions/7";
        long linkId = linkRepository.save(new Link(URI.create(urlStr)));
        AddLinkRequest request = new AddLinkRequest(urlStr);

        //when
        chatRepository.save(new TgChat(chatId));
        chatLinkRepository.addLink(chatId, linkId);

        //then
        assertThatExceptionOfType(LinkAlreadyTrackedException.class)
            .isThrownBy(() -> service.addLink(chatId, request));
    }

    @Test
    public void testRemoveLink() {
        //given
        long chatId = 10L;
        String urlStr = "https://stackoverflow/questions/8";
        RemoveLinkRequest request = new RemoveLinkRequest(urlStr);
        long linkId = linkRepository.save(new Link(URI.create(urlStr)));
        chatRepository.save(new TgChat(chatId));
        chatLinkRepository.addLink(chatId, linkId);

        //when
        service.removeLink(chatId, request);

        //then
        assertThat(linkRepository.findByUrl(URI.create(urlStr))).isPresent();
        assertThat(chatLinkRepository.existsByChatAndLinkId(chatId, linkId)).isFalse();
    }

    @Test
    public void testRemoveLinkWithMissingChat() {
        //given
        long id = 11L;
        RemoveLinkRequest request = new RemoveLinkRequest("https://stackoverflow/questions/9");

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.removeLink(id, request));
    }

    @Test
    public void testRemoveLinkWhenLinkNotFound() {
        //given
        long id = 12L;
        RemoveLinkRequest request = new RemoveLinkRequest("https://stackoverflow/questions/10");
        chatRepository.save(new TgChat(id));

        //then
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> service.removeLink(id, request));
    }

    @Test
    public void testFindLinkFollowerIdsByLinkId() {
        //given
        long chatId1 = 13L;
        long chatId2 = 14L;
        long linkId = linkRepository.save(new Link(URI.create("https://stackoverflow/questions/10")));
        chatRepository.save(new TgChat(chatId1));
        chatRepository.save(new TgChat(chatId2));
        chatLinkRepository.addLink(chatId1, linkId);
        chatLinkRepository.addLink(chatId2, linkId);

        //when
        List<Long> result = service.findLinkFollowerIdsByLinkId(linkId);

        //then
        assertThat(result).containsExactly(chatId1, chatId2);
    }

    @Test
    public void testFindLinkFollowerIdsByLinkIdWithMissingLink() {
        //given
        long id = 0;

        //then
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> service.findLinkFollowerIdsByLinkId(id));
    }

    @Test
    public void testGetChatById() {
        //given
        long id = 15L;
        chatRepository.save(new TgChat(id));

        //when
        ResponseEntity<TgChatDto> result = service.getChatById(id);

        //then
        assertThat(result).isEqualTo(new ResponseEntity<>(new TgChatDto(id, State.DEFAULT), HttpStatus.OK));
    }

    @Test
    public void testGetChatByIdWithMissingChat() {
        //given
        long id = 16L;

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.getLinksById(id));
    }

    @Test
    public void testUpdateChatById() {
        //given
        long id = 17L;
        TgChatDto dto = new TgChatDto(id, State.WAITING_TRACK);
        chatRepository.save(new TgChat(id));

        //when
        service.updateChatById(id, dto);

        //then
        assertThat(chatRepository.findById(id)).isPresent();
        assertThat(chatRepository.findById(id).get().getState()).isEqualTo(State.WAITING_TRACK);
    }

    @Test
    public void testUpdateChatByIdWithMissingChat() {
        //given
        long id = 18L;
        TgChatDto dto = new TgChatDto(id, State.DEFAULT);

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.updateChatById(id, dto));
    }
}
