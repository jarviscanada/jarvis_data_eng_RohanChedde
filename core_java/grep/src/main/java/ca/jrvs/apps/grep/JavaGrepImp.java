package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class JavaGrepImp implements JavaGrep{

    final Logger logger = Logger.getLogger(JavaGrep.class.getName());

    private String regex;
    private String rootPath;
    private String outFile;
    public static void main(String[] args){
        if (args.length!=3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }
    


    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try{
        javaGrepImp.process();
    } catch (IOException ex){
        javaGrepImp.logger.log(Level.SEVERE, "Error: Unable to process", ex);
    }
    }

    @Override
    public void process() throws IOException{

        //implementation
        List<String> matchedLines = new ArrayList<>();
        List<File> files = listFiles(rootPath);

        for(File file: files){
            List<String> lines = readLines(file);
            for(String line: lines){
                if(containsPattern(line)){
                    matchedLines.add(line);
                }
            }
        }
        writeToFile(matchedLines);
    }
    @Override
    public List<File> listFiles(String rootDir){
        //implementation
        List<File> fileList = new ArrayList<>();
        for(File file : new File(rootDir).listFiles()){
            if(file.isDirectory()){
                listFiles(file.getAbsolutePath());
            }
            else{
                fileList.add(file);
            }
        }
        return fileList;

    }
    @Override
    public List<String> readLines(File inputFile){
        //implementation
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file: " + inputFile.getAbsolutePath(), e);
        }
        return lines;

    }
    @Override
    public boolean containsPattern(String line){
        Pattern pattern = Pattern.compile(this.regex);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void writeToFile(List<String> lines) throws IOException{
        //implementation
        for (String line : lines) {
            logger.info(line);
        }
        java.nio.file.Files.write(java.nio.file.Paths.get(outFile), lines);
    }
    @Override
    public String getRootPath(){
        return rootPath;

    }
    @Override
    public void setRootPath(String rootPath){

        this.rootPath = rootPath;
    }
    @Override
    public String getRegex(){

        return regex;
    }
    @Override
    public void setRegex(String regex){

        this.regex = regex;
    }
    @Override
    public String getOutFile(){
        return outFile;
    }
    @Override
    public void setOutFile(String outFile){

        this.outFile = outFile;
    }

}
