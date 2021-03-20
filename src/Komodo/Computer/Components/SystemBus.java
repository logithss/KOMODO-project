package Komodo.Computer.Components;

import Komodo.Computer.Components.Processors.KeyScanner;
import Komodo.Computer.Components.Processors.Audio.Apu;
import Komodo.Computer.Components.Processors.Ppu;
import Komodo.Computer.Components.Processors.Cpu;

/**
 *
 * @author child
 */
public class SystemBus implements Clockable{
    
    private Memory memory;
    private Cpu cpu;
    private Ppu ppu;
    public Apu apu;
    private KeyScanner keyboardScanner;
    
    private SystemClock systemClock;
    private SystemClock apuClock;
    
    public boolean on;
    
    public SystemBus()
    {
        this.memory = new Memory(this);
        this.cpu = new Cpu(this);
        this.ppu = new Ppu(this);
        this.apu = new Apu(this);
        this.keyboardScanner = new KeyScanner(this);
        //clocks
        
        systemClock = new SystemClock("System Clock", this);
        apuClock = new SystemClock("APU Clock", apu);
    }
    
    public void run(){
        powerOn();
    }
    
    public void powerOn()
    {
        on = true;
        reset();
        this.apuClock.start();
        this.systemClock.haltClock();
        this.systemClock.start();
    }
    
    public void powerOff()
    {
        on = false;
        apu.closeApu();
        apuClock.stopClock();
        systemClock.stopClock();
        
        systemClock = new SystemClock("System Clock", this);
        apuClock = new SystemClock("APU Clock", apu);
    }
    
    public void reset()
    {
        cpu.reset();
    }
    
    @Override
    public void clock() {
        this.cpu.clock();
        //this.ppu.clock();
        this.apu.clock();
        //this.keyboardScanner.clock();
    }
    
    public Memory accessMemory() {return this.memory;}
    
    public Clock accessSystemClock() {return this.systemClock;}
    
    public Cpu accessCpu() {return this.cpu;}
    public Ppu accessPpu() {return this.ppu;}
    public Apu accessApu() {return this.apu;}
}
