package channel;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Sender {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private BufferedReader br;
    private final int PORT = 5000;

    public String Send (String data) {

        try {
            
            socket = new Socket("localhost", PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(System.in));

            String str1 = "";
            String str2 = "";

            while (!str1.equalsIgnoreCase ( "stop" ) ) {

                str1 = br.readLine();
                dos.writeUTF(str1);
                dos.flush();
                str2 = dis.readUTF();

                System.out.println("Server says : " + str2);

            }

            dos.close();
            socket.close();

        } catch (Exception e) {

            System.out.println("Error on sender side");

        }


        return "";
    }
 
    public String Recv_Ack () {

        return "";
    }

    public void Timeout () {

    }
}
