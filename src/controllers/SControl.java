package controllers;

import java.util.Date;
import java.util.Random;

import channel.Receiver;
import channel.Sender;
import utilities.Helper;

public class SControl {
    private String data = "";
    private String frame = "";

    /*
     * ###############################################################
     * 
     * implementing the dataflow without acknowledgement
     * 
     * ###############################################################
     */

    public void controlSender() {
        while (true) {
            // WaitForEvent();

            // if(Event(RequestToSend)){
            System.out.println("\nobtaining data...");
            WaitForEvent();
            GetData();

            System.out.println("\ncreating frame...");
            WaitForEvent();
            MakeFrame();
            // System.out.println("\nGenerated frame : " + frame);

            System.out.println("\nsending frame...");
            WaitForEvent();
            SendFrame();

        }
    }

    /*
     * ###################################################################
     * 
     * control sender with stop and wait for acknowledment handling
     * 
     * ###################################################################
     */

    public void controlSender_STOP_N_WAIT() {
        boolean canSend = true; // for first frame to be send

        while (true) {
            // wait for event

            if (canSend) {
                GetData();
                MakeFrame();
                SendFrame();
                canSend = false;
            }

            // recieve acknowledgement
            canSend = ReceiveAck() ? true : false;
            // canSend = true; // can send is changed to true on the basis of the
            // acknowledgement received

            System.out.println("Acknowledgement status here : " + canSend);
        }
    }

    public void controlSender_STOP_N_WAIT_ARQ() {

        int Sn = 0;
        boolean canSend = true;

        String sSn ="";

        while (true) {

            Thread threadTimer = new Thread(this::Timer);

            boolean canJump = false;

            while (!canJump) {

                if (canSend) {

                    if(!canJump){
                        GetData();
                        MakeFrame();
                        // StoreFrame(); // keep a copy
                        sSn = this.frame;
                    }

                    SendFrame();

                    // start timer
                    // StartTimer();

                    threadTimer.start();

                    Sn = Sn + 1;
                    canSend = false;

                }

                if (ReceiveAck()) {

                    // StopTimer();
                    threadTimer.stop();

                    // PurgeFrame(); // copy is discarded
                    sSn = "";
                    canSend = true;
                    canJump = true;
                }

                if (!threadTimer.isAlive()) {
                    // StartTimer();
                    // threadTimer.start();
                    // ResendFrame();
                    canJump = false;
                }

            }

        }

    }

    // * * * * * * *


    /*
        ############################################################

        

        ############################################################
    */


    private void Timer() {

        long start = new Date().getTime();
        long end = new Date().getTime();

        while (end - start < 30000) {
            end = new Date().getTime();
        }
    }

    public boolean ReceiveAck() {

        Receiver receiver = new Receiver();
        String ack = receiver.Recv(6000);
        Helper helper = new Helper();

        return helper.validateAcknowledgement(ack);

    }

    public void WaitForEvent() {
        Date date = new Date();

        long start = date.getTime();
        long end = start;

        long prevTime = 0;
        System.out.print("loading... : ");
        while (true) {
            date = new Date();
            end = date.getTime();
            if (end - start >= 3000) {
                break;
            } else {
                long currTime = (end - start) / 100;
                if (prevTime != currTime) {
                    System.out.print("#");
                    prevTime = currTime;
                }
            }
        }
    }

    public void GetData() {
        // obtain data from text file with random generated content

        /*
         * for experiments sake we generate a binary data String in each itteration of
         * length ranging between 46 bytes and 1500 bytes or 368 bits - 12000 bits
         */
        String tData = "";
        Random rand = new Random();

        int length_diff = rand.nextInt(11632); // difference between the maximum and minimum data length
        int baseLength = 368;

        int len = baseLength + length_diff;

        for (int i = 0; i < len; i++) {
            rand = new Random();
            int factor = rand.nextInt(10);

            if (factor % 2 == 0) {
                tData += '1';
            } else {
                tData += '0';
            }
        }

        this.data = tData;
    }

