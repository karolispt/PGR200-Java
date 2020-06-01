package pgr200eksamen.http;

public class HttpEndpoint {
    private String endpoint;

    public HttpEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public HttpQuery query()
    {
        if (!endpoint.contains("?"))
            return null;

        int queryIndex = endpoint.indexOf("?");
        String arguments = endpoint.substring(queryIndex + 1);

        return new HttpQuery(arguments);
    }

    public String endpoint()
    {
        if (!endpoint.contains("?"))
            return endpoint;

        int queryIndex = endpoint.indexOf("?");

        return endpoint.substring(0, queryIndex);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        String ep = endpoint();

        sb.append(ep);
        if(query() != null) {
            sb.append("?");
            sb.append(query());
        }
        return sb.toString();
    }

    public String[] split() {
        String ep = endpoint().substring(1);
        String[] data = ep.split("/");

        return data;
    }
}
