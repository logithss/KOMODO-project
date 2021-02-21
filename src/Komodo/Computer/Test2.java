/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;

import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.SystemBus;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
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
        
        Slider slider = new Slider();
         
        // The minimum value.
        slider.setMin(1);
         
        // The maximum value.
        slider.setMax(200);
         
        // Current value
        slider.setValue(1);
         
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
         
        slider.setBlockIncrement(10);
        
        slider.valueProperty().addListener(new ChangeListener<Number>() {
 
         @Override
         public void changed(ObservableValue<? extends Number> observable, //
               Number oldValue, Number newValue) {
 
            systembus.apu.noiseValue = newValue.intValue();
         }
      });
 
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(10);
        root.getChildren().addAll(label, slider);
        
        anim = new AnimationTimer() { //Game main loop

            @Override
            public void handle(long l) {
                label.setText("cycles : "+Long.toString(systembus.accessSystemClock().cycleCount)+"\nClock running : "+Boolean.toString(systembus.accessSystemClock().running)
                +"\nApu song pointer at : "+systembus.apu.x);
            }
        };
        anim.start();
        
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
        //systembus.powerOn();
        byte[] bytes = NumberUtility.wordToBytes((char)0xfa62);
        System.out.println("high: "+ Integer.toHexString(Byte.toUnsignedInt(bytes[0]))+"; low: "+ Integer.toHexString(Byte.toUnsignedInt(bytes[1])));
        char word = NumberUtility.bytesToWord((byte) 0xcf, (byte) 0xf6);
        System.out.println("bytes to word: "+ Integer.toHexString(word) );
        launch(args);
    }
    
}
