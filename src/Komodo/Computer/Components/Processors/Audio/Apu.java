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
    
    public SID sid = new SID();
    
    public Track[] tracks = new Track[3];
    
    private int bpm = 60;
    private float quarterNoteDurationMS = 1;
    private float lastTime = 0;

    public Apu(SystemBus systembus) {

        super(systembus);
        for(int i =0; i < 3; i++)
            tracks[i] = new Track(this.systembus, i);
        
        setBPM(0);
        lastTime = Clock.getTime();
    }

    @Override
    public void clock() {

        processSound();
        sid.generateSound();
    }

    public void processSound() {
        
        if( (Clock.getTime()-lastTime) >= quarterNoteDurationMS)
        {
            for(Track track : tracks)
                track.updateTrack();
            
            this.lastTime = Clock.getTime();
        }
    }
    
    public void setBPM(int value)
    {
        this.bpm = 60 + value * 10; //the value range is from 0-31 (bpm from 60 to 370)
        
        quarterNoteDurationMS = 60000/bpm/4;
    }

    public void closeApu()
    {
        sid.close();
    }
    
}
