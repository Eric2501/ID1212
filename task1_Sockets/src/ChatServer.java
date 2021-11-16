import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class ServerListening implements Runnable{
    BufferedReader input;
    PrintWriter output;
    String msg;

    public ServerListening(BufferedReader input, PrintWriter output){
        this.input = input;
        this.output = output;
    }

    @Override
    public void run(){
        try {
            msg = input.readLine();
            while (msg != null){
                System.out.println("client:" + msg );
                msg = input.readLine();
            }
            System.out.println("client");

            output.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

class ServerSending implements Runnable{

    String msg;
    PrintWriter output;
    BufferedReader input;

    public ServerSending(PrintWriter output, BufferedReader input){
        this.output = output;
        this.input = input;
    }

    @Override
    public void run(){
        while (true){
            try {
                msg = input.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.println(msg);
            output.flush();
        }
    }

}




public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket;
        Socket clientSocket;
        BufferedReader input;
        PrintWriter output;
        Scanner keyboardScanner = new Scanner(System.in);
        try{
            serverSocket = new ServerSocket(8080);
            while (true){
                clientSocket = serverSocket.accept();
                System.out.println("one client connected");
                output = new PrintWriter(clientSocket.getOutputStream());
                output.println("welcome to chat server!");
                output.flush();
                input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Thread sender = new Thread(new ServerSending(output,input));
                Thread receiver = new Thread(new ServerListening(input,output));
                receiver.start();
                sender.start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }


    }
}
