/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import java.util.ArrayList;

/**
 * 
 * @author child
 */
public class CommandBlock implements Comparable<CommandBlock>{
    public int startingAddress;
    public int addressCounter;
    public int byteSize = 0;
    private ArrayList<Command> commands;
    
    public CommandBlock(int address)
    {
        startingAddress = address;
        addressCounter = startingAddress;
        commands = new ArrayList<>();
    }
    
    public void addCommand(Command command)
    {
        commands.add(command);
        addressCounter += command.bytecode.length;
        byteSize += command.bytecode.length;
    }

    public byte[] getBytecodeArray()
    {
        int copyPointer = 0;
        byte[] mergedArray = new byte[byteSize];
        for(Command c : commands){
            System.arraycopy(c.bytecode, 0, mergedArray, copyPointer, c.bytecode.length);
            copyPointer += c.bytecode.length;
        }
        
        return mergedArray;
    }
    @Override
    public int compareTo(CommandBlock other) {
        return this.startingAddress - other.startingAddress;
    }
    
}
