package pgr200eksamen.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpQuery {
    private Map<String, String> arguments = new LinkedHashMap<>();

    public HttpQuery(String query)
    {
        for (String argument : query.split("&"))
        {
            String[] argData = argument.split("=", 2);
            String key = HttpHandlers.urlDecode(argData[0]);
            String value = HttpHandlers.urlDecode(argData[1]);

            arguments.put(key, value);
        }
    }

    public String get(String key)
    {
        return arguments.get(key);
    }

    public void add(String key, String value)
    {
        arguments.put(key, value);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> argument : arguments.entrySet())
        {
            if (sb.length() > 0)
                sb.append("&");

            String key = HttpHandlers.urlEncode(argument.getKey());
            String value = HttpHandlers.urlEncode(argument.getValue());

            sb.append(key).append("=").append(value); // Example: key=value
        }

        return sb.toString();
    }
}
