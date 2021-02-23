/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Computer.Components.Memory;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author toufik.issad
 */
public class MemoryPanel extends GridPane implements UIPanel{
    
    private Memory memory;
    
    private char startPointer;
    private int byteCount;
    private int lineCount;
    
    public MemoryPanel(Memory memory, char startPointer)
    {
       this(memory, startPointer, 16, 16);
    }
    
    public MemoryPanel(Memory memory, char startPointer, int byteCount, int lineCount)
    {
       this.memory = memory; 
       this.startPointer = startPointer;
       this.byteCount = byteCount;
       this.lineCount = lineCount;
       update();
    }
    
    public void update()
    {
        char currentAddress = startPointer;
        //memory view
        for(int y = 0; y <lineCount; y++)
        {
            Label lineAddress = new Label("$"+Integer.toHexString(currentAddress));
            //address.setStyle("-fx-background-color:gray;");
            this.add(lineAddress, 0, y);
            Label separator = new Label("   ");
            this.add(separator, 1, y);
            for(int x = 2; x <byteCount+2; x++)
            {
                Label l = new Label(String.format("%1$02X", Byte.toUnsignedInt(memory.readByte(currentAddress))) + " ");
                this.add(l, x, y);
                currentAddress++;
            }
        }
        this.setStyle(          "-fx-border-color: black ;\n" +
                                "    -fx-border-width: 5 ; \n" +
                                "    -fx-border-style: solid");
    }
}
