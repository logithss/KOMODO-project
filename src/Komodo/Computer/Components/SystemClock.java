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
public class SystemClock extends Device{
    
    public boolean running;
    private boolean halted;
    
    public long cycleCount = 0;
    
    public SystemClock(SystemBus systembus) {
        super(systembus);
        enable();
    }
    
    public void start()
    {
        this.running = true;
        this.halted = false;
        
        while(running)
        {
            if(!halted){
                //System.out.println("***Clock cycle***");
                this.systembus.clock();
                cycleCount++;
            }
            
            //System.out.println("***Clock End : "+cycleCount+" cycles executed***");
        }
    }
    
    public void halt()
    {
        this.halted = true;
    }
    
    public void stop()
    {
        this.running = false;
    }
    
    public void enable()
    {
        this.running = true;
        this.halted = false;
    }
    
}
