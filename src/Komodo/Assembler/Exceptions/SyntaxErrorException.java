/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler.Exceptions;

/**
 *
 * @author lojan
 */
public class SyntaxErrorException extends Exception {
    
    public SyntaxErrorException(String message) { 
        super(message);
    }
    
}
