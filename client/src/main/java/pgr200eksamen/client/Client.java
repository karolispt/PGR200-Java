package pgr200eksamen.client;

import pgr200eksamen.http.*;

import java.util.*;
import java.util.function.Function;

public class Client
{
    private enum CommandSet {
        INSERT,
        UPDATE,
        LIST,
        SHOW,
        SCHEDULE,
        RESET
    }

    private final int port = 10080;
    private final String ip = "localhost";

    public static void main(String[] args)
    {
        Client client = new Client();
        System.out.println(client.inputListen(args));
    }

    private Map<CommandSet, Function<String[], String>> commandFunctionMap = new HashMap<CommandSet, Function<String[], String>>() {
        {
            put(CommandSet.INSERT, (String[] args) -> insert(args));
            put(CommandSet.UPDATE, (String[] args) -> update(args));
            put(CommandSet.LIST, (String[] args) -> retrieve(args));
            put(CommandSet.SHOW, (String[] args) -> display(args));
            put(CommandSet.SCHEDULE, (String[] args) -> plan());
            put(CommandSet.RESET, (String[] args) -> reset());
        }
    };

    private List<String> endpoints = Arrays.asList("discussion", "conference", "Timeentry");

    private String inputListen(String[] args)
    {
        if (args.length > 0)
        {
            CommandSet command = CommandSet.valueOf(args[0].toUpperCase());
            if (!commandFunctionMap.containsKey(command))
                return "Command invalid";

            return commandFunctionMap.get(command).apply(args);
        }
        return "No input given.";
    }

    private HttpRequest createRequest(String endpoint)
    {
        HttpRequest request = new HttpRequest(ip, port, endpoint);
        return request;
    }

    private HttpRequest createRequest(String endpoint, String body)
    {
        HttpRequest request = new HttpRequest(ip, port, endpoint, body);
        return request;
    }

    private String reset() {
        HttpRequest request = createRequest("/reset", "database=reset");
        HttpResponse response = request.post();
        return response.getBody();
    }

    private String plan() {
        HttpRequest request = createRequest("/timetable");
        HttpResponse response = request.get();
        return response.getBody();
    }

    private String display(String[] args) {
        if (args.length > 2) {
            String id = args[2];
            if (isInteger(id)) {
                HttpRequest request = createRequest(endpoint(args[1]));
                request.setEndpoint(request.endpoint() + "/" + args[2]);
                HttpResponse response = request.get();
                return response.getBody();
            }
            else {
                return "'" + args[2] + "'" + " is not an valid ID, an ID have to be numerical";
            }
        }
        return "No ID specified";
    }

    private String retrieve(String[] args) {
        if (args.length > 1) {
            String path = args[1];
            if (endpoints.contains(path)) {
                HttpRequest request = createRequest(endpoint(args[1]));
                HttpResponse response = request.get();
                return response.getBody();
            }
            return "The path is invalid";
        }
        return "The path have to be specified";
    }

    private String insert(String[] args) {
        if (args.length > 2) {
            String command = commandToPost(args);
            if (isValidCommand(command)) {
                HttpRequest request = createRequest(endpoint(args[1]), command);
                HttpResponse response = request.post();
                return response.getBody();
            }
            return "You did not assign a valid value to the parameter";
        }
        return "Keys and values for the object have to be specified";
    }

    private String update(String[] args)  {
        if (args.length > 4) {
            String id = args[2];
            if (isInteger(id)) {
                String command = commandToPost(args);
                if (isValidCommand(command)){
                    HttpRequest request = createRequest(endpoint(args[1]), command);
                    request.setEndpoint(request.endpoint() + "/" + args[2]);
                    HttpResponse response = request.put();
                    return response.getBody();
                }
                return "You did not assign a valid value to the parameter";
            }
            return "'" + args[2] + "'" + " is not an valid ID, an ID have to be a number";
        }
        return "ID and key/value pairs have to be specified";
    }

    private boolean isInteger(String s) {
        return s.matches(("-?(0|[1-9]\\d*)"));
    }

    private boolean isValidCommand(String command) {
        return !command.contains("=-") && !command.endsWith("=");
    }

    private String endpoint(String arg) {
        Optional<String> result = endpoints.stream().filter(s -> s.equals(arg)).findFirst();
        return result.map(s -> "/" + s).orElse("/");
    }

    private String commandToPost(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            if (sb.length() > 0 && args[i].startsWith("-") && !args[i-1].startsWith("-")) {
                sb.append("&");
            }
            if (args[i].startsWith("-") && !args[i-1].startsWith("-")) {
                sb.append(args[i].substring(1));
                sb.append("=");
                try {
                    sb.append(args[i+1]);
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("The parameter " + args[i] + " needs a value");
                }
            }
        }
        return sb.toString();
    }
}