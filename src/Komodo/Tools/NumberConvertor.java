/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Tools;

import java.util.Scanner;

/**
 *
 * @author child
 */
public class NumberConvertor {
    
    static boolean running = true;
    
    public static void main(String[] args){
        help();
        
        Scanner in = new Scanner(System.in);
        
        while(running)
        {
            System.out.print("> ");
            String command = in.nextLine().toLowerCase();
            //System.out.println(">"+command);
            switch(command)
            {
                case "q":
                    quit();
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    if(!command.isEmpty())
                        convertNumber(command);
                    break;
                    
            }
        }
    }
    
    public static void convertNumber(String number)
    {
        try{
            int value;
            switch(number.charAt(0))
            {
                case 'b':
                    value = Integer.parseInt(number.substring(1), 2);
                    System.out.println("# "+value);
                    System.out.println("$ "+Integer.toHexString(value));
                    break;
                case 'h':
                    value = Integer.parseInt(number.substring(1), 16);
                    System.out.println("% "+Integer.toBinaryString(value));
                    System.out.println("# "+value);
                    break;
                default:
                    value = Integer.parseInt(number, 10);
                    System.out.println("% "+Integer.toBinaryString(value));
                    System.out.println("$ "+Integer.toHexString(value));
                    break;
            }
        }
        catch(NumberFormatException e)
        {
            System.out.println("number format is wrong");
        }
    }
    
    public static void help()
    {
        System.out.println("-----------------------------\n"
                + "Number base conversion\n"
                + "b -> binary\n"
                + "h -> hex \n"
                + "decimal with nothing in front\n"
                + "Ex. b101, hf2, 55\n"
                + "'help' to show this message\n"
                + "q or quit to exit\n"
                + "-----------------------------\n");
    }
    public static void quit()
    {
      running = false;  
    }
}
