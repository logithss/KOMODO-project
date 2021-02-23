/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;

import Komodo.Assembler.Assembler;
import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.SystemBus;
import Komodo.Computer.UI.*;
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
import javafx.stage.Stage;

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
        RegisterPanel registerPanel = new RegisterPanel(systembus.accessCpu());
        
        //memory view
        MemoryPanel memoryPanel = new MemoryPanel(systembus.accessMemory(), (char)0x00);
        
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.TOP_RIGHT);
        vbox.getChildren().addAll(registerPanel, memoryPanel);
        
        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(20));
        root.setRight(vbox);
        
        anim = new AnimationTimer() { //Game main loop

            @Override
            public void handle(long l) {
            }
        };
        anim.start();
        
        Scene scene = new Scene(root, 500, 700);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeApplication();
        });
        InputManager.init(scene);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
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
