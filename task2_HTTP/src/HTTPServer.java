import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class HTTPServer {

    ServerSocket serverSocket;
    Socket clientSocket;
    String[] HTTPRequest;
    String body;

    BufferedReader requestReader;


    public void parseRequest() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while (!(line = requestReader.readLine()).isBlank()){
            stringBuilder.append(line).append("\r\n");
        }

        String fullRequest = stringBuilder.toString();
        HTTPRequest = fullRequest.split("\r\n");

        if (getMethod().equals("POST")){
            body = requestReader.readLine();
            System.out.println(body);
        }
    }

    public String getURL(){
        String line = HTTPRequest[0];
        String[] words = line.split(" ");
        return words[1];
    }

    public String getMethod(){
        String line = HTTPRequest[0];
        String[] words = line.split(" ");
        return words[0];
    }

    public String readHtml(String path) throws IOException {
        BufferedReader htmlReader = new BufferedReader(new FileReader(path));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = htmlReader.readLine()) != null){
            builder.append(line);
        }

        return builder.toString();

    }

    public String constructResponse() throws IOException {
        StringBuilder response = new StringBuilder();

        String statusOK = "HTTP/1.1 200 OK\r\n";
        String header1 = "Content-Type: text/html\r\n";
        String emptyLine = "\r\n";
        String message = "";

        String[] generalRes = {statusOK,header1,emptyLine};
        for (int i = 0; i < generalRes.length; i ++){
            response.append(generalRes[i]);
        }

        if(getURL().equals("/")){
            message = readHtml("index.html") + "\r\n";
        }

        response.append(message);

        return response.toString();

    }

    public void Response() throws IOException {
        String response = constructResponse();

        OutputStream outputStream = clientSocket.getOutputStream();

        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();

    }


    public void startService(int port) throws IOException {
        serverSocket = new ServerSocket(port);

        while (true) {

            clientSocket = serverSocket.accept();

            requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            parseRequest();
            for (String s : HTTPRequest) {
                System.out.println(s);
            }
            Response();
        }
    }



    public static void main(String[] args) throws IOException {
        HTTPServer httpServer = new HTTPServer();

        httpServer.startService(8080);

    }

}
