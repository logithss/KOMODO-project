/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors;

import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.SystemBus;
import javafx.application.Platform;
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
    
    private double width;
    private double height;
    
    public Ppu(SystemBus systembus) {
        super(systembus);
    }

    @Override
    public void clock() {
        render();
    }
    
    public synchronized void render()
    {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.setFill(Color.WHITE);
            gc.setFill(decodeColor((byte)0xff));
            gc.fillText(String.valueOf('A'), 100, 100);        
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
        this.width = gc.getCanvas().getWidth();
        this.height = gc.getCanvas().getHeight();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        gc.setFont(Font.font(width/40));
    }
    
}
