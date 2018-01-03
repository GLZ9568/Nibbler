/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

import com.datastax.support.Nibbler;
import com.datastax.support.Util.FileFactory;
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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Mike Zhang on 23/11/17
 * <p>
 * Main GUI Class
 */

public class DiagParserGUI extends Application {

    private static final Logger logger = LogManager.getLogger(FileFactory.class);
    private final TextField labelSelectedDirectory = new TextField();
    private BorderPane border = new BorderPane();
    private String diagpath;
    private AnchorPane anchorpane = new AnchorPane();
    private ScrollPane scrollpane = new ScrollPane();
    private GridPane grid = new GridPane();
    private FileFactory ff;
    private TitledPane statuspane = new TitledPane();
    private StatusPane sp =  new StatusPane();
    private TitledPane dsetoolringpane = new TitledPane();
    private DsetoolRingPane rp =  new DsetoolRingPane();
    private TitledPane clusterinfopane = new TitledPane();
    private ClusterinfoPane cip = new ClusterinfoPane();
    private TitledPane infopane = new TitledPane();
    private NotoolInfoPane nip = new NotoolInfoPane();




    @Override
    public void start(Stage primaryStage) throws Exception {
        // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        HBox hbox = addHBox(primaryStage);
        border.setTop(hbox);
        //border.setCenter(addAnchorPane());
        border.setCenter(addScrollPane());
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
        final Button btnOpenDirectoryChooser = new Button();
        //btnOpenDirectoryChooser.setStyle("-fx-font-size: 7pt");
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

                    statuspane.setExpanded(false);
                    dsetoolringpane.setExpanded(false);
                    clusterinfopane.setExpanded(false);
                    infopane.setExpanded(false);
                    border.getChildren().remove(anchorpane);

                    //border.getChildren().remove(scrollpane);

                    anchorpane.getChildren().removeAll(grid);
                    //scrollpane..removeAll(grid);
                    ///first parse the input files//
                    startParsing();
                    buttonAnalyzed.setDisable(true);
                    ///display the analysis result///
                    displayAnalysisResult();
                    buttonAnalyzed.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    try {
                        File status_file = null;
                        File dsetool_ring_file = null;
                        File nodetool_info_file = null;
                        File cluster_info_file = null;
                        String current_file_path_tmp = Nibbler.class.getProtectionDomain()
                                .getCodeSource().getLocation().toURI().getPath().toString();

                        String current_file_path = new String();

                        if(System.getProperty("os.name").toString().toLowerCase().equals("windows"))
                            current_file_path=  current_file_path_tmp.substring(0,current_file_path_tmp.lastIndexOf("\\") + 1);
                        else {
                            current_file_path=  current_file_path_tmp.substring(0,current_file_path_tmp.lastIndexOf("/") + 1);
                        }
                        logger.info("save status file report to: " + current_file_path + "status.out");
                        status_file = new File(current_file_path + "status.out");
                        dsetool_ring_file = new File(current_file_path + "dse_ring.out");
                        nodetool_info_file = new File(current_file_path + "nodetool_info.out");
                        cluster_info_file = new File(current_file_path + "cluster_info.out");

                        FileWriter status_file_writer = new FileWriter(status_file);
                        FileWriter ring_file_writer = new FileWriter(dsetool_ring_file);
                        FileWriter nodetool_info_file_writer = new FileWriter(nodetool_info_file);
                        FileWriter cluster_info_file_writer = new FileWriter(cluster_info_file);
                        //logger.info("status file content is: "+ sp.getStatus_report());
                        status_file_writer.write(sp.getStatus_report());
                        ring_file_writer.write(rp.getRing_report());
                        nodetool_info_file_writer.write(nip.getNodetool_info_report());
                        cluster_info_file_writer.write(cip.getCluster_info_report());
                        status_file_writer.close();
                        ring_file_writer.close();
                        nodetool_info_file_writer.close();
                        cluster_info_file_writer.close();

                        TextArea textArea = new TextArea("Analysis Reports saved to " + current_file_path
                                +"\n\n"+ "Report files: status.out dse_ring.out nodetool_info.out cluster_info.out");
                        textArea.setEditable(false);
                        textArea.setWrapText(true);
                        GridPane gridPane = new GridPane();
                        gridPane.setMaxWidth(Double.MAX_VALUE);
                        gridPane.add(textArea, 0, 0);
                        alert.setTitle("Analysis is done! ");
                        alert.setHeaderText("");
                        alert.getDialogPane().setContent(gridPane);
                        alert.showAndWait();

                    } catch (IOException e) {
                        alert.setTitle("");
                        alert.setHeaderText("");
                        alert.setContentText("Error saving report files!!!");
                        alert.showAndWait();
                        e.printStackTrace();

                    } catch (URISyntaxException e1)
                    {
                        alert.setTitle("");
                        alert.setHeaderText("");
                        alert.setContentText("Error saving report files!!!");
                        alert.showAndWait();
                        e1.printStackTrace();
                    }


                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
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
            statuspane = sp.createstatusPane(ff);
            dsetoolringpane =  rp.createDsetoolRingPane(ff);
            clusterinfopane = cip.createClusterInfoPane(ff);
            infopane = nip.createinfoPane(ff);
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
        //border.setCenter(addAnchorPane());
        border.setCenter(addScrollPane());
        grid.getChildren().removeAll(clusterinfopane,statuspane,dsetoolringpane,infopane);

        grid.add(clusterinfopane,0,0);
        grid.add(statuspane,0,1);
        grid.add(dsetoolringpane,0,2);
        grid.add(infopane,0,3);
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

    private ScrollPane addScrollPane() {

        scrollpane.setContent(anchorpane);
        return scrollpane;
    }
}
