public class Sum {
    public static void main(String[] args) {
        int cnt = 0;
        StringBuilder sbForNums = new StringBuilder();
        for (String arg: args) {
            for (int i = 0; i < arg.length(); i++) {
                char ch = arg.charAt(i);
                int chType = Character.getType(ch);
                //System.out.println(ch + " " + Character.getType(ch) + " " + Character.SPACE_SEPARATOR);
                if (Character.isWhitespace(ch)) {
                    
                    if (sbForNums.length() > 0) {

                        cnt += Integer.parseInt(sbForNums.toString());
                    }
                    sbForNums.setLength(0);
                    continue;
                    
                } else {
                    sbForNums.append(ch);
                }
            }
            
            if (sbForNums.length() > 0) {
                cnt += Integer.parseInt(sbForNums.toString());
                sbForNums.setLength(0);
            }
        }

        
        
        System.out.println(cnt);
    }
}