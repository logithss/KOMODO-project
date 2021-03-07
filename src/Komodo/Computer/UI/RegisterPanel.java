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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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
    
    private Label[] registerValues; //PC A X Y STACK
    private Label[] flagValues; //C N B Z
    
    private ObservableList<Label> stackItems;
    private int stackSize = 5;
    private ListViewHandeler stackHandeler;
    
    public RegisterPanel(String title, Cpu cpu, Memory memory)
    {
        super(title);
        this.cpu = cpu;
        stackHandeler = new ListViewHandeler(memory, cpu.getStackStart(), (char) (cpu.getStackStart()+0xff), stackSize);
        stackHandeler.curentIsMiddle = true;
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
        for(int n = 0; n< 5; n++)
        {
            Label nameLabel = new Label(registerNames[n]);
            nameLabel.setStyle("-fx-font: 24 arial;");
            statsBox.add(nameLabel, 0, n);
        }
        
        registerValues = new Label[5];
        for(int n = 0; n< 5; n++)
        {
            Label valueLabel = new Label("");
            valueLabel.setStyle("-fx-font: 24 arial;");
            registerValues[n]= valueLabel;
            statsBox.add(registerValues[n], 2, n);
        }
        
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
        for(int i = 0; i <4; i++)
            flagBox.getChildren().add(flagValues[i]);
        
        Label statusLabel = new Label("STATUS");
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
        
        ListView stackView = new ListView();
        //stackView.setPrefWidth(100);
        stackView.setPrefHeight(150);
        stackView.setMouseTransparent( true );
        stackView.setFocusTraversable( false );
        
        
        stackItems = FXCollections.observableArrayList();
        for(int i =0; i< stackSize; i++)
        {
            Label stackItem = new Label("$1ef   0e");
            stackItems.add(stackItem);
        }
        stackView.setItems(stackItems);
        
        Label stackTitle = new Label("STACK");
        stackTitle.setStyle("-fx-font: 24 arial;");
        
        VBox stackBox = new VBox();
        stackBox.setSpacing(5);
        stackBox.getChildren().addAll(stackTitle, stackView);
        
        registerBox.getChildren().addAll(statsBox, stackBox);
        registerBox.setStyle("-fx-border-color: black ;\n" +
                            "    -fx-border-width: 1 ; \n" +
                            "    -fx-border-style: solid");
        
        super.setPanel(registerBox);
    }

    @Override
    public void update() {
        //update register values //PC A X Y STACK
        registerValues[0].setText(Integer.toHexString(cpu.getPC()));
        registerValues[1].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getA())));
        registerValues[2].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getX())));
        registerValues[3].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getY())));
        registerValues[4].setText(String.format("%02X",Byte.toUnsignedInt(cpu.getStackPointer())));
        
        //update status flags //C N B Z
        boolean[] flags = cpu.getFlags();
        for(int i =0; i<flagValues.length; i++)
            flagValues[i].setTextFill(flags[i] ? Color.GREEN : Color.RED);
        
        //update stack
        if(stackHandeler.curentIsMiddle){
            stackItems.get((stackSize/2)).setStyle("-fx-background-color: black");
            stackItems.get((stackSize/2)).setTextFill(Color.WHITE);
        }
        else
        {
            stackItems.get(0).setStyle("-fx-background-color: black");
            stackItems.get(0).setTextFill(Color.WHITE);
        }
        
                
        char stackViewStart = (char) (cpu.getStackStart()+Byte.toUnsignedInt(cpu.getStackPointer()));
        byte[] values = stackHandeler.fetchValues(stackViewStart);
        int offset = 0;
        if(stackHandeler.curentIsMiddle)
            offset = -(stackSize/2);
        
        for(int i = 0; i < values.length; i++)
        {
            byte fetchedValue = values[i];
            String valueText = String.format("%02X",fetchedValue);
            String addressText = String.format("%02X",stackViewStart+i+offset);
            
            if(fetchedValue == 100){
                valueText = "--";
                addressText = "--";
            }
            stackItems.get(i).setText("$"+addressText+ "    "+ valueText);
        }
    }
}
