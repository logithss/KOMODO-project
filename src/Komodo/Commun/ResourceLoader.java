/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Commun;


import com.sun.scenario.Settings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
 
public class ResourceLoader
{
    public static String resourcePath = "";
    
    /*public static OrderedProperties loadConfigFile()
    {
        OrderedProperties prop = new OrderedProperties();

        try {
            InputStream in = new FileInputStream("config.cfg"); //the config file is always in the engine root folder
            prop.load(in);
            in.close();

            resourcePath = prop.getProperty("e_resourcepath"); //right here set the resourcePath
            error_imagePath = prop.getProperty("e_error_image");
            error_image = loadImage(error_imagePath);
            error_texturePath = prop.getProperty("e_error_texture");
        }
        catch(IOException e) {
            System.out.println("'config.cfg' file was not found in root folder");
        }
        return prop;
    }*/
    //RessourceLoader.loadImage("images/brick.png");
    public static Image loadImage(String path)
    {
        try{
            FileInputStream inputstream = new FileInputStream(resourcePath + path);
            return new Image(inputstream, 500, 500, true, false);
        }
        catch(IOException e)
        {
            System.out.println("image at path '"+resourcePath+path+"' not found");
        }
        return null;
    }
    
    //AudioPlayer.player.start(ResourceLoader.loadMedia("sounds/musictest.wav"));
    public static Media loadMedia(String path)
    {
        try
        {
            return new Media(new File(resourcePath + path).toURI().toString());
        }
        catch(MediaException e)
        {
            System.out.println("media loading error");
            return null;
        }
        catch(Exception e)
        {
            System.out.println("media at path '"+resourcePath +path+"' not found ");
        }
        return null;
    }
    
    public static String loadStyleFile(String path)
    {
        try{
            String pathName = resourcePath + path;
            File file = new File(pathName);
            return file.toURI().toURL().toExternalForm();
        }
        catch(IOException e)
        {
            System.out.println("style at path '"+path+"' not found");
        }
        return null;
    }
}
