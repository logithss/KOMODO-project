/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components;

/**
 *
 * @author child
 */
public class Memory extends Device{
    
    private byte[] memory = new byte[0xFFFF];

    public Memory(SystemBus systemBus) {
        super(systemBus);
    }
    
    public byte readAddress(char address)
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
    
    public boolean writeValue(char address, byte value)
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
    
}
