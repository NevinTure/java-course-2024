package edu.java.scrapper;

import edu.java.scrapper.row_mappers.GitLinkRowMapper;
import edu.java.scrapper.row_mappers.TgChatRowMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest extends IntegrationEnvironment {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Autowired
    public IntegrationTest(JdbcTemplate jdbcTemplate,
        DataSource dataSource
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @Test
    public void testContainer() {
        assertThat(POSTGRES.isCreated()).isTrue();
        assertThat(POSTGRES.isRunning()).isTrue();
    }

    @Test
    public void test() throws SQLException {
//        DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
//        ResultSet set =metaData.getCatalogs();
//        System.out.println(set.next());
//        System.out.println(set.getString(2));
        System.out.println(jdbcTemplate.getDataSource().getConnection().getSchema());
        jdbcTemplate.update("insert into scrapper.public.tg_chat (id, state) values (1, 'DEFAULT');");
        jdbcTemplate.update("insert into scrapper.public.tg_chat (id, state) values (2, 'DEFAULT');");
        jdbcTemplate.update("insert into scrapper.public.tg_chat (id, state) values (3, 'DEFAULT');");
        jdbcTemplate.update("insert into git_link (tg_chat_id, url, last_check_at, last_update_at, last_push_at) " +
            "values (1, 'https://github.com/NevinTure/java-course-2024', '2012-08-24 14:00:00 +02:00', '2012-08-24 14:00:00 +02:00', '2012-08-24 14:00:00 +02:00')");
        jdbcTemplate.query("select git_link.id as link_id, url, tg_chat_id, last_check_at, last_update_at, last_push_at, state, tg_chat.id as chat_id from git_link inner join tg_chat on git_link.tg_chat_id = tg_chat.id where git_link.id = 1;", new GitLinkRowMapper());

    }
}
