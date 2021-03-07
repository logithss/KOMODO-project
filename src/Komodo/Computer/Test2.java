/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;

import Komodo.Assembler.Assembler;
import Komodo.Commun.Instruction;
import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.SystemBus;
import Komodo.Computer.UI.*;
import java.io.File;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author child
 */
public class Test2 extends Application {
    
    static SystemBus systembus;
    private Stage window;
    
    private AnimationTimer anim;
    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        
        //registers panel
        RegisterPanel registerPanel = new RegisterPanel("Registers", systembus.accessCpu(), systembus.accessMemory());
        
        //memory view
        MemoryPanel memoryPanel = new MemoryPanel("Memory", systembus.accessMemory(), (char)0x00);
        
        //TEST ONLY memory flash panel
        /*Button flashButton = new Button("Open flash Window");
        flashButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    openMemoryFlashWindow();
                }
            });*/
        
     
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.TOP_RIGHT);
        vbox.getChildren().addAll(registerPanel, memoryPanel/*, flashButton*/);
        //vbox.setMouseTransparent(true);
        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(20));
        root.setRight(vbox);
        //root.setMouseTransparent(true);
        
        anim = new AnimationTimer() { //Game main loop

            @Override
            public void handle(long l) {
                registerPanel.update();
                memoryPanel.update();
            }
        };
        anim.start();
        
        Scene scene = new Scene(root, 520, 700);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeApplication();
        });
        InputManager.init(scene);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void openMemoryFlashWindow()
    {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UNIFIED);
        dialog.initOwner(window);
        Scene panel = new Scene(new MemoryFlashPanel(systembus.accessMemory(), window), 400, 200);
        dialog.setScene(panel);
        dialog.show();
    }
    
    public void closeApplication() {
        //System.exit(0);
        //systembus.powerOff();
        systembus.powerOff();
        //anim.stop();
        //window.close();
        //System.gc();
        Platform.exit();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        systembus = new SystemBus();
        systembus.powerOn();
        launch(args);
        

    }
    
}
