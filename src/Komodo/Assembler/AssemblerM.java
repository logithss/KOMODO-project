/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Assembler.Assembler;
import Komodo.Assembler.UI.FileItem;
import Komodo.Commun.Instruction;
import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.SystemBus;
import Komodo.Computer.UI.*;
import java.io.File;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author child
 */
public class AssemblerM extends Application {
    
    private Stage window;
    private VBox fileVBox;
    
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        fileVBox = new VBox();
        /*for(int i =0; i < 20; i++)
            vbox.getChildren().add(new Label("aaa"));*/
        ScrollPane sp = new ScrollPane();
        sp.setContent(fileVBox);
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("add file");
        
        Button newFileButton = new Button("Add file");
        newFileButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    
                    FileItem fileItem = new FileItem(file);
                    Button removeButton = new Button("X");
                    removeButton.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent e) {
                            removeFileItem(fileItem);
                        }
                    });
                    fileItem.getChildren().add(removeButton);
                    fileVBox.getChildren().addAll(fileItem);
                }
            });
        
        BorderPane root = new BorderPane();
        root.setCenter(sp);
        
        root.setTop(newFileButton);
        
        
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Assembler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void removeFileItem(Object item)
    {
        fileVBox.getChildren().remove(item);
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
