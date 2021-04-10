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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    private double fontSize = 10; //size of one font character
    
    private boolean shouldRender = false;
    private int count = 5;
    
    //constant addresses
    final char ppuRegister = 0x0009;
    final char ppuBackgroudRegister = 0x0010;
    final char charMemoryStart = 0x0600;
    final char colorMemoryStart = 0x0E00;
    
    public volatile List requests = new ArrayList<Byte>();

    public Ppu(SystemBus systembus) {
        super(systembus);

        //load font
        try {
            fontData = new FileInputStream("resources/fonts/c64.otf");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Ppu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void clock() { //COMPLETED
        //this.read();
        executeFunctions((byte)1);
    }

    private Color decodeColor(byte value) { //COMPLETED
        //byte to color -> RRRGGGBB (8 bits, 3 for red data, 3 for green data, 2 for blue)
        int red = (((Byte.toUnsignedInt(value) & 0b11100000) >> 5) * 255 / 7);
        int green = (((Byte.toUnsignedInt(value) & 0b00011100) >> 2) * 255 / 7);
        int blue = ((Byte.toUnsignedInt(value) & 0b00000011) * 255 / 3);
        return Color.rgb(red, green, blue);
    }

    public void setGC(GraphicsContext gc) { //COMPLETED
        this.gc = gc;
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        reloadFont();
    }

    private void reloadFont() { //COMPLETED
        if (fontData != null) {
            try {
                fontData = new FileInputStream("resources/fonts/c64.otf");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Ppu.class.getName()).log(Level.SEVERE, null, ex);
            }
            fontSize = gc.getCanvas().getWidth() / 40; //calculate sizeFont (one line of the screen has 40 characters, no spaces between them)
            gc.setFont(Font.loadFont(fontData, fontSize));
        } else {
            System.out.println("null font");
        }
    }

    private void executeFunctions(byte b) { //COMPLETED

        byte fetchedByte = (byte) (systembus.accessMemory().readByte(ppuRegister));
        b = fetchedByte;
        byte opcode = (byte) (b & 0b00000011);
        switch (opcode) {
            case 0:
                nop();
                break;
            case 1:
                //System.out.println("render");
                //render();
                shouldRender = true;
                clearOP();
                break;
            case 2:
                //System.out.println("copy");
                //get direction of the copy using the 3rd bit pf the fetched byte
                boolean dir = ((b & 0b00000100)) > 0;
                copy(dir);
                clearOP();
                break;
            case 3:
                //System.out.println("fill");
                fill();
                clearOP();
                break;
            default:

        }
        /*byte fetchedByte2 = (byte) (systembus.accessMemory().readByte(ppuRegister));
        
        if(fetchedByte == fetchedByte2)
            clearOP();*/
    }
    
    private void read()
    {
        List<Byte> l = getRequests();
        synchronized(l){
            byte fetchedByte = (byte) (systembus.accessMemory().readByte(ppuRegister));
            if(fetchedByte == 0)
                return;
            else{
                l.add(fetchedByte);
                clearOP();
            }
        }
    }
    
    
    
    public void processRequests()
    {
        if(shouldRender){
            render();
            shouldRender = false;
        }
        
    }
    
    public void clearOP()
    {
        //reset the ppu register to 0
        systembus.accessMemory().writeByte(ppuRegister, (byte) 0);
    }

    private void nop() {
        //no operations are performed
    }
    
    public synchronized void render() {
        //read background register and clear background
        byte backgroundByte = systembus.accessMemory().readByte(ppuBackgroudRegister);
        gc.setFill(decodeColor(backgroundByte));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        
        //draw screen
        char charIndex = 0; //number of characters already drawn
        for (int y = 0; y < 25; y++) {

            for (int x = 0; x < 40; x++) {
                //reading the color of the character from memory
                gc.setFill(decodeColor(systembus.accessMemory().readByte( (char)(colorMemoryStart + charIndex) )));
                //reading the character from memory and drawing
                char c = (char)Byte.toUnsignedInt(systembus.accessMemory().readByte( (char)(charMemoryStart + charIndex)) );
                /*if(c == 0)
                    break;*/
                
                gc.fillText(String.valueOf(c), x * fontSize, y * fontSize); //fontSize used as an offset
                charIndex++;
            }

        }
    }
    
    public void forceRender() //re-render canvas when it has been resized
    {
        reloadFont();
        render();
    }

    private void copy(boolean dir) {
        //this instruction copies a block of memory from one place to the other (overlap is supported)
        //this is done by specifying a starting position, a target position and the number of bytes to copy over
        //start address of the copy is stored at address $ppuRegister + 1
        //target address of the copy is stored at address $ppuRegister + 3
        //size value of the copy is stored at address $ppuRegister + 5
        
        char funcStart = systembus.accessMemory().readWord( (char)(ppuRegister+1) );
        char funcTarget = systembus.accessMemory().readWord( (char)(ppuRegister+3) );
        char funcSize = systembus.accessMemory().readWord( (char)(ppuRegister+5) );
        
        if (dir == true) { //direction from end to beginning
            for (int i = funcSize; i > 0; i--) {
                systembus.accessMemory().writeByte((char)(funcTarget+i), systembus.accessMemory().readByte((char)(funcStart+i)));
            }
        } 
        else{ //direction from beginning to end
            for (int i = 0; i < funcSize; i++) {
                systembus.accessMemory().writeByte((char)(funcTarget+i), systembus.accessMemory().readByte((char)(funcStart+i)));
            }
        }
    }

    private void fill() {

        char funcStart = systembus.accessMemory().readWord( (char)(ppuRegister+1) );
        char funcSize = systembus.accessMemory().readWord( (char)(ppuRegister+3) );
        byte funcValue = systembus.accessMemory().readByte( (char)(ppuRegister+5) );

        for (int i = 0; i < funcSize; i++) {
            systembus.accessMemory().writeByte((char)(funcStart+i), funcValue);
        }
    }
    
    public synchronized List<Byte> getRequests()
    {
        synchronized(requests) {
            return Collections.synchronizedList(requests);
        }
    }

}

//NOTES
// takes adress at start and use as memory[startadress]
//take byte memory[5] use and 00000011 and find op code
// if copy use 00000100  -> then check >0
// if 0 -> target + i = start + i; i++
// if 1 -> target + i = start + i ; i--;
