/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors.Audio;

import Komodo.Computer.Components.Processors.Audio.Channel.Waveform;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * This SID class will manage the sound output generation by manipulating
 * the channel's output sound. Think of the SID as the hands that instruct the 
 * instruments, which are the channels.
 * @author lojan
 */
public class SID {
    
    /*The SID, also known as the sound interface device, will have three 
    channels*/
    public Channel[] channels = {new Channel(Waveform.SAWTOOTH), new Channel(Waveform.SQUARE), new Channel(Waveform.SAWTOOTH)};

    SourceDataLine sdl;

    /*Constructor which activates the AudioFormat and put it in SourceDataLine*/
    public SID() {

        AudioFormat af;

        try {
            af = new AudioFormat((float) 44100, 8, 1, true, false);
            sdl = AudioSystem.getSourceDataLine(af);
            sdl.open();
            sdl.start();
        } catch (LineUnavailableException e) {
            System.out.println(e);
        }
    }

    /*Method that controls the generation of the sound. The source data line reads from 
    all outputs, which are the channels*/
    public void generateSound() {

        channels[0].setPitch(440);

        double output1 = channels[0].generateSoundOutput();

        double output2 = channels[1].generateSoundOutput();
        double output3 = channels[2].generateSoundOutput();

        byte bufsum[] = new byte[1];

        bufsum[0] = (byte) (output1 * 2);

        sdl.write(bufsum, 0, 1);
    }

    /*Method that closes the sourceDataLine, which ultimately closes the whole SID*/
    public void close() {
        sdl.flush();
        sdl.close();
    }
   
        
    
}
