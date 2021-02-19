/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors;

import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.SystemBus;

/**
 *
 * @author child
 */
public class Cpu extends Device implements Clockable{
    byte A = 0;
    byte X = 0;
    byte Y = 0;
    char programPointer = 0;
    byte stackPointer = 0;
    
    char argumentFetched = 0;

    public Cpu(SystemBus systembus) {
        super(systembus);
    }

    @Override
    public void clock() {
        //systembus.accessSystemClock().halt();
        //int a = 1+1;
        //System.out.println("");
        
        /*step:
        1:read instruction byte
        2:look-up instruction
        3:get argument from addressing mode
        4:run instruction with argument
        */
    }
    
    /*void implied()
    {
        //do nothing
    }
    
    void immidiate()
    {
        //get byte and store it in value fetched
    }
    
    void absolute()
    {
        //get the bytes,get value at that address
    }
    
    void executeInstruction()
    {
        
    }
    
    void executeInstruction2_immidiate()
    {
        
    }
    
    void executeInstruction2_absolute()
    {
        
    }*/
    
    
    
    
}
