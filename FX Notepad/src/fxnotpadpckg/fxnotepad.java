package fxnotpadpckg;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Rehab A. Anwer
 */
public class fxnotepad extends Application {

    MenuBar menuBar;
    Menu fileMenu;
    Menu editMenu;
    Menu helpMenu;
    MenuItem newItem;
    MenuItem openItem;
    MenuItem saveItem;
    MenuItem exitItem;
    MenuItem undoItem;
    MenuItem cutItem;
    MenuItem copyItem;
    MenuItem pasteItem;
    MenuItem deleteItem;
    MenuItem selectAllItem;
    MenuItem aboutItem;
    SeparatorMenuItem sep1;
    SeparatorMenuItem sep2;
    SeparatorMenuItem sep3;
    TextArea textArea;

    static boolean savedBefore = false;
    static File selectedFile;
    static IndexRange indexRange;
    static String selectedText;

    @Override
    public void init() throws Exception {
        super.init(); //To change body of generated methods, choose Tools | Templates.

        menuBar = new MenuBar();
        fileMenu = new Menu("File");
        editMenu = new Menu("Edit");
        helpMenu = new Menu("Help");
        newItem = new MenuItem("New");
        openItem = new MenuItem("Open");
        saveItem = new MenuItem("Save");
        exitItem = new MenuItem("Exit");
        undoItem = new MenuItem("Undo");
        cutItem = new MenuItem("Cut");
        copyItem = new MenuItem("Copy");
        pasteItem = new MenuItem("Paste");
        deleteItem = new MenuItem("Delete");
        selectAllItem = new MenuItem("Select All");
        aboutItem = new MenuItem("About Notepad");
        sep1 = new SeparatorMenuItem();
        sep2 = new SeparatorMenuItem();
        sep3 = new SeparatorMenuItem();

        newItem.setAccelerator(KeyCombination.keyCombination("Alt+n"));
        saveItem.setAccelerator(KeyCombination.keyCombination("Alt+s"));
        exitItem.setAccelerator(KeyCombination.keyCombination("Alt+e"));
        openItem.setAccelerator(KeyCombination.keyCombination("Alt+p"));

        fileMenu.getItems().addAll(newItem, openItem, saveItem, sep1, exitItem);
        editMenu.getItems().addAll(undoItem, sep2, cutItem, copyItem, pasteItem, deleteItem, sep3, selectAllItem);
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

        textArea = new TextArea();
        textArea.setPrefColumnCount(1000);
        textArea.setPrefRowCount(500);
    }

    @Override
    public void start(Stage primaryStage) {

        newItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (textArea.getText().length() != 0) {
                    ButtonType alertRes = confirmSaving();

                    if (alertRes == ButtonType.YES) {
                        saveFile(primaryStage);
                    }
                    textArea.clear();

                }

            }
        });

        openItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (textArea.getText().length() != 0) {
                    String message = "Do you want to save?";
                    Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        saveFile(primaryStage);
                    }
                }
                String data = openFile(primaryStage);
                textArea.setText(data);
            }
        });

        saveItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saveFile(primaryStage);
            }
        });

        exitItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (textArea.getText().length() != 0) {
                    String message = "Do you want to save?";
                    Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        saveFile(primaryStage);
                    }
                }
                primaryStage.close();
            }
        });

        undoItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.undo();
            }
        });

        cutItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                indexRange = textArea.getSelection();
                selectedText = textArea.getSelectedText();
                textArea.deleteText(indexRange);
            }
        });

        copyItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedText = textArea.getSelectedText();
            }
        });

        pasteItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.replaceSelection(selectedText);
            }
        });

        deleteItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange indRange = textArea.getSelection();
                textArea.deleteText(indRange);
            }
        });

        selectAllItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.selectAll();
            }
        });

        aboutItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("About FX Notepad");
                alert.setHeaderText(null);
                alert.setContentText("FX Notepad\nCreated by: Rehab Ayman\n2019");
                alert.showAndWait();

            }
        });

        primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
            if (textArea.getText().length() != 0) {
                String message = "Do you want to save?";
                Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    saveFile(primaryStage);
                }
            }
            primaryStage.close();
        }
        );

        BorderPane borderPanel = new BorderPane();

        borderPanel.setTop(menuBar);

        borderPanel.setCenter(textArea);
        Scene scene = new Scene(borderPanel, 1000, 500);

        primaryStage.setTitle(
                "FX Notepad");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    String openFile(Stage stage) {

        String data = new String();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open Resource File");
        selectedFile = chooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                FileInputStream inputStream = new FileInputStream(selectedFile);
                int fileSize = inputStream.available();
                byte[] b = new byte[fileSize];
                inputStream.read(b);
                data = new String(b);
                inputStream.close();

            } catch (FileNotFoundException ex) {
                Logger.getLogger(fxnotepad.class
                        .getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(fxnotepad.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
            stage.setTitle(selectedFile.toPath().toString());
        }

        return data;
    }

    void saveFile(Stage stage) {

        String data = textArea.getText();
        if (!savedBefore && selectedFile == null) {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save to Resource File");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            chooser.getExtensionFilters().add(extFilter);
            selectedFile = chooser.showSaveDialog(stage);
        }

        FileWriter fw;
        try {
            fw = new FileWriter(selectedFile, false);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(data);
            fw.close();
            pw.close();

        } catch (IOException ex) {
            Logger.getLogger(fxnotepad.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        stage.setTitle(selectedFile.toPath().toString());
        savedBefore = true;
    }

    ButtonType confirmSaving() {
        String message = "Do you want to save?";
        Alert alert = new Alert(AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        return alert.getResult();
    }
}
