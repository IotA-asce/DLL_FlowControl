public class App {
    public static void main(String[] args) throws Exception {
        
        System.out.println("\n\n:::::::::::::::::::::::::::::::initiating packet delivery:::::::::::::::::::::::::::::::");
        System.out.println("________________________________________________________________________________________\n\n");

        ReceiverControl rc = new ReceiverControl();
        rc.controlReceiver();
    }
}
