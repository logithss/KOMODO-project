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
    
    public double interupt = 0;
    private long timerLast = 0;
    public long debugDelay = 0;
    
    private long timecounter = 0;
    
    public Clock(String name) {
        this.setName(name);
    }
    
    public void run(){
        this.running = true;
        //this.halted = false;
        interupt += debugDelay;
        
        while(running)
        {
            timecounter += (getTime()-timerLast);
            /*System.out.println("stopped: "+(getTime()-timerLast));
            System.out.println("get: "+getTime());
            System.out.println("---------");*/
            if(timecounter >= interupt)
            {
                if(!halted){
                    interupt = 0;
                    clockCycle();
                    interupt += debugDelay;
                    //System.out.println(interupt);
                    cycleCount++;
                }
                
                timecounter = 0;
            }
            
            timerLast = getTime();
            //System.out.println("last: "+timerLast);
        }
    }
    
    public static long getTime() //get current time (in milliseconds)
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
        interupt = 0;
        clockCycle();
        cycleCount++;
    }
    
    public void setInteruptTime(double time) //milliseconds to halt the computer
    {
        this.interupt += time;
    }
    
}
