package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class JavaGrepLambdaImp extends JavaGrepImp {

    public static void main(String[] args){
        if (args.length!=3){
            throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
        }
    
    JavaGrepImp javaGrepLambdaImp = new JavaGrepLambdaImp();
    javaGrepLambdaImp.setRegex(args[0]);
    javaGrepLambdaImp.setRootPath(args[1]);
    javaGrepLambdaImp.setOutFile(args[2]);

    try{
        javaGrepLambdaImp.process();
    } catch (IOException ex){
        javaGrepLambdaImp.logger.log(Level.SEVERE, "Error: Unable to process", ex);
    }
    }
    /**Implement using Lambda and stream API */
    @Override
    public List<String> readLines(File inputFile){
        List<String> lines = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            lines = br.lines().collect(Collectors.toList());
        } catch (IOException ex){
            logger.log(Level.SEVERE, "Error: Unable to read file " + inputFile.toString(), ex);
        }
        return lines;
    }   

    @Override
    public List<File> listFiles(String rootDir){
        List<File> fileList = new ArrayList<>();
        File root = new File(rootDir);
        if(!root.isDirectory()){
            throw new IllegalArgumentException("Error: " + rootDir + " is not a directory");
        }
        try (java.util.stream.Stream<java.nio.file.Path> paths = java.nio.file.Files.walk(root.toPath())) {
            fileList = paths
                    .filter(java.nio.file.Files::isRegularFile)
                    .map(java.nio.file.Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error: Unable to list files for " + rootDir, ex);
        }
        return fileList;
    }

} 