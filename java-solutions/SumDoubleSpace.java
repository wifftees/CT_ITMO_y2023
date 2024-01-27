public class SumDoubleSpace {
    public static void main(String[] args) {
        double cnt = 0;
        StringBuilder sbForNums = new StringBuilder();
        for (String arg: args) {

            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                if (Character.isSpaceChar(ch)) {
                    if (sbForNums.length() > 0) {
                        cnt += Double.parseDouble(sbForNums.toString());
                    }
                    sbForNums.setLength(0);
                    continue;
                    
                } else {
                    sbForNums.append(ch);
                }
            }
            
            if (sbForNums.length() > 0) {
                cnt += Double.parseDouble(sbForNums.toString());
                sbForNums.setLength(0);
            }
        }

        
        
        System.out.println(cnt);
    }
}