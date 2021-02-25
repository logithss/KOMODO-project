/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import java.io.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author child
 */
public class Assembler {

    public void start() {

        ArrayList<String> labels = new ArrayList<>();
        

        try {
            File file = new File("resources\\AssemblyFile.txt");
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {

                String line = scan.nextLine();

                if (line.startsWith(";")) {
                    scan.skip(line);
                } else if (line.startsWith(":")) {
                    String label = line.substring(1, line.length());
                    labels.add(label);

                } else if ("".equals(line)){ 
                    scan.skip(line);
                }

            }
            
            System.out.println(labels);

            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File was not found ");
        }

    }

}
