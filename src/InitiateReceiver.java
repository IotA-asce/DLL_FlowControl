public class InitiateReceiver {
    public static void main(String[] args) {
        System.out.println("\n\n:::::::::::::::::::::::::::::::initiating packet delivery:::::::::::::::::::::::::::::::");
        System.out.println("________________________________________________________________________________________\n\n");

        ReceiverControl rc = new ReceiverControl();
        rc.controlReceiver();
        // SenderControl sc = new SenderControl();
        // sc.controlSender();
    }
}
