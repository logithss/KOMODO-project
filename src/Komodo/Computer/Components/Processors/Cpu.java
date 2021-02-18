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
    static char accumulatorA = 0;
    static char accumulatorB = 0;
    static char registerX = 0;
    
    static char programPointer = 0;
    static char stackPointer = 0;

    public Cpu(SystemBus systemBus) {
        super(systemBus);
    }

    @Override
    public void clock() {
        System.out.println("cpu code");
    }
    
    
}
