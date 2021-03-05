/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Commun.Instruction;
import Komodo.Commun.Instructions;
import Komodo.Commun.NumberUtility;
import static Komodo.Commun.NumberUtility.decodeAssemblyNumber;
import java.util.ArrayList;
import java.util.Scanner;
import Komodo.Assembler.Exceptions.IllegalException;
import static Komodo.Commun.NumberUtility.wordToBytes;

/**
 *
 * @author lojan
 */
public class Command {
    
    private String mnemonic; 
    private String operand; 
    public byte[] bytecode;
    
    //new code from logithss
    private String assemblyLine;
    public boolean needLabel = false;
    public String labelName = "label2";
    
    

    public Command(String mnemonic, String operand) {
        this.mnemonic = mnemonic;
        this.operand = operand;
    }
    
    public Command(String assemblyLine) throws IllegalException
    {
        this.assemblyLine = assemblyLine;
        this.process();
    }

    public String getMnemonic() {
        return this.mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getOperand() {
        return this.operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }
    
    public String toString() { 
        return "Mnemonic: " + this.mnemonic + "\nOperand: " + this.operand;
    }
    
    
    //new code from logithss
    public void assignAddress(int address)
    {
        char addressWord = (char)address;
        byte[] addressBytes = NumberUtility.wordToBytes(addressWord);
        this.bytecode[1] = addressBytes[0];
        this.bytecode[2] = addressBytes[1];
    }
    
    public void process() throws IllegalException { 
        
        Scanner parse = new Scanner(assemblyLine);
        boolean isNumeric = true; 
        String parsedText = parse.next(); 
        
        if (parsedText.startsWith("$") || parsedText.startsWith("%")) { 
            /*Possiblity that the first parsed text starts with a number, either 
            hex or binary, if that is the case, then decode it*/
            bytecode = new byte[1];
            this.bytecode[0] = (byte) decodeAssemblyNumber(parsedText);
        } else if (parsedText.isEmpty()) {
            /*Possibility that the parsed text is empty*/
            System.out.println("Parsed Text is empty");
        } else {
            /*There is a possiblity that the parsed text is still a number without 
            any symbols at the beginning*/
            try {
                Double num = Double.parseDouble(parsedText);
            } catch (NumberFormatException e) {
                isNumeric = false;
            }

        }
        
        
        if (isNumeric) {
            /*If it is indeed a number, then directly decode it */
            bytecode = new byte[1];
            this.bytecode[0] = (byte) decodeAssemblyNumber(parsedText);
        } else { 
            /*Otherwise if it does not start with a number, but rather a text 
            (mnemonic), then list all the possible instructions using the parsed 
            text */
            ArrayList<Instruction> fetchedInstructions = Instructions.getInstructionByMnemonic(parsedText);
            
            /*Next, we parsed the thing that comes after the mnemonic, which is the 
            argument*/
            
            
            String parsedOperand = "";
            Instruction.AddressingMode adressingMode;
            String argument = "";
            
            if (parse.hasNext()) {
             parsedOperand = parse.next();
            } else { 
                parsedOperand = "";
            }
            
            
            
            /*Cases for how the argument is written. Depending on how its written 
            it will create an array with its corresponding byte code and will parse the 
            number part of it*/
            if (parsedOperand.startsWith("!")) {
                adressingMode = Instruction.AddressingMode.INDIRECT;
                bytecode = new byte[3]; 
                argument = parsedOperand.substring(1, parsedOperand.length());
                        
            } else if (parsedOperand.startsWith("x!")) { 
                adressingMode = Instruction.AddressingMode.INDIRECT_X;
                bytecode = new byte[3];
                argument = parsedOperand.substring(2, parsedOperand.length());
                
            } else if (parsedOperand.startsWith("#")) { 
                adressingMode = Instruction.AddressingMode.IMMEDIATE;
                bytecode = new byte[2];
                argument = parsedOperand.substring(1, parsedOperand.length());
            } else if (parsedOperand.startsWith("x")) { 
                adressingMode = Instruction.AddressingMode.ABSOLUTE_X;
                bytecode = new byte[3];
                argument = parsedOperand.substring(1, parsedOperand.length());
            } else if (parsedOperand.startsWith("y")) { 
                adressingMode = Instruction.AddressingMode.ABSOLUTE_Y;
                bytecode = new byte[3];
                argument = parsedOperand.substring(1, parsedOperand.length());
            } else if (parsedOperand.isEmpty()) {
                adressingMode = Instruction.AddressingMode.IMPLIED;
                bytecode = new byte[1];
            } else { 
                adressingMode = Instruction.AddressingMode.ABSOLUTE;
                bytecode = new byte[3];
                argument = parsedOperand.substring(0, parsedOperand.length());
            }
            
            
            
            
            
            boolean isMatched = false;
            int counter = 0;
 
            
            /*Must check whether the instructions does in fact exist, and that it will 
            assign the corresponding adressing mode to the right instruction*/
            Instruction newInstruction = null;
            while (!isMatched) {
                if (fetchedInstructions.get(counter).addressingMode == adressingMode) {
                    newInstruction = fetchedInstructions.get(counter);
                    
                    isMatched = true;
                    break;
                } else {
                    counter++;
                }

                if (counter >= fetchedInstructions.size()) {

                    throw new IllegalException("No matches found");

                }
            }
            
            /*Label handeling: checks whether the argument is written in letters or not 
           if it is the case, then set the labelname to it*/
            if (!argument.isEmpty()) {
                try {
                int somethingBetter = NumberUtility.decodeAssemblyNumber(argument);
                } catch (NumberFormatException e) {
                     needLabel = true; 
                     labelName = argument;
                }
            }
            
            /*Using the newly assigned instruction, we can now fetch the opcode 
            and put it in the first byte*/
            this.bytecode[0] = newInstruction.opcode;

            
            /*Once again, using the newly assigned instruction, fetch some information 
            depending on the addresing mode and assign its bytes to corresponding values */
            switch (newInstruction.addressingMode) {
                case IMMEDIATE:
                    this.bytecode[1] = (byte) decodeAssemblyNumber(argument);
                    break;

                case IMPLIED:
                    break;

                default:
                    if (!needLabel) {
                    char number = (char) decodeAssemblyNumber(argument);
                    byte[] bytes = wordToBytes(number);
                    this.bytecode[1] = bytes[0];
                    this.bytecode[2] = bytes[1];
                    }
                    break;
            }
            
           
            
            
            
             
            
            
   
          
        }
        
        
    
       
        
            
        }
    
      /*
        IMPLIED = nothing 
        IMMIDIATE = #
        ABSOLUTE = only number 
        ABSOLUTEX = x
        ABSOLUTEY = y
        INDIRECT = !number
        INDIRECTX = x!number
        
        DECIMAL = normal number 
        HEX = $number 
        BINARY = %number 
        
        
        */
               
 
    
}
    
    
    
    

