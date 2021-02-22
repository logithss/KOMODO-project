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
        GridPane registerView = new GridPane();
        registerView.setStyle(
                                "-fx-border-color: blue ;\n" +
                                "    -fx-border-width: 5 ; \n" +
                                "    -fx-border-style: segments(10, 15, 15, 15)  line-cap round ;");
        
        registerView.setStyle("-fx-background-color:gray;");
        registerView.setPadding(new Insets(20));
        Label labelPC = new Label("PC:   $C545 [10541]");
        labelPC.setStyle("-fx-font: 24 arial;");
        Label labelA = new Label("A:   $55 [85]");
        labelA.setStyle("-fx-font: 24 arial;");
        Label labelX = new Label("X:   $5 [5]");
        labelX.setStyle("-fx-font: 24 arial;");
        Label labelY = new Label("Y:   $0 [0]");
        labelY.setStyle("-fx-font: 24 arial;");
        Label stackPointer = new Label("Stack:   $0 [0]");
        stackPointer.setStyle("-fx-font: 24 arial;");
        
        HBox statBox = new HBox();
        statBox.setAlignment(Pos.TOP_LEFT);
        statBox.setPadding(new Insets(5));
        statBox.setSpacing(20);
        Label carry = new Label("C");
        carry.setStyle("-fx-font: 24 arial;");
        carry.setTextFill(Color.RED);
        Label negative = new Label("N");
        negative.setStyle("-fx-font: 24 arial;");
        negative.setTextFill(Color.RED);
        Label bigger = new Label("B");
        bigger.setStyle("-fx-font: 24 arial;");
        bigger.setTextFill(Color.GREEN);
        Label zero = new Label("Z");
        zero.setStyle("-fx-font: 24 arial;");
        zero.setTextFill(Color.RED);
        statBox.getChildren().addAll(carry, negative, bigger, zero);
        
        registerView.add(labelPC, 0, 0);
        registerView.add(labelA, 0, 1);
        registerView.add(labelX, 0, 2);
        registerView.add(labelY, 0, 3);
        registerView.add(stackPointer, 0, 4);
        registerView.add(statBox, 0, 5);
        
        //memory view
        GridPane memoryView = new GridPane();
        for(int y = 0; y <16; y++)
        {
            Label address = new Label("$"+Integer.toHexString(1000+y));
            //address.setStyle("-fx-background-color:gray;");
            memoryView.add(address, 0, y);
            Label separator = new Label(" |  ");
            memoryView.add(separator, 1, y);
            for(int x = 2; x <18; x++)
            {
                Label l = new Label("4f ");
                memoryView.add(l, x, y);
            }
        }
        memoryView.setStyle(
                                "-fx-border-color: black ;\n" +
                                "    -fx-border-width: 5 ; \n" +
                                "    -fx-border-style: solid");
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.TOP_RIGHT);
        vbox.getChildren().addAll(registerView, memoryView);
        
        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(20));
        root.setRight(vbox);
        
        anim = new AnimationTimer() { //Game main loop

            @Override
            public void handle(long l) {
            }
        };
        anim.start();
        
        Scene scene = new Scene(root, 300, 250);
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
