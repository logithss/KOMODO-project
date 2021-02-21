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
public abstract class Clock extends Thread implements Runnable{
    
    public boolean running;
    private boolean halted;
    public long cycleCount = 0;
    
    public Clock() {
        resume();
    }
    
    public void run(){
        this.running = true;
        this.halted = false;
        
        while(running)
        {
            if(!halted){
                //System.out.println("***Clock cycle***");
                clockCycle();
                cycleCount++;
            }
        }
        
        //System.out.println("***Clock End : "+cycleCount+" cycles executed***");
    }
    
    public abstract void clockCycle();
    
    public void haltClock()
    {
        this.halted = true;
    }
    
    public void stopClock()
    {
        this.running = false;
    }
    
    public void resumeClock()
    {
        this.running = true;
        this.halted = false;
    }
    
    public void stepClock()
    {
        clockCycle();
        cycleCount++;
    }
    
}
