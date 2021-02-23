/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Commun;

/**
 *
 * @author toufik.issad
 */
public class Instruction {
    public String mnemonic;
    public byte opcode;
    public AddressingMode addressingMode;
    
    public Instruction(String mnemonic, byte opcode, AddressingMode addressingMode)
    {
        this.mnemonic = mnemonic;
        this.opcode = opcode;
        this.addressingMode = addressingMode;
    }
    
    public static enum AddressingMode {IMPLIED, IMMEDIATE, ABSOLUTE, ABSOLUTE_X, ABSOLUTE_Y, INDIRECT, INDIRECT_X};
}
