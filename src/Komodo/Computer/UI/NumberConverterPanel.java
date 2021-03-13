
package Komodo.Computer.UI;

import Komodo.Assembler.UI.FileItem;
import Komodo.Computer.Components.Memory;
import Komodo.Tools.NumberConvertor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author child
 */
public class NumberConverterPanel extends VBox{
    
    TextField positionField;
    Label errorLabel;
    
    public NumberConverterPanel(Stage stage)
    {
        
        //1) file selection
        HBox fileBox = new HBox();
        Label fileName = new Label("Number: ");
        positionField = new TextField();
        errorLabel = new Label("");
        
        Button newFileButton = new Button("Select file");
        positionField.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    positionField.selectAll();
                    errorLabel.setText(NumberConvertor.convertNumber(positionField.getText()));
                }
            });
        
        fileBox.getChildren().addAll(fileName, positionField);
        fileBox.setSpacing(5);
        
        
        
        //combine all elements into root
        this.getChildren().addAll(fileBox, errorLabel);
        this.setSpacing(5);
        
        
    }
    
}
