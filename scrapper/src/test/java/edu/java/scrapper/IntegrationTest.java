package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends IntegrationEnvironment {

    @Test
    public void testContainer() {
        assertThat(POSTGRES.isCreated()).isTrue();
        assertThat(POSTGRES.isRunning()).isTrue();
    }

}
