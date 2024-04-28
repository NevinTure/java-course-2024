package edu.java.bot;

import edu.java.bot.services.LinkUpdateService;
import edu.java.models.dtos.LinkUpdateRequest;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;

@Order(-1)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class QueueUpdatesTest extends KafkaEnvironment {

    @SpyBean
    private LinkUpdateService linkUpdateService;

    @Test
    public void testListener() {
        //given
        LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            "http://localhost",
            "Обновление",
            List.of(1L)
        );

        //when
        producer.send(new ProducerRecord<>(TOPIC_NAME, request));

        //then
        Mockito.verify(linkUpdateService, Mockito.after(2000).times(1)).update(request);
    }

    @Test
    public void testSendToDlq() {
        //given
        LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            "http://localhost",
            "Обновление",
            List.of(1L)
        );

        //when
        Mockito.doThrow(DeserializationException.class).when(linkUpdateService).update(request);
        producer.send(new ProducerRecord<>(TOPIC_NAME, request));

        //then
        ConsumerRecord<String, LinkUpdateRequest> record =
            KafkaTestUtils.getSingleRecord(dlqConsumer, TOPIC_NAME + "_dlq");
        LinkUpdateRequest receivedRequest = record.value();
        assertThat(receivedRequest).isEqualTo(request);
    }

}
