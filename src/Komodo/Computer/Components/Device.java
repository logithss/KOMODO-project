/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components;

/**
 *
 * @author child
 */
public abstract class Device{
    
    protected SystemBus systembus;
    
    protected boolean statusChanged = false;
    
    public Device(SystemBus systembus)
    {
        this.systembus = systembus;
    }
    
    public void hasStatusChanged()
    {
        
    }
}
