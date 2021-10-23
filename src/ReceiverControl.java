import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import channel.Receiver;

public class ReceiverControl {
    private String frame = "";
    private String dAdd = "";
    private String sAdd = "";
    private String dLen = "";
    private String data = "";
    private String crc = "";
    // private String acknowledgement = "";

    public void controlReceiver() {
        while (true) {
            WaitForEvent();
            // if (Event(ArrivalNotification)) {
            
            ReceiveFrame();
            System.out.println("\nFrame recieved...");

            ExtractData();
            System.out.println("\nData extraction complete...");

            System.out.println("\ndelivering data to file...");
            DeliverData();
            System.out.println("\nwriting to file complete");
            Toolkit.getDefaultToolkit().beep();
            // Toolkit.getDefaultToolkit().beep();
            // }
        }
    }

    public void WaitForEvent() {
        Date date = new Date();

        long start = date.getTime();
        long end = start;

        long prevTime = 0;
        System.out.print("initiating connection... : ");
        while (true) {
            date = new Date();
            end = date.getTime();
            if (end - start >= 3000) {
                return;
            } else {
                long currTime = (end - start) / 100;
                if (prevTime != currTime) {
                    System.out.print("#");
                    prevTime = currTime;
                }
            }
        }

    }

    public void ReceiveFrame() {
        Receiver receiver = new Receiver();
        System.out.println("\n\n\twaiting for sender...");
        this.frame = receiver.Recv();
    }
    
    public void ExtractData() {
        
        System.out.println("------Extracting data..");


        // String temp = this.frame;

        // System.out.println("frame : " + frame);

        System.out.println("------------extracting destination address...");
        int ptr = 0;
        // first 6bytes will be the destination address
        this.dAdd = this.frame.substring(ptr, ptr + (6 * 8));
        ptr += (6 * 8);
        System.out.println("------------------extraction complete");


        // next 6bytes will be the source address
        System.out.println("------------extracting source address...");
        this.sAdd = this.frame.substring(ptr, ptr + (6 * 8));
        ptr += (6 * 8);
        System.out.println("------------------extraction complete");


        // next 2bytes will be the length of data
        System.out.println("------------extracting data-length...");
        this.dLen = this.frame.substring(ptr, ptr + (2 * 8));
        int dataLengthInt = binaryToDecimal(this.dLen);
        System.out.println("------------------extraction complete");

        ptr += (2 * 8);

        // next n bytes (46-1500) will be the data
        printStatus(0);
        this.data = "";
        if (dataLengthInt % 8 == 0) {
            this.data = this.frame.substring(ptr, ptr + dataLengthInt);
            ptr += dataLengthInt;
        } else {
            int tlen = ((dataLengthInt / 8) + 1) * 8;
            this.data = frame.substring(ptr, ptr + tlen);
            ptr += tlen;
        }
        printStatus(1);

        // the receeding 4bytes will be the CRC
        printStatus(0);
        this.crc = this.frame.substring(ptr, frame.length());
        printStatus(1);

        // we need to determine if the frame recieved does not contain error
        // validateFrame();

        printStatus(1);
    }

    public void DeliverData() {
        
        // store the data in another file

        try {
            
            // String fileName = Calendar.getInstance().getTime().toString() + ".txt";
            Date date = new Date();
            String fileName = "./receivedData/";
            fileName += date.getTime() ;
            fileName += ".txt";
            
            System.out.println(fileName);

            File file = new File(fileName);
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(fileName);

            // data saving format : timestamp -> data
            // Calendar calendar = Calendar.getInstance();
            // fileWriter.write(calendar.getTime().toString());

            fileWriter.write("| Destination address : " + dAdd + " |\n");
            fileWriter.write("| Sender address      :" + sAdd + " |\n");
            fileWriter.write("| data length         :" + dLen + " |\n");
            fileWriter.write("| Data                : " + this.data + " |\n");
            fileWriter.write("| CRC                 : " + this.crc + " |\n\n");
            fileWriter.close();

        } catch (Exception e) {
            // System.err.println("Error handling file...");
            e.printStackTrace();
        }

    }

    private int binaryToDecimal(String binary) {
        final int FACTOR = 2;
        int multiplicant = 1;
        int number = 0;

        for(int i = 0; i < binary.length(); i++){
            if(binary.charAt(i) == '1'){
                number += multiplicant;
                multiplicant *= FACTOR;

            }else{

                number += 0;
                multiplicant *= FACTOR;

            }
        }

        return number;
    }

    private void printStatus(int status_code){
        switch (status_code) {
            case 0:
                System.out.println("-------extracting");
                break;
            case 1:
                System.out.println("-----------extraction complete");
        
            default:
                break;
        }
    }
}
