package pgr200eksamen.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;

public class HttpRequest {
    String ip;
    int port;
    String endpoint;
    String body;
    Socket socket;

    OutputStream os;

    public HttpRequest(String ip, int port, String endpoint)
    {
        this.ip = ip;
        this.port = port;
        this.endpoint = endpoint;

        try
        {
            socket = new Socket(ip, port);
            os = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public HttpRequest(String ip, int port, String endpoint, String body)
    {
        this.ip = ip;
        this.port = port;
        this.endpoint = endpoint;
        this.body = body;

        try
        {
            socket = new Socket(ip, port);
            os = socket.getOutputStream();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String endpoint()
    {
        return this.endpoint;
    }

    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    public void writeOS(String line)
    {
        try {
            byte[] oline = (line + "\r\n").getBytes();
            os.write(oline);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // HTTP GET
    public HttpResponse get()
    {
        writeOS("GET " + endpoint + " HTTP/1.1");
        writeOS("Host: " + ip);
        writeOS("Connection: close");
        writeOS("");

        try {
            os.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        HttpResponse response = new HttpResponse(socket);
        return response;
    }

    // HTTP POST
    public HttpResponse post()
    {
        writeOS("POST " + endpoint + " HTTP/1.1");
        writeOS("Host: " + ip);
        writeOS("Connection: close");
        writeOS("Content-Type: text/plain");
        writeOS("Content-Length: " + body.length());
        writeOS("");
        writeOS(body);

        try {
            os.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        HttpResponse response = new HttpResponse(socket);
        return response;
    }

    // HTTP PUT
    public HttpResponse put()
    {
        writeOS("PUT " + endpoint + " HTTP/1.1");
        writeOS("Host: " + ip);
        writeOS("Connection: close");
        writeOS("Content-Type: text/plain");
        writeOS("Content-Length: " + body.length());
        writeOS("");
        writeOS(body);

        try {
            os.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        HttpResponse response = new HttpResponse(socket);
        return response;
    }
}
