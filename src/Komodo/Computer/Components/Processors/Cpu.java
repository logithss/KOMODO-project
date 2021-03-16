
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
    byte stackPointer = (byte)0x0;
    byte argumentFetched = 0;
    char newAddress;
    char newestAddress;
    char displayPc=0;
    
    boolean[] flags = new boolean[4];//CNBZ

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
        //System.out.println((int) pc + " : " + OPcode);
        Instruction instruction = Instructions.getInstructionByOpcode(OPcode);
        
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

            case "INC": inc(); break;
            case "INX":inx();break;
            case "INY":iny();break;
            case "DEC":dec();break;
            case "DEX":dex();break;
            case "DEY":dey();break;
            case "SHR":shr();break;
            case "SHL":shl();break;
            case "AND":and();break;
            case "OR": or();break;
            case "XOR": xor();break;
            case "BNZ": bnz();break;
            case "BZR":bzr();break;
            case "BCR": bcr();break;
            case "BNG":bng();break;
            case "BBG":bbg();break;
            case "BSL":bsl();break;
            case "CMP":cmp();break;
            case "CPX":cpx();break;
            case "CPY":cpy();break;
            case "CLO":clo();break;
            case "CLN":cln();break;
            case "CLB":clb();break;
            case "CLZ":clz();break;
            case "CLF":clf();break;
            case "JMP":jmp();break;
            case "JSR":jsr();break;
            case "RSR":rsr();break;
            case "LDA":lda();break;
            case "LDX":ldx();break;
            case "LDY":ldy();break;
            case "STA":sta();break;
            case "STX":stx();break;
            case "STY":sty();break;
            case "PHA":pha();break;
            case "PHX":phx();break;
            case "PLA":pla();break;
            case "PLX":plx();break;
            case "TAX":tax();break;
            case "TAY":tay();break;
            case "TXA":txa();break;
            case "TYA":tya();break;
            case "SAX":sax();break;
            case "TSX":tsx();break;
            case "TXS":txs();break;
            case "BRK":brk();break;
            case "DLY":dly();break;
            case "RNG":rng();break;

        }

        pc++;
        displayPc = pc;
    }

    private void checkAllFlags() {

        
        // general method to check all flags
        // used in the clock function to check all functions
        
        
        
    }

    
    
            
    
    private void implied() {

        argumentFetched = -1;
    }

    
    
    private void immediate(){
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
    
  
    
    private void absoluteX(){
        pc++;
        newAddress = (char) (systembus.accessMemory().readWord((char)(pc)) + Byte.toUnsignedInt(X));
        System.out.println(Integer.toBinaryString(newAddress));
        argumentFetched = systembus.accessMemory().readByte(newAddress);

        pc++;
    }
    
    private void absoluteY(){
        pc++;
        newAddress = (char) (systembus.accessMemory().readWord((char)(pc)) + Byte.toUnsignedInt(Y));
        argumentFetched = systembus.accessMemory().readByte(newAddress);
        //Y = argumentFetched;
        
        pc++;
    }
       
    private void indirect(){
        
        pc++;
        newAddress = systembus.accessMemory().readWord(pc);
        newAddress = systembus.accessMemory().readWord(newAddress);
        argumentFetched = systembus.accessMemory().readByte(newAddress);
        
       
        pc++;
    }
       
    private void indirectX(){
        pc++;
        System.out.println("pc: "+(int)pc);
        newAddress = systembus.accessMemory().readWord(pc);
        System.out.println("new address1: "+Integer.toHexString(newAddress));
        //newAddress = (char) (systembus.accessMemory().readWord((char)(pc)) + Byte.toUnsignedInt(X));
        newAddress = systembus.accessMemory().readWord(newAddress);
        System.out.println("new address2: "+Integer.toHexString(newAddress));
        argumentFetched = systembus.accessMemory().readByte( (char)(newAddress+ Byte.toUnsignedInt(X)) );
        System.out.println("argument fetched: "+Integer.toHexString(argumentFetched));
        pc++;
    }
       


    // DO NOT INCLUDE IMMEDIATE
    // INCLUDE INDIRECT
    // ABS X - ADD ARG TO X AND GO TO THAT ADDRESS
    //now these are the instruction methods
 
    
    private void ifOverflowed(byte a, byte b){
        
        int result = Byte.toUnsignedInt(a) + Byte.toUnsignedInt(b);
         if(result >= 256){
            flags[0] = true;
        } 
    }
    
     private void ifNegative(byte a, byte b){
        
        int result = Byte.toUnsignedInt(a) - Byte.toUnsignedInt(b);
         if(result < 0){
             
            flags[1] = true;
        } 
    }
     
     
     
     private void nop() {
        
        
    }

    private void add() {
        
        ifOverflowed(A, argumentFetched);
        A += argumentFetched;
        
        if(A==0){
            flags[3] = true;
        }
        
    }

    private void sub() {
         ifNegative(A, argumentFetched);
        A -= argumentFetched;

        if(A==0){
            flags[3] = true;
        }
         
    }

 
    
    
    private void inc() {
        
        ifOverflowed(A, (byte)1);
        
        A++;

        if (A ==0){
              flags[3]=true;
        }
        // if register overflowed
        // if register negative
        // if register zero
    }

    private void inx() {
        ifOverflowed(X, (byte)1);
        X++;
        if (X ==0){
              flags[3]=true;
        }
    }

    private void iny() {
        ifOverflowed(Y, (byte)1);
        Y++;
        if (Y ==0){
              flags[3]=true;
        }
    }

    private void dec() {
        ifNegative(A, (byte)1);
        A--;
        
        if (A ==0){
            flags[3]=true;
        }
    }

    private void dex() {
        ifNegative(X, (byte)1);
        X--;
        
        
        if(X == 0 ){
            flags[3] = true;
        }
    }

    private void dey() {
        ifNegative(Y, (byte)1);
        Y--;
        
        if(Y == 0 ){
            flags[3] = true;
        }
    }

    
    private void shr() {
        
        int result = (int)(A>>1);
        
        if(result == 0 ){
            flags[3] = true;
        }
        
        A =  (byte)(A >> 1);

        
    }

    private void shl() {
        int result = (int)(A<<1);
        
        if(result == 0 ){
            flags[3] = true;
        }
        A =  (byte)(A << 1);

    }

    //zeros - and/or/xor/loads/pull/ transfers the final register 
    
    
    private void and() {
        
        int result =(int) (argumentFetched & A);
        
        if(result == 0 ){
            flags[3] = true;
        }
        A = (byte) (argumentFetched & A);
    }
    

    private void or() {
         int result =(int) (argumentFetched | A);
        
        if(result == 0 ){
            flags[3] = true;
        }
        A = (byte) (argumentFetched | A);
    }

    private void xor() {
         int result =(int) (argumentFetched ^ A);
        
        if(result == 0 ){
            flags[3] = true;
        }

        A = (byte) (argumentFetched ^ A);
    }

    private void bnz() {

        if (flags[3] == false) {
            pc = newAddress;
            pc--;
        }
    }

    private void bzr() {

        if (flags[3] == true) {
            pc = newAddress;
            pc--;
        }
    }

    private void bcr() {

        if (flags[0] == true) {
            pc = newAddress;
            pc--;
        }
    }

    private void bng() {

        if (flags[1] == true) {
            pc = newAddress;
            pc--;
        }
    }

    private void bbg() {

        if (flags[2] == true) {
            pc = newAddress;
            pc--;
        }
    }

    private void bsl() {

        //question if smaller  = is bigger explicitly false?
        if (flags[2] == true) {
            pc = newAddress;
            pc--;
        }

    }

    private void cmp() {//CNBZ

        /*
        A equal:B-> FALSE Z-> TRUE
        A bigger:B-> TRUE Z-> FALSE
        A smaller:B-> FALSE Z-> FALSE
        */
        int val = Byte.compare(A, argumentFetched);

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
        
        pc = newAddress;
        pc--;
    }

    private void jsr() {

        
        char stackAddress = (char) (stackStart + stackPointer);
        
        systembus.accessMemory().writeWord(stackAddress, (char)(pc+1));

        stackPointer++;
        stackPointer++;
        
        pc = newAddress;
        pc--;

        
    }

    private void rsr() {

         
         stackPointer--;
         stackPointer--;
         
         char stackAddress = (char) (stackStart + stackPointer);
         
         pc = systembus.accessMemory().readWord(stackAddress);

        
    }

    private void lda() {
        A = argumentFetched;
        
        if(A == 0){
            flags[3] = true;   
        }
    }

    private void ldx() {
        X = argumentFetched;
        
          if(X == 0){
            flags[3] = true;   
        }
    }

    private void ldy() {
        Y = argumentFetched;
        
          if(Y == 0){
            flags[3] = true;   
        }
    }

    private void sta() {
        systembus.accessMemory().writeByte(newAddress, A);
    }

    private void stx() {
         systembus.accessMemory().writeByte(newAddress, X);
    }

    private void sty() {
         systembus.accessMemory().writeByte(newAddress, Y);
    }

    public void pha() {

        char stackAddress = (char) (stackStart + stackPointer);

        systembus.accessMemory().writeByte(stackAddress, A);

        stackPointer++;

    }

    public void phx() {

        char stackAddress = (char) (stackStart + stackPointer);

        systembus.accessMemory().writeByte(stackAddress, X);

        stackPointer++;

    }

    public void pla() {

        
         stackPointer--;
         stackPointer--;
         
         char stackAddress = (char) (stackStart + stackPointer);
         
        
         
         A = systembus.accessMemory().readByte(stackAddress);
        
          
        
        if(A == 0 ){
            flags[3] = true;
        }
          
        
    }

    public void plx() {

         stackPointer--;
         stackPointer--;
         
         char stackAddress = (char) (stackStart + stackPointer);
         
         X = systembus.accessMemory().readByte(stackAddress);
         
          
        
        if(X == 0 ){
            flags[3] = true;
        }
    }

    private void tax() {
        X = A;
        
        
        
        if(X == 0 ){
            flags[3] = true;
        }
    }

    private void tay() {
        Y = A;
        
         
        
        if(Y == 0 ){
            flags[3] = true;
        }
    }

    private void txa() {
        A = X;
        
        
        if(A == 0 ){
            flags[3] = true;
        }
    }

    private void tya() {
        A = Y;
        
          
        
        if(A == 0 ){
            flags[3] = true;
        }
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
    
    private void dly(){
        
        systembus.accessSystemClock().setInteruptTime(Byte.toUnsignedInt(argumentFetched)*10);
    }

    private void rng() {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt();

        
        if(argumentFetched == -1){
             Y = Byte.valueOf(Integer.toString(randomInt));
        }
        else{
            systembus.accessMemory().writeByte(newAddress,Byte.valueOf(Integer.toString(randomInt)) );
        }   
    }
    
    public char getPC(){return this.displayPc;}
    public byte getA(){return this.A;}
    public byte getX(){return this.X;}
    public byte getY(){return this.Y;}
    public byte getStackPointer(){return this.stackPointer;}
    public boolean[] getFlags(){return this.flags;}
    
    public char getStackStart() {return this.stackStart;}
    
    public void setPC(char pc){this.pc = pc; this.displayPc = pc;}
    public void setA(byte a){this.A = a;}
    public void setX(byte x){this.X = x;}
    public void setY(byte y){this.Y = y;}
    public void setStackPointer(byte pointer){this.stackPointer = pointer;}
    public void setFlag(int index, boolean value){
        if(index < flags.length)
            flags[index]=value;
    }
    
    public void reset()
    {
        this.A = 0;
        this.X = 0;
        this.Y = 0;
        this.pc = 0;
        this.displayPc = pc;
        this.stackPointer = 0;
        
        for(int i =0; i<flags.length; i++)
            flags[i] = false;
    }

}
