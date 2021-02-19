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
    private Thread apuThread;
    private KeyboardInterface keyboardInterface;
    
    SystemClock systemClock;
    
    public SystemBus()
    {
        this.memory = new Memory(this);
        this.cpu = new Cpu(this);
        this.ppu = new Ppu(this);
        //apu initialisation
        apu = new Apu(this);
        //apu.start();
        apuThread = new Thread(apu);
        
        this.keyboardInterface = new KeyboardInterface(this);
        this.systemClock = new SystemClock(this);
    }
    
    public void run(){
        powerOn();
    }
    
    public void powerOn()
    {
        reset();
        //apuThread.start();
        this.systemClock.start();
        System.out.println("end?");
    }
    
    public void powerOff()
    {
        apu.running =  false;
        this.systemClock.running = false;
    }
    
    public void reset()
    {
        //reset behavior of computer
    }
    
    @Override
    public void clock() {
        //System.out.println("aaa");
        //System.out.println("running");
        this.cpu.clock();
        this.ppu.clock();
        this.keyboardInterface.clock();
    }
    
    public Memory accessMemory() {return this.memory;}
    public SystemClock accessSystemClock() {return this.systemClock;}
}
