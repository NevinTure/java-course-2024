package edu.java.scrapper;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends IntegrationEnvironment {

    @Test
    public void testContainer() {
        assertThat(POSTGRES.isCreated()).isTrue();
        assertThat(POSTGRES.isRunning()).isTrue();
    }

}
