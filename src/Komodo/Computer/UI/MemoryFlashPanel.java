/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Assembler.UI.FileItem;
import Komodo.Computer.Components.Memory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author child
 */
public class MemoryFlashPanel extends VBox{
    
    Memory memory;
    
    File file = null;
    TextField positionField;
    Label errorLabel;
    
    public MemoryFlashPanel(Memory memory, Stage stage)
    {
        this.memory = memory;
        
        //1) file selection
        HBox fileBox = new HBox();
        Label fileName = new Label("Choose a file: ");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        
        Button newFileButton = new Button("Select file");
        newFileButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File chosenFile = fileChooser.showOpenDialog(stage);
                    if(chosenFile != null){
                        setFile(chosenFile);
                        fileName.setText(chosenFile.getName());
                    }
                }
            });
        
        fileBox.getChildren().addAll(fileName, newFileButton);
        fileBox.setSpacing(5);
        
        //2) starting position
        HBox positionBox = new HBox();
        Label positionLabel = new Label("Starting position: ");
        positionField = new TextField();
        positionBox.getChildren().addAll(positionLabel, positionField);
        
        //3) flash button
        HBox flashBox = new HBox();
        errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        Button flashButton = new Button("Flash");
        flashButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    errorLabel.setText(flashMemory());
                }
            });
        flashBox.getChildren().addAll(flashButton, errorLabel);
        flashBox.setSpacing(5);
        
        //combine all elements into root
        this.getChildren().addAll(fileBox, positionBox,flashBox);
        this.setSpacing(5);
        
        //drag&drop code
        this.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != this
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        this.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    setFile(db.getFiles().get(0));
                    fileName.setText(db.getFiles().get(0).getName());
                    //dropped.setText(db.getFiles().toString());
                    success = true;
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });
    }
    
    private void setFile(File file){this.file = file;}
    
    private String flashMemory()
    {
        String positionText = positionField.getText();
        if(positionText.isEmpty() || file == null)
            return "Information not filled";
        try{
            int position = Integer.parseInt(positionText, 16);
            byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
            this.memory.flashMemory(data, position);
        }
        catch(NumberFormatException e)
        {
            return "Number must be in Hex";
        } catch (IOException ex) {
            return "Unable to open file";
        }
        return ""; //success
    }
    
}
