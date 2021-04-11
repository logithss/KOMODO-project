

package Komodo.Assembler.UI;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author child
 */
public class SyntaxHelpPopup extends BorderPane{
    
    private String helpText = "Help: \n"
            + "Syntax is an assembler that converts Assembly code into byte instructions readable by the Komodo Computer.\n"
            + "- To start: add one or multiple files using the 'add file' button. Additionally you can also drag & drop your files in the\n"
            + "middle area. You can also remove files by clicking on the 'X' buttom besides their name.\n"
            + "- You also need to specify an output path by either clicking on the 'Set output path' button and choosing a path or drag & dropping\n"
            + "an output file in the bar.\n"
            + "- To assemble your files, click on the 'Assemble files' button.\n"
            + "- Syntax will print in the console on the bottom the result of the assembly. If there were any errors, the name of the error will be shown\n"
            + "allong with the line where the error occured.";
    
    public SyntaxHelpPopup(Stage stage)
    {
        TextArea textbox = new TextArea(helpText);
        textbox.setEditable(false);
        textbox.setWrapText(true);
        //this.getChildren().add(textbox);
        this.setCenter(textbox);
        
    }
    
}
