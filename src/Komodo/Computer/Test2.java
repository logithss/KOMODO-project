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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
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
        
        //disassembly view
        DisassemblerPanel dissassemblerPanel = new DisassemblerPanel("Disassembler", systembus.accessCpu(), systembus.accessMemory());
        
        //TEST ONLY memory flash panel
        /*Button flashButton = new Button("Open flash Window");
        flashButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    openMemoryFlashWindow();
                }
            });*/
        
        //upper panels
        VBox upperPanels = new VBox();
        upperPanels.setPadding(new Insets(20, 10, 20, 10));
        upperPanels.setSpacing(20);
        upperPanels.setAlignment(Pos.TOP_RIGHT);
        upperPanels.getChildren().addAll(registerPanel, memoryPanel, dissassemblerPanel/*, flashButton*/);
        
        ScrollPane upperPanelsScroll = new ScrollPane();
        upperPanelsScroll.setPrefWidth(525);
        upperPanelsScroll.setContent(upperPanels);
        
        //lower panels
        /*VBox lowerPanelsBox = new VBox();
        lowerPanelsBox.setPadding(new Insets(10));
        lowerPanelsBox.setSpacing(20);
        RegisterPanel registerPanel2 = new RegisterPanel("Registers", systembus.accessCpu(), systembus.accessMemory());
        Button collapseButton = new Button("-");
        collapseButton.setPrefSize(10, 10);
        collapseButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    System.out.println("click");
                    registerPanel2.setVisible(!registerPanel2.isVisible());
                    registerPanel2.setManaged(registerPanel2.isVisible());
                    collapseButton.setText(registerPanel2.isVisible() ? "-": "+");
                }
            });
        lowerPanelsBox.getChildren().addAll(collapseButton, registerPanel2);
        lowerPanelsBox.setStyle("-fx-border-color: black ;\n" +
                            "    -fx-border-width: 1 ; \n" +
                            "    -fx-border-style: solid");*
        
        TitlePanel lowerTitlePanel = new TitlePanel("Clock");
        lowerTitlePanel.setPanel(lowerPanelsBox);*/
        
        ClockPanel clockPanel = new ClockPanel("Clock", systembus);
        
        VBox rightBox = new VBox();
        rightBox.setPadding(new Insets(20));
        rightBox.setSpacing(10);
        rightBox.getChildren().addAll(upperPanelsScroll, clockPanel);
        
        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(20));
        root.setRight(rightBox);
        //root.setMouseTransparent(true);

        
        anim = new AnimationTimer() { //Game main loop

            @Override
            public void handle(long l) {
                registerPanel.update();
                memoryPanel.update();
                dissassemblerPanel.update();
            }
        };
        anim.start();
        
        Scene scene = new Scene(root, 520, 700);
        primaryStage.setOnCloseRequest(e -> {
            //e.consume();
            closeApplication();
        });
        
        TextField field = new TextField();
        Label keys = new Label("a");
        rightBox.getChildren().addAll(keys, field);
        InputManager.init(field); //temp root
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
        System.out.println("******* alive?: "+systembus.accessSystemClock().isAlive());
        anim.stop();
        window.close();
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
