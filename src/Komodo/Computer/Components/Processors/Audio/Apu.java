package Komodo.Computer.Components.Processors.Audio;

import Komodo.Computer.Components.Clock;
import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.Memory;
import Komodo.Computer.Components.Processors.Audio.SID;
import Komodo.Computer.Components.SystemBus;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author child
 */
public class Apu extends Device implements Clockable{
    
    //constants
    final private int numberOfTracks = 3; //number of tracks supported by the APU
    final private char ApuOpcodeRegister = 0x0011; //CPU needs to write its instruction for the APU in this address
    final private char ApuAddressRegisters = 0x0012; //6 next bytes (2 bytes per address, 3 addresses for each channel) 
    
    public SID sid = new SID();
    public Track[] tracks = new Track[numberOfTracks];
    
    private int bpm = 60;
    private int quarterNoteSampleCount = 1; //number of samples duration for one quarter note (depends on the BPM)
    private int sampleCounter = 0; //counter for how much samples are left to be played

    public Apu(SystemBus systembus) {

        super(systembus);
        reset();
    }

    @Override
    public void clock() {
        //update the APU and let the tracks control the SID, then generate the sound output using the SID
        readApuRegisters();
        processSound();
        sid.generateSound();
    }
    
    private void readApuRegisters() //read the APU registers to see if the CPU has written a new instruction in memory
    {
        byte opcode = systembus.accessMemory().readByte(ApuOpcodeRegister);
        if(opcode > 0){//an opcode was written
            //check which channels are affected and if instruction is halt/unhalt
            boolean halt = (opcode & 0b00001000)>0; //1-> halt, 0-> unhalt
            System.out.println("halt: "+halt);
            if((opcode & 0b00000001)>0){
                System.out.println("ch 1");
                tracks[0].jump(systembus.accessMemory().readWord(ApuAddressRegisters)); //assuming that the cpu has already provided the jump location in the ppu registers
                tracks[0].halt(halt);
            }
            if((opcode & 0b00000010)>0){
                System.out.println("ch 2");
                tracks[1].jump(systembus.accessMemory().readWord( (char)(ApuAddressRegisters+2)) ); //+2 to get the next 2 bytes after the first address
                tracks[1].halt(halt);
            }
            if((opcode & 0b00000100)>0){
                System.out.println("ch 3");
                tracks[2].jump(systembus.accessMemory().readWord( (char)(ApuAddressRegisters+4)) ); //same
                tracks[2].halt(halt);
            }
            System.out.println("APU completed");
                
            //the opcode byte is then cleared to 0
            systembus.accessMemory().writeByte(ApuOpcodeRegister, (byte)0);
        }
            //else, no instruction from the cpu  
    }

    public void processSound() {
        if(sampleCounter <=0) //update the tracks if a quarter note of duration has passed
        {
            for(Track track : tracks)
                track.updateTrack();
            sampleCounter = quarterNoteSampleCount;
        }
        sampleCounter--;
    }
    
    public void setBPM(int value)
    {
        this.bpm = 60 + value * 10; //the value range is from 0-31 (bpm from 60 to 370)
        
        quarterNoteSampleCount = (44100*60)/bpm/4;
    }
    
    public Track[] getTrack(int index)
    {
        if(index >= numberOfTracks)
            return tracks;
        else
            return new Track[]{tracks[index]}; //if we get an index higher then the number of tracks, we return all of the tracks (normal behavior)
    }

    public void closeApu()
    {
        sid.close();
    }
    
    public void reset()
    {
        //sid reset
        this.sid.reset();
        //apu reset
        setBPM(0);
        sampleCounter = 0;
        //track reset
        for(int i =0; i < numberOfTracks; i++)
            tracks[i] = new Track(this.systembus, this, i);
    }
    
}
