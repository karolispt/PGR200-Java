package pgr200eksamen.http;

import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class TestHttpClient {
    @Test
    public void shouldReadRequest() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80, "/echo?status=200");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(200);
    }

    @Test
    public void shouldReadOtherStatusCodes() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80, "/echo?status=404");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(404);
    }

    @Test
    public void shouldReadResponseHeaders() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com", 80,
                "/echo?status=307&Location=http%3A%2F%2Fwww.google.com");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(307);
        assertThat(response.getHeader("Location")).isEqualTo("http://www.google.com");
    }

    @Test
    public void shouldReadResponseBody() throws IOException {
        HttpRequest request = new HttpRequest("urlecho.appspot.com",
                80, "/echo?body=Hello+world!");
        HttpResponse response = request.get();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Hello world!");
    }

    @Test
    public void shouldReadPost() throws IOException, SQLException {
        HttpRequest request = new HttpRequest("httpbin.org", 80, "/post",
                "name=CoolConference&yo=Bro");
        HttpResponse response = request.post();
        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(response.getBody()).contains("name=CoolConference&yo=Bro");
    }
}
