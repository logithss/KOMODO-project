/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import java.io.*;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author child
 */
public class Assembler {

    public void start() {

        try {
            File file = new File("resources\\AssemblyFile.txt");
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {
                
                System.out.println(scan.nextLine());

            }

            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File was not found ");
        }

    }

}
