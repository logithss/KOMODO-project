/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Commun;

import java.util.ArrayList;
import Komodo.Commun.Instruction.AddressingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author toufik.issad
 */
public class Instructions {
    
    private static final Instruction[] instructionsArray = 
    {
        new Instruction("NOP",(byte)0x0,AddressingMode.IMPLIED),new Instruction("ADD",(byte)0x1,AddressingMode.IMMEDIATE),new Instruction("ADD",(byte)0x2,AddressingMode.ABSOLUTE),new Instruction("ADD",(byte)0x3,AddressingMode.ABSOLUTE_X),new Instruction("ADD",(byte)0x4,AddressingMode.ABSOLUTE_Y),new Instruction("ADD",(byte)0x5,AddressingMode.INDIRECT),new Instruction("ADD",(byte)0x6,AddressingMode.INDIRECT_X), new Instruction("SUB",(byte)0x7,AddressingMode.IMMEDIATE),new Instruction("SUB",(byte)0x8,AddressingMode.ABSOLUTE),new Instruction("SUB",(byte)0x9,AddressingMode.ABSOLUTE_X),new Instruction("SUB",(byte)0xa,AddressingMode.ABSOLUTE_Y),new Instruction("SUB",(byte)0xb,AddressingMode.INDIRECT),new Instruction("SUB",(byte)0xc,AddressingMode.INDIRECT_X),new Instruction("INC",(byte)0xd,AddressingMode.IMPLIED),new Instruction("INX",(byte)0xe,AddressingMode.IMPLIED),new Instruction("INY",(byte)0xf,AddressingMode.IMPLIED),
        new Instruction("DEC",(byte)0x10,AddressingMode.IMPLIED),new Instruction("DEX",(byte)0x11,AddressingMode.IMPLIED),new Instruction("DEY",(byte)0x12,AddressingMode.IMPLIED),new Instruction("SHR",(byte)0x13,AddressingMode.IMPLIED),new Instruction("SHL",(byte)0x14,AddressingMode.IMPLIED),new Instruction("AND",(byte)0x15,AddressingMode.IMPLIED),new Instruction("OR",(byte)0x16,AddressingMode.IMPLIED),new Instruction("XOR",(byte)0x17,AddressingMode.IMPLIED),new Instruction("BNZ",(byte)0x18,AddressingMode.ABSOLUTE),new Instruction("BZR",(byte)0x19,AddressingMode.ABSOLUTE),new Instruction("BCR",(byte)0x1a,AddressingMode.ABSOLUTE),new Instruction("BNG",(byte)0x1b,AddressingMode.ABSOLUTE),new Instruction("BBG",(byte)0x1c,AddressingMode.ABSOLUTE),new Instruction("BSL",(byte)0x1d,AddressingMode.ABSOLUTE),new Instruction("CMP",(byte)0x1e,AddressingMode.IMMEDIATE),new Instruction("CMP",(byte)0x1f,AddressingMode.ABSOLUTE),
        new Instruction("CMP",(byte)0x20,AddressingMode.ABSOLUTE_X),new Instruction("CMP",(byte)0x21,AddressingMode.ABSOLUTE_Y),new Instruction("CMP",(byte)0x22,AddressingMode.INDIRECT),new Instruction("CMP",(byte)0x23,AddressingMode.INDIRECT_X),new Instruction("CPX",(byte)0x24,AddressingMode.IMMEDIATE),new Instruction("CPX",(byte)0x25,AddressingMode.ABSOLUTE),new Instruction("CPX",(byte)0x26,AddressingMode.ABSOLUTE_X),new Instruction("CPX",(byte)0x27,AddressingMode.ABSOLUTE_Y),new Instruction("CPX",(byte)0x28,AddressingMode.INDIRECT),new Instruction("CPX",(byte)0x29,AddressingMode.INDIRECT_X),new Instruction("CPY",(byte)0x2a,AddressingMode.IMMEDIATE),new Instruction("CPY",(byte)0x2b,AddressingMode.ABSOLUTE),new Instruction("CPY",(byte)0x2c,AddressingMode.ABSOLUTE_X),new Instruction("CPY",(byte)0x2d,AddressingMode.ABSOLUTE_Y),new Instruction("CPY",(byte)0x2e,AddressingMode.INDIRECT),new Instruction("CPY",(byte)0x2f,AddressingMode.INDIRECT_X),
        new Instruction("CLO",(byte)0x30,AddressingMode.IMPLIED),new Instruction("CLN",(byte)0x31,AddressingMode.IMPLIED),new Instruction("CLB",(byte)0x32,AddressingMode.IMPLIED),new Instruction("CLZ",(byte)0x33,AddressingMode.IMPLIED),new Instruction("CLF",(byte)0x34,AddressingMode.IMPLIED),new Instruction("JMP",(byte)0x35,AddressingMode.IMMEDIATE),new Instruction("JMP",(byte)0x36,AddressingMode.ABSOLUTE),new Instruction("JMP",(byte)0x37,AddressingMode.ABSOLUTE_X),new Instruction("JMP",(byte)0x38,AddressingMode.ABSOLUTE_Y),new Instruction("JMP",(byte)0x39,AddressingMode.INDIRECT),new Instruction("JSR",(byte)0x3a,AddressingMode.IMMEDIATE),new Instruction("JSR",(byte)0x3b,AddressingMode.ABSOLUTE),new Instruction("JSR",(byte)0x3c,AddressingMode.ABSOLUTE_X),new Instruction("JSR",(byte)0x3d,AddressingMode.ABSOLUTE_Y),new Instruction("JSR",(byte)0x3e,AddressingMode.INDIRECT),new Instruction("RSR",(byte)0x3f,AddressingMode.IMPLIED),
        new Instruction("LDA",(byte)0x40,AddressingMode.IMMEDIATE),new Instruction("LDA",(byte)0x41,AddressingMode.ABSOLUTE),new Instruction("LDA",(byte)0x42,AddressingMode.ABSOLUTE_X),new Instruction("LDA",(byte)0x43,AddressingMode.ABSOLUTE_Y),new Instruction("LDA",(byte)0x44,AddressingMode.INDIRECT),new Instruction("LDA",(byte)0x45,AddressingMode.INDIRECT_X),/**/new Instruction("LDX",(byte)0x46,AddressingMode.IMMEDIATE),new Instruction("LDX",(byte)0x47,AddressingMode.ABSOLUTE),new Instruction("LDX",(byte)0x48,AddressingMode.ABSOLUTE_X),new Instruction("LDX",(byte)0x49,AddressingMode.ABSOLUTE_Y),new Instruction("LDX",(byte)0x4a,AddressingMode.INDIRECT),new Instruction("LDX",(byte)0x4b,AddressingMode.INDIRECT_X),/**/new Instruction("LDY",(byte)0x4c,AddressingMode.IMMEDIATE),new Instruction("LDY",(byte)0x4d,AddressingMode.ABSOLUTE),new Instruction("LDY",(byte)0x4e,AddressingMode.ABSOLUTE_X),new Instruction("LDY",(byte)0x4f,AddressingMode.ABSOLUTE_Y),
        new Instruction("LDY",(byte)0x51,AddressingMode.INDIRECT),new Instruction("LDY",(byte)0x51,AddressingMode.INDIRECT_X),/**/new Instruction("STA",(byte)0x52,AddressingMode.ABSOLUTE),new Instruction("STA",(byte)0x53,AddressingMode.ABSOLUTE_X),new Instruction("STA",(byte)0x54,AddressingMode.ABSOLUTE_Y),new Instruction("STA",(byte)0x55,AddressingMode.INDIRECT),new Instruction("STA",(byte)0x56,AddressingMode.INDIRECT_X),/**/new Instruction("STX",(byte)0x57,AddressingMode.ABSOLUTE),new Instruction("STX",(byte)0x58,AddressingMode.ABSOLUTE_X),new Instruction("STX",(byte)0x59,AddressingMode.ABSOLUTE_Y),new Instruction("STX",(byte)0x5a,AddressingMode.INDIRECT),new Instruction("STX",(byte)0x5b,AddressingMode.INDIRECT_X),/**/new Instruction("STY",(byte)0x5c,AddressingMode.ABSOLUTE),new Instruction("STY",(byte)0x5d,AddressingMode.ABSOLUTE_X),new Instruction("STY",(byte)0x5e,AddressingMode.ABSOLUTE_Y),new Instruction("STY",(byte)0x5f,AddressingMode.INDIRECT),
        new Instruction("STY",(byte)0x60,AddressingMode.INDIRECT_X),new Instruction("PHA",(byte)0x61,AddressingMode.IMPLIED),new Instruction("PHX",(byte)0x62,AddressingMode.IMPLIED),new Instruction("PLA",(byte)0x63,AddressingMode.IMPLIED),new Instruction("PLX",(byte)0x64,AddressingMode.IMPLIED),new Instruction("TAX",(byte)0x65,AddressingMode.IMPLIED),new Instruction("TAY",(byte)0x66,AddressingMode.IMPLIED),new Instruction("TXA",(byte)0x67,AddressingMode.IMPLIED),new Instruction("TYA",(byte)0x68,AddressingMode.IMPLIED),new Instruction("SAX",(byte)0x69,AddressingMode.IMPLIED),new Instruction("TSX",(byte)0x6a,AddressingMode.IMPLIED),new Instruction("TXS",(byte)0x6b,AddressingMode.IMPLIED),new Instruction("BRK",(byte)0x6c,AddressingMode.IMPLIED),new Instruction("DLY",(byte)0x6d,AddressingMode.IMMEDIATE),new Instruction("DLY",(byte)0x6e,AddressingMode.ABSOLUTE),new Instruction("DLY",(byte)0x6f,AddressingMode.INDIRECT),
        new Instruction("RNG",(byte)0x70,AddressingMode.ABSOLUTE)
    };
    private static final ArrayList<Instruction> instructionsList = new ArrayList<>(Arrays.asList(instructionsArray));
    
