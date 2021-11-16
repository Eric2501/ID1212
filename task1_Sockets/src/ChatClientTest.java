import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClientTest {
    public static void main(String[] args) {

        Socket clientSocket;
        BufferedReader input;
        PrintWriter output;
        Scanner keyboardScanner = new Scanner(System.in);

        try {
            clientSocket = new Socket("localhost", 8080);
            output = new PrintWriter(clientSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            Thread sender = new Thread(new ClientSending(keyboardScanner,output));
            sender.start();
            Thread receiver = new Thread(new ClientListening(input,output,clientSocket));
            receiver.start();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}