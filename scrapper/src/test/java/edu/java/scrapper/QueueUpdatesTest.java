package edu.java.scrapper;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.services.LinkUpdateSenderService;
import edu.java.scrapper.utils.UpdateType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.net.URI;

@SpringBootTest
@ActiveProfiles("primary")
public class QueueUpdatesTest {

    private final LinkUpdateSenderService service;

    @Autowired
    public QueueUpdatesTest(LinkUpdateSenderService service) {
        this.service = service;
    }

    @Test
    public void test() {
        service.sendUpdate(new Link(1L, URI.create("http://localhost")), UpdateType.UPDATE);
    }
}
