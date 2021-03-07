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
    
    private double interupt = 0;
    private static double lastTime = 0;
    public double debugDelay = 0;
    
    public Clock(String name) {
        this.setName(name);
    }
    
    public void run(){
        this.running = true;
        this.halted = false;
        
        while(running)
        {
            if(interupt <=0)
            {
                if(!halted){
                    //System.out.println("***Clock cycle***");
                    clockCycle();
                    interupt =debugDelay;
                    cycleCount++;
                }
            }
            else
            {
                double deltaTime = ((getTime() - lastTime)/1000);
                interupt-= deltaTime;
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
        System.out.println("halt after "+this.cycleCount+" cycles");
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
    
    public void setInteruptTime(double time) //milliseconds to halt the computer
    {
        this.interupt += time;
    }
    
}
