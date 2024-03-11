package edu.java.scrapper;

import edu.java.scrapper.model.Link;
import edu.java.scrapper.repositories.LinkRepository;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends IntegrationEnvironment {
    private final LinkRepository linkRepository;

    @Autowired
    public IntegrationTest(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Test
    public void testContainer() {
        assertThat(POSTGRES.isCreated()).isTrue();
        assertThat(POSTGRES.isRunning()).isTrue();
    }

    @Test
    public void test() {
//        DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
//        ResultSet set =metaData.getCatalogs();
//        System.out.println(set.next());
//        System.out.println(set.getString(2));
        System.out.println(linkRepository.save(new Link(URI.create("https://github/new/repo"))));
        System.out.println(linkRepository.save(new Link(URI.create("https://github/1/2"))));
        System.out.println(linkRepository.save(new Link(URI.create("https://github/3/4"))));
        System.out.println(linkRepository.save(new Link(URI.create("https://github/5/6"))));


    }
}
