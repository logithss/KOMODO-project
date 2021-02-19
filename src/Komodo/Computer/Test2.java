/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;

import Komodo.Computer.Components.SystemBus;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
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
        
        Label label = new Label();
        label.setText("cycles : ");
        anim = new AnimationTimer() { //Game main loop

            @Override
            public void handle(long l) {
                label.setText("cycles : "+Long.toString(systembus.accessSystemClock().cycleCount)+"\n clock running : "+Boolean.toString(systembus.accessSystemClock().running));
            }
        };
        anim.start();
        
        StackPane root = new StackPane();
        root.getChildren().addAll(label);
        
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeApplication();
        });
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
