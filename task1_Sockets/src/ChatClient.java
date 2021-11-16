import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class  ClientSending implements Runnable{
    Scanner keyboardScanner;
    PrintWriter output;
    String msg;
    public ClientSending(Scanner keyboardScanner, PrintWriter output){
        this.keyboardScanner = keyboardScanner;
        this.output = output;
    }
    @Override
    public void run(){
        while (true){
            msg = keyboardScanner.nextLine();
            output.println(msg);
            output.flush();
        }
    }

}

class ClientListening implements Runnable{
    BufferedReader input;
    PrintWriter output;
    Socket clientSocket;
    String msg;

    public ClientListening(BufferedReader input,PrintWriter output,Socket clientSocket){
        this.input = input;
        this.output = output;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        try {
            msg = input.readLine();
            while (msg != null){
                System.out.println("Server:"+ msg);
                msg = input.readLine();
            }
            System.out.println("Server out of service");
            output.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


public class ChatClient {
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
