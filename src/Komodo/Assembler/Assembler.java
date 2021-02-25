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

    ArrayList<String> labels = new ArrayList<>();
    ArrayList<String> commentsToIgnore = new ArrayList<>();
    ArrayList<Command> commands = new ArrayList<>();

    public void start() {

        try {
            File file = new File("resources\\AssemblyFile.txt");
            Scanner scan = new Scanner(file);

            while (scan.hasNext()) {

                String line = scan.nextLine();

                if (line.startsWith(":")) {
                    String label = line.substring(1, line.length());
                    labels.add(label);

                } else if (line.startsWith(";")) {
                    String comments = line.substring(1, line.length());
                    commentsToIgnore.add(comments);
                } else if (line.isEmpty()) {
                    scan.skip(line);
                } else {
                    String mnemonic = line.substring(0,3);
                    String operand = line.substring(3,line.length());
                    
                    Command instruction = new Command(mnemonic , operand);
                    commands.add(instruction);
                    
                    
                }

            }

            System.out.println(labels);
            System.out.println(commentsToIgnore);
            System.out.println(commands);

            scan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File was not found ");
        }

    }

}
