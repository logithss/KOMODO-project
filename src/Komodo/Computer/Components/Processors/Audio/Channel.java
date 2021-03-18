/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors.Audio;

import java.util.Random;

/**
 *
 * @author lojan
 */
public class Channel {

    private float volume;
    private float pitch;
    private Waveform waveform;
    Random rng = new Random();
    int i = 0;
    double pulseWidth = 0.1;

    public static enum Waveform {
        SAWTOOTH, SQUARE, TRIANGLE, RNG, SINE
    };

    public void playNote(int index) {

        this.pitch = CalculatePitch(index);

    }

    public Channel(Waveform waveform) {
        this.waveform = waveform;
    }
    
    

    public float CalculatePitch(int index) {

        /*Creating an math equation for this: https://pages.mtu.edu/~suits/NoteFreqCalcs.html */
        
        return 0;
    }

    public float getVolume() {

        return volume;
    }

    public void setVolume(int volume) {

        /*For this method, we will need a number ranging from 0 to 3, and from that 
        number, we will give a volume to it. So in this case, we will get the number 
        and multiply it by 0.33*/
        this.volume = volume * 1/3;
    }

    public double getPitch() {

        return pitch;
    }

    public void setPitch(int pitch) {

        /*For this method, we will need a number ranging from x to y, and from that number, 
        we will give it a pitch, which ranges from 131 to 2093. In this case, we get the pitch by multiplying 
        it by some number*/
        this.pitch = pitch;
    }

    public Waveform getWaveform() {

        return waveform;
    }

    public void setWaveform(int index) {

        this.waveform = Waveform.values()[index];
    }
    
    public double generateSoundOutput() {

        
        float frequency = this.pitch;
        int phase = 0;
        
        

        double t = i / ((float) 44100 / (frequency)) + phase;

        double value;

        switch (this.waveform) {
            case SAWTOOTH:
                value = 2f * (t - (float) Math.floor(t + pulseWidth));
                break;

            case SQUARE:
                value = Math.signum(Math.sin(2 * Math.PI * t) + pulseWidth);
                break;

            case TRIANGLE:
                value = 1f - 4f * (float) Math.abs(Math.round(t - 0.25f) - (t - 0.25f)); //triangle
                break;

            case SINE:
                value = Math.sin(2 * Math.PI * t);
                break;
            default:
                if (rng.nextBoolean() == true) {
                    value = (byte) ((byte) 1);
                } else {
                    value =  0;
                }
                
        }    
        
        
        i++; 
        return value;

    }

}
