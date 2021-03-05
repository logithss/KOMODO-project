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
public class AssemblerMain {
    
    public static void main(String[] args){
        Assembler assembler = new Assembler();
            ArrayList<File> files = new ArrayList<>();
            files.add(new File("resources\\AssemblyFile.txt"));
            try {
                assembler.assembleFiles(files, "resources\\AssemblyFile.asm");
                System.out.println("Files succesfully assembled");
            } catch (Exception ex) {
                System.out.println("here");
                System.err.println(ex.getMessage());
            }
    }
}
