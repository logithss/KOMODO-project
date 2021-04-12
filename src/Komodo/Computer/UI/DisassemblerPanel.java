/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Komodo.Computer.UI;

import Komodo.Commun.Instruction;
import Komodo.Commun.Instruction.AddressingMode;
import Komodo.Commun.Instructions;
import Komodo.Commun.NumberUtility;
import Komodo.Computer.Components.Memory;
import Komodo.Computer.Components.Processors.Cpu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author child
 */
public class DisassemblerPanel extends TitlePanel implements UIPanel{

    private Memory memory;
    private Cpu cpu;
    private ListViewHandeler instructionHandler;
    
    private int listSize = 10;
    private ObservableList<Label> items;
    private int codeIndex = 0;
    
    public DisassemblerPanel(String title, Cpu cpu, Memory memory) {
        super(title);
        this.titleLabel.setTooltip(new Tooltip("List of dissasembled instrcution,\nstarting at address of PC"));
        this.cpu = cpu;
        this.memory = memory;
        instructionHandler = new ListViewHandeler(memory, (char)0, (char) 0xffff, (char) listSize*3, 0);
        this.construct();
        this.update();
    }
    
    private void construct()
    {
        
        ListView codeView = new ListView();
        //stackView.setPrefWidth(100);
        codeView.setPrefHeight(250);
        //codeView.setMouseTransparent( true );
        codeView.setFocusTraversable( false );
        
        
        items = FXCollections.observableArrayList();
        for(int i =0; i< listSize; i++)
        {
            Label item = new Label("$1ef   0e");
            items.add(item);
        }
        codeView.setItems(items);
        
        Label title = new Label("DISASSEMBLY");
        title.setStyle("-fx-font: 24 arial;");
        
        VBox rootBox = new VBox();
        rootBox.setPadding(new Insets(10));
        rootBox.setSpacing(20);
        rootBox.getChildren().addAll(codeView);
        super.setPanel(rootBox);
    }

    @Override
    public void update() {
        items.get(-instructionHandler.index).setStyle("-fx-background-color: blue");
        items.get(-instructionHandler.index).setTextFill(Color.WHITE);
        
                
        char stackViewStart = (char)(cpu.getPC());
        byte[] code = instructionHandler.fetchValues(stackViewStart);
        int offset = instructionHandler.index;
        codeIndex = 0;
        
        for(int i = 0; i < this.listSize; i++)
        {
            String line = disassembleCode(code);
            if(line != null && (i+offset) >= 0){
                items.get(i).setText("$"+String.format("%04X", (stackViewStart+offset+i)) + "   " + line);
            }
            else
                items.get(i).setText("---");
        }
    }
    
    public String disassembleCode(byte[] code)
    {
        byte opcode = code[codeIndex];
        codeIndex++;
        Instruction instruction = Instructions.getInstructionByOpcode(opcode);
        if(instruction == null)
            return null;
        
        String mnemonicText = instruction.mnemonic;
        String addressingModeText = "";
        String argumentText = "";
        AddressingMode addMode = instruction.addressingMode;
        int bytesToFetch = 0;
        
        switch(addMode)
        {
            case IMPLIED:
                break;
            case IMMEDIATE:
                addressingModeText = "#";
                bytesToFetch = 1;
                break;
            case ABSOLUTE:
                bytesToFetch = 2;
                break;
            case ABSOLUTE_X:
                addressingModeText = "x";
                bytesToFetch = 2;
                break;
            case ABSOLUTE_Y:
                addressingModeText = "y";
                bytesToFetch = 2;
                break;
            case INDIRECT:
                addressingModeText = "!";
                bytesToFetch = 2;
                break;
            case INDIRECT_X:
                addressingModeText = "x!";
                bytesToFetch = 2;
                break;
        }
        
        if(bytesToFetch == 1){
            argumentText = "$"+String.format("%02X",code[codeIndex]);
            codeIndex++;
        }
        else if(bytesToFetch == 2){
            byte highByte = code[codeIndex];
            codeIndex++;
            int value = NumberUtility.bytesToWord(highByte, code[codeIndex]);
            argumentText = "$"+String.format("%04X", value);
            codeIndex++;
        }
        
        return mnemonicText.toUpperCase()+"   "+addressingModeText.toUpperCase()+" "+argumentText;
    }
    
}
