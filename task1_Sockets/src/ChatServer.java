import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerListening implements Runnable{
    PrintWriter output = null;
    ServerSocket serverSocket = null;
    ArrayList<Socket> clientSocketList = new ArrayList<Socket>();

    private void socketCheck(){
        for (int i = 0 ; i < clientSocketList.size(); i ++){
            if (clientSocketList.get(i).isClosed()){
                System.out.println(clientSocketList.get(i).isClosed());
                clientSocketList.remove(i);
            }
        }
    }

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
                clientSocketList.add(clientSocket);
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

            SocketList clientSocketList = new SocketList(this.clientSocketList);

            new Thread(new ServerSending(clientSocket,clientSocketList)).start();

        }
    }
}

class SocketList{
    ArrayList<Socket> SocketList = new ArrayList<Socket>();

    public SocketList(ArrayList<Socket> clientSocketList){
        this.SocketList = clientSocketList;
    }

    public void socketCheck(){
        for (int i = 0 ; i < SocketList.size(); i ++){
            if (SocketList.get(i).isClosed()){
                System.out.println(SocketList.get(i).isClosed());
                SocketList.remove(i);
            }
        }
    }
}

class ServerSending implements Runnable{

    SocketList clientSocketList;
    Socket clientSocket;

    public ServerSending(Socket clientSocket, SocketList clientSocketList){
        this.clientSocket = clientSocket;
        clientSocketList.socketCheck();
        this.clientSocketList = clientSocketList;
    }

    @Override
    public void run(){
        try {
            while (true){
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg = input.readLine();

                for (int i = 0; i < clientSocketList.SocketList.size(); i++){
                    clientSocketList.socketCheck();
                    PrintWriter output = new PrintWriter(clientSocketList.SocketList.get(i).getOutputStream());
                    output.println(msg);
                    output.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("one client out");
        }
    }

}




public class ChatServer {

    ArrayList<Socket> clientSocketList = new ArrayList<Socket>();

    public static void main(String[] args) {

        Thread listening = new Thread(new ServerListening());

        listening.start();

    }
}
