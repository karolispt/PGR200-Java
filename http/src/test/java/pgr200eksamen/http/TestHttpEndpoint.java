package pgr200eksamen.http;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TestHttpEndpoint {
    @Test
    public void findArguments() {
        HttpEndpoint endpoint = new HttpEndpoint("/echo?status=200");
        HttpQuery query = endpoint.query();
        assertThat(query.toString()).isEqualTo("status=200");
        assertThat(query.get("status")).isEqualTo("200");
    }

    @Test
    public void distinguishEndpointFromQuery() {
        HttpEndpoint endpoint = new HttpEndpoint("/urlecho?status=200");
        assertThat(endpoint.endpoint()).isEqualTo("/urlecho");
        assertThat(endpoint.query().toString()).isEqualTo("status=200");
        assertThat(endpoint.toString()).isEqualTo("/urlecho?status=200");
        assertThat(endpoint.query().get("status")).isEqualTo("200");
    }

    @Test
    public void urlHandling() {
        HttpEndpoint endpoint = new HttpEndpoint("/myapp/echo");
        assertThat(endpoint.endpoint()).isEqualTo("/myapp/echo");
        assertThat(endpoint.query()).isNull();
        assertThat(endpoint.toString()).isEqualTo("/myapp/echo");
    }

    @Test
    public void understandArguments() {
        HttpEndpoint endpoint = new HttpEndpoint("?status=403&body=mer+bl%E5b%E6r+%26+jordb%E6r");
        assertThat(endpoint.query().get("status")).isEqualTo("403");
        assertThat(endpoint.query().get("body")).isEqualTo("mer blåbær & jordbær");
    }

    @Test
    public void createQueries() {
        HttpQuery query = new HttpQuery("status=200");
        query.add("body", "mer blåbær");
        assertThat(query.toString()).isEqualTo("status=200&body=mer+bl%E5b%E6r");
    }

    @Test
    public void urlParsing() { //Skrevet med UTF-8 encoding i motsetning til tidligere tester??
        HttpEndpoint endpoint = new HttpEndpoint("/myapp/echo?status=402&body=vi%20plukker%20bl%C3%A5b%C3%A6r");
        assertThat(endpoint.endpoint()).isEqualTo("/myapp/echo");
        assertThat(endpoint.split()).containsExactly("myapp", "echo");
        assertThat(endpoint.query().get("status")).isEqualTo("402");
    }
}
