package channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {

    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private final int PORT = 5000;

    public String Recv() {

        try {

            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String str1 = ""; // data from input
            String str2 = "";

            str1 = dis.readUTF();
            dos.writeUTF(str2);
            dos.flush();

            dis.close();
            socket.close();
            serverSocket.close();

            return str1;

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("\n\nerror\n\n_____________________________________________________________\n\n");

        }

        return "";
    }


    public String Recv(int port) {

        try {

            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String str1 = ""; // data from input
            String str2 = "";

            str1 = dis.readUTF();
            dos.writeUTF(str2);
            dos.flush();

            dis.close();
            socket.close();
            serverSocket.close();

            return str1;

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("\n\nerror\n\n_____________________________________________________________\n\n");

        }

        return "";
    }


}
