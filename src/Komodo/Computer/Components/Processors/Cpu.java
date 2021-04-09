
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
    char stackStart = 0x100;
    byte stackPointer = (byte) 0x0;
    byte argumentFetched = 0;
    char newAddress;
    boolean lastOPisImplied = false;
    char displayPc = 0;

    boolean[] flags = new boolean[4]; //flag array

    public Cpu(SystemBus systembus) {
        super(systembus);

    }

    @Override
    public void clock() {

        //retrieve instruction OPcode
        byte OPcode = systembus.accessMemory().readByte(pc);
        lastOPisImplied = false;
        Instruction instruction = Instructions.getInstructionByOpcode(OPcode);

        if (instruction == null) {
            pc++;
            displayPc = pc;
            return;
        }

        // retrieve  addressing Mode through instruction list
        switch (instruction.addressingMode) {
            case IMPLIED:
                implied();
                break;
            case IMMEDIATE:
                immediate();
                break;
            case ABSOLUTE:
                absolute();
                break;
            case ABSOLUTE_X:
                absoluteX();
                break;
            case ABSOLUTE_Y:
                absoluteY();
                break;
            case INDIRECT:
                indirect();
                break;
            case INDIRECT_X:
                indirectX();
                break;
        }

        // retieve and execute function through instruction mnemonic
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

            case "INC":
                inc();
                break;
            case "INX":
                inx();
                break;
            case "INY":
                iny();
                break;
            case "DEC":
                dec();
                break;
            case "DEX":
                dex();
                break;
            case "DEY":
                dey();
                break;
            case "SHR":
                shr();
                break;
            case "SHL":
                shl();
                break;
            case "AND":
                and();
                break;
            case "OR":
                or();
                break;
            case "XOR":
                xor();
                break;
            case "BNZ":
                bnz();
                break;
            case "BZR":
                bzr();
                break;
            case "BCR":
                bcr();
                break;
            case "BNG":
                bng();
                break;
            case "BBG":
                bbg();
                break;
            case "BSL":
                bsl();
                break;
            case "CMP":
                cmp();
                break;
            case "CPX":
                cpx();
                break;
            case "CPY":
                cpy();
                break;
            case "CLO":
                clo();
                break;
            case "CLN":
                cln();
                break;
            case "CLB":
                clb();
                break;
            case "CLZ":
                clz();
                break;
            case "CLF":
                clf();
                break;
            case "JMP":
                jmp();
                break;
            case "JSR":
                jsr();
                break;
            case "RSR":
                rsr();
                break;
            case "LDA":
                lda();
                break;
            case "LDX":
                ldx();
                break;
            case "LDY":
                ldy();
                break;
            case "STA":
                sta();
                break;
            case "STX":
                stx();
                break;
            case "STY":
                sty();
                break;
            case "PHA":
                pha();
                break;
            case "PHX":
                phx();
                break;
            case "PLA":
                pla();
                break;
            case "PLX":
                plx();
                break;
            case "TAX":
                tax();
                break;
            case "TAY":
                tay();
                break;
            case "TXA":
                txa();
                break;
            case "TYA":
                tya();
                break;
            case "SAX":
                sax();
                break;
            case "TSX":
                tsx();
                break;
            case "TXS":
                txs();
                break;
            case "BRK":
                brk();
                break;
            case "DLY":
                dly();
                break;
            case "RNG":
                rng();
                break;

        }
        //increment counter after clock
        pc++;
        displayPc = pc;
    }

    private void implied() {

        lastOPisImplied = true;
    }

    // retrieve instruction at next address
    private void immediate() {
        pc++;
        argumentFetched = systembus.accessMemory().readByte(pc);
    }

    // retieve two following bytes and consider that as new Address
    private void absolute() {

        pc++;
        newAddress = systembus.accessMemory().readWord(pc);
        argumentFetched = systembus.accessMemory().readByte(newAddress);

        pc++;
    }

    private void absoluteX() {
        pc++;
        //add byte value in address to X register
        newAddress = (char) (systembus.accessMemory().readWord((char) (pc)) + Byte.toUnsignedInt(X));
        System.out.println(Integer.toBinaryString(newAddress));
        argumentFetched = systembus.accessMemory().readByte(newAddress);
        // sum of which is the newest Address
        pc++;
    }

    // similar to absX but Y
    private void absoluteY() {
        pc++;
        newAddress = (char) (systembus.accessMemory().readWord((char) (pc)) + Byte.toUnsignedInt(Y));
        argumentFetched = systembus.accessMemory().readByte(newAddress);
        

        pc++;
    }

    private void indirect() {
        // gets next two bytes as transitive address
        // gets the two bytes at the transitive address as the new Address
        // reads instruction at newest address
        pc++;
        newAddress = systembus.accessMemory().readWord(pc);

        newAddress = systembus.accessMemory().readWord(newAddress);

        argumentFetched = systembus.accessMemory().readByte(newAddress);

        pc++;
    }

    private void indirectX() {
        // similar to indirect - last byte is added to X register 
        // sum is considered as the last address
        pc++;
        System.out.println("pc: " + (int) pc);
        newAddress = systembus.accessMemory().readWord(pc);
        System.out.println("new address1: " + Integer.toHexString(newAddress));
        newAddress = systembus.accessMemory().readWord(newAddress);
        System.out.println("new address2: " + Integer.toHexString(newAddress));
        argumentFetched = systembus.accessMemory().readByte((char) (newAddress + Byte.toUnsignedInt(X)));
        System.out.println("argument fetched: " + Integer.toHexString(argumentFetched));
        pc++;
    }

    // check if two bytes over flow
    private void ifOverflowed(byte a, byte b) {

        int result = Byte.toUnsignedInt(a) + Byte.toUnsignedInt(b);
        if (result >= 256) {
            flags[0] = true;
        }
    }

    // check if substraction of two bytes result in negative value
    private void ifNegative(byte a, byte b) {

        int result = Byte.toUnsignedInt(a) - Byte.toUnsignedInt(b);
        if (result < 0) {

            flags[1] = true;
        }
    }

    // no operation
    private void nop() {

    }

    // add byte at current address to A  register
    private void add() {

        ifOverflowed(A, argumentFetched);
        A += argumentFetched;

        if (A == 0) {
            flags[3] = true;
        }

    }

    // substract byte at current address from A  register and put in A
    private void sub() {
        ifNegative(A, argumentFetched);
        A -= argumentFetched;

        if (A == 0) {
            flags[3] = true;
        }

    }

    // increment A register value
    private void inc() {

        ifOverflowed(A, (byte) 1);

        A++;

        if (A == 0) {
            flags[3] = true;
        }

    }

    // increment X register value
    private void inx() {
        ifOverflowed(X, (byte) 1);
        X++;
        if (X == 0) {
            flags[3] = true;
        }
    }

    // increment Y register value
    private void iny() {
        ifOverflowed(Y, (byte) 1);
        Y++;
        if (Y == 0) {
            flags[3] = true;
        }
    }

    // decrement A REGISTER
    private void dec() {
        ifNegative(A, (byte) 1);
        A--;

        if (A == 0) {
            flags[3] = true;
        }
    }

    // decrement X REGISTER
    private void dex() {
        ifNegative(X, (byte) 1);
        X--;

        if (X == 0) {
            flags[3] = true;
        }
    }

    // decrement X REGISTER
    private void dey() {
        ifNegative(Y, (byte) 1);
        Y--;

        if (Y == 0) {
            flags[3] = true;
        }
    }

    // right shift byte in A register by 1
    private void shr() {

        int result = (int) (A >> 1);

        if (result == 0) {
            flags[3] = true;
        }

        A = (byte) (A >> 1);

    }

    // left shift byte in A register by 1
    private void shl() {
        int result = (int) (A << 1);

        if (result == 0) {
            flags[3] = true;
        }
        A = (byte) (A << 1);

    }

    // apply and between current instruction byte and A and register result in A
    private void and() {

        int result = (int) (argumentFetched & A);

        if (result == 0) {
            flags[3] = true;
        }
        A = (byte) (argumentFetched & A);
    }

