import java.util.ArrayList;

public class Helper {
    
    
    public static void main(String[] args) {
        
        String CRC = "x32+x26+x23+x22+x16+x12+x11+x10+x8+x7+x5+x4+x2+x+1";
        String binaryCRC = conv(CRC, 48);

        // System.out.println((int)('x' - 48));

        System.out.println("\n" + binaryCRC);
    }

    public static String conv (String input, int j) {
        
        // returns the binary of the CRC polynomial
        
        boolean flag = false;                           // for the near existence of an 'x'
        int num = 0;
        final int CHAR_STANDARD = 48;
        ArrayList<Integer> list = new ArrayList<>();
        
        for(int i = 0; i < input.length(); i++){
            // System.out.println(num);

            if(input.charAt(i) == 'x'){
                // System.out.println("one");
                // we consider the decimal till the next character is not a '+'
                flag = true;
                continue;
            }

            else if(input.charAt(i) == '+'){

                // we need to terminate the part here
                // System.out.println("two");

                flag = false;
                list.add(num);
                num = 0;
                continue;

            }


            if(!flag){
                // System.out.println("three");

                continue;
            }else{

                num *= 10;
                num += input.charAt(i) - CHAR_STANDARD;
                // System.out.println("four");

            }

        }


        // now we process the arraylist
        for(int i = 0; i < list.size(); i++){
            System.out.print(list.get(i) + "\t");
        }

        // the first element determines the length of the char array
        String binarCRC = "";

        int itterator = list.get(0);
        int cursor = 0;


        while(itterator >= 0 && cursor < list.size()){
            if(itterator == list.get(cursor)){
                
                binarCRC += '1';
                itterator--;
                cursor++;

            }else{
                
                binarCRC += '0';
                itterator--;

                // cursor stays the same

            }
        }

        return binarCRC;
    }
}