    //get instruction from its mnemonic (by name, returns all addressing mode possible)
    public static ArrayList<Instruction> getInstructionByMnemonic(String mnemonic) {
        List resultList = instructionsList.stream().filter(instruction -> mnemonic.equalsIgnoreCase(instruction.mnemonic)).collect(Collectors.toList());
        if(resultList.size() <=0)
            return null;
        else
            
            return new ArrayList<>(resultList);
    }
    
    //get instruction from its OpCode (operation code)
    public static Instruction getInstructionByOpcode(byte opcode){
        return instructionsList.get(opcode);
    }
    
    //print the table and print an error message if OpCode doesn't match with intruction index in list
    public static void testTable()
    {
        int i = 0;
        String lastmnemonic = "";
        System.out.println("******************TEST START***************\n");
        for(Instruction inst : instructionsArray)
        {
            System.out.println(Integer.toHexString(i) + " -> " + "OPcode : "+Integer.toHexString(inst.opcode) + ", Mnemonic : "+inst.mnemonic+", AddressingMode : "+inst.addressingMode);
            if(i != (int)inst.opcode)
                System.out.println("***Wrong OPcode at instruction "+Integer.toHexString(i)+" ***");
            if(!lastmnemonic.equals(inst.mnemonic))
            {
                lastmnemonic = inst.mnemonic;
                System.out.print("\n");
            }
            i++;
        }
        System.out.println("******************TEST END***************\n");
    }
}
