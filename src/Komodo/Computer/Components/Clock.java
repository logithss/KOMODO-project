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
    
    public volatile boolean running;
    public volatile boolean halted;
    public long cycleCount = 0;
    
    private double interupt = 0;
    private static long timerLast = 0;
    public double debugDelay = 0;
    
    public Clock(String name) {
        this.setName(name);
    }
    
    public void run(){
        this.running = true;
        this.halted = false;
        
        while(running)
        {
            if((getTime()-timerLast) >= interupt)
            {
                if(!halted){
                    interupt = 0;
                    clockCycle();
                    interupt += debugDelay;
                    timerLast = getTime();
                    cycleCount++;
                }
            }
        }
    }
    
    private static long getTime() //get current time (in milliseconds)
    {
        return System.currentTimeMillis();
    }
    
    public abstract void clockCycle();
    
    public void haltClock()
    {
        this.halted = true;
    }
    
    public void toggleHalt()
    {
        this.halted = !this.halted;
    }
    
    public void stopClock()
    {
        this.interupt = 0;
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
    
    public void setInteruptTime(double time) //milliseconds to halt the computer
    {
        this.interupt += time;
    }
    
}
