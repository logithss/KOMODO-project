/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Assembler.Exceptions.SyntaxErrorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author child
 */
public class Test {
    
    public static void main(String[] args){
        byte a = (byte)0x7;
        int un = Byte.toUnsignedInt(a);
        System.out.println("raw: "+un);
        for(int i =0; i<5; i++)
            un += 0xff;
        System.out.println("sub: "+(byte)(un));
    }
}
