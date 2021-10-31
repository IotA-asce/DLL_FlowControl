package channel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Sender {
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private final int PORT = 5000;

    public String Send(String data) {

        try {

            socket = new Socket("localhost", PORT);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String str2 = "";

            dos.writeUTF(data);
            dos.flush();
            str2 = dis.readUTF();

            dos.close();
            socket.close();

            return str2;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on sender side");
        }

        return "";
    }
}
