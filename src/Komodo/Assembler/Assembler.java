/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import java.io.*;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author child
 */
public class Assembler {

    //ArrayList<String> labels = new ArrayList<>();
    //ArrayList<String> commentsToIgnore = new ArrayList<>();
    //ArrayList<Command> commands = new ArrayList<>();
    
    //new code from logithss
    private ArrayList<Command> commands;
    private ArrayList<Command> incompleteCommands;
    private HashMap<String, Integer> labels;
    
    private int addressCounter =0;
    private int byteCount = 0;
    

    /*public void start() {

        try {
            File file = new File("resources\\AssemblyFile.txt");
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {

                String line = scan.nextLine();

                if (line.startsWith(":")) {
                    String label = line.substring(1, line.length());
                    labels.add(label);

                } else if (line.startsWith(";")) {
                    String comments = line.substring(1, line.length());
                    commentsToIgnore.add(comments);
                } else if (line.isEmpty()) {
                    scan.skip(line);
                } else {
                    String mnemonic = line.substring(0,3);
                    String operand = line.substring(3,line.length());
                    
                    Command instruction = new Command(mnemonic , operand);
                    commands.add(instruction);
                    
                    
                }

            }

            System.out.println(labels);
            System.out.println(commentsToIgnore);
            System.out.println(commands);

            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File was not found ");
        }

    }

*/
    public void assembleFiles(ArrayList<File> assemblyFiles, String exportPath) throws FileNotFoundException, IOException
    {
        //init
        commands  = new ArrayList<>();
        incompleteCommands = new ArrayList<>();
        labels = new HashMap<>();
        addressCounter = 0;
        byteCount = 0;
        
        //go through all files
        Iterator<File> fileIterator = assemblyFiles.iterator();
        
        while(fileIterator.hasNext())
        {
            File currentFile = fileIterator.next();
            Scanner scan = new Scanner(currentFile);
            
            //interpret the file line by line
            while (scan.hasNext()) {
                String assemblyLine = scan.nextLine();
                interpretLine(assemblyLine);
            }
        }
        
        //when all files are processed, assign labels to commands that need it
        for(Command command : incompleteCommands)
        {
            command.assignAddress(labels.get(command.labelName));
        }
        
        //commands are now all ready to be written in byte file
        
        //combine all arrays as one big array
        byte[] finalByteArray = new byte[byteCount];
        System.out.println("bytecout: "+byteCount);
        Iterator<Command> commandIterator = commands.iterator();
        int copyPointer = 0;
        while(commandIterator.hasNext())
        {
            System.out.println("pointer: "+copyPointer);
            byte[] instruction = commandIterator.next().bytecode;
            System.arraycopy(instruction, 0, finalByteArray, copyPointer, instruction.length);
            copyPointer +=instruction.length;
        }
        
        //file is then written to using the given path
        writeToFile(exportPath, finalByteArray);
    }
    
    public void interpretLine(String assemblyLine)
    {
        char prefix = assemblyLine.charAt(0);
        
        Command newCommand = null;
        
        switch(prefix){
            case ';':
                //comment, ignore line
                break;
            case ':':
                //label, calculate address its pointing to and put in labels map
                System.out.println(addressCounter);
                labels.put(assemblyLine.substring(1), addressCounter);
                break;
            case '*':
                //create a new block of code, TODO later
                break;
            default:
                newCommand = new Command(assemblyLine);
                break;
        }
        
        if(newCommand == null)
            return;
        else
        {
            addressCounter+=newCommand.bytecode.length;
            byteCount+=newCommand.bytecode.length;
            if(newCommand.needLabel)
                incompleteCommands.add(newCommand);
        }
        
        commands.add(newCommand);
                
    }
    
    public void writeToFile(String stringPath, byte[]code) throws IOException
    {
        //byte[] byteCodeArray = new byte[code.length];
        //Arrays.fill(byteCodeArray, (byte)0xff);
        Path path = Paths.get(stringPath);
        Files.write(path, code);
    }

}
