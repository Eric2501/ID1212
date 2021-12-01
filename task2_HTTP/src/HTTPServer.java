import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class HTTPServer {

    String[] HTTPRequest;
    ArrayList<HTTPGuess> gameList = new ArrayList<>();
    int gameListCounter = 0;

    public void printRequest(){
        if (HTTPRequest!=null){
            System.out.println("\nREQUEST:");
            for (String s : HTTPRequest){
                System.out.println(s);
            }
        }else {
            System.out.println("parse not successful");
        }
    }


    public void parseRequest(BufferedReader requestReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while (!(line = requestReader.readLine()).equals("")){
            stringBuilder.append(line).append("\r\n");
        }

        String fullRequest = stringBuilder.toString();
        HTTPRequest = fullRequest.split("\r\n");
    }

    public void response(OutputStream outputStream, HTTPResponseGenerator myGenerator) throws IOException {
        String response = myGenerator.construct();
        System.out.println("\nRESPONSE:\n" + response);
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    public String getURL(){
        String line = HTTPRequest[0];
        String[] words = line.split(" ");
        return words[1];
    }

    public String getGuessNum(){
//        String line = HTTPRequest[0];
        String URL = getURL();
        String[] NumList = URL.split("=");
        System.out.println(NumList[1]);
        return NumList[1];
    }

    public String getCookie(){
        String cookie = null;
        for (String s : HTTPRequest){
            if (s.startsWith("Cookie")){
                cookie = s.split("=")[1];
            }
        }
        return cookie;
    }

    public void startService(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket;
        BufferedReader requestReader;
        OutputStream outputStream;

//        clientSocket = serverSocket.accept();

//        outputStream = clientSocket.getOutputStream();

        while (true) {

            try {

                HTTPResponseGenerator generator;

                System.out.println("\nparsing incomming message");
                clientSocket = serverSocket.accept();
                parseRequest(new BufferedReader(new InputStreamReader(clientSocket.getInputStream())));
                printRequest();
                outputStream = clientSocket.getOutputStream();

                if (getURL().equals("/")){
                    generator = new HTTPResponseGenerator("index",gameListCounter);
                    HTTPGuess newGame = new HTTPGuess(gameListCounter);
                    gameList.add(newGame);
                    gameListCounter ++;
                }else
                if (getURL().equals("/favicon.ico")){
                    generator = new HTTPResponseGenerator("error",gameListCounter);
                }else {
                    int gameID = Integer.parseInt(getCookie());
                    HTTPGuess myGame = gameList.get(gameID);
                    myGame.play(Integer.parseInt(getGuessNum()));
                    if (!myGame.status.equals("correct")){
                        generator = new HTTPResponseGenerator("wrong",myGame.getTryTimes(),myGame.status,gameID);
                    }else {
                        generator = new HTTPResponseGenerator("correct",myGame.getTryTimes(),myGame.status,gameID);
                    }
                }
                response(outputStream,generator);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        HTTPServer httpServer = new HTTPServer();
        httpServer.startService(8080);
    }


}
