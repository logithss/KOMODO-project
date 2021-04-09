/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors;

import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.SystemBus;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author child
 */
public class Ppu extends Device implements Clockable {

    GraphicsContext gc;
    FileInputStream fontData;

    private double width;
    private double height;

    int i = 5;
    int x = 0;
    int ppuDrawClock = 0;

    public Ppu(SystemBus systembus) {
        super(systembus);

        //Loading the font for the ppu display
        try {
            fontData = new FileInputStream("resources/fonts/c64.otf");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Ppu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // PPU Clocking function
    @Override
    public void clock() {
        render();
    }

    // Function to render one display at a time 
    public synchronized void render() {
        
        reloadFont();
        gc.setFill(Color.BLACK);    //reset to black color
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
  
        gc.setFill(decodeColor((byte) 0xff));
        gc.fillText(String.valueOf((char) Byte.toUnsignedInt((systembus.accessMemory().readByte((char) 0)))), x, 0);

        // Calling function that executes display functions
        executeFunctions();

        x += i;

        if (x > gc.getCanvas().widthProperty().intValue() | x < 0) {
            i *= -1;
        }
    }

    // Function decoding byte into rgb color code values
    private Color decodeColor(byte value) {
        int red = (((Byte.toUnsignedInt(value) & 0b11100000) >> 5) * 255 / 7); //the first three bits 
        int green = (((Byte.toUnsignedInt(value) & 0b00011100) >> 2) * 255 / 7); // the middle three bits
        int blue = ((Byte.toUnsignedInt(value) & 0b00000011) * 255 / 3);         // last two bits
        return Color.rgb(red, green, blue);
    }

    // Creates the display for the ppu
    public void setGC(GraphicsContext gc) {
        this.gc = gc;
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        reloadFont();
    }

    
    // reset font
    private void reloadFont() {
        if (fontData != null) {

            try {
                fontData = new FileInputStream("resources/fonts/c64.otf");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Ppu.class.getName()).log(Level.SEVERE, null, ex);
            }
            gc.setFont(Font.loadFont(fontData, gc.getCanvas().getWidth() / 40));
        } else {
            System.out.println("font null");
        }
    }

    
    // main function deciding which method ppu will execute depending on 5th memory byte
    private void executeFunctions() {
        //and operation to retrieve last two bits of 5th byte
        byte opCode = (byte) (systembus.accessMemory().memory[5] & 00000011); 

        
        // execute depending on opCode
        switch (opCode) {
            case 00000000:
                nop();
                break;
            case 00000001:
                draw();
                break;
            case 00000010:
                copy();
                break;
            case 00000011:
                fill();
                break;
            default:

        }

    }

    // no operation needed
    private void nop() {

    }

    // draws characters on the display
    private void drawChar() {

        int cycle = 0;
        // cycle through a 40 x 25 display
        for (int j = 0; j < 25; j++) {

            for (int k = 0; k < 40; k++) {
                
                // fill display with 1000 bytes in memory
                gc.fillText(String.valueOf(systembus.accessMemory().memory[cycle + ppuDrawClock]), k, j);
                cycle++;
            }

        }

    }

    
    // paints colors on display
    private void drawColor() {

        int cycle = 0;
        
        // cycle through a 40 x 25 display
        for (int j = 0; j < 25; j++) {
            for (int k = 0; k < 40; k++) {

                 // fill display with 1000 bytes in memory
                gc.setFill(decodeColor(systembus.accessMemory().memory[cycle + ppuDrawClock]));
                cycle++;
            }

        }

    }

    // executes drawChar() and drace Color() for every 2000 bytes
    private void draw() {

        for (int i = 0; i < systembus.accessMemory().memory.length / 2000; i++) {

            drawChar();
            ppuDrawClock += 1000;
            drawColor();
            ppuDrawClock += 1000;

        }

    }

    // copies values from one place to another in memory 
    private void copy() {
        
        //retrieve type of copy to be executed through third bit
        byte copyOp = (byte) (systembus.accessMemory().memory[5] & 00000100);
        
        // setting start points, size, and target points through memory slots 6 to 11
        char funcStart = NumberUtility.bytesToWord(systembus.accessMemory().memory[6], systembus.accessMemory().memory[7]);
        char funcSize = NumberUtility.bytesToWord(systembus.accessMemory().memory[8], systembus.accessMemory().memory[9]);
        char funcTarget = NumberUtility.bytesToWord(systembus.accessMemory().memory[10], systembus.accessMemory().memory[11]);

        // if normal copy
        if (copyOp <= 0) {

            for (int i = 0; i < funcSize; i++) {

                systembus.accessMemory().writeByte(funcTarget, systembus.accessMemory().readByte(funcStart));
                funcStart++;
                funcTarget++;

            }
            // if reverse copy
        } else if (copyOp > 1) {

            for (int i = funcSize; i > 0; i--) {
                char a = (char) i;
                
                systembus.accessMemory().writeByte((char) (funcTarget + a), systembus.accessMemory().readByte((char) (funcStart + a)));

            }
        }

    }

    // filling memory slots with one byte value
    private void fill() {

        // retrieve start points, size and byte to fill through 6th to 10th byte
        char funcStart = NumberUtility.bytesToWord(systembus.accessMemory().memory[6], systembus.accessMemory().memory[7]);
        char funcSize = NumberUtility.bytesToWord(systembus.accessMemory().memory[8], systembus.accessMemory().memory[9]);
        byte funcValue = systembus.accessMemory().memory[10];

        for (int i = 0; i < funcSize; i++) {
        // fill memory slots
            systembus.accessMemory().writeByte(funcStart, funcValue);
            funcStart++;
        }

    }

}

