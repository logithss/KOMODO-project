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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class InputManager {
    private static HashMap<KeyCode, Boolean> keyMap = new HashMap<>();
    
    public static void init(Scene scene)
    {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
            public void handle(KeyEvent key) {
                keyMap.put(key.getCode(), true);
            }
        });
        
        scene.setOnKeyReleased(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent key) {
                keyMap.put(key.getCode(), false);
            }
        });
    }
    public static boolean keyPressed(KeyCode keyName)
    {
        if(keyMap.containsKey(keyName))
        {
            return keyMap.get(keyName);
        }
        else
            return false;
    }
}
