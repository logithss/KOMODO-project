/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler.UI;

import Komodo.Assembler.AssemblerM;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author child
 */
public class FileItem extends HBox{
    public File filePath;
    
    public FileItem(File path)
    {
        this.filePath = path;
        Label name = new Label(filePath.getName());
        this.getChildren().addAll(name);
    }
}
