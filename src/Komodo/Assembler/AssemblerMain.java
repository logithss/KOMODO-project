/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Assembler;

import Komodo.Assembler.Assembler;
import Komodo.Assembler.UI.FileItem;
import Komodo.Assembler.UI.SyntaxHelpPopup;
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
import javafx.scene.control.Tooltip;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author child
 */
public class AssemblerMain extends Application {
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
        sp.setTooltip(new Tooltip("Files to assemble (drag & drop)"));
        sp.setContent(fileVBox);
        
        //drag&drop code for files
        sp.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != sp
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    
                    //add borders when dragging
                    sp.setStyle("-fx-border-color: red ;\n" +
                                "    -fx-border-width: 2 ; \n" +
                                "    -fx-border-style: solid ;");
                }
                event.consume();
            }
        });
        
        sp.setOnDragExited(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                //remove borders when dragging
                    sp.setStyle("-fx-border-color: red ;\n" +
                                "    -fx-border-width: 0 ; \n" +
                                "    -fx-border-style: solid ;");
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
                    //remove borders when dragging
                    sp.setStyle("-fx-border-color: red ;\n" +
                                "    -fx-border-width: 0 ; \n" +
                                "    -fx-border-style: solid ;");
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
        newFileButton.setTooltip(new Tooltip("Add files to assemble"));
        newFileButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    fileChooser.setTitle("Select file");
                    List <File> files = fileChooser.showOpenMultipleDialog(primaryStage);
                    if(files != null && files.size() >0){
                        for(File file : files){
                            createFileItem(file);
                        }
                    }
                }
            });
        
        Button assembleButton = new Button("Assemble file");
        assembleButton.setTooltip(new Tooltip("Assemble file"));
        assembleButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    assembleFiles();
                }
            });
        
        Button helpButton = new Button("?");
        helpButton.setTooltip(new Tooltip("Open help menu"));
        helpButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    openHelpPopup();
                }
            });
        
        BorderPane root = new BorderPane();
        root.setMargin(sp, new Insets(10));
        root.setCenter(sp);
        
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(5);
        buttonBox.getChildren().addAll(newFileButton, assembleButton, helpButton);
        VBox topBox = new VBox();
        topBox.setSpacing(5);
        topBox.getChildren().addAll(buttonBox, new Label("Files to assemble: "));
        root.setTop(topBox);
        
        //console
        
        console = new TextArea();
        console.setPadding(new Insets(5, 5, 5, 5));
        console.setEditable(false);
        
        
        Button outputButton = new Button("Set output path");
        outputButton.setTooltip(new Tooltip("Specify the output path"));
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
        outputBox.setPadding(new Insets(5,5,5,5));
        
        
        //drag&drop code for output
        outputBox.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                if (event.getGestureSource() != sp
                        && event.getDragboard().hasFiles()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    
                    //add borders when dragging
                    outputBox.setStyle("-fx-border-color: red ;\n" +
                                "    -fx-border-width: 2 ; \n" +
                                "    -fx-border-style: solid ;");
                }
                event.consume();
            }
        });
        
        outputBox.setOnDragExited(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent event) {
                //remove borders when dragging
                    outputBox.setStyle("-fx-border-color: red ;\n" +
                                "    -fx-border-width: 0 ; \n" +
                                "    -fx-border-style: solid ;");
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
                    
                    //remove borders when dragging
                    outputBox.setStyle("-fx-border-color: red ;\n" +
                                "    -fx-border-width: 0 ; \n" +
                                "    -fx-border-style: solid ;");
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
        removeButton.setTooltip(new Tooltip("Remove file"));
        removeButton.setOnAction(
        new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                removeFileItem(fileItem);
            }
        });
        fileItem.getChildren().add(removeButton);
        fileVBox.getChildren().clear();
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
                    int size = assembler.assembleFiles(files, outputPath);
                    printLine(">Files succesfully assembled ("+size+" bytes)");
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
        fileChooser.setTitle("Select output path");
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
    
    private void openHelpPopup()
    {
        final Stage dialog = new Stage();
        dialog.setTitle("Help");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setAlwaysOnTop(true);
        dialog.setResizable(true);
        dialog.initStyle(StageStyle.DECORATED);
        dialog.initOwner(window);
        Scene panel = new Scene(new SyntaxHelpPopup(window), 600, 250);
        dialog.setScene(panel);
        dialog.show();
    }
    


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
