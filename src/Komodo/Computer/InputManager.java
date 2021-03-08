/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class InputManager {
    
    private static volatile TreeMap<KeyCode, Integer> keyMap = new TreeMap<>();
    
    private static KeyCode latestCode;
    
    public static void init(Node scene)
    {   
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent key) {
                latestCode = key.getCode();
            }
        });
        
        scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent key) {
                synchronized(keyMap) {
                    if(keyMap.containsKey(key.getCode()))
                        keyMap.remove(key.getCode());
                }
            }
        });
        
        scene.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent key) {
                synchronized(keyMap) {
                keyMap.put(latestCode, (int)key.getCharacter().charAt(0));
                }
            }
        });
        
    }
    
    public static synchronized Map<KeyCode, Integer> getKeyPressed()
    {
        synchronized(keyMap) {
            return Collections.synchronizedMap(keyMap);
        }
    }
}
