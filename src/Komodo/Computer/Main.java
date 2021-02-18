/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;


import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.SystemBus;



public class Main {
    
    public static void main(String[] args){
        SystemBus systembus = new SystemBus();
        systembus.powerOn();
    }
}
