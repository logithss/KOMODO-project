/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Computer.Components.Memory;
import Komodo.Computer.Components.Processors.Cpu;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author toufik.issad
 */
public class RegisterPanel extends TitlePanel implements UIPanel{
    
    private Cpu cpu;
    
    private TextField[] registerValues; //PC A X Y STACK
    private Label[] flagValues; //C N B Z
    
    private ObservableList<Label> stackItems;
    private int stackSize = 5;
    private ListViewHandeler stackHandeler;
    
    public RegisterPanel(String title, Cpu cpu, Memory memory)
    {
        super(title);
        this.cpu = cpu;
        stackHandeler = new ListViewHandeler(memory, cpu.getStackStart(), (char) (cpu.getStackStart()+0xff), stackSize, 2);
        construct();
        update();
    }
    
    public void construct()
    {
        HBox registerBox = new HBox();
        registerBox.setPadding(new Insets(10));
        registerBox.setSpacing(20);
        
        GridPane statsBox = new GridPane();
        //statsBox.getChildren().addAll(labelPC, labelA, labelX, labelY, stackPointer, flagBox);
        
        String[] registerNames = {"PC", "A", "X", "Y", "STACK*"};
        String[] tooltipRegisterNames = {"Program counter", 
            "A register, where to perform arithmetic", 
            "X register", 
            "Y register", 
            "Stack pointer"};
        for(int n = 0; n< 5; n++)
        {
            Label nameLabel = new Label(registerNames[n]);
            Tooltip toolTip = new Tooltip(tooltipRegisterNames[n]);
            nameLabel.setTooltip(toolTip);
            nameLabel.setStyle("-fx-font: 24 arial;");
            statsBox.add(nameLabel, 0, n);
        }
        
        registerValues = new TextField[5];
        for(int n = 0; n< 5; n++)
        {
            TextField editBox = new TextField("");
            editBox.setStyle("-fx-font: 24 arial;");
            editBox.setEditable(true);
            editBox.setMaxWidth(150);
            registerValues[n]= editBox;
            statsBox.add(registerValues[n], 2, n);
        }
        
        //get reference to parent node
        RegisterPanel r = this;
        registerValues[0].setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    if ((registerValues[0].getText() != null && !registerValues[0].getText().isEmpty())) {
                        try{
                            char value = (char) Integer.parseInt(registerValues[0].getText(), 16);
                            
                            cpu.setPC(value);
                            //unfocus this textbox
                            r.requestFocus();
                        }
                        catch(NumberFormatException ex)
                        {
                            registerValues[0].setText(Integer.toHexString(cpu.getPC()));
                        }
                    }
                }
             });
        
        registerValues[1].setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    if ((registerValues[1].getText() != null && !registerValues[1].getText().isEmpty())) {
                        try{
                            byte value = (byte) Integer.parseInt(registerValues[1].getText(), 16);
                            
                            cpu.setA(value);
                            
                            //unfocus this textbox
                            r.requestFocus();
                        }
                        catch(NumberFormatException ex)
                        {
                            registerValues[1].setText(Integer.toHexString(cpu.getA()));
                        }
                    }
                }
             });
        registerValues[2].setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    if ((registerValues[2].getText() != null && !registerValues[2].getText().isEmpty())) {
                        try{
                            byte value = (byte) Integer.parseInt(registerValues[2].getText(), 16);
                            
                            cpu.setX(value);
                            
                            //unfocus this textbox
                            r.requestFocus();
                        }
                        catch(NumberFormatException ex)
                        {
                            registerValues[2].setText(Integer.toHexString(cpu.getX()));
                        }
                    }
                }
             });
        registerValues[3].setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    if ((registerValues[3].getText() != null && !registerValues[3].getText().isEmpty())) {
                        try{
                            byte value = (byte) Integer.parseInt(registerValues[3].getText(), 16);
                            
                            cpu.setY(value);
                            
                            //unfocus this textbox
                            r.requestFocus();
                        }
                        catch(NumberFormatException ex)
                        {
                            registerValues[3].setText(Integer.toHexString(cpu.getY()));
                        }
                    }
                }
             });
        registerValues[4].setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent e) {
                    if ((registerValues[4].getText() != null && !registerValues[4].getText().isEmpty())) {
                        try{
                            byte value = (byte) Integer.parseInt(registerValues[4].getText(), 16);
                            
                            cpu.setStackPointer(value);
                            
                            //unfocus this textbox
                            r.requestFocus();
                        }
                        catch(NumberFormatException ex)
                        {
                            registerValues[4].setText(Integer.toHexString(cpu.getStackPointer()));
                        }
                    }
                }
             });
        
        for(int n = 0; n< 5; n++)
        {
            Label insert = new Label("  ");
            insert.setStyle("-fx-font: 24 arial;");
            statsBox.add(insert, 1, n);
        }
        
        HBox flagBox = new HBox();
        flagBox.setAlignment(Pos.TOP_LEFT);
        //flagBox.setPadding(new Insets(5));
        flagBox.setSpacing(5);
        
        flagValues = new Label[4];
        flagValues[0] = new Label("C");
        flagValues[0].setStyle("-fx-font: 24 arial;");
        flagValues[1] = new Label("N");
        flagValues[1].setStyle("-fx-font: 24 arial;");
        flagValues[2] = new Label("B");
        flagValues[2].setStyle("-fx-font: 24 arial;");
        flagValues[3] = new Label("Z");
        flagValues[3].setStyle("-fx-font: 24 arial;");
        for(int i = 0; i <4; i++){
            final int n = i;
            flagValues[i].setOnMouseClicked((mouseEvent) -> {
                cpu.toggleFlag(n);
            });
            flagBox.getChildren().add(flagValues[i]);
        }
        
        Label statusLabel = new Label("STATUS");
        statusLabel.setTooltip(new Tooltip("Status flags: \nClear, \nNegatif, \nBigger, \nZero"));
        statusLabel.setStyle("-fx-font: 24 arial;");
        statsBox.add(statusLabel, 0, 5);
        statsBox.add(flagBox, 2, 5);
        
        GridPane stackList = new GridPane();
        //stackList.add(stackTitle, 0, 0);
        
        Label add1 = new Label("$40FF");
        add1.setStyle("-fx-font: 24 arial;");
        Label add2 = new Label("$4100");
        add2.setStyle("-fx-font: 24 arial;");
        Label add3 = new Label("$4101");
        add3.setStyle("-fx-font: 24 arial;");
        Label add4 = new Label("$4102");
        add4.setStyle("-fx-font: 24 arial;");
        Label add5 = new Label("$4103");
        add5.setStyle("-fx-font: 24 arial;");
        
        stackList.add(add1, 0, 1);
        stackList.add(add2, 0, 2);
        stackList.add(add3, 0, 3);
        stackList.add(add4, 0, 4);
        stackList.add(add5, 0, 5);
        
        Label num1 = new Label("B5");
        num1.setStyle("-fx-font: 24 arial;");
        Label num2 = new Label("15");
        num2.setStyle("-fx-font: 24 arial;");
        Label num3 = new Label("8F");
        num3.setStyle("-fx-font: 24 arial;");
        Label num4 = new Label("02");
        num4.setStyle("-fx-font: 24 arial;");
        Label num5 = new Label("00");
        num5.setStyle("-fx-font: 24 arial;");
        
        stackList.add(num1, 2, 1);
        stackList.add(num2, 2, 2);
        stackList.add(num3, 2, 3);
        stackList.add(num4, 2, 4);
        stackList.add(num5, 2, 5);
        
        for(int i = 0; i< 5; i++)
        {
            Label ins = new Label("         ");
            stackList.add(ins, 1, i);
        }
        Tooltip stackTooltip = new Tooltip("Values stored in stack\nStack starts at 0x0100");
        ListView stackView = new ListView();
        stackView.setTooltip(stackTooltip);
        //stackView.setPrefWidth(100);
        stackView.setPrefHeight(150);
        //stackView.setMouseTransparent( true );
        stackView.setFocusTraversable( false );
        
        
        stackItems = FXCollections.observableArrayList();
        for(int i =0; i< stackSize; i++)
        {
            Label stackItem = new Label("$1ef   0e");
            stackItems.add(stackItem);
        }
        stackView.setItems(stackItems);
        
        Label stackTitle = new Label("STACK LIST");
        stackTitle.setTooltip(stackTooltip);
        stackTitle.setStyle("-fx-font: 24 arial;");
        
        VBox stackBox = new VBox();
        stackBox.setSpacing(5);
        stackBox.getChildren().addAll(stackTitle, stackView);
        
        registerBox.getChildren().addAll(statsBox, stackBox);
        
        super.setPanel(registerBox);
    }

    @Override
    public void update() {
        //update register values //PC A X Y STACK
        if(!registerValues[0].isFocused()) registerValues[0].setText(Integer.toHexString(cpu.getPC()));
        if(!registerValues[1].isFocused()) registerValues[1].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getA())));
        if(!registerValues[2].isFocused()) registerValues[2].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getX())));
        if(!registerValues[3].isFocused()) registerValues[3].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getY())));
        if(!registerValues[4].isFocused()) registerValues[4].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getStackPointer())));
        
        //update status flags //C N B Z
        boolean[] flags = cpu.getFlags();
        for(int i =0; i<flagValues.length; i++)
            flagValues[i].setTextFill(flags[i] ? Color.GREEN : Color.RED);
        
        //update stack
        stackItems.get(-stackHandeler.index).setStyle("-fx-background-color: blue");
        stackItems.get(-stackHandeler.index).setTextFill(Color.WHITE);
        
                
        char stackViewStart = (char) (cpu.getStackStart()+Byte.toUnsignedInt(cpu.getStackPointer()));
        byte[] values = stackHandeler.fetchValues(stackViewStart);
        int offset = stackHandeler.index;
        
        for(int i = 0; i < values.length; i++)
        {
            byte fetchedValue = values[i];
            String valueText = String.format("%02X",fetchedValue);
            String addressText = String.format("%04X",stackViewStart+i+offset);
            
            if(fetchedValue == 100){
                valueText = "--";
                addressText = "--";
            }
            stackItems.get(i).setText("$"+addressText+ "    "+ valueText);
        }
    }
}
