/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Computer.Components.Processors.Cpu;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
    
    public RegisterPanel(String title, Cpu cpu)
    {
        super(title);
        this.cpu = cpu;
        construct();
    }
    
    public void construct()
    {
        HBox registerBox = new HBox();
        this.setStyle(
                                "-fx-border-color: blue ;\n" +
                                "    -fx-border-width: 5 ; \n" +
                                "    -fx-border-style: solid  line-cap round ;" +
                                "-fx-background-color:gray;");
        registerBox.setPadding(new Insets(5));
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
            registerValues[n].setStyle("-fx-font: 24 arial;");
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
        flagValues[0].setTextFill(Color.RED);
        flagValues[1] = new Label("N");
        flagValues[1].setStyle("-fx-font: 24 arial;");
        flagValues[1].setTextFill(Color.RED);
        flagValues[2] = new Label("B");
        flagValues[2].setStyle("-fx-font: 24 arial;");
        flagValues[2].setTextFill(Color.GREEN);
        flagValues[3] = new Label("Z");
        flagValues[3].setStyle("-fx-font: 24 arial;");
        flagValues[3].setTextFill(Color.RED);
        for(int i = 0; i <4; i++)
            flagBox.getChildren().add(flagValues[i]);
        
        Label statusLabel = new Label("STATUS");
        statusLabel.setStyle("-fx-font: 24 arial;");
        statsBox.add(statusLabel, 0, 5);
        statsBox.add(flagBox, 2, 5);
        
        /*ListView<String> stackList = new ListView<String>();
        stackList.setStyle("-fx-font: 24 arial;");
        stackList.getItems().add("$40ff b5");
        stackList.getItems().add("$4100 15");
        stackList.getItems().add("$4101 8f");
        stackList.getItems().add("$4102 02");
        stackList.getItems().add("$4103 00");*/
        
        GridPane stackList = new GridPane();
        Label stackTitle = new Label("STACK");
        stackTitle.setStyle("-fx-font: 24 arial;");
        stackList.add(stackTitle, 0, 0);
        
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
        
        registerBox.getChildren().addAll(statsBox, stackList);
    }

    @Override
    public void update() {
        //update register values
        //registerValues[0].setText( (cpu.) );
    }
}
