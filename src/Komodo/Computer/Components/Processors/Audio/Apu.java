package Komodo.Computer.Components.Processors.Audio;

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

    public Apu(SystemBus systembus) {

        super(systembus);

    }

    @Override
    public void clock() {

        soundLoop();
        //System.out.println("apu");
    }

    public void soundLoop() {

        sid.generateSound();

    }

    
}