    public void MakeFrame() {
        // data to be converted into IEEE 802.3 Ethernet frame format
        // minimum length to be sent : ( 6 + 6 + 2 + 46 + 4 ) = 64 bytes
        // maximum length to be sent : ( 6 + 6 + 2 + 1500+ 4) = 1518 bytes

        System.out.println("Creating frame -------- |");
        String destinationAddress = "12:12:12:12:12:12";
        String sourceAddress = "12:12:12:12:12:12";

        System.out.println("\n\tmake frame subprocess");

        System.out.println("\t\tappending destination address...");
        String dAddress = convertAddressToBinary(destinationAddress);
        int dal = dAddress.length();
        if (dal < 48) {
            int ddal = 48 - dal;
            for (int i = 0; i < ddal; i++) {
                dAddress += "0";
            }
        }
        // System.out.println("destination address : " + dAddress);

        System.out.println("\t\tappending source address...");
        String sAddress = convertAddressToBinary(sourceAddress);
        int sal = sAddress.length();
        if (sal < 48) {
            int dsal = 48 - sal;
            for (int i = 0; i < dsal; i++) {
                sAddress += "0";
            }
        }
        // System.out.println("source address : " + sAddress);

        System.out.println("\t\tappending data length...");
        String lengthOfData = toBinary(data.length());
        int lda = lengthOfData.length();
        if (lda < 16) {
            int dlda = 48 - lda;
            for (int i = 0; i < dlda; i++) {
                lengthOfData += "0";
            }
        }
        // System.out.println("data length : " + lengthOfData);

        System.out.println("\t\tappending data...");
        String bData = checkIfBinary(data) ? data : toBinary(data);
        // System.out.println("data : " + bData);

        System.out.println("\t\tappending CRC...");
        String CRC = createCRC(data);
        // System.out.println("crc : " + CRC);

        // inserting padding to data to make integer byte size
        System.out.println("\t\tevening out data with padding...");
        int dataLen = bData.length();
        if (dataLen % 8 != 0) {

            int factor = (dataLen / 8) + 1;
            int flength = factor * 8;

            for (int i = 0; i < (flength - dataLen); i++) {
                bData += 0;
            }
        }

        System.out.println("\t\tanalyzing for error...");
        this.frame = dAddress + sAddress + lengthOfData + bData + CRC;

    }

    public boolean SendFrame() {

        Sender sender = new Sender();

        sender.Send(this.frame);

        return true;
    }

    // 101010110100001100011001110101010011010100011100
    // 1010101101000011000110011101010100110101000111000110001

    // 011000110001000111000110001000111010101001110011
    // 1000100011100011000100011101010100111001101011110110001

    // utility methods

    // # CRC generation with CRC-16-CDMA2000, value -> 0xC867
    private String createCRC(String data) {

        // binary value of the polynomial: 1100100001100111
        // String div = "1100100001100111";
        // String div = "100110000010001110110110111";
        String div = "100000100110000010001110110110101";
        return mod2div(data, div);
    }

    private String mod2div(String divd, String divs) {

        int pick = divs.length();
        String temp = divd.substring(0, pick);

        int count = 0;
        int n = divd.length();
        System.out.print("\nCRC status : ");
        while (pick < n) {
            count++;
            if (count % 120 == 0) {
                System.out.print("#");
            }

            if (temp.charAt(0) == '1') {

                temp = xor1(divs, temp) + divd.charAt(pick);

            } else {

                String t = "";

                for (int i = 0; i < pick; i++) {
                    t += '0';
                }

                temp = xor1(t, temp) + divd.charAt(pick);

            }

            pick++;

        }

        if (temp.charAt(0) == '1') {

            temp = xor1(divs, temp);

        } else {

            String t = "";

            for (int i = 0; i < pick; i++) {
                t += '0';
            }
            temp = xor1(t, temp);
        }

        System.out.println();

        return temp;
    }

    private String xor1(String a, String b) {
        String res = "";
        int n = b.length();

        for (int i = 0; i < n; i++) {
            if (a.charAt(i) == b.charAt(i)) {
                res += 0;
            } else {
                res += 1;
            }
        }

        return res;
    }

    // ***********************************************************

    private boolean checkIfBinary(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) == '1' || data.charAt(i) == '0') {
                continue;
            } else {
                return false;
            }
        }

        return true;
    }

    private String convertAddressToBinary(String address) {
        // address received in the format of XX : XX : XX : XX : XX : XX

        // cleaning up the address
        String temp = "";

        for (int i = 0; i < address.length(); i++) {
            if (address.charAt(i) == ':') {
                continue;
            } else {
                temp += address.charAt(i);
            }
        }

        address = temp;

        String returnAddress = "";
        String temp1 = "";
        String temp2 = "";
        for (int i = 0; i < address.length(); i++) {
            if (i % 2 != 0) {
                temp1 = toBinary((int) (address.charAt(i) - 48));

                while (temp1.length() < 4) {
                    temp1 += '0';
                }
            } else {
                temp2 = toBinary((int) (address.charAt(i) - 48));

                while (temp2.length() < 4) {
                    temp2 += '0';
                }

                returnAddress += temp2;
                returnAddress += temp1;
            }
        }

        return returnAddress;
    }

    private String toBinary(int num) {
        String binary = "";

        while (num > 0) {
            int l = num % 2;
            num /= 2;
            binary += l;
        }

        return binary;
    }

    private String toBinary(String data) {
        int num = 0;
        int count = 1;
        for (int i = data.length() - 1; i >= 0; i++) {
            num += data.charAt(i) - 32 * count;
            count *= 10;
        }

        String binary = "";

        while (num > 0) {
            int l = num % 2;
            num /= 2;
            binary += l;
        }

        return binary;
    }
}
