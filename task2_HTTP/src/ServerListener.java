import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener implements Runnable{

    ServerSocket serverSocket = null;

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true){
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("one client connected");

            new Thread(new ServerGaming(clientSocket));
        }
    }
}

