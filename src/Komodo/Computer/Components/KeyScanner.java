/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components;

/**
 *
 * @author child
 */
public class KeyScanner extends Device implements Clockable{
    
    public KeyScanner(SystemBus systemBus) {
        super(systemBus);
    }

    @Override
    public void clock() {
        //System.out.println("keyboard interface code");
    }
    
}
