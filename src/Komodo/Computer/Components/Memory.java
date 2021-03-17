/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components;

import Komodo.Commun.NumberUtility;

/**
 *
 * @author child
 */
public class Memory extends Device{
    
    private byte[] memory;

    public Memory(SystemBus systemBus) {
        super(systemBus);
        memory = new byte[65536];
        memory[0] = 96;
        /*memory[0] = 0x3;
        memory[1] = 0x6d;
        memory[2] = (byte)0x00;
        memory[3] = (byte)0x36;
        memory[4] = (byte)0x00;
        memory[5] = (byte)0x00;
        memory[6] = (byte)0x6c;*/
        
    }
    
    //read value at address
    public byte readByte(char address)
    {
        try
        {
            //System.out.println("reading memory at: "+(int)address);
            return memory[address];
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new ArrayIndexOutOfBoundsException("Address '"+Integer.toHexString(address)+"' is out of memory range");
        }
        catch(NullPointerException e)
        {
            throw new NullPointerException("Address '"+Integer.toHexString(address)+"' is undefined");
        }
    }
    
    //read word at address(byte at address + byte at address+1)
    public char readWord(char address)
    {
        try
        {
            return NumberUtility.bytesToWord(memory[address], memory[address+1]);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            throw new ArrayIndexOutOfBoundsException("Address '"+Integer.toHexString(address)+"' is out of memory range");
        }
        catch(NullPointerException e)
        {
            throw new NullPointerException("Address '"+Integer.toHexString(address)+"' is undefined");
        }
    }
    
    //write byte at address
    public boolean writeByte(char address, byte value)
    {
        try
        {
            memory[address] = value;
            return true;
        }
        catch(ArrayIndexOutOfBoundsException | NullPointerException e)
        {
            return false;
        }
    }
    
    //write the two bytes one after another from the address
    public boolean writeWord(char address, byte highValue, byte lowValue)
    {
        try
        {         
            memory[address] = highValue;
            memory[address+1] = lowValue;
            return true;
        }
        catch(ArrayIndexOutOfBoundsException | NullPointerException e)
        {
            return false;
        }
    }
    
    //same, but takes a char
    public boolean writeWord(char address, char value)
    {
        try
        {
            memory[address] = (byte) ((value&0xff00)>>8);
            memory[address+1] = (byte) (value&0x00ff);
            return true;
        }
        catch(ArrayIndexOutOfBoundsException | NullPointerException e)
        {
            return false;
        }
    }
    
    public void flashMemory(byte[] data, int position)
    {
        if(position < 0xffff && position + data.length <= 0xffff)
            System.arraycopy(data, 0, this.memory, position, data.length);
    }
}
