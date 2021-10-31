package controllers;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;


import channel.Receiver;
import channel.Sender;
import utilities.Helper;

public class RControl {
    private String frame = "";
    private String dAdd = "";
    private String sAdd = "";
    private String dLen = "";
    private String data = "";
    private String crc = "";
    // private String acknowledgement = "";


    /* 
       ***************************************** 
    
    Receiver controller function controlling the flow with no restriction 

       *****************************************
    */

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


    /* 
       *****************************************

        Receiver control function controlling the flow of data in Stop and wait format
        # stop and wait format implementation here

       *****************************************
    */

    public void controlReceiverSTOP_N_WAIT(){

        ReceiveFrame();
        ExtractData();
        DeliverData();
        SendAck();   

    }

    /* 
       *****************************************
    
        # delay function acting as a buffer to control the early completion of a mechanism

       *****************************************
    */

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


    /* 
       *****************************************

        receive frame function calls the Receiver's Receive function in the Receive class
        implementing a socket connection

       *****************************************
    */

    public void ReceiveFrame() {
        Receiver receiver = new Receiver();
        System.out.println("\n\n\twaiting for sender...");
        this.frame = receiver.Recv();
    }

    /*
        *****************************************
        
        sends acknowledgement on the basis of the CRC validation
        
        *****************************************
    */
    
    public void SendAck(){
        Sender sender = new Sender();
        Helper helper = new Helper();

        sender.Send(helper.createAcknowledgement(validateCRC()));
    }

    /*
        *****************************************

        the ExtractData() function trims the data frame received into the following 

            * destination address
            * source address
            * data length
            * data
            * CRC

        *****************************************
    */

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

    /*
        *****************************************

        the DeliverData() function stores the frame received in a file with 
        the name formatted to the time stamp at which the frame was received

        *****************************************
    */

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


    /*
   
        *****************************************
        
        a utility function that converts a given binary string to decimal

        argument type : String
        return type : integer (int_64)

        *****************************************
    */

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


    /*
        *****************************************

        a utility function for printing status wrt code received

        *****************************************
    */

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

    /*
        *****************************************

        utility function to validate CRC and send response in boolean format

        *****************************************
    */

    public boolean validateCRC(){
        
        String tcrc = new Helper().createCRC(this.data);
        return tcrc.equals(this.crc) ? true : false;
    }
}
