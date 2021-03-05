/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Commun;

/**
 *
 * @author child
 */
public class NumberUtility {
    
    public static char bytesToWord(byte highByte, byte lowByte) //combine two bytes into one char
    {
        return ( (char)((Byte.toUnsignedInt(highByte)<<8) | Byte.toUnsignedInt(lowByte)) );
    }
    
    public static byte[] wordToBytes(char word) //combine two bytes into one char
    {
        byte[] bytes = new byte[2];
        //System.out.println("byte high after shift: "+ ((word>>8)&0xff) );
        bytes[0] = (byte)((word>>8)&0xff);
        //System.out.println( "print: "+Integer.toHexString(Byte.toUnsignedInt(bytes[0])));
        bytes[1] = (byte)((word)&0xff);
        return bytes;
    }
    
    public static byte getUnsignedByte(int number)
    {
        return (byte)number;
    }
    
    public static int decodeAssemblyNumber(String number)
    {
        
        
        try {
            
             switch(number.charAt(0))
        {
            case '$':
                //number is hex
                return Integer.parseInt(number.substring(1), 16);
            case '%':
                //number is binary
                return Integer.parseInt(number.substring(1), 2);
            default:
                return Integer.parseInt(number, 10);
            
        }
            
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
      
    }
}
