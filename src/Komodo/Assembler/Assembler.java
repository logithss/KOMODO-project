/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Assembler.Exceptions.IllegalInstructionException;
import Komodo.Assembler.Exceptions.SyntaxErrorException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    
    public void assembleFiles(ArrayList<File> assemblyFiles, String exportPath) throws FileNotFoundException, IOException, SyntaxErrorException, IllegalInstructionException, Exception
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
            int lineCount = 1;
            //interpret the file line by line
            while (scan.hasNext()) {
                String assemblyLine = scan.nextLine();
                if(!assemblyLine.isEmpty())
                {
                    try {
                        interpretLine(assemblyLine, lineCount);
                    } catch (SyntaxErrorException ex) {
                        throw new SyntaxErrorException(currentFile.getName()+": "+ex.getMessage() + " at line " + lineCount);
                    } catch (IllegalInstructionException ex) {
                        throw new IllegalInstructionException(currentFile.getName()+": "+ex.getMessage() + " at line " + lineCount);
                    }
                }
                lineCount++;
            }
        }
        
        //when all files are processed, give labels to commands that need them
            for(Command command : incompleteCommands)
            {
                Integer address = labels.get(command.labelName);
                if(address != null)
                    command.assignAddress(address);
                else
                    throw new SyntaxErrorException("Undefined label '"+command.labelName+"' at line " + command.lineNumber);

            }
        
        //commands are now all ready to be written in byte file
        
        //sort blocks by starting positions
        Collections.sort(blocks);
        
        //remove empty blocks. Cannot remove while iterating, so we use another array
        ArrayList<CommandBlock> blocksToRemove = new ArrayList<>();
        for(CommandBlock block: blocks)
        {
            if(block.byteSize==0)
                blocksToRemove.add(block);
        }
        blocks.removeAll(blocksToRemove);
        
        if(blocks.size()==0)
            throw new Exception("Files empty or incorrect format, no output was generated");
        
        //calculate size of final array
        int byteCount = blocks.get(blocks.size()-1).startingAddress+blocks.get(blocks.size()-1).byteSize;
        byte[] finalByteArray = new byte[byteCount];
        
        //merge all instructions into one byte array
        int bytePointer = blocks.get(0).startingAddress;
        int filledPointer = bytePointer;
        
        //value to use when filling blanks in the file
        byte blankFillValue = 0;
        
        for(CommandBlock block : blocks)
        {
                System.out.println("block start "+block.startingAddress+", size is "+block.byteSize);
                //System.out.println("filledPointer: "+filledPointer);
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
        
        
        //write final byte array into file uing given path
        writeToFile(exportPath, finalByteArray);
    }
    
    public void writeToFile(String stringPath, byte[]code) throws IOException
    {
        System.out.println("output: ");
        for(int i : code)
            System.out.println(Integer.toHexString(i));
        Path path = Paths.get(stringPath);
        Files.write(path, code);
    }
    
    public void interpretLine(String assemblyLine, int line) throws SyntaxErrorException, IllegalInstructionException
    {
        char prefix = assemblyLine.charAt(0);
        
        Command newCommand = null;
        
        switch(prefix){
            case ';':
                //comment, ignore line
                break;
            case ':':
                //label, calculate address its pointing to and put in labels map
                //System.out.println(currentBlock.addressCounter);
                labels.put(assemblyLine.substring(1), currentBlock.addressCounter);
                break;
            case '*':
                //create a new code block at the starting address specified
                newBlock(NumberUtility.decodeAssemblyNumber(assemblyLine.substring(1)));
                break;
            default:
            {
                try {
                    newCommand = new Command(assemblyLine);
                    newCommand.lineNumber = line;
                } catch (IllegalInstructionException ex) {
                    throw ex;
                    //newCommand = null;
                }
                catch (SyntaxErrorException ex) {
                    throw ex;
                    //newCommand = null;
                }
            }
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
    
    public void newBlock(int startingAddress)
    {
        CommandBlock block = new CommandBlock(startingAddress);
        currentBlock = block;
        blocks.add(block);
    }

}
