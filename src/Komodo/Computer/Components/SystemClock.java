/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.Components;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author child
 */
public class SystemClock extends Clock{

    public ArrayList<Clockable> clockablesList = new ArrayList<>();
    
    public SystemClock(String name) {
        super(name);
    }
    
    public SystemClock(String name, Clockable... clockables) {
        super(name);
        clockablesList.addAll(Arrays.asList(clockables));
        //System.out.println("*****clock name: "+name+ "; cycle*****");
    }

    @Override
    public void clockCycle() {
        for(Clockable c : clockablesList)
        {
            c.clock();
            //System.out.println(c.toString());
        }
    }
}
