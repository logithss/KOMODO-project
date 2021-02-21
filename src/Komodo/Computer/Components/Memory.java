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
    
    private byte[] memory = new byte[0xFFFF];

    public Memory(SystemBus systemBus) {
        super(systemBus);
    }
    
    public byte readByte(char address)
    {
        try
        {
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
    
    public char readWord(char address)
    {
        try
        {
            return NumberUtility.bytesToChar(memory[address], memory[address+1]);
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
}
