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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

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
    private char selectedAddress = 0;
    
    private GridPane grid;
    
    private AddressLabel[][] labelArray;
    private TextField editBox;
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
            //System.out.println(newValue);
            if(newValue != null){
                int value = (int)newValue;
                memStart.getValueFactory().setValue(value - value % 16);
                update( (int)(value - value % 16) );
            }
        });
        
        
        /*memStart.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            try {
              
                memStart.getValueFactory().setValue((int)Integer.parseInt(newValue,10));
            } catch (NumberFormatException e) {
                memStart.getValueFactory().setValue(80);
            }
        });*/
        
        
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
        
        //textbox to edit memory value
        editBox = new TextField ();
        editBox.setPrefWidth(50);
        editBox.setOnAction(new EventHandler<ActionEvent>() {
        @Override
            public void handle(ActionEvent e) {
                if ((editBox.getText() != null && !editBox.getText().isEmpty())) {
                    try{
                        byte value = (byte) Integer.parseInt(editBox.getText(), 16);
                        memory.writeByte((char) (selectedAddress), value);
                        editBox.setText("");
                        update();
                        //select the  next memory cell
                        selectNextCell();
                    }
                    catch(NumberFormatException ex)
                    {
                        editBox.setText("");
                    }
                } else {
                    //just skip to next cell
                    selectNextCell();
                }
             }
         });
        
        //limit number of character in editBox (character limit is 2)
        editBox.lengthProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() > oldValue.intValue()) {
                    // Check if the new character is greater than LIMIT
                    if (editBox.getText().length() >= 2) {

                        // if it's 11th character then just setText to previous
                        // one
                        editBox.setText(editBox.getText().substring(0, 2));
                    }
                }
            }
        });
        
        controls.add(memStart, 1, 0);
        controls.add(new Label("    "), 2, 0);
        controls.add(new Label("Edit: "), 3, 0);
        controls.add(editBox, 4, 0);
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
        memoryBox.setPadding(new Insets(10, 10, 10, 10));
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
        calculateSelectedAddress(currentSelectedCellX, currentSelectedCellY);
        update();
    }
    public void update()
    {
        if((startPointer + labelArray[currentSelectedCellX][currentSelectedCellY].index) <= 0xffff){
            byte byteValue = memory.readByte((char) (startPointer + labelArray[currentSelectedCellX][currentSelectedCellY].index));
            byteValueLabel.setText(Integer.toString(Byte.toUnsignedInt(byteValue)).toUpperCase());
        }
        else
        {
            byteValueLabel.setText("--");
        }
        if((startPointer + labelArray[currentSelectedCellX][currentSelectedCellY].index) <= 0xfffe){
            char wordValue = memory.readWord((char) (startPointer + labelArray[currentSelectedCellX][currentSelectedCellY].index));
            wordValueLabel.setText(Integer.toString(wordValue).toUpperCase());
        }
        else
        {
            wordValueLabel.setText("----");
        }
        
        //draw memory values in grid
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
    
    private void selectNextCell()
    {
        int x = currentSelectedCellX;
        int y  = currentSelectedCellY;
        x++;
        if(x >= byteCount)
            {
                y++;
                x=0;
                if(y >= lineCount)
                {
                    startPointer+=byteCount;
                    y=lineCount-1;
                }
            }
        if((labelArray[x][y].index+startPointer) <= 0xffff)
            selectCell(x,y);
        update();
    }
    
    private void selectCell(int x, int y)
    {
        //l.setStyle("-fx-background-color: gray");
        Label l = labelArray[currentSelectedCellX][currentSelectedCellY];
        l.setStyle("-fx-background-color: transparent");
        l = labelArray[x][y];
        l.setStyle("-fx-background-color: gray");
        calculateSelectedAddress(x, y);
        //System.out.println("selected address: "+ Integer.toHexString((int)selectedAddress));
        currentSelectedCellX = x;
        currentSelectedCellY = y;
        
        editBox.requestFocus();
        update();
    }
    
    private void calculateSelectedAddress(int x, int y)
    {
        Label l = labelArray[x][y];
        selectedAddress = (char) (startPointer + ((AddressLabel)l).index);
    }
    
}
