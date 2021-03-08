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
import java.util.List;
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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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
    private Assembler assembler;
    private Stage window;
    private FileChooser fileChooser;
    
    private VBox fileVBox;
    private TextArea console;
    
    Label outputLabel;
    
    private String outputPath = null;
    
    public void start(Stage primaryStage) {
        
        assembler = new Assembler(); 
        window = primaryStage;
        
        fileVBox = new VBox();
        /*for(int i =0; i < 20; i++)
            vbox.getChildren().add(new Label("aaa"));*/
        ScrollPane sp = new ScrollPane();
        //sp.setPadding(new Insets(100, 100, 10, 0));
        sp.setContent(fileVBox);
        
        //drag&drop code for files
        sp.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != sp
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        sp.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    for(File f : db.getFiles())
                    {
                        createFileItem(f);
                    }
                    //dropped.setText(db.getFiles().toString());
                    success = true;
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });
        
        
        
        fileVBox.setSpacing(5);
        
        fileChooser = new FileChooser();
        fileChooser.setTitle("Select file");
        
        Button newFileButton = new Button("Add file");
        newFileButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    List <File> files = fileChooser.showOpenMultipleDialog(primaryStage);
                    if(files != null && files.size() >0){
                        for(File file : files){
                            createFileItem(file);
                        }
                    }
                }
            });
        
        Button assembleButton = new Button("Assemble files");
        assembleButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    assembleFiles();
                }
            });
        
        Button helpButton = new Button("?");
        helpButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    System.out.println("HEEEELLLPPP!!!");
                }
            });
        
        BorderPane root = new BorderPane();
        root.setMargin(sp, new Insets(10));
        root.setCenter(sp);
        
        Menu menu1 = new Menu("Menu 1");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.getChildren().addAll(newFileButton, assembleButton, helpButton);
        VBox topBox = new VBox();
        topBox.setSpacing(5);
        topBox.getChildren().addAll(menuBar, buttonBox);
        root.setTop(topBox);
        
        //console
        
        console = new TextArea();
        console.setPadding(new Insets(5, 5, 5, 5));
        console.setEditable(false);
        
        
        Button outputButton = new Button("Set output path");
        outputButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    setOutputPath();
                }
            });
        outputLabel = new Label("Output path: ");
        
        HBox outputBox = new HBox();
        outputBox.setSpacing(5);
        outputBox.getChildren().addAll(outputLabel, outputButton);
        
        
        //drag&drop code for output
        outputBox.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != sp
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });

        outputBox.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    outputPath = db.getFiles().get(0).getAbsolutePath();
                    outputLabel.setText(""+outputPath);
                    //dropped.setText(db.getFiles().toString());
                    success = true;
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);

                event.consume();
            }
        });
        
        VBox bottomBox = new VBox();
        bottomBox.setSpacing(5);
        bottomBox.getChildren().addAll(outputBox, console);
        root.setBottom(bottomBox);
        printLine("Syntax v1.0 \nready:");
        
        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Assembler");
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void createFileItem(File file)
    {
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
    
    private void assembleFiles()
    {
        if(outputPath == null)
            setOutputPath();
        
        if(outputPath != null){
            if(fileVBox.getChildren().size()>0){
                ArrayList<File> files = new ArrayList<>();
                for(Node item : fileVBox.getChildren())
                    files.add( ((FileItem)item).filePath);
                try {
                    console.setText(""); //clear console
                    printLine("---START---");
                    assembler.assembleFiles(files, outputPath);
                    printLine(">Files succesfully assembled");
                } catch (Exception ex) {
                    printLine(ex.getMessage());
                    printLine(">Assembling failed");
                }

                printLine("---END---");
            }
            else
                printLine(">No files to assemble");
        }
        else
            printLine(">No output path set");
    }
    public void removeFileItem(Object item)
    {
        fileVBox.getChildren().remove(item);
    }
    
    private void setOutputPath()
    {
        File file = fileChooser.showOpenDialog(window);
        if(file != null)
        {
            outputPath = file.getAbsolutePath();
            outputLabel.setText(""+outputPath);
        }
    }
    
    public void printLine(String line)
    {
        if(!console.getText().isEmpty())
            console.setText(console.getText()+"\n"+line);
        else
            console.setText(line);
        console.setScrollTop(1000000000);
    }
    


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