// apply or between current instruction byte and A and register result in A
    private void or() {
        int result = (int) (argumentFetched | A);

        if (result == 0) {
            flags[3] = true;
        }
        A = (byte) (argumentFetched | A);
    }

    // apply xor between current instruction byte and A and register result in A
    private void xor() {
        int result = (int) (argumentFetched ^ A);

        if (result == 0) {
            flags[3] = true;
        }

        A = (byte) (argumentFetched ^ A);
    }

    // check if zero is set, then branch  to newest Address created by next two bytes
    private void bnz() {

        if (flags[3] == false) {
            pc = newAddress;
            pc--;
        }
    }

    // same fucntion as bnz() but branch if zero not set
    private void bzr() {

        if (flags[3] == true) {
            pc = newAddress;
            pc--;
        }
    }

    // same as bnz() but if carry set
    private void bcr() {

        if (flags[0] == true) {
            pc = newAddress;
            pc--;
        }
    }

    // "" "" but if negative flag set
    private void bng() {

        if (flags[1] == true) {
            pc = newAddress;
            pc--;
        }
    }

    // "" "" but if bigger flag set
    private void bbg() {

        if (flags[2] == true) {
            pc = newAddress;
            pc--;
        }
    }

    // "" "" but if bigger flag not set
    private void bsl() {

        if (flags[2] == false) {
            pc = newAddress;
            pc--;
        }

    }

    private void cmp() {//CNBZ

        // compares A to instruction byte
        System.out.println();
        int val = Integer.compare(Byte.toUnsignedInt(A), Byte.toUnsignedInt(argumentFetched));

        // if equal
        System.out.println("CMP: " + val);
        if (val == 0) {
            flags[3] = true; //zero
            flags[2] = false; // bigger

            // if A > instruction byte
        } else if (val > 0) {
            flags[3] = false;
            flags[2] = true;

            // if A < instruction byte
        } else {
            flags[3] = false;
            flags[2] = false;
        }

    }

    // same as cmp() but X instead
    private void cpx() {

        int val = Byte.compare(X, argumentFetched);

        if (val == 0) {
            flags[3] = true;
            flags[2] = false;

        } else if (val > 0) {
            flags[3] = false;
            flags[2] = true;

        } else {
            flags[3] = false;
            flags[2] = false;
        }

    }

    // same as cmp() but Y instead
    private void cpy() {

        int val = Byte.compare(Y, argumentFetched);

        if (val == 0) {
            flags[3] = true;
            flags[2] = false;

        } else if (val > 0) {
            flags[3] = false;
            flags[2] = true;

        } else {
            flags[3] = false;
            flags[2] = false;
        }

    }

    // clear flag methods
    //overflowed
    private void clo() {
        flags[0] = false;
    }
