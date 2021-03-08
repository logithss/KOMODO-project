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
import java.util.HashMap;
import java.util.Iterator;
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
    
    private static TreeMap<KeyCode, Integer> keyMap = new TreeMap<>();
    
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
                if(keyMap.containsKey(key.getCode()))
                    keyMap.remove(key.getCode());
            }
        });
        
        scene.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent key) {
                keyMap.put(latestCode, (int)key.getCharacter().charAt(0));
            }
        });
        
    }
    
    public static Iterator<Integer> getKeyPressed()
    {
        return keyMap.values().iterator();
    }
//    public static boolean keyPressed(KeyCode keyName)
//    {
//        if(keyMap.containsKey(keyName))
//        {
//            return keyMap.get(keyName);
//        }
//        else
//            return false;
//    }
}
