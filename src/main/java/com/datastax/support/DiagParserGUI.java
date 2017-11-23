/*
 * Copyright (c)
 *
 * Date: 23/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class DiagParserGUI extends Application {
    private final Label labelSelectedDirectory = new Label();
    private String diagpath;
    private AnchorPane anchorpane = new AnchorPane();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        BorderPane border = new BorderPane();

        HBox hbox = addHBox(primaryStage);
        border.setTop(hbox);
        border.setCenter(addAnchorPane());
        primaryStage.setTitle("Diag Parser");
        //primaryStage.setScene(new Scene(root, 1024,768));
        primaryStage.setScene(new Scene(border, 1024, 768));
        primaryStage.show();
    }

/*
 * Creates an HBox with two buttons for the top region
 */

    private HBox addHBox(final Stage primaryStage) {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);   // Gap between nodes
        hbox.setStyle("-fx-background-color: #336699;");
        Button btnOpenDirectoryChooser = new Button();
        btnOpenDirectoryChooser.setText("Open Diag Directory");
        btnOpenDirectoryChooser.setPrefSize(200, 20);
        Button buttonAnalyzed = new Button("Start Analyzing");
        buttonAnalyzed.setPrefSize(200, 20);
        btnOpenDirectoryChooser.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                openDiagDirectory(primaryStage);
            }
        });
        buttonAnalyzed.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if (diagpath != null) {
                    displayAnalysisResult();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning!");
                    alert.setHeaderText("");
                    alert.setContentText("Please choose the diag path first!");
                    alert.showAndWait();

                }
            }
        });
        hbox.getChildren().addAll(btnOpenDirectoryChooser, buttonAnalyzed);
        hbox.getChildren().add(labelSelectedDirectory);

        return hbox;
    }

    private void openDiagDirectory(final Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(primaryStage);

        if (selectedDirectory == null) {
            labelSelectedDirectory.setText("No Directory selected");
            diagpath = null;
        } else {
            labelSelectedDirectory.setText("Diag path: " + selectedDirectory.getAbsolutePath());
            diagpath = selectedDirectory.getAbsolutePath();
        }
    }

    //*** call the analyser interface here****///
    private void displayAnalysisResult() {
        TitledPane tp = new TitledPane();
        tp.setExpanded(false);
        tp.setPrefSize(1024, 20);
        tp.setText("nodetool status");
        Text t = new Text();
        t.setText("This is a text sample");
        tp.setContent(t);
        anchorpane.getChildren().addAll(tp);
    }

    private AnchorPane addAnchorPane() {


        //Button buttonSave = new Button("Save");
        //Button buttonCancel = new Button("Cancel");

        //HBox hb = new HBox();
        // hb.setPadding(new Insets(0, 10, 10, 10));
        // hb.setSpacing(10);
        // hb.getChildren().addAll(buttonSave, buttonCancel);

        // anchorpane.getChildren().addAll(grid,hb);
        // Anchor buttons to bottom right, anchor grid to top
        //AnchorPane.setBottomAnchor(hb, 8.0);
        // AnchorPane.setRightAnchor(hb, 5.0);
        // AnchorPane.setTopAnchor(grid, 10.0);
        //anchorpane.getChildren().addAll(tp);
        return anchorpane;
    }
}
