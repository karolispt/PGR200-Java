package pgr200eksamen.http;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestServer {
    private static HttpServer server;

    @BeforeClass
    public static void startServer() throws IOException, SQLException {
        server = new HttpServer(0);
        server.setDataSource(createDataSource());
    }

    @Test
    public void shouldWriteStatusCode() {
        HttpRequest request = new HttpRequest("localhost", server.port(), "/echo?status=200");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void shouldReadOtherStatusCodes() {
        HttpRequest request = new HttpRequest("localhost", server.port(), "/echo?status=404");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldReadResponseHeaders() {
        HttpRequest request = new HttpRequest("localhost", server.port(),
                "/echo?status=307&Location=http%3A%2F%2Fwww.kristiania.no");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(307);
        assertThat(response.getHeader("Location")).isEqualTo("http://www.kristiania.no");
    }

    @Test
    public void shouldReadResponseBody() {
        HttpRequest request = new HttpRequest("localhost", server.port(),
                "/echo?body=Hello+world!");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hello world!");
    }

    @Test
    public void shouldNotCrashFromUnexpectedInput() {
        HttpRequest request = new HttpRequest("localhost", server.port(), "/nosuchpath");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldNotCrashFromUnexpectedInput2() {
        HttpRequest request = new HttpRequest("localhost", server.port(), "/discussion/notAnId");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }
    @Test
    public void shouldNotCrashFromUnexpectedInput3() {
        HttpRequest request = new HttpRequest("localhost", server.port(), "/discussion/14325632");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    public static DataSource createDataSource() {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        Flyway.configure().dataSource(ds).load().migrate();
        return ds;
    }
}
