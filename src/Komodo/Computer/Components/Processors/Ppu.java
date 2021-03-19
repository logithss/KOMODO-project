/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors;

import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.SystemBus;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class Ppu extends Device implements Clockable{
    
    GraphicsContext gc;
    FileInputStream fontData;
    
    private double width;
    private double height;
    
    int i = 5;
    int x = 0;
    
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
    public void clock() {
        render();
    }
    
    public synchronized void render()
    {
        reloadFont();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        //gc.setFill(Color.WHITE);
        gc.setFill(decodeColor((byte)0xff));
        gc.fillText(String.valueOf((char)Byte.toUnsignedInt((systembus.accessMemory().readByte((char)0))) ), x, 0);

        x += i;

        if(x> gc.getCanvas().widthProperty().intValue() | x<0)
            i *= -1;
    }
    
    private Color decodeColor(byte value)
    {
        int red =  (((Byte.toUnsignedInt(value) & 0b11100000)>>5)*255/7);
        int green =  (((Byte.toUnsignedInt(value) & 0b00011100)>>2)*255/7);
        int blue =  ((Byte.toUnsignedInt(value) & 0b00000011)*255/3);
        return Color.rgb(red,green,blue);
    }
    
    public void setGC(GraphicsContext gc)
    {
        this.gc = gc;
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        
        reloadFont();
    }
    
    private void reloadFont()
    {
        if(fontData != null){
            try {
                fontData = new FileInputStream("resources/fonts/c64.otf");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Ppu.class.getName()).log(Level.SEVERE, null, ex);
            }
            gc.setFont(Font.loadFont(fontData, gc.getCanvas().getWidth()/40));
        }
        else
            System.out.println("font null");
    }
    
}
