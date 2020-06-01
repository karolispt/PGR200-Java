package pgr200eksamen.http;

import pgr200eksamen.db.*;
import pgr200eksamen.db.entities.*;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalTime;

public class HttpServer {
    private int port;
    private int localPort;

    private String status;
    private String body;
    private String type;
    private String location;

    private static DataSource source;

    public HttpServer(int port) throws IOException, SQLException {
        this.port = port;

        begin();
    }

    public static void main(String[] args) throws IOException, SQLException
    {
        HttpServer server = new HttpServer(10080);
        source = DbMain.DataSource();
        DbMain.main(args);
    }

    public DataSource dataSource()
    {
        return source;
    }

    public void setDataSource(DataSource source)
    {
        this.source = source;
    }

    public int port()
    {
        return localPort;
    }

    private void begin() throws IOException
    {
        ServerSocket socket = new ServerSocket(port);
        this.localPort = socket.getLocalPort();

        new Thread(() -> serverRuntime(socket)).start();
        System.out.println("Running");
    }

    private void defaultArguments() {
        status = "200 OK";
        body = "Resource not found\r\n";
        type = "text/plain";
        location = null;
    }

    private void serverRuntime(ServerSocket socket)
    {
        while (true) {
            try
            {
                Socket client = socket.accept();

                InputStream is = client.getInputStream();
                OutputStream os = client.getOutputStream();

                defaultArguments();

                String request = HttpHandlers.lineReadline(is);
                String[] requests = request.split(" ");
                HttpEndpoint endpoint = new HttpEndpoint(requests[1]);

                if (!endpoint.endpoint().equals("/echo") && !endpoint.endpoint().equals("/"))
                {
                    String requestType = requests[0].trim();

                    switch (requestType)
                    {
                        case "GET":
                        {
                            HttpQuery query = endpoint.query();
                            setQueryArguments(query);

                            try {
                                dbQuery(endpoint);
                                status = "200 OK";
                            }
                            catch (SQLException e)
                            {
                                status = "404 Not Found";
                            }
                            catch (IOException e)
                            {
                                status = "404 Not Found";
                            }
                            break;
                        }
                        case "POST":
                        {
                            String req;

                            // Wait until ""
                            while(!(req = HttpHandlers.lineReadline(is)).equals(""))
                            {

                            }

                            body = HttpHandlers.lineReadline(is);
                            HttpQuery query = new HttpQuery(body);

                            try {
                                dbInsert(endpoint, query);
                                status = "201 Created";
                            }
                            catch (SQLException e)
                            {
                                status = "404 Not Found";
                            }
                            break;
                        }
                        case "PUT":
                        {
                            String req;

                            // Wait until ""
                            while(!(req = HttpHandlers.lineReadline(is)).equals(""))
                            {

                            }

                            body = HttpHandlers.lineReadline(is);
                            HttpQuery query = new HttpQuery(body);

                            try {
                                dbUpdate(endpoint, query);
                                status = "204 No Content";
                            }
                            catch (SQLException e)
                            {
                                status = "404 Not Found";
                            }

                            break;
                        }
                    }
                }
                else
                {
                    HttpQuery query = endpoint.query();
                    setQueryArguments(query);
                }

                output(os);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void dbUpdate(HttpEndpoint endpoint, HttpQuery query) throws SQLException
    {
        String[] contents = endpoint.split();
        int entityId = Integer.parseInt(contents[1]);

        switch (contents[0])
        {
            case "discussion":
            {
                DiscussionDao discussionDao = new DiscussionDao(source);
                Discussion discussion = discussionDao.selectSingle(entityId);

                String title = query.get("title");
                String desc = query.get("description");
                String topic = query.get("topic");
                if (title != null) discussion.setTitle(title);
                if (desc != null) discussion.setDescription(desc);
                if (topic != null) discussion.setTopic(topic);

                discussionDao.update(discussion, entityId);
                break;
            }
            case "conference":
                ConferenceDao conferenceDao = new ConferenceDao(source);
                Conference conference = conferenceDao.selectSingle(entityId);

                String title = query.get("title");
                if (title != null) conference.setTitle(title);

                conferenceDao.update(conference, entityId);
                break;
            case "timeentry":
                TimeEntryDao timeEntryDao = new TimeEntryDao(source);
                TimeEntry timeEntry = timeEntryDao.selectSingle(entityId);

                String startTime = query.get("starttime");
                String endTime = query.get("endtime");

                if (startTime != null)
                {
                    LocalTime sT = LocalTime.parse(startTime);
                    timeEntry.setStartTime(sT);
                }

                if (endTime != null)
                {
                    LocalTime eT = LocalTime.parse(endTime);
                    timeEntry.setEndTime(eT);
                }

                timeEntryDao.update(timeEntry, entityId);
                break;
        }
    }

    private void dbInsert(HttpEndpoint endpoint, HttpQuery query) throws SQLException
    {
        String[] contents = endpoint.split();
        String type = contents[0];

        DAO dao;
        switch (type)
        {
            case "discussion":
            {
                dao = new DiscussionDao(source);
                Discussion discussion = new Discussion();

                discussion.setTitle(query.get("title"));
                discussion.setDescription(query.get("description"));
                discussion.setTopic(query.get("topic"));

                dao.insert(discussion);

                break;
            }
            case "conference":
            {
                dao = new ConferenceDao(source);
                Conference conference = new Conference();

                conference.setTitle(query.get("title"));

                dao.insert(conference);
                break;
            }
            case "timeentry":
            {
                dao = new TimeEntryDao(source);
                TimeEntry timeEntry = new TimeEntry();

                LocalTime startTime = LocalTime.parse(query.get("starttime"));
                LocalTime endTime = LocalTime.parse(query.get("endtime"));

                timeEntry.setStartTime(startTime);
                timeEntry.setEndTime(endTime);

                dao.insert(timeEntry);
                break;
            }
            case "reset":
            {
                DbMain.resetDB();
            }
        }
    }

    private void dbQuery(HttpEndpoint endpoint) throws SQLException, IOException
    {
        String ep = endpoint.endpoint();

        System.out.println("dbQuery: endpoint is '" + ep + "'");
        if (ep.matches("/timetable"))
        {
            TimetableDao timetableDao = new TimetableDao(source);
            body = timetableDao.getTimetable().toString();
        }
        else if (ep.matches(".*/.*/.*"))
        {
            getSpecific(endpoint);
        }
        else
        {
            getAll(endpoint);
        }
    }

    private void getAll(HttpEndpoint endpoint) throws SQLException, IOException
    {
        String[] contents = endpoint.split();
        DAO dao;

        System.out.println("getAll: id is '" + contents[0] + "'");
        switch (contents[0])
        {
            case "Discussion":
            {
                dao = new DiscussionDao(source);
                body = dao.selectAll().toString();
                break;
            }
            case "Conference":
            {
                dao = new ConferenceDao(source);
                body = dao.selectAll().toString();
                break;
            }
            case "TimeEntry":
            {
                dao = new TimeEntryDao(source);
                body = dao.selectAll().toString();
                break;
            }
            default:
            {
                throw new IOException("Invalid URL provided");
            }
        }
    }

    private void getSpecific(HttpEndpoint endpoint) throws SQLException, IOException
    {
        String[] contents = endpoint.split();
        String id = contents[1];

        DAO dao;

        if (id.matches("-?(0|[1-9]\\d*)"))
        {

            int entityId = Integer.parseInt(id);
            switch (contents[0])
            {
                case "Discussion":
                {
                    dao = new DiscussionDao(source);
                    Discussion discussion = ((DiscussionDao) dao).selectSingle(entityId);

                    body = discussion.toString();
                    break;
                }
                case "Conference":
                {
                    dao = new ConferenceDao(source);
                    Conference conference = ((ConferenceDao) dao).selectSingle(entityId);

                    body = conference.toString();
                    break;
                }
                case "TimeEntry":
                {
                    dao = new TimeEntryDao(source);
                    TimeEntry timeEntry = ((TimeEntryDao) dao).selectSingle(entityId);

                    body = timeEntry.toString();
                    break;
                }
                default:
                {
                    throw new IOException("Invalid URL provided");
                }
            }
        }
        else
        {
            throw new IOException("Invalid ID provided");
        }
    }

    private byte[] bytify(String content)
    {
        return content.getBytes();
    }

    private void output(OutputStream os) throws IOException
    {
        os.write(bytify("HTTP/1.1 " + status + "\r\n"));
        os.write(bytify("X-Server-Name: Web Server\r\n"));
        os.write(bytify("Connection: close\r\n"));

        if (location != null)
            os.write(bytify("Location: " + location + "\r\n"));

        os.write(bytify("Content-Type: " + type + "\r\n"));
        os.write(bytify("Content-Length: " + body.length() + "\r\n"));
        os.write(bytify("\r\n"));
        os.write(bytify(body));
    }

    private void setQueryArguments(HttpQuery query)
    {
        if (query != null)
        {
            status = query.get("status");
            if (status == null)
                status = "200 OK";

            body = query.get("body");
            if (body == null)
                body = "empty body";

            type = query.get("Content-Type");
            if (type == null)
                type = "text/plain";

            location = query.get("Location");
        }
    }
}
