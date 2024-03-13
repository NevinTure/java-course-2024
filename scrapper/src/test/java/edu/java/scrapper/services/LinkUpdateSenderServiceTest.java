package edu.java.scrapper.services;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.scrapper.clients.bot_api.BotApiClient;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.utils.UpdateType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.net.URI;
import java.util.List;
import static org.mockito.ArgumentMatchers.eq;

public class LinkUpdateSenderServiceTest extends IntegrationEnvironment {

    @MockBean
    private BotApiClient client;
    @MockBean
    private ChatLinkService chatLinkService;
    @Autowired
    private LinkUpdateSenderService service;

    @Test
    public void testLinkUpdateSend() {
        //given
        long linkId = 1L;
        String urlStr = "https://github.com/new/repo20";
        Link link = new Link(linkId, URI.create(urlStr));
        UpdateType type = UpdateType.PUSH;
        List<Long> tgChatIds = List.of(1L,2L,3L);

        //when
        Mockito.when(chatLinkService.findLinkFollowerIdsByLinkId(linkId)).thenReturn(tgChatIds);
        service.sendUpdate(link, type);

        //then
        Mockito.verify(client).sendUpdate(eq(new LinkUpdateRequest(
            1,
            urlStr,
            "Новый коммит",
            tgChatIds
        )));
    }
}
