import java.util.Date;

public class SenderControl {
    private boolean RequestToSend = true;
    private String data = "";
    private String frame = "";

    public void controlSender() {
        while (true) {
            WaitForEvent();

            // if(Event(RequestToSend)){
            GetData();
            MakeFrame();
            SendFrame();
            // }
        }
    }

    private boolean Event() {
        return false;
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
            if (end - start >= 30000) {
                break;
            } else {
                long currTime = (end - start) / 1000;
                if (prevTime != currTime) {
                    System.out.print("#");
                    prevTime = currTime;
                }
            }
        }
    }

    public void GetData() {

    }

    public void MakeFrame() {
        // data to be converted into IEEE 802.3 Ethernet frame format
        // minimum length to be sent : ( 6 + 6 + 2 + 46 + 4 ) = 64   bytes
        // maximum length to be sent : ( 6 + 6 + 2 + 1500+ 4) = 1518 bytes
        String destinationAddress = "";
        String sourceAddress = "";
        String frame = "";

        String dAddress = convertAddressToBinary(destinationAddress);
        String sAddress = convertAddressToBinary(sourceAddress);

        String lengthOfData = toBinary(data.length());

        

    }

    public void SendFrame() {

    }

    private String convertAddressToBinary(String address){
        // address received in the format of XX : XX : XX : XX : XX : XX
        
        // cleaning up the address 
        String temp = "";

        for(int i = 0; i < address.length(); i++){
            if(address.charAt(i) == ':'){
                continue;
            }else{
                temp += address.charAt(i);
            }
        }

        address = temp;


        String returnAddress = "";
        String temp1 = "";
        String temp2 = "";
        for (int i = 0; i < address.length(); i++){
            if(i % 2 != 0){
                temp1 = toBinary((int)(address.charAt(i) - 32));
            }else{
                temp2 = toBinary((int)(address.charAt(i) - 32));
                returnAddress += temp2;
                returnAddress += temp1;
            }
        }   
        
        return returnAddress;
    }

    private String toBinary(int num){
        String binary = "";
        
        while(num > 0){
            int l = num % 2;
            num /= 2;
            binary += l;
        }

        return binary;
    }
}
