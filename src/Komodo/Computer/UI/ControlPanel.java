/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Computer.Components.SystemBus;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author child
 */
public class ControlPanel extends TitlePanel implements UIPanel{

    private SystemBus systembus;
    
    private Button playButton;
    private Button powerButton;
    private Button stepButton;
    private Label cycleCountText;
    
    public ControlPanel(String title, SystemBus systembus) {
        super(title);
        this.systembus = systembus;
        systembus.accessSystemClock().haltClock();
        construct();
    }
    
    private void construct()
    {
        VBox root = new VBox();
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        
        playButton = new Button("halt");
        boolean a = false;
        playButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    if(systembus.accessSystemClock().halted){
                        systembus.accessSystemClock().resumeClock();
                        //playButton.setText("Halt");
                    }
                    else
                    {
                        systembus.accessSystemClock().haltClock();
                        //playButton.setText("Resume");
                    }   
                    
                    //systembus.accessSystemClock().debugDelay = 10;
                    //playButton.setText(systembus.accessSystemClock().halted ? "resume": "halt");
                }
            });
        
        powerButton = new Button("powerOff");
        powerButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    if(systembus.on)
                        systembus.powerOff();
                    else
                        systembus.powerOn();
                }
            });
        
        stepButton = new Button("Step");
        stepButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    systembus.accessSystemClock().stepClock();
                }
            });
        
        Label delayText = new Label("Delay(ms): ");
        TextField delayField = new TextField();
        delayField.setPrefWidth(100);
        delayField.setOnAction(new EventHandler<ActionEvent>() {
        @Override
            public void handle(ActionEvent e) {
                if ((delayField.getText() != null && !delayField.getText().isEmpty())) {
                    try{
                        int value = Integer.parseInt(delayField.getText());
                        systembus.accessSystemClock().debugDelay = value;
                    }
                    catch(NumberFormatException ex)
                    {
                        delayField.setText("");
                    }
                } else {
                    //just skip to next cell
                    //delayField.setText("");
                }
             }
         });
        HBox delayBox = new HBox();
        delayBox.getChildren().addAll(delayText, delayField);
        
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        buttonBox.getChildren().addAll(powerButton, playButton, stepButton, separator, delayBox);
        
        cycleCountText = new Label("Cycles: ");
        
        Separator separatorRoot = new Separator();
        separatorRoot.setOrientation(Orientation.HORIZONTAL);
        
        root.getChildren().addAll(buttonBox, separatorRoot, cycleCountText);
        
        root.setPadding(new Insets(10));
        root.setSpacing(10);
        
        super.setPanel(root);
    }

    @Override
    public void update() {
        playButton.setText(systembus.accessSystemClock().halted ? "resume": "halt");
        powerButton.setText(systembus.on ? "Power Off": "Power On");
        
        stepButton.setVisible(systembus.on && systembus.accessSystemClock().halted);
        stepButton.setManaged(systembus.on && systembus.accessSystemClock().halted);
        
        playButton.setVisible(systembus.on);
        playButton.setManaged(systembus.on);
        
        cycleCountText.setText("Cycles: "+systembus.accessSystemClock().cycleCount);
        
        if(systembus.accessSystemClock().interupt > 0)
            cycleCountText.setText(cycleCountText.getText()+ " (halted: "+systembus.accessSystemClock().interupt+"ms)");
    }
    
}
