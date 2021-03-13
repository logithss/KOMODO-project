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
                        System.out.println(convertNumber(command));
                    break;
                    
            }
        }
    }
    
    public static String convertNumber(String number)
    {
        String output = "";
        try{
            int value;
            switch(number.charAt(0))
            {
                case 'b':
                    value = Integer.parseInt(number.substring(1), 2);
                    output = "# "+value + "\n"+ "$ "+Integer.toHexString(value);
                    break;
                case '%':
                    value = Integer.parseInt(number.substring(1), 2);
                    output = "# "+value + "\n"+ "$ "+Integer.toHexString(value);
                    break;
                case 'h':
                    value = Integer.parseInt(number.substring(1), 16);
                    output = "% "+Integer.toBinaryString(value) + "\n" + "# "+value;
                    break;
                case '$':
                    value = Integer.parseInt(number.substring(1), 16);
                    output = "% "+Integer.toBinaryString(value) + "\n" + "# "+value;
                    break;
                default:
                    value = Integer.parseInt(number, 10);
                    output = "% "+Integer.toBinaryString(value) + "\n" + "$ "+Integer.toHexString(value);
                    break;
            }
        }
        catch(NumberFormatException e)
        {
            output = "number format is wrong";
        }
        
        return output;
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
