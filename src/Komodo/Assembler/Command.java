/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Commun.Instruction;
import Komodo.Commun.Instructions;
import Komodo.Commun.NumberUtility;

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
    public boolean needLabel = true;
    public String labelName = "label2";
    
    

    public Command(String mnemonic, String operand) {
        this.mnemonic = mnemonic;
        this.operand = operand;
    }
    
    public Command(String assemblyLine)
    {
        this.assemblyLine = assemblyLine;
        this.bytecode = new byte[3];
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
    
    public void process() { 
        
        this.bytecode[0]=1;
        this.bytecode[1]=2;
        this.bytecode[2]=3;
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
        
//        Instruction fetchedInstruction = Instructions.getInstructionByMnemonic(mnemonic);
//        byte opcode = fetchedInstruction.opcode;
//        
//        bytecode[0] = opcode;
//        
//        switch (fetchedInstruction.addressingMode) {
//            case IMPLIED:
//                break;
//                
//               
//            case     
//            default:
//               
//        }
         
    }
    
    
    
    
}
