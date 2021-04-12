/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author child
 */
public class CustomHexSpinner extends HBox{
    
    public IntegerProperty value;
    private int lastValidValue = 0;
    private Button incButton;
    private Button decButton;
    private TextField textfield;

    public CustomHexSpinner() {
        
        value = new SimpleIntegerProperty();
        value.set(0);
        
        textfield = new TextField("0");
        textfield.setMaxHeight(5);
        //select text when focused on textField
        textfield.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.booleanValue() == true)
                textfield.selectAll();
        });
        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            if ((textfield.getText() != null && !textfield.getText().isEmpty())) {
                value.set(verifyValue(newValue, oldValue));
            }
        });
        
        incButton = new Button("^");
        incButton.setMaxHeight(5);
        incButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                textfield.setText(Integer.toHexString(value.get()+16));
            }
        });
        
        decButton = new Button("v");
        decButton.setMaxHeight(5);
        decButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override 
            public void handle(ActionEvent e) {
                if(value.get() >= 0x10)
                    textfield.setText(Integer.toHexString(value.get()-16));
                else
                    textfield.setText("0");
            }
        });
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(incButton, decButton);
        
        this.getChildren().addAll(textfield, vbox);
        //this.setMaxHeight(textfield.getMaxHeight());
        
    }
    
    
    private int verifyValue(String value, String oldValue)
    {
        try
        {
            int newValue = Integer.parseInt(value, 16);

            if(newValue > 0xffff)
                newValue = 0xffff;
            else if(newValue < 0)
                newValue = 0;


            newValue -= newValue %16;
            
            lastValidValue = newValue;
            return newValue;
        }
        catch(NumberFormatException e)
        {
            return lastValidValue;
        }
    }
    
    
    
}
