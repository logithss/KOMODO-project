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

    public Cpu(SystemBus systemBus) {
        super(systemBus);
    }

    @Override
    public void clock() {
        System.out.println("cpu code");
    }
    
    
}