// negative

    private void cln() {
        flags[1] = false;
    }
    //bigger
    private void clb() {
        flags[2] = false;
    }
    //zero
    private void clz() {
        flags[3] = false;
    }
    // all flags
    private void clf() {
        Arrays.fill(flags, false);
    }
    // jump to address created by two bytes
    private void jmp() {

        pc = newAddress;
        pc--;
    }
    
    // jump to address created by two bytes but push old address to stack
    private void jsr() {
        
        //push to stack
        char stackAddress = (char) (stackStart + Byte.toUnsignedInt(stackPointer));

        systembus.accessMemory().writeWord(stackAddress, (char) (pc));
        
        //increment stack by two bytes
        stackPointer++;
        stackPointer++;

        pc = newAddress;
        pc--;

    }

    // return to address stored in stack
    private void rsr() {
        
        // decrement stack
        stackPointer--;
        stackPointer--;

        char stackAddress = (char) (stackStart + Byte.toUnsignedInt(stackPointer));

        pc = systembus.accessMemory().readWord(stackAddress);

    }

    // load instruction byte to A
    private void lda() {
        A = argumentFetched;

        if (A == 0) {
            flags[3] = true;
        }
    }

    // load instruction byte to X
    private void ldx() {
        X = argumentFetched;

        if (X == 0) {
            flags[3] = true;
        }
    }

    // load instruction byte to Y
    private void ldy() {
        Y = argumentFetched;

        if (Y == 0) {
            flags[3] = true;
        }
    }

    // store value in A register at new Address
    private void sta() {
        systembus.accessMemory().writeByte(newAddress, A);
    }

    
    // store value in X register at new Address
    private void stx() {
        systembus.accessMemory().writeByte(newAddress, X);
    }

    
    // store value in Y register at new Address
    private void sty() {
        systembus.accessMemory().writeByte(newAddress, Y);
    }

    // push value in A to stack
    public void pha() {

        char stackAddress = (char) (stackStart + Byte.toUnsignedInt(stackPointer));

        systembus.accessMemory().writeByte(stackAddress, A);

        stackPointer++;

    }

    // push value in X to stack
    public void phx() {

        char stackAddress = (char) (stackStart + Byte.toUnsignedInt(stackPointer));

        systembus.accessMemory().writeByte(stackAddress, X);

        stackPointer++;

    }
    
    // pull value in stack into A
    public void pla() {

        stackPointer--;
        stackPointer--;

        char stackAddress = (char) (stackStart + Byte.toUnsignedInt(stackPointer));

        A = systembus.accessMemory().readByte(stackAddress);

        if (A == 0) {
            flags[3] = true;
        }

    }

    // pull value in stack into X
    public void plx() {

        stackPointer--;
        stackPointer--;

        char stackAddress = (char) (stackStart + Byte.toUnsignedInt(stackPointer));

        X = systembus.accessMemory().readByte(stackAddress);

        if (X == 0) {
            flags[3] = true;
        }
    }

    // copy A to X
    private void tax() {
        X = A;

        if (X == 0) {
            flags[3] = true;
        }
    }

    // copy A to Y
    private void tay() {
        Y = A;

        if (Y == 0) {
            flags[3] = true;
        }
    }

    // copy X to A
    private void txa() {
        A = X;

        if (A == 0) {
            flags[3] = true;
        }
    }

    // copy Y to A
    private void tya() {
        A = Y;

        if (A == 0) {
            flags[3] = true;
        }
    }

    //swap A and X
    private void sax() {

        byte aswap = X;
        byte xswap = A;

        A = aswap;
        X = xswap;
    }

    // set X  as pointer in stack
    private void tsx() {

        X = stackPointer;
        if (X == 0) {
            flags[3] = true;
        }
    }

    // reverse tsx()
    private void txs() {

        stackPointer = X;
    }

    // pause clock
    private void brk() {
        systembus.accessSystemClock().haltClock();
    }

    //delay ppu clock by 10 ms
    private void dly() {

        systembus.accessSystemClock().setInteruptTime(Byte.toUnsignedInt(argumentFetched) * 10);
    }

    // generate random int and store in new Address
    private void rng() {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt();

        if (lastOPisImplied == true) {
            Y = (byte) randomInt;
        } else {
            systembus.accessMemory().writeByte(newAddress, (byte) randomInt);
        }
    }

    public char getPC() {
        return this.displayPc;
    }

    public byte getA() {
        return this.A;
    }

    public byte getX() {
        return this.X;
    }

    public byte getY() {
        return this.Y;
    }

    public byte getStackPointer() {
        return this.stackPointer;
    }

    public boolean[] getFlags() {
        return this.flags;
    }

    public char getStackStart() {
        return this.stackStart;
    }

    public void setPC(char pc) {
        this.pc = pc;
        this.displayPc = pc;
    }

    public void setA(byte a) {
        this.A = a;
    }

    public void setX(byte x) {
        this.X = x;
    }

    public void setY(byte y) {
        this.Y = y;
    }

    public void setStackPointer(byte pointer) {
        this.stackPointer = pointer;
    }

    public void setFlag(int index, boolean value) {
        if (index < flags.length) {
            flags[index] = value;
        }
    }

    public void reset() {
        this.A = 0;
        this.X = 0;
        this.Y = 0;
        this.pc = 0;
        this.displayPc = pc;
        this.stackPointer = 0;

        for (int i = 0; i < flags.length; i++) {
            flags[i] = false;
        }
    }

}
