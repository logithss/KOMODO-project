/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *///https://stackoverflow.com/questions/26265575/playing-multiple-byte-arrays-simultaneously-in-java
package Komodo.Computer.Components.Processors;

import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.Memory;
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
public class Apu extends Device implements Runnable , Clockable{
    
    public boolean running = true;
    public Apu(SystemBus systembus)
    {
        super(systembus);
    }
    
    public void run(){
        try {
            soundLoop();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(Apu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void clock() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void soundLoop() throws LineUnavailableException
    {
        int x = -3;
        double vol = 0;
        /**/
        int[] song = {
        659, 659, 0, 659, 0, 523, 659, 0, 784, 0, 0, 0, 392, 0, 0, 0, 523, 0, 0, 392, 0, 0, 330, 0, //1
        0, 440, 0, 494, 0, 466, 440, 0, 392, 659, 784, 880, 0, 698, 784, 0, 659, 0, 523, 587, 494, 
        0, 0, 0, 523, 0, 0, 392, 0, 0, 330, 0, 0, 440, 0, 494, 0, 466, 440, 0, 392, 659, 784, 880, 0, 698, 784, 
        0, 659, 0, 523, 587, 494,
        
        0, 0, 0, 262, 0, 784, 740, 698, 622, 0, 659, 0, 415, 440, 523, 0, 440, 523, 587,//2
        0, 0, 0, 784, 740, 698, 622, 0, 659, 0, 1046, 0, 1046, 1046, 0,
        0, 0, 0, 262, 0, 784, 740, 698, 622, 0, 659, 0, 415, 440, 523, 0, 440, 523, 587,
        0, 0, 622, 0, 0, 587, 0, 0, 523, 0, 98, 0, 98, 98,
        
        0, 0, 0, 262, 0, 784, 740, 698, 622, 0, 659, 0, 415, 440, 523, 0, 440, 523, 587,//replay 2
        0, 0, 0, 784, 740, 698, 622, 0, 659, 0, 1046, 0, 1046, 1046, 0,
        0, 0, 0, 262, 0, 784, 740, 698, 622, 0, 659, 0, 415, 440, 523, 0, 440, 523, 587,
        0, 0, 622, 0, 0, 587, 0, 0, 523, 0, 98, 0, 98, 98,
        
        0, 0, 0, 523, 523, 0, 523, 0, 523, 587, 0, 659, 523, 0, 440, 392, //3
        0, 98, 0, 523, 523, 0, 523, 0, 523, 587, 659, 0, 0, 0, 1046,
        0, 0, 0, 523, 523, 0, 523, 0, 523, 587, 0, 659, 523, 0, 440, 392, 0, 65, 0,
        
        659, 659, 0, 659, 0, 523, 659, 0, 784, 0, 0, 0, 392, 0, 0, 0, 523, 0, 0, 392, 0, 0, 330, 0, //replay1
        0, 440, 0, 494, 0, 466, 440, 0, 392, 659, 784, 880, 0, 698, 784, 0, 659, 0, 523, 587, 494, 
        0, 0, 0, 523, 0, 0, 392, 0, 0, 330, 0, 0, 440, 0, 494, 0, 466, 440, 0, 392, 659, 784, 880, 0, 698, 784, 
        0, 659, 0, 523, 587, 494, 0, 0, 0,
        
        659, 523, 0, 392, 0, 0, 415, 0, 440, 698, 0, 698, 440, 0, 0, //4
        494, 880, 0, 880, 880, 784, 698, 659, 523, 0, 440, 392, 0, 0, 0, 
        659, 523, 0, 392, 0, 0, 415, 0, 440, 698, 0, 698, 440, 0, 0, 0,
        494, 698, 0, 698, 698, 659, 587, 523, 659, 0, 659, 523, 0, 0,
        
        659, 523, 0, 392, 0, 0, 415, 0, 440, 698, 0, 698, 440, 0, 0, //replay4
        494, 880, 0, 880, 880, 784, 698, 659, 523, 0, 440, 392, 0, 0, 0, 
        659, 523, 0, 392, 0, 0, 415, 0, 440, 698, 0, 698, 440, 0, 0, 0,
        494, 698, 0, 698, 698, 659, 587, 523, 659, 0, 659, 523, 0, 0,
                
        523, 0, 0, 392, 0, 0, 330, 0, 0, 440, 0, 494, 0, 440, 0, 415, 0, 466, 0, 415, 415, 
        392, 392, 392, 392, 392, 392, 0, 262, 0, 0, 0, 0, 0 //5
        };
        

        int frequency = 0;
        int phase = 0;
        float pulseWidth = 0.05f;
        
        byte[] bufsum = new byte[ 1 ];
        AudioFormat af = new AudioFormat( (float )44100, 8, 1, true, false );
        SourceDataLine sdl = AudioSystem.getSourceDataLine( af );
        while(running){
            
            sdl.open();
            sdl.start();
            for( int i = 0; i < 150 * (float )44100 / 1000; i++ ) {


                double t;
                if(x <= 78)
                    t = i/((float)44100/(frequency/2)) + phase;
                else
                    t = i/((float)44100/(frequency/4)) + phase;

                double t2 = i/((float)44100/frequency) + phase;

                double t3;
                t3 = i/((float)44100/(frequency/2)) + phase;
                if(x >= 277)
                    vol = 1;
                else
                    vol = 0;

                double value = 0;
                double value2 = 0;
                double value3 = 0;
                double value4 = 0;

                //value = Math.sin(2 * Math.PI * t); //Sine
                value2 = Math.signum(Math.sin(2 * Math.PI * t2) + pulseWidth); //pulse Square
                value3 = 1f - 4f * (float)Math.abs( Math.round(t-0.25f) - (t-0.25f) ); //triangle
                value4 = 2f*(t3-(float)Math.floor(t3+pulseWidth)); //sawtooth

                bufsum[ 0 ] = (byte) (value*2 + value2*2 + value3*2 + value4*2 * vol);


                sdl.write( bufsum, 0, 1 );
            }
        
            x++;
            if(x >= 0 && x < song.length){
                frequency = song[x];
            }
            else if(x >= 0 && x > song.length)
            {
                x=0;
                frequency = song[0];
            }

        }
    }
}
