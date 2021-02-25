/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Commun.Instruction;
import Komodo.Commun.Instructions;

/**
 *
 * @author lojan
 */
public class Command {
    
    private String mnemonic; 
    private String operand; 
    private byte[] bytecode;
    

    public Command(String mnemonic, String operand) {
        this.mnemonic = mnemonic;
        this.operand = operand;
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
    
    public void process() { 
        
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
