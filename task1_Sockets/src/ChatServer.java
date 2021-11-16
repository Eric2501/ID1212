import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class ServerListening implements Runnable{
    PrintWriter output = null;
    ServerSocket serverSocket = null;

    @Override
    public void run(){
        try {
             this.serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("one client connected");


            try {
                output = new PrintWriter(clientSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            output.println("welcome to chat server!");
            output.flush();

            new Thread(new ServerSending(clientSocket)).start();

        }
    }
}

class ServerSending implements Runnable{

    String msg;
    PrintWriter output;
    BufferedReader input;
    Socket clientSocket;

    public ServerSending(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run(){
        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream());

            msg = input.readLine();

            while (msg != null){
                output.println("client:" + msg);
                output.flush();

                System.out.println("client:" + msg);
                msg = input.readLine();
            }

        } catch (IOException e) {
            System.out.println("Client out");
        }
    }
}




public class ChatServer {
    public static void main(String[] args) {

        Thread listening = new Thread(new ServerListening());

        listening.start();

    }
}
