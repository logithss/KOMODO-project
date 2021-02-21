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
public class Ppu extends Device implements Clockable{
    
    public Ppu(SystemBus systembus) {
        super(systembus);
    }

    @Override
    public void clock() {
        //System.out.println("ppu code");
        //this.systembus.accessSystemClock().halt();
    }
    
}
