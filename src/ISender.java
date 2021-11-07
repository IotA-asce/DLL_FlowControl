

import controllers.SControl;

public class ISender {
    public static void main(String[] args) {
        System.out.println("\n\n:::::::::::::::::::::::::::::::initiating packet delivery:::::::::::::::::::::::::::::::");
        System.out.println("________________________________________________________________________________________\n\n");

        SControl sControl = new SControl();
        // sControl.controlSender();
        // sControl.controlSender_STOP_N_WAIT();
        sControl.controlSender_STOP_N_WAIT_ARQ();
    }
}
