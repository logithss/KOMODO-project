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
    public boolean halted;
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
    
    public Track(SystemBus systembus, Apu apu, int trackNumber)
    {
        this.systembus = systembus;
        this.apu = apu;
        this.trackNumber = trackNumber;
        halted = true;
    }
    
    public void updateTrack() //every time a quarter note worth of time has passed
    {
        if(halted) //track is not updated if halted
            return;
        
        beatCounter--;
        if(beatCounter > 0)
            return; //we still have quarter notes to play, dont change the sid and dont read the next instruction3
        
        boolean completed = false;
        while(!completed){
            completed = readNextInstruction(); //last note fully played, fetch next instruction
        }
    }
    
    public boolean readNextInstruction()
    {
        //read the next instruction, the pointer is always pointing to the next instruction when updateTrack is called
        //System.out.println("reading instruction at: "+Integer.toHexString(memoryPointer));
        //System.out.println("CH: "+this.trackNumber+" add: "+Integer.toHexString(memoryPointer));
        byte instruction = systembus.accessMemory().readByte(memoryPointer);
        
        //check if instruction is a note instruction (first bit set)
        Track[] tracks = new Track[3]; //track fetched from APU
        
        if( (Byte.toUnsignedInt(instruction) & (byte)0x80) > 0) //note instruction
        {
            int duration = (Byte.toUnsignedInt(instruction) & (byte)(0x7f) );
            memoryPointer++;
            byte argument1 = systembus.accessMemory().readByte(memoryPointer); //read argument from memory
            int channel = (Byte.toUnsignedInt(argument1) & (byte)(0xc0)) >> 6;
            int pitch = (Byte.toUnsignedInt(argument1) & (byte)(0x3F));
            
            //instruct the specified track using this info?
            tracks = apu.getTrack(channel);
            for(Track track: tracks)
                track.playNote(pitch, duration);
            memoryPointer++;
            return true;
            //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>: "+Integer.toHexString(memoryPointer));
        }
        else //other instruction
        {
            //System.out.println("-------------------------------------------------------");
            int opcode = (Byte.toUnsignedInt(instruction) & (byte)(0x7) );
            int channel;
            int value;
            byte argument1;
            char address;
            switch(opcode) //determine what instruction to execute
            {
                case 0://halt
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    tracks = apu.getTrack(channel);
                    for(Track track: tracks)
                        track.halt((Byte.toUnsignedInt(instruction) & (byte)(0x40)) > 0 ? true : false);
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
                        tracks = apu.getTrack(channel);
                        for(Track track: tracks)
                            track.decrementTimer();
                    }
                    else//set
                    {
                        memoryPointer++;
                        address = systembus.accessMemory().readWord(memoryPointer);
                        memoryPointer++;
                        
                        tracks = apu.getTrack(channel);
                        for(Track track: tracks)
                            track.setTimer(duration, address);
                    }
                    break;
                case 2://waveform
                    int waveform = (Byte.toUnsignedInt(instruction) & (byte)(0x60)) >> 5;
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    
                    tracks = apu.getTrack(channel);
                    for(Track track: tracks)
                        track.setWaveform(waveform);
                    break;
                case 3://volume
                    int volume = (Byte.toUnsignedInt(instruction) & (byte)(0x60)) >> 5;
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    
                    tracks = apu.getTrack(channel);
                    for(Track track: tracks)
                        track.setVolume(volume);
                    break;
                case 4://jump
                    channel = (Byte.toUnsignedInt(instruction) & (byte)(0x18)) >> 3;
                    memoryPointer++;
                    address = systembus.accessMemory().readWord(memoryPointer);
                    memoryPointer++;
                    tracks = apu.getTrack(channel);
                    for(Track track: tracks)
                        track.jump(address);
                    if(channel == this.trackNumber | channel >= apu.tracks.length)
                        this.memoryPointer--; //because we increment the memory pointer after
                    break;
                case 5://bpm
                    memoryPointer++;
                    argument1 = systembus.accessMemory().readByte(memoryPointer);
                    value = (Byte.toUnsignedInt(argument1) & (byte)(0x1f));
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
            return false;
        }
        
        //System.out.println("*******************************************> "+this.beatCounter);
    }
    
    public void playNote(int note, int duration)
    {
        //System.out.println("**playNote**"+" | "+this.trackNumber);
        //System.out.println("note: "+note);
        this.note = note;
        //System.out.println("duration: "+duration);
        this.beatCounter = duration;
        apu.sid.channels[this.trackNumber].playNote(note);
    }
    
    public void halt(boolean halt)
    {
        //System.out.println("**halt**"+" | "+this.trackNumber);
        //System.out.println("halt?: "+halt);
        this.halted = halt;
        apu.sid.channels[this.trackNumber].playNote(0); //play pitch 0 to silence the SID channel
    }
    
    public void decrementTimer()
    {
        //System.out.println("**decrementTimer**"+" | "+this.trackNumber);
        if(this.timer <= 0)
            return; //do nothing, should not happen
        this.timer ++;
    }
    
    public void setTimer(int timer, char address)
    {
        //System.out.println("**setTimer**"+" | "+this.trackNumber);
        this.timer = timer;
        this.timerAddress = address;
    }
    
    public void setWaveform(int waveform)
    {
        //System.out.println("**setWaveform**"+" | "+this.trackNumber);
        this.waveform = waveform;
        apu.sid.channels[this.trackNumber].setWaveform(waveform);
    }
    
    public void setVolume(int volume)
    {
        //System.out.println("**setVolume**"+" | "+this.trackNumber);
        this.volume = volume;
        apu.sid.channels[this.trackNumber].setVolume(volume);
    }
    
    public void jump(char address)
    {
        //System.out.println("**jump**"+" | "+this.trackNumber);
        //System.out.println("address: "+Integer.toHexString(address));
        this.memoryPointer = address;
        this.beatCounter = 0;
        /*if(!halted)
            readNextInstruction();*/
    }
}
