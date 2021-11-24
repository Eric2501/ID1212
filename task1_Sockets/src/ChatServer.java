import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ServerListening implements Runnable{
    PrintWriter output = null;
    ServerSocket serverSocket = null;
    ArrayList<Socket> clientSocketList = new ArrayList<Socket>();

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
    ArrayList<Socket> SocketList;

    public SocketList(ArrayList<Socket> clientSocketList){
        this.SocketList = clientSocketList;
    }

    public void socketCheck() throws IOException {


        for (int i = 0 ; i < SocketList.size(); i ++){

            try {
                InputStreamReader inputStream = new InputStreamReader(SocketList.get(i).getInputStream());
            } catch (IOException e) {
                SocketList.get(i).close();
                SocketList.remove(i);
                e.printStackTrace();
            }
        }
    }
}

class ServerSending implements Runnable{

    SocketList clientSocketList;
    Socket clientSocket;

    public ServerSending(Socket clientSocket, SocketList clientSocketList) {
        this.clientSocket = clientSocket;
        this.clientSocketList = clientSocketList;
    }

    private void clientSocketCheck() throws IOException {
        try {
            InputStreamReader inputStream = new InputStreamReader(clientSocket.getInputStream());
        } catch (IOException e) {
            clientSocket.close();
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            while (true){
                clientSocketCheck();
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

    public static void main(String[] args) {

        Thread listening = new Thread(new ServerListening());

        listening.start();

    }
}
