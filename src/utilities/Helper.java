package utilities;

import java.util.ArrayList;

public class Helper {
    public static void main(String[] args) {

        String CRC = "x32+x26+x23+x22+x16+x12+x11+x10+x8+x7+x5+x4+x2+x+1";
        String binaryCRC = conv(CRC, 48);

        // System.out.println((int)('x' - 48));

        System.out.println("\n" + binaryCRC);
    }

    public static String conv(String input, int j) {

        // returns the binary of the CRC polynomial

        boolean flag = false; // for the near existence of an 'x'
        int num = 0;
        final int CHAR_STANDARD = 48;
        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < input.length(); i++) {
            // System.out.println(num);

            if (input.charAt(i) == 'x') {
                // System.out.println("one");
                // we consider the decimal till the next character is not a '+'
                flag = true;
                continue;
            }

            else if (input.charAt(i) == '+') {

                // we need to terminate the part here
                // System.out.println("two");

                flag = false;
                list.add(num);
                num = 0;
                continue;

            }

            if (!flag) {
                // System.out.println("three");

                continue;
            } else {

                num *= 10;
                num += input.charAt(i) - CHAR_STANDARD;
                // System.out.println("four");

            }

        }

        // now we process the arraylist
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + "\t");
        }

        // the first element determines the length of the char array
        String binarCRC = "";

        int itterator = list.get(0);
        int cursor = 0;

        while (itterator >= 0 && cursor < list.size()) {
            if (itterator == list.get(cursor)) {

                binarCRC += '1';
                itterator--;
                cursor++;

            } else {

                binarCRC += '0';
                itterator--;

                // cursor stays the same

            }
        }

        return binarCRC;
    }

    public boolean validateAcknowledgement(String ack) {
        // two type of acknowledgement frame are possible

        // one contains on signal at odd intervals and the other contains at even
        // intervals
        // positive signal : odd intevals
        // negative signal : even intervals

        // ack is of length 8

        int evenCount = 0;
        int oddCount = 0;

        for (int i = 0; i < ack.length(); i++) {
            if (i % 2 == 0 && ack.charAt(i) == '1') {
                oddCount++;
            } else if (i % 2 != 0 && ack.charAt(i) == '1') {
                evenCount++;
            }
        }

        return evenCount > oddCount ? false : true;
    }

    public String createAcknowledgement(boolean isValidated) {
        String even = "01010101";
        String odd = "10101010";

        return isValidated ? odd : even;
    }

    // # CRC generation with CRC-16-CDMA2000, value -> 0xC867
    public String createCRC(String data) {

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
}
