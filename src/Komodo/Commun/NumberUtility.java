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
    
    public static char bytesToChar(byte highByte, byte lowByte) //combine two bytes into one char
    {
        return ( (char)((Byte.toUnsignedInt(highByte)<<8) | Byte.toUnsignedInt(lowByte)) );
    }
}
