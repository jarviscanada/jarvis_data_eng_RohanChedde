package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc{

    public boolean matchJpeg(String filename) {
        return filename.matches("([^\\s]+(\\.(?i)(jpe?g))$)");
    }

    public boolean matchIp(String ip) {
        return ip.matches("(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})");
    }

    public boolean isEmptyLine(String line){
        //using regex to check if the line contains only whitespace characters
        return line.matches("^\\s*$");
    }


    public static void main(String[] args){
        RegexExcImp regex = new RegexExcImp();
        Boolean x = regex.isEmptyLine(" .");
        System.out.println(x);
    }

}
