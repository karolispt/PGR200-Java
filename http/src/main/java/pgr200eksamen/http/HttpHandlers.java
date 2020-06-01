package pgr200eksamen.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class HttpHandlers {
    public static String lineReadline(InputStream input) throws IOException
    {
        StringBuilder sb = new StringBuilder();

        int c; // Character
        while ((c = input.read()) != -1)
        {
            if (c == '\r')
            {
                c = input.read();
                assert c == '\n'; // Only break if a new-line char is found
                break;
            }
            sb.append((char) c);
        }

        return sb.toString();
    }

    public static String urlDecode(String str)
    {
        try
        {
            return URLDecoder.decode(str, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Unsupported encoding", e);
        }
    }

    public static String urlEncode(String str)
    {
        try
        {
            return URLEncoder.encode(str, "ISO-8859-1");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("Unsupported encoding", e);
        }
    }
}
