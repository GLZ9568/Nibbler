/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

import com.datastax.support.Util.FileFactory;

/**
 * Created by Mike Zhang on 23/11/17
 * <p>
 * Main GUI Class
 */

public class DiagParserGUI extends Application {
    private final TextField labelSelectedDirectory = new TextField();
    private BorderPane border = new BorderPane();
    private String diagpath;
    private AnchorPane anchorpane = new AnchorPane();
    private GridPane grid = new GridPane();
    private FileFactory ff;
    private TitledPane statuspane = new TitledPane();
    private TitledPane dsetoolringpane = new TitledPane();

    private TitledPane clusterinfopane = new TitledPane();



    @Override
    public void start(Stage primaryStage) throws Exception {
        // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));



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
        final Button buttonAnalyzed = new Button("Start Analyzing");
        buttonAnalyzed.setPrefSize(200, 20);
        labelSelectedDirectory.setPrefSize(700, 20);
        labelSelectedDirectory.setStyle("-fx-background-color: #336699; -fx-text-inner-color: red;");
        btnOpenDirectoryChooser.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                openDiagDirectory(primaryStage);
            }
        });
        buttonAnalyzed.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if (diagpath != null) {

                    ///refresh the output
                    //anchorpane.getChildren().clear();

                    statuspane.setExpanded(false);
                    dsetoolringpane.setExpanded(false);
                    clusterinfopane.setExpanded(false);
                    border.getChildren().remove(anchorpane);

                    anchorpane.getChildren().removeAll(grid);
                    ///first parse the input files//
                    startParsing();
                    buttonAnalyzed.setDisable(true);
                    ///display the analysis result///
                    displayAnalysisResult();
                    buttonAnalyzed.setDisable(false);
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

    private void startParsing() {

        ff = new FileFactory();
        boolean b = ff.readFiles(new File(diagpath));

        if (b) {
            statuspane = new StatusPane().createstatusPane(ff);
            dsetoolringpane =  new DsetoolRingPane().createDsetoolRingPane(ff);
            clusterinfopane = new ClusterinfoPane().createClusterInfoPane(ff);
        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText("");
            alert.setContentText("No Valid Diag files!");
            alert.showAndWait();

        }
    }

    //*** call the analyser interface here****///
    private void displayAnalysisResult() {
       /* TitledPane tp = new TitledPane();
        tp.setExpanded(false);
        tp.setPrefSize(1024, 20);
        tp.setText("nodetool status");
        Text t = new Text();
        t.setText("This is a text sample");
        tp.setContent(t);*/
        border.setCenter(addAnchorPane());
        grid.getChildren().removeAll(statuspane,dsetoolringpane);

        grid.add(statuspane,0,1);
        grid.add(dsetoolringpane,0,2);
        grid.add(clusterinfopane,0,0);
        anchorpane.getChildren().add(grid);
        //anchorpane.getChildren().add(statuspane);
       // anchorpane.getChildren().add(dsetoolringpane);
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
