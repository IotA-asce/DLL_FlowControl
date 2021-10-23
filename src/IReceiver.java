

import controllers.RControl;

public class IReceiver {
    public static void main(String[] args) {
        System.out.println("\n\n:::::::::::::::::::::::::::::::initiating packet delivery:::::::::::::::::::::::::::::::");
        System.out.println("________________________________________________________________________________________\n\n");

        RControl rControl = new RControl();
        rControl.controlReceiver();
    }
}
