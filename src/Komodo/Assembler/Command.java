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
import Komodo.Assembler.Exceptions.IllegalInstructionException;
import Komodo.Assembler.Exceptions.SyntaxErrorException;
import static Komodo.Commun.NumberUtility.wordToBytes;

/**
 * The Command class will more specifically take the task of processing the 
 * assembly file. 
 * @author lojan
 */
public class Command {
    
    /*Variables 
    @param mnemonic : corresponds to the three letter instructions
    @param operand : corresponds to what accompanies the mnemonic, only some 
                     mnemonic require them
    @param bytecode: corresponds to the array of machine code that will be put 
                     into the byte file*/
    
    
    /*
        IMPLIED = nothing 
        IMMIDIATE = #,number
        ABSOLUTE = only number 
        ABSOLUTEX = x,number
        ABSOLUTEY = y,number
        INDIRECT = !,number
        INDIRECTX = x!,number
        
        DECIMAL = number 
        HEX = $number 
        BINARY = %number 
        */
    
    private String mnemonic; 
    private String operand; 
    public byte[] bytecode;
    
    //new code from logithss
    public int lineNumber;
    private String assemblyLine;
    public boolean needLabel = false;
    public String labelName = "";
    
    

    /*Constructor that creates a command object with both mnemonic and operand,
    will be useful to get fetch the mnemonic and operand.*/
    public Command(String mnemonic, String operand) {
        this.mnemonic = mnemonic;
        this.operand = operand;
    }
    /*Constructor that will create an object to process the lines of assembly  
    given to it*/
    public Command(String assemblyLine) throws IllegalInstructionException, SyntaxErrorException
    {
        this.assemblyLine = assemblyLine;
        //check first if line is a number
        String split = assemblyLine.trim().split(";")[0];
        try{
            byte b = (byte) decodeAssemblyNumber(split);
            bytecode = new byte[1];
            this.bytecode[0] = b;
        }
        catch(NumberFormatException e)
        {
            this.process();
        }
    }

    /*Method that returns the mnemonic of the command object 
    @return mnemonic of command*/
    public String getMnemonic() {
        return this.mnemonic;
    }

    /*Method that sets the mnemonic of our choice (not useful, good practice)*/
    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    /*Method that returns the operand of the command object 
    @return operand of command*/
    public String getOperand() {
        return this.operand;
    }

    /*Method that sets the operand of our choice (not useful, good practice)*/
    public void setOperand(String operand) {
        this.operand = operand;
    }
    
    /*Method that describes the command object when printing it
    @return description of command object*/
    public String toString() { 
        return "Mnemonic: " + this.mnemonic + "\nOperand: " + this.operand;
    }
    
    
    /*Method that is called when the command that requires a label, it will 
    be called when everything about that label is calculated, more precisely 
    its address*/
    public void assignAddress(int value)
    {
        if(bytecode.length == 2)
        {
           this.bytecode[1] = (byte) value; 
        }
        else if(bytecode.length == 3)
        {
            char addressWord = (char)value;
            byte[] addressBytes = NumberUtility.wordToBytes(addressWord);
            this.bytecode[1] = addressBytes[0];
            this.bytecode[2] = addressBytes[1];
        }
    }
    
    
    /*Method that processes the assembly code, read the follwoing commented  
    lines to know what is happening.*/
    public void process() throws IllegalInstructionException, SyntaxErrorException {
        
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
            
            //comment from logithss: check if arrayList is empty, which means that mnemonic doesnt exist
            //throw an exception in that case
            
            if(fetchedInstructions == null)
                throw new IllegalInstructionException("Illigal mnemonic");
            
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
            
            String[] split = parsedOperand.trim().split(";")[0].trim().split(",");
            
            /*Cases for how the argument is written. Depending on how its written 
            it will create an array with its corresponding byte code and will parse the 
            number part of it*/
            switch(split[0].toLowerCase()){
                case "!":
                    adressingMode = Instruction.AddressingMode.INDIRECT;
                    bytecode = new byte[3]; 
                    argument = split[1];
                    break;
                case "x!":
                    adressingMode = Instruction.AddressingMode.INDIRECT_X;
                    bytecode = new byte[3];
                    argument = split[1];
                    break;
                case "#":
                    adressingMode = Instruction.AddressingMode.IMMEDIATE;
                    bytecode = new byte[2];
                    argument = split[1];
                    break;
                case "x":
                    adressingMode = Instruction.AddressingMode.ABSOLUTE_X;
                    bytecode = new byte[3];
                    argument = split[1];
                    break;
                case "y":
                    adressingMode = Instruction.AddressingMode.ABSOLUTE_Y;
                    bytecode = new byte[3];
                    argument = split[1];
                    break;
                case "":
                    adressingMode = Instruction.AddressingMode.IMPLIED;
                    bytecode = new byte[1];
                    break;
                default:
                    adressingMode = Instruction.AddressingMode.ABSOLUTE;
                    bytecode = new byte[3];
                    argument = split[0];
                    
            }
        
            boolean isMatched = false;
            int counter = 0;
 
            
            /*Must check whether the instructions does in fact exist, and that it will 
            assign the corresponding adressing mode to the right instruction*/
            Instruction newInstruction = null;
            //System.out.println(adressingMode);
            while (!isMatched) {
                if (fetchedInstructions.get(counter).addressingMode == adressingMode) {
                    newInstruction = fetchedInstructions.get(counter);
                    
                    isMatched = true;
                    break;
                } else {
                    counter++;
                }

                if (counter >= fetchedInstructions.size()) {

                    throw new IllegalInstructionException("Illegal addressing mode");

                }
            }
            
            /*Label handeling: checks whether the argument is written in letters or not 
           if it is the case, then set the labelname to it*/
            if (!argument.isEmpty()) {
                try {
                    int somethingBetter = NumberUtility.decodeAssemblyNumber(argument);
                } 
                catch (NumberFormatException e) {
                    needLabel = true;
                    labelName = argument;
                }
            }
            
            /*Using the newly assigned instruction, we can now fetch the opcode 
            and put it in the first byte*/
            this.bytecode[0] = newInstruction.opcode;
            
            
            /*Once again, using the newly assigned instruction, fetch some information 
            depending on the addresing mode and assign its bytes to corresponding values */
            try{
                switch (newInstruction.addressingMode) {
                    case IMMEDIATE:
                        if(needLabel)break;
                        this.bytecode[1] = (byte) decodeAssemblyNumber(argument);
                        break;

                    case IMPLIED:
                        break;

                    default:
                        if(needLabel)break;
                        char number = (char) decodeAssemblyNumber(argument);
                        byte[] bytes = wordToBytes(number);
                        this.bytecode[1] = bytes[0];
                        this.bytecode[2] = bytes[1];
                        break;
                }
            }
            catch(NumberFormatException e)
            {
                throw new SyntaxErrorException("Incorect argument formating");
            }
        }
        
    }
}
    
    
    
    

