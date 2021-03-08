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
import java.util.Iterator;

/**
 *
 * @author child
 */
public class KeyScanner extends Device implements Clockable{
    
    private char keybordRegisterStart = 0x10;
    
    public KeyScanner(SystemBus systemBus) {
        super(systemBus);
    }

    @Override
    public void clock() {
        updateMemory();
    }
    
    private void updateMemory()
    {
        //System.out.println("scan");
        Iterator<Integer> keys = InputManager.getKeyPressed();
        byte count = 0;
        
        while(keys.hasNext()){
            systembus.accessMemory().writeByte( (char) (keybordRegisterStart+count+1), keys.next().byteValue());
            count++;
        }
        systembus.accessMemory().writeByte(keybordRegisterStart, count);
        
        
    }
    
}
