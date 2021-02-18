/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;


import Komodo.Commun.Instruction;
import Komodo.Commun.Instructions;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.SystemBus;



public class Main {
    
    public static void main(String[] args){
        //SystemBus systembus = new SystemBus();
        //systembus.powerOn();
        
        byte a = -125;
        byte b = 7;
        
        byte c = (byte)(a+b);
        
        System.out.println(c);
        System.out.println(Byte.toUnsignedInt(c));
        System.out.println( Integer.toBinaryString(Byte.toUnsignedInt(c)) );
        
        
        //1098 -> 1098
        //0000000000000000000110101 -> 10010010
        //1000111 -> 1000111
        
        //wrong method
        System.out.println( "wrong : "+Integer.toBinaryString((int)(c)) );
        
        //print the instruction table
        Instructions.testTable();
    }
}
