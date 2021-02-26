/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author child
 */
public class AssemblerMain {
    
    public static void main(String[] args){
        Assembler assembler = new Assembler();
        try {
            ArrayList<File> files = new ArrayList<>();
            files.add(new File("resources\\AssemblyFile.txt"));
            assembler.assembleFiles(files, "resources\\AssemblyFile.asm");
            
        } catch (IOException ex) {
            Logger.getLogger(AssemblerMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
