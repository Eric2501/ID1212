import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class HTTPServer {

    String[] HTTPRequest;

    public void parseRequest(BufferedReader requestReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while (!(line = requestReader.readLine()).isBlank()){
            stringBuilder.append(line).append("\r\n");
        }

        String fullRequest = stringBuilder.toString();
        HTTPRequest = fullRequest.split("\r\n");
    }

    public String getGuessNum(){
        String line = HTTPRequest[0];
        String[] words = line.split(" ");
        char[] URLArray = words[1].toCharArray();
        String Num = null;
        for(int index = 0; index < URLArray.length; index++ ){
            if (URLArray[index] == '='){
                if (URLArray.length == index + 2){
                    char c1 = URLArray[index + 1];
                    Num = String.valueOf(c1);
                }else if(URLArray.length == index + 3){
                    char c1 = URLArray[index + 1];
                    char c2 = URLArray[index + 2];
                    char[] NumArray = {c1, c2};
                    Num = String.valueOf(NumArray);
                }else if(URLArray.length == index + 4){
                    char c1 = URLArray[index + 1];
                    char c2 = URLArray[index + 2];
                    char c3 = URLArray[index + 3];
                    char[] NumArray = {c1, c2, c3};
                    Num = String.valueOf(NumArray);
                }
            }
        }
        System.out.println(Num);
        return Num;
    }

    public String getURL(){
        String line = HTTPRequest[0];
        String[] words = line.split(" ");
        return words[1];
    }

    public void Response(OutputStream outputStream, HTTPResponseGenerator myGenerator) throws IOException {
        String response = myGenerator.construct();
        outputStream.write(response.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }


    public void startService(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket;
        BufferedReader requestReader;
        OutputStream outputStream;
        HTTPGuess my_guess = null;


        while (true) {

            clientSocket = serverSocket.accept();
            outputStream = clientSocket.getOutputStream();
            requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            HTTPResponseGenerator generator;

            try {
                parseRequest(requestReader);

                if (getURL().equals("/")){
                     my_guess = new HTTPGuess();
                     generator = new HTTPResponseGenerator("index",my_guess.getTryTimes(),my_guess.status);
                }else
                {
                    my_guess.play(Integer.parseInt(getGuessNum()));
                    if (!my_guess.status.equals("correct")){
                        generator = new HTTPResponseGenerator("wrong",my_guess.getTryTimes(),my_guess.status);
                    }else {
                        generator = new HTTPResponseGenerator("correct",my_guess.getTryTimes(),my_guess.status);
                    }
                }
                Response(outputStream,generator);
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
