/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors.Audio;

import Komodo.Computer.Components.SystemBus;

/**
 *
 * @author child
 */
public class Track {
    private boolean running;
    private SystemBus systembus;
    private Apu apu;
    private char memoryPointer = 0; //keeps track of where the next instruction is in memory
    public int trackNumber;
    
    private int beatCounter = 0; //number of quarter note still needing to be played
    private int waveform = 0;
    private float note = 0;
    private float volume = 3;
    private int timer = 0; //-
    private char timerAddress = 0;
    
    public Track(SystemBus systembus, int trackNumber)
    {
        this.systembus = systembus;
        this.apu = systembus.accessApu();
        this.trackNumber = trackNumber;
        running = false;
    }
    
    public void updateTrack() //every time a quarter note worth of time has passed
    {
        beatCounter--;
        if(beatCounter > 0)
            return; //we still have quarter notes to play, dont change the sid and dont read the next instruction
    }
    
    public void readNextInstruction()
    {
        //read the next instruction, the pointer is always pointing to the next instruction when updateTrack is called
        byte instruction = systembus.accessMemory().readByte(memoryPointer);
        
        //check if instruction is a note instruction (first bit set)
        
        if( (Byte.toUnsignedInt(instruction) & (byte)0x80) > 0) //note instruction
        {
            int duration = (Byte.toUnsignedInt(instruction) & (byte)(0x7f) );
            memoryPointer++;
            byte argument1 = systembus.accessMemory().readByte(memoryPointer); //read argument from memory
            int channel = (Byte.toUnsignedInt(instruction) & (byte)(0xc0)) >> 6;
            int pitch = (Byte.toUnsignedInt(instruction) & (byte)(0x3F));
            
            //instruct the specified track using this info?
            if(channel <=2)
            {
                apu.tracks[channel].playNote(pitch, duration);
            }
            else
            {
                //channel argument was set to 3 (undefined for now)
            }
            memoryPointer++;
        }
        else //other instruction
        {
            int opcode = (Byte.toUnsignedInt(instruction) & (byte)(0x7) );
            int channel;
            int value;
            byte argument1;
            char address;
            switch(opcode) //determine what instruction to execute
            {
                case 0://halt
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    apu.tracks[channel].halt((Byte.toUnsignedInt(instruction) & (byte)(0x40)) > 0 ? false : true);
                    break;
                case 1://timer
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    memoryPointer++;
                    argument1 = systembus.accessMemory().readByte(memoryPointer); //read argument from memory
                    int duration = Byte.toUnsignedInt(argument1);
                    //we need to see if this instruction is a set timer or decrement timer
                    if((Byte.toUnsignedInt(instruction) & (byte)(0x20)) > 1)
                    {
                        //decrement
                        apu.tracks[channel].decrementTimer();
                    }
                    else//set
                    {
                        memoryPointer++;
                        address = systembus.accessMemory().readWord(memoryPointer);
                        memoryPointer++;
                        apu.tracks[channel].setTimer(timer, address);
                    }
                    break;
                case 2://waveform
                    int waveform = (Byte.toUnsignedInt(instruction) & (byte)(0x60)) >> 5;
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    apu.tracks[channel].setWaveform(waveform);
                    break;
                case 3://volume
                    int volume = (Byte.toUnsignedInt(instruction) & (byte)(0x60)) >> 5;
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    apu.tracks[channel].setVolume(volume);
                    break;
                case 4://jump
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    memoryPointer++;
                    address = systembus.accessMemory().readWord(memoryPointer);
                    memoryPointer++;
                    apu.tracks[channel].jump(address);
                    break;
                case 5://bpm
                    memoryPointer++;
                    argument1 = systembus.accessMemory().readByte(memoryPointer);
                    value = (Byte.toUnsignedInt(instruction) & (byte)(0x1f));
                    apu.setBPM(value);
                    break;
                case 6:
                    break;
                case 7://return
                    break;
                default://illegal
                    System.out.println("something has gone teribly wrong! and u suck !");
            }
            
            memoryPointer++;
        }
    }
    
    public void playNote(int note, int duration)
    {
        this.note = note;
        this.beatCounter = duration;
        apu.sid.channels[this.trackNumber].playNote(note);
    }
    
    public void halt(boolean halt)
    {
        this.running = halt;
    }
    
    public void decrementTimer()
    {
        if(this.timer <= 0)
            return; //do nothing, should not happen
        this.timer ++;
    }
    
    public void setTimer(int timer, char address)
    {
        this.timer = timer;
        this.timerAddress = address;
    }
    
    public void setWaveform(int waveform)
    {
        this.waveform = waveform;
        apu.sid.channels[this.trackNumber].setWaveform(waveform);
    }
    
    public void setVolume(int volume)
    {
        this.volume = volume;
        apu.sid.channels[this.trackNumber].setVolume(volume);
    }
    
    public void jump(char address)
    {
        this.memoryPointer = address;
        readNextInstruction();
    }
}
