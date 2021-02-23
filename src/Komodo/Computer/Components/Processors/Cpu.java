/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors;

import Komodo.Commun.Instruction;
import Komodo.Commun.Instructions;
import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.SystemBus;

/**
 *
 * @author child
 */
public class Cpu extends Device implements Clockable{
    byte A = 0;
    byte X = 0;
    byte Y = 0;
    char pc = 0;
    char stackStart = 0;
    byte stackPointer = 2;
    byte argumentFetched = 0;

    
    public Cpu(SystemBus systembus) {
        super(systembus);
    }

    @Override
    public void clock() {
        /*1:read instruction byte
        2:look-up instruction
        3:get argument from addressing mode
        4:run instruction with argument
        */
        
        byte OPcode = systembus.accessMemory().readByte(pc); //instruction OPcode
        System.out.println((int)pc+" : "+OPcode);
        Instruction instruction = Instructions.getInstructionByOpcode(OPcode);
        //fetching argument from memory
        switch(instruction.addressingMode){
            case IMPLIED:
                implied();
                break;
            case IMMEDIATE:
                immidiate();
                break;
            case ABSOLUTE:
                absolute();
                break;
        }
        
        switch(instruction.mnemonic){
            case "NOP":
                nop();
                break;
            case "ADD":
                add();
                break;
            case "SUB":
                sub();
                break;
            case "LDA":
                lda();
                break;
        }
        
        pc++;
    }
    
    private void implied()
    {
        //execute code to fetch value using implied method
        //we do not change argumentFetched
    }
    
    private void immidiate()
    {
        //execute code to fetch value using implied method
        pc++;
        argumentFetched = systembus.accessMemory().readByte(pc);
    }
    
    private void absolute()
    {
        //execute code to fetch value using implied method
        pc++;
        char newAddress = systembus.accessMemory().readWord(pc);
        argumentFetched = systembus.accessMemory().readByte(newAddress);
    }
    
    
    //now these are the instruction methods
    
    private void nop()
    {
        //dont do anything and move on to the next cycle
        System.out.println("NOP!!!");
    }
    
    private void add()
    {
        A+=argumentFetched;
    }
    
    private void sub()
    {
        A-=argumentFetched;
    }
    
    private void lda()
    {
        A = argumentFetched;
    }
    
    
    /*void implied()
    {
        //do nothing
    }
    
    void immidiate()
    {
        //get byte and store it in value fetched
    }
    
    void absolute()
    {
        //get the bytes,get value at that address
    }
    
    void executeInstruction()
    {
        
    }
    
    void executeInstruction2_immidiate()
    {
        
    }
    
    void executeInstruction2_absolute()
    {
        
    }*/
    
    
    
    
}
