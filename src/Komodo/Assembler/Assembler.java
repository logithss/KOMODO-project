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
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Assembler class will be able to assemble one or more files. To be more 
 * precise, assembly files, which has many instructions for the computer to do,  
 * and turn them into machine code, which is a file that the computer will  
 * understand.
 * @author child
 */
public class Assembler {
    
    //new code from logithss
    private ArrayList<CommandBlock> blocks;
    private CommandBlock currentBlock;
    private ArrayList<Command> incompleteCommands;
    private HashMap<String, Integer> labels;
    
    
    public int assembleFiles(ArrayList<File> assemblyFiles, String exportPath) throws FileNotFoundException, IOException, SyntaxErrorException, IllegalInstructionException, Exception
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
                    catch(Exception ex)
                    {
                        throw new IllegalInstructionException(currentFile.getName()+": "+ex.getMessage() + " at line " + lineCount);
                    }
                }
                lineCount++;
            }
        }
        
        //when all files are processed, give labels to commands that need them
        /*System.out.println("************STORED LABELS**************");
        for(String i : labels.keySet())
            System.out.println("'"+i+"'");*/
        //System.out.println("************REQUESTED LABELS**************");
            for(Command command : incompleteCommands)
            {
                //System.out.println("searching label '"+command.labelName+"'");
                Integer address = labels.get(command.labelName);
                if(address != null)
                    command.assignAddress(address);
                else
                    throw new SyntaxErrorException("Undefined label '"+command.labelName+"' at line " + command.lineNumber);

            }
        
        //commands are now all ready to be written in byte file
        System.out.println("how many blocks?: "+blocks.size());
        //sort blocks by starting positions
        Collections.sort(blocks);
        
        //remove empty blocks. Cannot remove while iterating, so we use another array
        ArrayList<CommandBlock> blocksToRemove = new ArrayList<>();
        for(CommandBlock block: blocks)
        {
            System.out.println("BLOCK STARTS AT "+block.startingAddress);
            System.out.println("this block length is: "+block.byteSize);
            if(block.byteSize==0){
                blocksToRemove.add(block);
                System.out.println("block is removed");
            }
        }
        blocks.removeAll(blocksToRemove);
        
        if(blocks.size()==0)
            throw new Exception("Files empty or incorrect format, no output was generated");
        
        //calculate size of final array
        int byteCount;
        if(blocks.size() > 1) //we have multiple blocks, the number of bytes in the final file is equal to the starting address of the last 
                              //code block (already ordered before) plus its size
            byteCount = (blocks.get(blocks.size()-1).startingAddress+blocks.get(blocks.size()-1).byteSize) - blocks.get(0).startingAddress;
        else //if we only have one block, the final size is the size of the block
            byteCount = blocks.get(0).byteSize;
        
        byte[] finalByteArray = new byte[byteCount];
        System.out.println("final size: "+finalByteArray.length);
        
        //merge all instructions into one byte array
        int bytePointer = 0;
        int filledPointer = blocks.get(0).startingAddress;
        
        //value to use when filling blanks in the file
        byte blankFillValue = 0;
        
        for(CommandBlock block : blocks)
        {
                //System.out.println("block start "+block.startingAddress+", size is "+block.byteSize);
                //System.out.println("filledPointer: "+filledPointer);
                //fill array if there are blanks
                if(block.startingAddress > filledPointer){
                    System.out.println("FILL: " + bytePointer + " -> "+ (block.startingAddress - blocks.get(0).startingAddress));
                    Arrays.fill(finalByteArray, bytePointer, (block.startingAddress - filledPointer) + bytePointer, blankFillValue);
                    bytePointer += block.startingAddress - filledPointer;
                    filledPointer = block.startingAddress;
                }
                //bytePointer = block.startingAddress;
                //write block instruction into final array
                byte[] instruction = block.getBytecodeArray();
                if(instruction.length != 0){
                    System.out.println("COPY: "+ bytePointer +" SIZE: "+ block.byteSize);
                    System.arraycopy(instruction, 0, finalByteArray, bytePointer, block.byteSize);
                    bytePointer += block.byteSize;

                    filledPointer += block.byteSize;
                }
        }
        
        
        //write final byte array into file uing given path
        writeToFile(exportPath, finalByteArray);
        return finalByteArray.length;
    }
    
    public void writeToFile(String stringPath, byte[]code) throws IOException
    {
        //System.out.println("output: ");
        //for(int i : code)
            //System.out.println(Integer.toHexString(i));
        Path path = Paths.get(stringPath);
        Files.write(path, code);
    }
    
    public void interpretLine(String assemblyLine, int line) throws SyntaxErrorException, IllegalInstructionException
    {
        char prefix = assemblyLine.trim().charAt(0);
        System.out.println("PREFIX: "+prefix);
        Command newCommand = null;
        
        switch(prefix){
            case ';':
                //comment, ignore line
                break;
            case ':':
                //label, calculate address its pointing to and put in labels map
                //System.out.println(currentBlock.addressCounter);
                System.out.println("label:"+assemblyLine.replaceAll("\t", "").substring(1));
                String correctedLabel = assemblyLine.replaceAll("\t", "").split(";")[0].trim().substring(1).trim();
                labels.put(correctedLabel, currentBlock.addressCounter);
                break;
            case '/':
                //variable, considered almost like a label
                try{
                    String[] split = assemblyLine.split("="); //split the line using the '=' symbol
                    //for(String s: split)
                        //System.out.println(s);
                    String varname = split[0].trim().substring(1);
                    //System.out.println("varname: "+varname);
                    String argument = split[1].split(";")[0].trim();
                    int value = NumberUtility.decodeAssemblyNumber(argument);
                    labels.put(varname, value);
                }
                catch(NumberFormatException e)
                {
                    System.out.println("here");
                    throw new SyntaxErrorException("illegal argument");
                }
                catch(NoSuchElementException e)
                {
                    throw new SyntaxErrorException("malformed argument");
                }
                break;
            case '*':
                //create a new code block at the starting address specified
                try{
                    newBlock(NumberUtility.decodeAssemblyNumber(assemblyLine.substring(1)));
                }
                catch(NumberFormatException e)
                {
                    throw new SyntaxErrorException("illegal argument for code block insert");
                }
                break;
            default:
            {
                try {
                    System.out.println(assemblyLine);
                    newCommand = new Command(assemblyLine.trim());
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
