/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author child
 */
public class TitlePanel extends StackPane{
    
    private HBox titleBox;
    
    public TitlePanel(String title)
    {
        Label titleLabel = new Label(title);
        titleLabel.setPadding(new Insets(0, 2, 0, 2));
        titleLabel.setStyle("-fx-background-color: #f4f4f4;-fx-font: 16 arial;");
        titleLabel.setOnMouseClicked((mouseEvent) -> {
                    //l.setStyle("-fx-background-color: gray");
                    System.out.println("clicking label");
                });
        titleBox = new HBox();
        titleBox.getChildren().add(titleLabel);
        titleBox.setPadding(new Insets(-10, 0, 0, 10));
        titleBox.setAlignment(Pos.TOP_LEFT);
        titleBox.setMouseTransparent(true);
        this.setAlignment(titleBox, Pos.TOP_LEFT);
        
        //this.setMouseTransparent(true);
    }
    
    public void setPanel(Pane panel)
    {
        this.getChildren().clear();
        this.setAlignment(titleBox, Pos.TOP_LEFT);
        this.getChildren().addAll(panel, titleBox);
        
        
        panel.setStyle("-fx-border-color: black ;\n" +
                            "    -fx-border-width: 1 ; \n" +
                            "    -fx-border-style: solid;" +
                            "-fx-border-radius: 18 18 18 18;"+
                            "-fx-background-radius: 0 0 18 18;");
    }
}
