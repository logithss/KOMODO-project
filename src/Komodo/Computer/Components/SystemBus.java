package Komodo.Computer.Components;

import Komodo.Computer.Components.Processors.Apu;
import Komodo.Computer.Components.Processors.Ppu;
import Komodo.Computer.Components.Processors.Cpu;

/**
 *
 * @author child
 */
public class SystemBus extends Thread implements Clockable{
    
    private Memory memory;
    private Cpu cpu;
    private Ppu ppu;
    private Apu apu;
    private KeyboardInterface keyboardInterface;
    
    SystemClock systemClock;
    
    public SystemBus()
    {
        this.memory = new Memory(this);
        this.cpu = new Cpu(this);
        this.ppu = new Ppu(this);
        //this.apu = new Apu(this);
        this.keyboardInterface = new KeyboardInterface(this);
        this.systemClock = new SystemClock(this);
    }
    
    public void run(){
        powerOn();
    }
    
    public void powerOn()
    {
        reset();
        this.systemClock.start();
    }
    
    public void powerOff()
    {
        this.systemClock.stop();
    }
    
    public void reset()
    {
        //reset behavior of computer
    }
    
    @Override
    public void clock() {
        //System.out.println("aaa");
        this.cpu.clock();
        this.ppu.clock();
        this.keyboardInterface.clock();
    }
    
    public Memory accessMemory() {return this.memory;}
    public SystemClock accessSystemClock() {return this.systemClock;}
}
