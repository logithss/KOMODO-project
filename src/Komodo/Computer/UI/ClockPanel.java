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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author child
 */
public class ClockPanel extends TitlePanel implements UIPanel{

    private SystemBus systembus;
    private Button playButton;
    
    public ClockPanel(String title, SystemBus systembus) {
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
        
        Button powerButton = new Button("reset");
        powerButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    systembus.reset();
                }
            });
        
        Button stepButton = new Button("Step");
        stepButton.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    systembus.accessSystemClock().stepClock();
                }
            });
        
        buttonBox.getChildren().addAll(playButton, stepButton, powerButton);
        
        root.getChildren().addAll(buttonBox);
        root.setStyle("-fx-border-color: black ;\n" +
                            "    -fx-border-width: 1 ; \n" +
                            "    -fx-border-style: solid");
        
        super.setPanel(root);
    }

    @Override
    public void update() {
        playButton.setText(systembus.accessSystemClock().halted ? "resume": "halt");
    }
    
}
