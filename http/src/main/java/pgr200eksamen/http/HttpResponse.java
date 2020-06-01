package pgr200eksamen.http;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpResponse {
    Socket socket;
    InputStream is;
    int status;

    Map<String, String> headers = new LinkedHashMap<>();
    String body;

    public HttpResponse(Socket socket) {
        this.socket = socket;
        try {
            is = socket.getInputStream();
            status();
            headers();
            body();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void status() throws IOException {
        String line = parseLine();
        String[] lineData = line.split(" ");
        String status = lineData[1];
        this.status = Integer.parseInt(status);
    }

    private void headers() throws IOException {
        while (is.available() != 0) {
            String line = parseLine();
            if(line.isEmpty())
                break;

            String[] headerData = line.split(":", 2);
            String headerName = headerData[0].trim();
            String headerValue = headerData[1].trim();
            headers.put(headerName, headerValue);
        }
    }

    private void body() throws IOException{
        int contentLength = Integer.parseInt(headers.get("Content-Length"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < contentLength; i++) {
            int c = is.read();
            sb.append((char)c);
        }
        body = sb.toString();
    }

    private String parseLine() throws IOException {
        return HttpHandlers.lineReadline(is);
    }

    public int getStatusCode() {
        return status;
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public String getBody() {
        return body;
    }
}
