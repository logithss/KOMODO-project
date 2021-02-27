/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Commun.NumberUtility;
import java.io.*;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author child
 */
public class Assembler {
    
    //new code from logithss
    private ArrayList<CommandBlock> blocks;
    private CommandBlock currentBlock;
    private ArrayList<Command> incompleteCommands;
    private HashMap<String, Integer> labels;
    
    
    public void assembleFiles(ArrayList<File> assemblyFiles, String exportPath) throws FileNotFoundException, IOException
    {
        //init
        blocks = new ArrayList<>();
        incompleteCommands = new ArrayList<>();
        labels = new HashMap<>();
        newBlock(0); //create initial block at address 0
        
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
        
        //when all files are processed, give labels to commands that need them
        for(Command command : incompleteCommands)
        {
            command.assignAddress(labels.get(command.labelName));
        }
        
        //commands are now all ready to be written in byte file
        
        //sort blocks by starting positions
        Collections.sort(blocks);
        
        //calculate size of final array
        int byteCount = blocks.get(blocks.size()-1).startingAddress+blocks.get(blocks.size()-1).byteSize;
        byte[] finalByteArray = new byte[byteCount];
        
        //merge all instructions into one byte array
        int bytePointer = 0;
        int filledPointer = 0;
        
        //value to use when filling blanks in the file
        byte blankFillValue = 0;
        
        for(CommandBlock block : blocks)
        {
            System.out.println("block start "+block.startingAddress+", size is "+block.byteSize);
            //fill array if there are blanks
            if(block.startingAddress > filledPointer){
                Arrays.fill(finalByteArray, bytePointer, block.startingAddress, blankFillValue);
                filledPointer = block.startingAddress;
            }
            bytePointer = block.startingAddress;
            
            //write block instruction into final array
            byte[] instruction = block.getBytecodeArray();
            if(instruction.length != 0){
                System.arraycopy(instruction, 0, finalByteArray, bytePointer, block.byteSize);
                bytePointer += block.byteSize;
                
                if(bytePointer > filledPointer) filledPointer = bytePointer;
            }
        }
        
        
        //write final byte array into file suing given path
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
                System.out.println(currentBlock.addressCounter);
                labels.put(assemblyLine.substring(1), currentBlock.addressCounter);
                break;
            case '*':
                //create a new code block at the starting address specified
                newBlock(NumberUtility.decodeAssemblyNumber(assemblyLine.substring(1)));
                break;
            default:
                newCommand = new Command(assemblyLine);
                break;
        }
        
        if(newCommand == null)
            return;
        else
        {
            if(newCommand.needLabel)
                incompleteCommands.add(newCommand);
        }
        currentBlock.addCommand(newCommand);
                
    }
    
    public void writeToFile(String stringPath, byte[]code) throws IOException
    {
        Path path = Paths.get(stringPath);
        Files.write(path, code);
    }
    
    public void newBlock(int startingAddress)
    {
        CommandBlock block = new CommandBlock(startingAddress);
        currentBlock = block;
        blocks.add(block);
    }

}
