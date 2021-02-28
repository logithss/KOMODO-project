/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Computer.Components.Memory;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author toufik.issad
 */
public class MemoryPanel extends TitlePanel implements UIPanel{
    
    private Memory memory;
    
    private char startPointer;
    private int byteCount;
    private int lineCount;
    
    private int currentSelectedCellX = 0;
    private int currentSelectedCellY = 0;
    
    private GridPane grid;
    private AddressLabel[][] labelArray;
    private Label[] addressArray;
    private Label byteValueLabel;
    private Label wordValueLabel;
    
    public MemoryPanel(String title, Memory memory, char startPointer)
    {
       this(title, memory, startPointer, 16, 16);
    }
    
    public MemoryPanel(String title, Memory memory, char startPointer, int byteCount, int lineCount)
    {
        super(title);
        this.memory = memory; 
        this.startPointer = startPointer;
        this.byteCount = byteCount;
        this.lineCount = lineCount;
       
        this.grid = new GridPane();
        this.labelArray = new AddressLabel[byteCount][lineCount];
        this.addressArray = new Label[lineCount];
        byteValueLabel = new Label();
        wordValueLabel = new Label();
        construct();
        update();
        
        GridPane controls = new GridPane();
        controls.add(new Label("Start value: "), 0, 0);
        Spinner memStart = new Spinner(0, 0xfff0, 0, 0x10);
        memStart.setEditable(true);
        memStart.setPrefSize(125, 25);
        
        memStart.valueProperty().addListener((obs, oldValue, newValue) ->{
            //to remove focus from address spinner
            grid.requestFocus();
            System.out.println(newValue);
            int value = (int)newValue;
            memStart.getValueFactory().setValue(value - value % 16);
            update( (int)(value - value % 16) );
        });
        
        memStart.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue ov, Boolean t, Boolean t1) {

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (memStart.isFocused()) {
                            memStart.getEditor().selectAll();
                        }
                    }
                });
            }
        });
        
        controls.add(memStart, 1, 0);
        //controls.setPadding(new Insets(0, 0, 0, 0));
        controls.add(new Label("Byte value: "), 0, 1);
        controls.add(byteValueLabel, 1, 1);
        controls.add(new Label("    "), 2, 1);
        controls.add(new Label("Word value: "), 3, 1);
        controls.add(wordValueLabel, 4, 1);
        
        Separator separator = new Separator();
        
        VBox memoryBox = new VBox();
        memoryBox.setSpacing(10);
        memoryBox.getChildren().addAll(controls, separator, grid);
        memoryBox.setPadding(new Insets(25, 10, 25, 10));
        memoryBox.setStyle(          "-fx-border-color: black ;\n" +
                                "    -fx-border-width: 1 ; \n" +
                                "    -fx-border-style: solid");
        this.setPanel(memoryBox);
        
    }
    
    private void construct()
    {
        int index =0;
        //memory view
        for(int y = 0; y <lineCount; y++)
        {
            Label lineAddress = new AddressLabel("$"+Integer.toHexString(0));
            //address.setStyle("-fx-background-color:gray;");
            grid.add(lineAddress, 0, y);
            addressArray[y] = lineAddress;
            Label separator = new Label("   ");
            grid.add(separator, 1, y);
            for(int x = 2; x <byteCount+2; x++)
            {
                AddressLabel l = new AddressLabel("00 ");
                l.index = index;
                l.x = x-2; 
                l.y = y;
                l.setOnMouseClicked((mouseEvent) -> {
                    selectCell(l.x, l.y);
                });
                grid.add(l, x, y);
                labelArray[x-2][y] = l;
                index++;
            }
        }
    }
    public void update(int newValue)
    {
        this.startPointer = (char) newValue;
        update();
    }
    public void update()
    {
        byte byteValue = memory.readByte((char) (startPointer + labelArray[currentSelectedCellX][currentSelectedCellY].index));
        byteValueLabel.setText(String.format("%02X", byteValue & 0x0FFFFF).toUpperCase());
        char wordValue = memory.readWord((char) (startPointer + labelArray[currentSelectedCellX][currentSelectedCellY].index));
        wordValueLabel.setText(String.format("%04X", wordValue & 0x0FFFFF).toUpperCase());
        int currentAddress = startPointer;
        for(int y = 0; y< lineCount; y++)
        {
            if(currentAddress <= 0xfff0)
            {
                addressArray[y].setText(("$"+Integer.toHexString(currentAddress)).toUpperCase());
            }
            else
            {
                addressArray[y].setText("");
            }
            
            for(int x = 0; x< byteCount; x++)
            {
                if(currentAddress > 0xffff)
                {
                    labelArray[x][y].setText("");
                    //currentAddress++;
                    //break;
                }
                else{
                    labelArray[x][y].setText(String.format("%1$02X", Byte.toUnsignedInt(memory.readByte((char)currentAddress))) + " ");
                    currentAddress++;
                }
            }
        }
    }
    
    private void selectCell(int x, int y)
    {
        //l.setStyle("-fx-background-color: gray");
        Label l = labelArray[currentSelectedCellX][currentSelectedCellY];
        l.setStyle("-fx-background-color: transparent");
        l = labelArray[x][y];
        l.setStyle("-fx-background-color: gray");
        currentSelectedCellX = x;
        currentSelectedCellY = y;
        
        update();
    }
    
}