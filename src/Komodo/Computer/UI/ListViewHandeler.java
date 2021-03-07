/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Computer.Components.Memory;
import Komodo.Commun.NumberUtility;

/**
 *
 * @author child
 */
public class ListViewHandeler {
    private Memory memory;
    
    public char start;
    public char end;
    public int size;
    public boolean curentIsMiddle;
    
    public ListViewHandeler(Memory memory, char start, char end, int size)
    {
        this.memory = memory;
        this.start = start;
        this.end = end;
        this.size = size;
    }
    
    public byte[] fetchValues(char current)
    {
        int index = curentIsMiddle ? -(size/2) : 0;
        byte[] values = new byte[size];
        
        for(int i = 0; i < size; i++)
        {
            if(verifyAddress((char)(start+current+index+i)))
            {
                values[i] = memory.readByte((char)(start+current+index+i));
            }
            else{
                values[i] = 100;
                //System.out.println("illigal index");
            }
        }
        
        return values;
    }
    
    private boolean verifyAddress(char value)
    {
        if(value >=start && value <= end && value <= 0xffff)
            return true;
        return false;
    }
    
}
