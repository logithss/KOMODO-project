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
import java.util.Arrays;
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

    int i = 5;
    int x = 0;
    int ppuDrawClock = 0;

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

    public synchronized void render() {
        reloadFont();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        //gc.setFill(Color.WHITE);
        gc.setFill(decodeColor((byte) 0xff));
        gc.fillText(String.valueOf((char) Byte.toUnsignedInt((systembus.accessMemory().readByte((char) 0)))), x, 0);
        
        executeFunctions();

        x += i;

        if (x > gc.getCanvas().widthProperty().intValue() | x < 0) {
            i *= -1;
        }
    }

    private Color decodeColor(byte value) {
        int red = (((Byte.toUnsignedInt(value) & 0b11100000) >> 5) * 255 / 7);
        int green = (((Byte.toUnsignedInt(value) & 0b00011100) >> 2) * 255 / 7);
        int blue = ((Byte.toUnsignedInt(value) & 0b00000011) * 255 / 3);
        return Color.rgb(red, green, blue);
    }

    public void setGC(GraphicsContext gc) {
        this.gc = gc;
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setTextBaseline(VPos.TOP);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);

        reloadFont();
    }

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

    private void executeFunctions() {

        byte opCode = (byte) (systembus.accessMemory().memory[5] & 00000011);

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

    private void nop() {

    }


    private void drawChar() {

        for (int i = 0; i < systembus.accessMemory().memory.length; i++) {

            for (int j = 0; j < 25; j++) {

                for (int k = 0; k < 40; k++) {

                    gc.fillText(String.valueOf(systembus.accessMemory().memory[i + ppuDrawClock]), k, j);

                }

            }

        }
    }

    private void drawColor() {

        for (int i = 0; i < systembus.accessMemory().memory.length; i++) {
            for (int j = 0; j < 25; j++) {
                for (int k = 0; k < 40; k++) {

                    gc.setFill(decodeColor(systembus.accessMemory().memory[i + ppuDrawClock]));
                }

            }

        }
    }

    private void draw() {

        for (int i = 0; i < systembus.accessMemory().memory.length / 2000; i++) {

            drawChar();
            ppuDrawClock += 1000;
            drawColor();
            ppuDrawClock += 1000;

        }

    }

    private void copy() {

        byte copyOp = (byte) (systembus.accessMemory().memory[5] & 00000100);

        char funcStart = NumberUtility.bytesToWord(systembus.accessMemory().memory[6], systembus.accessMemory().memory[7]);
        char funcSize = NumberUtility.bytesToWord(systembus.accessMemory().memory[8], systembus.accessMemory().memory[9]);
        char funcTarget = NumberUtility.bytesToWord(systembus.accessMemory().memory[10], systembus.accessMemory().memory[11]);

        if (copyOp <= 0) {

            for (int i = 0; i < funcSize; i++) {

                systembus.accessMemory().writeByte(funcTarget, systembus.accessMemory().readByte(funcStart));
                funcStart++;
                funcTarget++;

            }
        } else if (copyOp > 1) {

            for (int i = funcSize; i > 0; i--) {
                char a = (char) i;
                byte b = 12;
                systembus.accessMemory().writeByte((char) (funcTarget + a), systembus.accessMemory().readByte((char) (funcStart + a)));

            }
        }

    }

    private void fill() {

        char funcStart = NumberUtility.bytesToWord(systembus.accessMemory().memory[6], systembus.accessMemory().memory[7]);
        char funcSize = NumberUtility.bytesToWord(systembus.accessMemory().memory[8], systembus.accessMemory().memory[9]);
        byte funcValue = systembus.accessMemory().memory[10];

        for (int i = 0; i < funcSize; i++) {

            systembus.accessMemory().writeByte(funcStart, funcValue);
            funcStart++;
        }

    }

}

//NOTES
// takes adress at start and use as memory[startadress]
//take byte memory[5] use and 00000011 and find op code
// if copy use 00000100  -> then check >0
// if 0 -> target + i = start + i; i++
// if 1 -> target + i = start + i ; i--;
