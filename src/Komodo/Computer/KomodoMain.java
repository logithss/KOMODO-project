
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer;

import Komodo.Assembler.Assembler;
import Komodo.Commun.Instruction;
import Komodo.Commun.NumberUtility;
import Komodo.Commun.ResourceLoader;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
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
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author child
 */
public class KomodoMain extends Application {
    
    static SystemBus systembus;
    private Stage window;
    
    private Canvas renderCanvas;
    
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
        
        //control panel
        ControlPanel clockPanel = new ControlPanel("Control", systembus);
        
        //upper panels
        VBox upperPanels = new VBox();
        upperPanels.setPadding(new Insets(20, 10, 20, 10));
        upperPanels.setSpacing(20);
        upperPanels.setAlignment(Pos.TOP_RIGHT);
        upperPanels.getChildren().addAll(registerPanel, memoryPanel, dissassemblerPanel);
        
        ScrollPane upperPanelsScroll = new ScrollPane();
        upperPanelsScroll.setPrefWidth(525);
        upperPanelsScroll.setContent(upperPanels);
        
        VBox rightBox = new VBox();
        rightBox.setPadding(new Insets(20));
        rightBox.setSpacing(10);
        rightBox.setPrefWidth(570);
        rightBox.getChildren().addAll(upperPanelsScroll, clockPanel);
        
        Menu menu1 = new Menu("Computer");
        MenuItem menuItem1 = new MenuItem("Open flash window");
        menuItem1.setOnAction(e -> {
            openMemoryFlashWindow();
        });
        MenuItem menuItem2 = new MenuItem("Open number coverter window");
        menuItem2.setOnAction(e -> {
            openNumberConverterWindow();
        });
        
        MenuItem menuItem3 = new MenuItem("Fullscreen");
        menuItem3.setOnAction(e -> {
            //rightBox
            rightBox.setVisible(!rightBox.isVisible());
            rightBox.setManaged(!rightBox.isVisible());
        });
        
        menu1.getItems().addAll(menuItem1, menuItem2, menuItem3);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        
        //render canvas
        float ratio = 1.6f;
        int width = 1280;
        renderCanvas = new Canvas(width, width/ratio);
        GraphicsContext gc = renderCanvas.getGraphicsContext2D();
        systembus.accessPpu().setGC(gc);
        
        BorderPane root = new BorderPane();
        root.setRight(rightBox);
        root.setTop(menuBar);
        root.setLeft(renderCanvas);
        
        //renderCanvas.widthProperty().bind(primaryStage.widthProperty().subtract(rightBox.widthProperty()) );
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            renderCanvas.widthProperty().set(primaryStage.widthProperty().subtract(rightBox.widthProperty()).intValue());
            if(renderCanvas.widthProperty().intValue() <= 200)
                renderCanvas.widthProperty().set(200);
        });
        renderCanvas.heightProperty().bind(renderCanvas.widthProperty().divide(ratio));

        
        anim = new AnimationTimer() { //Game main loop

            @Override
            public void handle(long l) {
                systembus.accessPpu().clock();
                registerPanel.update();
                memoryPanel.update();
                dissassemblerPanel.update();
                clockPanel.update();
            }
        };
        anim.start();
        
        Scene scene = new Scene(root, 520, 700);
        primaryStage.setOnCloseRequest(e -> {
            //e.consume();
            closeApplication();
        });
        
        //String styleSheet = ResourceLoader.loadStyleFile("C:\\Users\\child\\Documents\\NetBeansProjects\\JavaFXApplication7\\resources\\stylesheets");
        //if(styleSheet != null)
        //    scene.getStylesheets().add("\\resources\\stylesheets");
        //scene.getStylesheets().add("style.css");
        
        //initiate input manager
        InputManager.init(rightBox); //temp root
        primaryStage.setTitle("Komodo emulator v1.0");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void openMemoryFlashWindow()
    {
        final Stage dialog = new Stage();
        dialog.setTitle("Flash memory");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UNIFIED);
        dialog.initOwner(null);
        Scene panel = new Scene(new MemoryFlashPanel(systembus.accessMemory(), window), 400, 200);
        dialog.setScene(panel);
        dialog.show();
    }
    
    public void openNumberConverterWindow()
    {
        final Stage dialog = new Stage();
        dialog.setTitle("Number Converter");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(false);
        dialog.initStyle(StageStyle.UNIFIED);
        dialog.initOwner(null);
        Scene panel = new Scene(new NumberConverterPanel(window), 400, 200);
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
