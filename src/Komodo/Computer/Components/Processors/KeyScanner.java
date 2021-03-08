/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components.Processors;

import Komodo.Computer.Components.Clockable;
import Komodo.Computer.Components.Device;
import Komodo.Computer.Components.SystemBus;
import Komodo.Computer.InputManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.input.KeyCode;

/**
 *
 * @author child
 */
public class KeyScanner extends Device implements Clockable{
    
    private char keybordRegisterStart = 0x20;
    
    public KeyScanner(SystemBus systemBus) {
        super(systemBus);
    }

    @Override
    public void clock() {
        updateMemory();
    }
    
    private void updateMemory()
    {
        Map<KeyCode, Integer> keys = InputManager.getKeyPressed();
        Iterator<Integer> ite = keys.values().iterator();
        byte count = 0;
        synchronized(keys) {
            while(ite.hasNext()){
                systembus.accessMemory().writeByte( (char) (keybordRegisterStart+count+1), ite.next().byteValue());
                count++;
                }
        }
        systembus.accessMemory().writeByte(keybordRegisterStart, count);
    }
    
}
