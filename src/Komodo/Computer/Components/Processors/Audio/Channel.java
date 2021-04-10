/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors.Audio;

import java.util.Random;

/**
 *The channel
 * @author lojan
 */
public class Channel {

    
    /*Variables 
    @param volume: corresponds to the volume of the channel 
    @param pitch: corresponds to the frequency of the noth that it will play
    @param waveform: corresponds to the waveform of the note, which can be 
                     either sawtooth, square, traingle, RNG, or sine
    */
    private float volume;
    private float pitch;
    private Waveform waveform;
    Random rng = new Random();
    int i = 0;

    /*Enums of Waveform*/
    public static enum Waveform {
        SAWTOOTH, SQUARE, TRIANGLE, RNG, SINE
    };

    /*This method plays a note of certain frequency, which is given by the math 
    equation
    @param index: the number between the desired note and the current note*/
    public void playNote(int index) {

        this.pitch = CalculatePitch(index);

    }

    /*Constructor that creates a channel */
    public Channel(Waveform waveform) {
        this.waveform = waveform;
    }

    /*This method creates a math equation to calculate the pitch of any waveform
    https://pages.mtu.edu/~suits/NoteFreqCalcs.html
    @param index: the number between the desired note and the current note*/
    public static float CalculatePitch(int index) {
        /* */

        if (index == 0) {
            return 0;

        } else {
            float n = (float) Math.pow(1.059463094359, index-1) * 131;
             
            return n;
        }
    }

    /*Method which return the volume of the channel
    @return volume of channel*/
    public float getVolume() {

        return volume;
    }

    /*The volume varies from 0 to 3 in our program. This means that the volume  
    of the computer can be either 0, 33, 66 or 100, hence why it is multilplied  
    by 1/3*/
    public void setVolume(int volume) {

        this.volume = volume * 1/3;
    }

    /*Method which returns the pitch of the channel
    @return pitch of channel*/
    public float getPitch() {

        return pitch;
    }

    
    public void setPitch(int pitch) {

        this.pitch = pitch;
    }

    /*Method which returns the current waveform of the channel
    @return waveform of channel*/
    public Waveform getWaveform() {

        return waveform;
    }

    /*This method sets a channel's waveform at some index of the array of waveforms, 
    which is an Enum*/
    public void setWaveform(int index) {

        this.waveform = Waveform.values()[index];
    }

    /*This method generate sounds depending on the channel's current waveform 
    @return the pitch at which the channel will play*/
    public double generateSoundOutput() {

        float frequency = this.pitch;
        int phase = 0;

        double t = i / ((float) 44100 / (frequency)) + phase;

        double value;

        switch (this.waveform) {
            case SAWTOOTH:
                value = 2f * (t - (float) Math.floor(t));
                break;

            case SQUARE:
                value = Math.signum(Math.sin(2 * Math.PI * t));
                break;

            case TRIANGLE:
                value = 1f - 4f * (float) Math.abs(Math.round(t - 0.25f) - (t - 0.25f));
                break;

            case SINE:
                value = Math.sin(2 * Math.PI * t);
                break;
            default:
                if (rng.nextBoolean() == true && pitch >0) {
                    value = (byte) ((byte) 1);
                } else {
                    value = 0;
                }

        }

        i++;
        
        if(i > 1000000){
            i = 0;
        }
        return value;

    }

}
