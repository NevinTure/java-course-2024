package edu.java.scrapper;

import edu.java.models.dtos.LinkUpdateRequest;
import edu.java.scrapper.model.Link;
import edu.java.scrapper.services.ChatLinkService;
import edu.java.scrapper.services.LinkUpdateSenderService;
import edu.java.scrapper.utils.UpdateType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import java.net.URI;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("kafka")
public class QueueUpdatesTest extends KafkaEnvironment {

    @Autowired
    private LinkUpdateSenderService service;

    @MockBean
    private ChatLinkService chatLinkService;

    @Test
    public void testProducerSend() {
        //given
        long id = 1L;
        List<Long> tgChatIds = List.of(id);
        String urlStr = "http://localhost";

        //when
        Mockito.when(chatLinkService.findLinkFollowerIdsByLinkId(id)).thenReturn(tgChatIds);
        service.sendUpdate(new Link(id, URI.create(urlStr)), UpdateType.UPDATE);
        ConsumerRecord<String, LinkUpdateRequest> record =
            KafkaTestUtils.getSingleRecord(consumer, TOPIC_NAME);
        LinkUpdateRequest request = record.value();

        //then
        assertThat(request.getDescription()).isEqualTo("Обновление");
        assertThat(request.getUrl()).isEqualTo(urlStr);
    }
}
