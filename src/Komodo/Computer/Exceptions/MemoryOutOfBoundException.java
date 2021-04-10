/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Exceptions;

import Komodo.Assembler.Exceptions.*;

/**
 *
 * @author lojan
 */
public class MemoryOutOfBoundException extends Exception {
    
    public MemoryOutOfBoundException(String message) { 
        super(message);
    }
    
}
