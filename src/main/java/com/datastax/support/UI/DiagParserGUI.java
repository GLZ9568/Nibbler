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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
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
    private StatusPane sp = new StatusPane();
    private TitledPane dsetoolringpane = new TitledPane();
    private DsetoolRingPane rp = new DsetoolRingPane();
    private TitledPane clusterinfopane = new TitledPane();
    private ClusterinfoPane cip = new ClusterinfoPane();
    private TitledPane infopane = new TitledPane();
    private TitledPane nodeStatusTitledPane = new TitledPane();
    private NotoolInfoPane nip = new NotoolInfoPane();
    private ConfInfoPane cfip;
    private NodeStatusTitledPane nsp;
    private CfstatsTitledPane cfsp;
    private TitledPane confInfoPane = new TitledPane();
    private TitledPane cfStatsPane = new TitledPane();


    @Override
    public void start(Stage primaryStage) throws Exception {
        // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        HBox hbox = addHBox(primaryStage);
        border.setTop(hbox);
        //border.setCenter(addAnchorPane());
        border.setCenter(addScrollPane());
        primaryStage.setTitle("Diag Parser");
        //primaryStage.setScene(new Scene(root, 1024,768));
        //primaryStage.setMaxWidth(768);
        // primaryStage.setMinHeight(1024);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screen_height = visualBounds.getHeight();
        double screen_width = visualBounds.getWidth();
        logger.info("screen height is: " + screen_height);
        logger.info("screen width is: " + screen_width);
        primaryStage.setMinHeight(screen_height * 0.8);
        primaryStage.setMinWidth(screen_width * 0.7);
        //primaryStage.setMaxWidth(screen_width*0.7);
        primaryStage.setScene(new Scene(border, screen_width * 0.7, screen_height * 0.8));
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

                    // statuspane.setExpanded(false);
                    //dsetoolringpane.setExpanded(false);
                    clusterinfopane.setExpanded(false);
                    nodeStatusTitledPane.setExpanded(false);
                    infopane.setExpanded(false);
                    confInfoPane.setExpanded(false);
                    cfStatsPane.setExpanded(false);
                    border.getChildren().remove(anchorpane);

                    //border.getChildren().remove(scrollpane);

                    anchorpane.getChildren().removeAll(grid);
                    //scrollpane..removeAll(grid);
                    ///first parse the input files//
                    startParsing();
                    buttonAnalyzed.setDisable(true);
                    ///display the analysis result///
                    Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
                    double screen_width = visualBounds.getWidth();
                    double screen_height = visualBounds.getHeight();
                    //primaryStage.setScene(new Scene(border, screen_width * 0.7+15, screen_height * 0.8));
                    primaryStage.setMinWidth(screen_width*0.7+15);
                    displayAnalysisResult();
                    saveAnalysisReport();
                    buttonAnalyzed.setDisable(false);

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

    //*** call the analyser interface here****///
    private void startParsing() {

        ff = new FileFactory(new File(diagpath));
        //boolean b = ff.initiate(new File(diagpath));
        boolean b = ff.getInitiateSuccessCheck();

        if (b) {
            // statuspane = sp.createstatusPane(ff);
            //  dsetoolringpane =  rp.createDsetoolRingPane(ff);
            clusterinfopane = cip.createClusterInfoPane(ff);
            //nodeStatusTitledPane = new NodeStatusTitledPane(ff);
            nsp = new NodeStatusTitledPane(ff);
            nodeStatusTitledPane = nsp.getNodeStatusTitledPane();
            infopane = nip.createinfoPane(ff);
            cfip = new ConfInfoPane(ff);
            confInfoPane = cfip.getConfInfoPane();
            cfsp = new CfstatsTitledPane(ff);
            cfStatsPane = cfsp.getCfstatsTitledPane();

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText("");
            alert.setContentText("No Valid Diag files!");
            alert.showAndWait();

        }
    }


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
        grid.getChildren().removeAll(clusterinfopane, nodeStatusTitledPane, infopane, confInfoPane, cfStatsPane);


        grid.add(clusterinfopane, 0, 0);
        //grid.add(statuspane,0,1);
        // grid.add(dsetoolringpane,0,2);
        grid.add(nodeStatusTitledPane, 0, 1);
        grid.add(infopane, 0, 2);
        grid.add(cfStatsPane, 0, 3);
        grid.add(confInfoPane, 0, 4);
        anchorpane.getChildren().add(grid);
        scrollpane.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                anchorpane.setPrefWidth(arg2.doubleValue() - 15);
                grid.setPrefWidth(arg2.doubleValue() - 15);
                clusterinfopane.setPrefWidth(arg2.doubleValue() - 15);
                nodeStatusTitledPane.setPrefWidth(arg2.doubleValue() - 15);
                infopane.setPrefWidth(arg2.doubleValue() - 15);
                confInfoPane.setPrefWidth(arg2.doubleValue() - 15);
                cfStatsPane.setPrefWidth(arg2.doubleValue() - 15);


            }
        });
        //anchorpane.getChildren().add(statuspane);
        // anchorpane.getChildren().add(dsetoolringpane);
    }

    private void saveAnalysisReport()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        String report_status_success_str = "";
        String report_status_fail_str = "";
        String cluster_info_report_path = cip.save_cluster_info_report();
        if (cluster_info_report_path.equals("")
                || cluster_info_report_path.equals("error")) {
            report_status_fail_str += "Error saving report file: cluster_info.out !!\n";
        } else {
            report_status_success_str += cluster_info_report_path + "\n";
        }

        String node_info_report_path = nip.save_node_info_report();

        if (node_info_report_path.equals("")
                || node_info_report_path.equals("error")) {
            report_status_fail_str += "Error saving report file: nodetool_info.out !!\n";
        } else {
            report_status_success_str += node_info_report_path + "\n";
        }

        String node_status_report_path = nsp.save_node_status_report();

        if (node_status_report_path.equals("")
                || node_status_report_path.equals("error")) {
            report_status_fail_str += "Error saving report file: node_status_info.out !!\n";
        } else {
            report_status_success_str += node_status_report_path + "\n";
        }

        String table_stats_report_path = cfsp.save_table_stats_report();

        if (table_stats_report_path.equals("")
                || table_stats_report_path.equals("error")) {
            report_status_fail_str += "Error saving report file: table_stats_info.out !!\n";
        } else {
            report_status_success_str += table_stats_report_path + "\n";
        }

        String node_conf_file_report_path = cfip.save_node_conf_file_report();

        if (node_conf_file_report_path.equals("")
                || node_conf_file_report_path.equals("error")) {
            report_status_fail_str += "Error saving report file: node_conf_file_info.out !!\n";
        } else {
            report_status_success_str += node_conf_file_report_path + "\n";
        }


        TextArea textArea = new TextArea("Analysis Report Files saved to: \n" +
                report_status_success_str + "\n" + report_status_fail_str
        );
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);
        alert.setTitle("Analysis is done! ");
        alert.setHeaderText("");
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();
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