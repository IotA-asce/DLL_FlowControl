package channel;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {

    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedReader br;
    private final int PORT = 5000;

    public String Recv (String data) {

        try {
            
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));

            String str1 = data;                       // data from input
            String str2 = "";

            while(!str1.equalsIgnoreCase("stop")){
                
                str1 = dis.readUTF();
                System.out.println("Client says: " + str1);
                str2 = br.readLine();

                dos.writeUTF(str2);
                dos.flush();
            }

            dis.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
 
            e.printStackTrace();
            System.out.println("\n\nerror\n\n_____________________________________________________________\n\n");
 
        }

        return data;
    }

    public String Send_Ack () {
        return "";
    }
}
