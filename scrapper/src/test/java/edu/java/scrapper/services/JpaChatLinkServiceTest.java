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
import edu.java.scrapper.repositories.jpa.JpaChatRepository;
import edu.java.scrapper.repositories.jpa.JpaLinkRepository;
import edu.java.scrapper.services.jpa.JpaChatLinkService;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest("app.database-access-type=jpa")
public class JpaChatLinkServiceTest extends IntegrationEnvironment {

    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;
    private final JpaChatLinkService service;

    @Autowired
    public JpaChatLinkServiceTest(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository,
        JpaChatLinkService service
    ) {
        this.chatRepository = chatRepository;
        this.linkRepository = linkRepository;
        this.service = service;
    }

    @Test
    @Transactional
    @Rollback
    public void testChatRegistration() {
        //given
        long id = 1L;

        //when
        service.register(id);

        //then
        assertThat(chatRepository.existsById(id)).isTrue();
    }

    @Test
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
    public void testUnregisterWithMissingChat() {
        //given
        long id = 4L;

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.unregister(id));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetLinksByChatId() {
        //given
        long chatId = 5L;
        URI url = URI.create("https://github.com/new/repo");
        Link link = new Link(url);
        linkRepository.save(link);

        //when
        TgChat chat = new TgChat(chatId);
        chat.getLinkList().add(link);
        chatRepository.save(chat);

        //then
        assertThat(service.getLinksById(chatId))
            .isEqualTo(new ResponseEntity<>(new ListLinksResponse(List.of(
                new LinkResponse(link.getId(), url))), HttpStatus.OK)
            );
    }

    @Test
    @Transactional
    @Rollback
    public void testGetLinksByChatIdWithMissingChat() {
        //given
        long id = 6L;

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.getLinksById(id));
    }

    @Test
    @Transactional
    @Rollback
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
        Optional<TgChat> foundChat = chatRepository.findById(chatId);
        assertThat(foundLink).isPresent();
        assertThat(foundChat).isPresent();
        assertThat(foundChat.get().getLinkList().contains(foundLink.get())).isTrue();
    }

    @Test
    @Transactional
    @Rollback
    public void testAddLinkWithMissingChat() {
        //given
        long id = 8L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://stackoverflow/questions/6");

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.addLink(id, addLinkRequest));
    }

    @Test
    @Transactional
    @Rollback
    public void testAddLinkWhenLinkAlreadyTracked() {
        //given
        long chatId = 9L;
        String urlStr = "https://stackoverflow/questions/7";
        Link link = new Link(URI.create(urlStr));
        linkRepository.save(link);
        AddLinkRequest request = new AddLinkRequest(urlStr);

        //when
        TgChat chat = new TgChat(chatId);
        chat.getLinkList().add(link);
        chatRepository.save(chat);

        //then
        assertThatExceptionOfType(LinkAlreadyTrackedException.class)
            .isThrownBy(() -> service.addLink(chatId, request));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLink() {
        //given
        long chatId = 10L;
        String urlStr = "https://stackoverflow/questions/8";
        RemoveLinkRequest request = new RemoveLinkRequest(urlStr);
        Link link = new Link(URI.create(urlStr));
        Link link1 = new Link(URI.create(urlStr + "4"));
        linkRepository.save(link);
        linkRepository.save(link1);
        TgChat chat = new TgChat(chatId);
        chat.getLinkList().add(link);
        chat.getLinkList().add(link1);
        chatRepository.save(chat);

        //when
        service.removeLink(chatId, request);

        //then
        assertThat(linkRepository.findByUrl(URI.create(urlStr))).isPresent();
        Optional<TgChat> foundChat = chatRepository.findById(chatId);
        assertThat(foundChat.get().getLinkList().contains(link)).isFalse();
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLinkWithMissingChat() {
        //given
        long id = 11L;
        RemoveLinkRequest request = new RemoveLinkRequest("https://stackoverflow/questions/9");

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.removeLink(id, request));
    }

    @Test
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
    public void testFindLinkFollowerIdsByLinkId() {
        //given
        long chatId1 = 13L;
        long chatId2 = 14L;
        Link link = new Link(URI.create("https://stackoverflow/questions/10"));
        linkRepository.save(link);
        TgChat chat1 = new TgChat(chatId1);
        chat1.getLinkList().add(link);
        chatRepository.save(chat1);
        TgChat chat2 = new TgChat(chatId2);
        chat2.getLinkList().add(link);
        chatRepository.save(chat2);

        //when
        List<Long> result = service.findLinkFollowerIdsByLinkId(link.getId());

        //then
        assertThat(result).containsExactly(chatId1, chatId2);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindLinkFollowerIdsByLinkIdWithMissingLink() {
        //given
        long id = 0;

        //then
        assertThatExceptionOfType(LinkNotFoundException.class)
            .isThrownBy(() -> service.findLinkFollowerIdsByLinkId(id));
    }

    @Test
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
    public void testGetChatByIdWithMissingChat() {
        //given
        long id = 16L;

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.getLinksById(id));
    }

    @Test
    @Transactional
    @Rollback
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
    @Transactional
    @Rollback
    public void testUpdateChatByIdWithMissingChat() {
        //given
        long id = 18L;
        TgChatDto dto = new TgChatDto(id, State.DEFAULT);

        //then
        assertThatExceptionOfType(ChatNotFoundException.class)
            .isThrownBy(() -> service.updateChatById(id, dto));
    }
}
