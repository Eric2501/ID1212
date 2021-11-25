import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class ServerGaming implements Runnable{

    HTTPGuess myGame = new HTTPGuess();
    Socket clientSocket;
    BufferedReader requestReader;
    String[] HTTPRequest;

    public ServerGaming(Socket clientSocket){
        this.clientSocket = clientSocket;
        try {
            requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseRequest() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while (!(line = requestReader.readLine()).isBlank()){
            stringBuilder.append(line).append("\r\n");
        }

        String fullRequest = stringBuilder.toString();
        HTTPRequest = fullRequest.split("\r\n");

    }



    @Override
    public void run() {
        try {
            parseRequest();
            System.out.println(Arrays.toString(HTTPRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
