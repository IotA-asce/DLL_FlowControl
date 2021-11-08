

import controllers.RControl;

public class IReceiver {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("\n\n:::::::::::::::::::::::::::::::initiating packet delivery:::::::::::::::::::::::::::::::");
        System.out.println("________________________________________________________________________________________\n\n");

        RControl rControl = new RControl();
        // rControl.controlReceiver();
        // rControl.controlReceiverSTOP_N_WAIT();
        rControl.controlReceiver_STOP_N_WAIT_ARQ();
        rControl.controlReceiver_GO_BACK_N();
    }
}
