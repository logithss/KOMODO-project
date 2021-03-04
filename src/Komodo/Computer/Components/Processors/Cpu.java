
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
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author child
 */
public class Cpu extends Device implements Clockable {

    byte A = 0;
    byte X = 0;
    byte Y = 0;
    char pc = 0;
    char stackStart = 0;
    byte stackPointer = 2;
    byte argumentFetched = 0;
    char newAddress;
    boolean[] flags = new boolean[4];

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
        System.out.println((int) pc + " : " + OPcode);
        Instruction instruction = Instructions.getInstructionByOpcode(OPcode);
        //fetching argument from memory
        switch (instruction.addressingMode) {
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

        switch (instruction.mnemonic) {
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

    private void implied() {
        //execute code to fetch value using implied method
        //we do not change argumentFetched
    }

    private void immidiate() {
        //execute code to fetch value using implied method
        pc++;
        argumentFetched = systembus.accessMemory().readByte(pc);
    }

    private void absolute() {
        //execute code to fetch value using implied method
        pc++;
        newAddress = systembus.accessMemory().readWord(pc);
        argumentFetched = systembus.accessMemory().readByte(newAddress);

        pc++;
    }

    // DO NOT INCLUDE IMMEDIATE
    // INCLUDE INDIRECT
    // ABS X - ADD ARG TO X AND GO TO THAT ADDRESS
    //now these are the instruction methods
    private void nop() {
        //dont do anything and move on to the next cycle
        System.out.println("NOP!!!");
    }

    private void add() {
        A += argumentFetched;

        //overflowed
    }

    private void sub() {
        A -= argumentFetched;

        //overflowed
    }

    private void inc() {
        A++;

        // if register overflowed
        // if register negative
        // if register zero
    }

    private void inx() {
        X++;
    }

    private void iny() {
        Y++;
    }

    private void dec() {
        A--;
    }

    private void dex() {
        X--;
    }

    private void dey() {
        Y--;
    }

    private void SHR() {
        argumentFetched = (byte) (argumentFetched >> 1);

    }

    private void SHL() {
        argumentFetched = (byte) (argumentFetched << 1);

    }

    private void and() {
        A = (byte) (argumentFetched & A);
    }

    private void or() {
        A = (byte) (argumentFetched | A);
    }

    private void xor() {

        A = (byte) (argumentFetched ^ A);
    }

    private void cmp() {

        // when comparing change the bigger flag and zero flag
        // A > arg big flag set
        // A = arg zero flag set
        // A < arg big flag explicitly not set
    }

    private void bnz() {

        if (flags[3] = false) {
            //use newAddress
        }
    }

    private void bzr() {

        if (flags[3] = true) {
            //use newAddress
        }
    }

    private void clo() {
        flags[0] = false;
    }

    private void cln() {
        flags[1] = false;
    }

    private void clb() {
        flags[2] = false;
    }

    private void clz() {
        flags[3] = false;
    }

    private void clf() {
        Arrays.fill(flags, false);
    }

    private void jmp() {

        //use newAddress
    }

    private void lda() {
        A = argumentFetched;
    }

    private void ldx() {
        X = argumentFetched;
    }

    private void ldy() {
        Y = argumentFetched;
    }

    private void sta() {
        argumentFetched = A;
    }

    private void stx() {
        argumentFetched = X;
    }

    private void sty() {
        argumentFetched = Y;
    }

    public void pha() {

        char stackAddress = (char) (stackStart + stackPointer);

        systembus.accessMemory().writeByte(stackAddress, A);

        stackPointer++;

    }

    private void tax() {
        X = A;
    }

    private void tay() {
        Y = A;
    }

    private void txa() {
        A = X;
    }

    private void tya() {
        A = Y;
    }

    private void sax() {

        byte aswap = X;
        byte xswap = A;

        A = aswap;
        X = xswap;
    }

    private void tsx() {

        X = stackPointer;
    }

    private void txs() {

        stackPointer = X;
    }

    private void brk() {
        systembus.accessSystemClock().haltClock();
    }

    private void rng() {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt();

        Y = Byte.valueOf(Integer.toString(randomInt));
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
