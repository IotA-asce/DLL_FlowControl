package cpp;

class jv {
    public String xor1(String a, String b) {
        String result = "";

        int len = b.length();

        for (int i = 1; i < len; i++) {
            if (a.charAt(i) == b.charAt(i))
                result += "0";
            else
                result += '1';
        }

        return result;
    }

    public String mod2div(String divident, String divisor) throws Exception{

        int pick = divisor.length();
        String prevPercentage = "";

        String temp = divident.substring(0, pick);
        int n = divident.length();
        System.out.println("v : ");
        String lo = "";
        while (pick < n) {
            int percentage = (pick * 100) / n;
            
            String sPercentage = "";
            if(percentage < 10){
                sPercentage = "0" + percentage;
            }
            else{
                sPercentage += percentage;
            }

            
            if(!prevPercentage.equalsIgnoreCase(sPercentage)){
                System.out.print("\033[A\033[2k");      // cursor sits at the top left of the terminal
                System.out.println("...loading : " + sPercentage + "/100" + " pick : " + pick + ", n : " + n);
                Thread.sleep(200);
                // System.out.print("\33[2K");      // cursor sits at the top left of the terminal
            }
            
            prevPercentage = sPercentage;
            // System.out.println("\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b");
            // System.out.flush();

            if (temp.charAt(0) == '1')
                temp = xor1(divisor, temp) + divident.charAt(pick);
            else
                lo = "";
            for (int x = 0; x < pick; x++) {
                lo += '0';
            }
            temp = xor1(lo, temp) + divident.charAt(pick);

            pick += 1;
        }

        if (temp.charAt(0) == '1')
            temp = xor1(divisor, temp);
        else
            lo = "";
        for (int x = 0; x < pick; x++) {
            lo += '0';
        }
        temp = xor1(lo, temp);

        return temp;

    }

    public void encodeData(String data, String key) throws Exception{
        int keyLen = key.length();

        String appendedData = (data + "1".repeat(keyLen - 1) + // creates a string of repeated string
                '0');

        String remiander = mod2div(appendedData, key);

        System.out.println("data : " + data);
        System.out.println("remiander : " + remiander);
    }

}