import java.util.Date;

public class ReceiverControl {
    private String frame = "";
    private String data = "";
    private String acknowledgement = "";


    public void controlReceiver() {
        while (true) {
            WaitForEvent();
            // if (Event(ArrivalNotification)) {
            //     ReceiveFrame();
            //     ExtractData();
            //     DeliverData();
            // }
        }
    }

    public void WaitForEvent(){
        Date date = new Date();

        long start = date.getTime();
        long end = start;

        long prevTime = 0;
        System.out.print("initiating connection... : ");
        while(true){
            date = new Date();
            end = date.getTime();
            if(end - start >= 30000){
                break;
            }
            else{
                long currTime = (end - start) / 1000;
                if(prevTime != currTime ){
                    System.out.print("#");
                    prevTime = currTime;
                }
            }
        }

        System.out.println(" :: timeout() ");
        
    }

    public void ReceiveFrame(){

    }

    public void ExtractData(){

    }

    public void DeliverData(){

    }
}
