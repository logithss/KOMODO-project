/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Computer.Components.Processors.Cpu;
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
public class RegisterPanel extends HBox implements UIPanel{
    
    private Cpu cpu;
    
    public RegisterPanel(Cpu cpu)
    {
        this.cpu = cpu;
        update();
        System.out.println("register");
    }
    
    public void update()
    {
        this.setStyle(
                                "-fx-border-color: blue ;\n" +
                                "    -fx-border-width: 5 ; \n" +
                                "    -fx-border-style: solid  line-cap round ;" +
                                "-fx-background-color:gray;");
        this.setPadding(new Insets(5));
        this.setSpacing(20);
        
        GridPane statsBox = new GridPane();
        //statsBox.getChildren().addAll(labelPC, labelA, labelX, labelY, stackPointer, flagBox);
        
        String[] name = {"PC", "A", "X", "Y", "STACK*"};
        for(int n = 0; n< 5; n++)
        {
            Label nameLabel = new Label(name[n]);
            nameLabel.setStyle("-fx-font: 24 arial;");
            statsBox.add(nameLabel, 0, n);
        }
        
        String[] value = {"$45FC", "$45 [75]", "$A7 [184]", "$0 [0]", "$200"};
        for(int n = 0; n< 5; n++)
        {
            Label valueLabel = new Label(value[n]);
            valueLabel.setStyle("-fx-font: 24 arial;");
            statsBox.add(valueLabel, 2, n);
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
        flagBox.getChildren().addAll(carry, negative, bigger, zero);
        
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
        
        this.getChildren().addAll(statsBox, stackList);
        System.out.println("new2");
    }
}
