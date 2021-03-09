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
    
    int n = 0;
    
    int counter = 1000;
    int c = 3;
    public Ppu(SystemBus systembus) {
        super(systembus);
        c = counter;
    }

    @Override
    public void clock() {
        render();
    }
    
    public synchronized void render()
    {
        Platform.runLater(()->{
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            gc.setFill(Color.WHITE);
            char address = 0;
            for(int y=0; y < 25; y++)
            {
                for(int x=0; x < 40; x++)
                {
                    gc.setFill(decodeColor(systembus.accessMemory().readByte(address)));
                    gc.fillText(String.valueOf(n), width/40/2 + width/40*x, height/25 + height/25*y);
                    address++;
                }
            }

            n++;
            if(n>9)
                n=0;
            });
    }
    
    private Color decodeColor(byte value)
    {
        int red =  (((Byte.toUnsignedInt(value) & 0b11100000)>>5)*255/7);
        int green =  (((Byte.toUnsignedInt(value) & 0b00011100)>>2)*255/7);
        int blue =  ((Byte.toUnsignedInt(value) & 0b00000011)*255/3);
        //System.out.println("r: "+Integer.toBinaryString(red)+" g: "+Integer.toBinaryString(green)+" b: "+Integer.toBinaryString(blue));
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
