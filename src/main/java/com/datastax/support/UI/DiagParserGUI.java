/*
 * Copyright (c)
 *
 * Date: 24/11/2017
 *
 * Author: Chun Gao & Mike Zhang
 *
 */

package com.datastax.support.UI;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

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
    private ClusterinfoPane cip;
    private TitledPane infopane = new TitledPane();
    private TitledPane nodeStatusTitledPane = new TitledPane();
    private NotoolInfoPane nip = new NotoolInfoPane();
    private ConfInfoPane cfip;
    private NodeStatusTitledPane nsp;
    private CfstatsTitledPane cfsp;
    private TpstatsTitledPane tptp;
    private SystemResourceTitledPane srtp;
    private TitledPane confInfoPane = new TitledPane();
    private TitledPane cfStatsPane = new TitledPane();
    private TitledPane nodeResourceTitledPane = new TitledPane();
    private TitledPane tpstatsTitledPane = new TitledPane();

    @Override
    public void start(Stage primaryStage) throws Exception {

        HBox hbox = addHBox(primaryStage);
        border.setTop(hbox);
        border.setCenter(addScrollPane());
        primaryStage.setTitle("Nibbler v1.0.0 - DS Diag Analyzer");

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        double screen_height = visualBounds.getHeight();
        double screen_width = visualBounds.getWidth();
        logger.info("screen height is: " + screen_height);
        logger.info("screen width is: " + screen_width);
        primaryStage.setMinHeight(screen_height * 0.8);
        primaryStage.setMinWidth(screen_width * 0.7);
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
        labelSelectedDirectory.setStyle("-fx-background-color: #336699; -fx-text-inner-color: white;");
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
                    nodeResourceTitledPane.setExpanded(false);
                    tpstatsTitledPane.setExpanded(false);
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
                   // double screen_height = visualBounds.getHeight();
                    //primaryStage.setScene(new Scene(border, screen_width * 0.7+15, screen_height * 0.8));
                    primaryStage.setMinWidth(screen_width * 0.7 + 15);
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

        boolean b = ff.getInitiateSuccessCheck();

        if (b) {
            // statuspane = sp.createstatusPane(ff);
            //  dsetoolringpane =  rp.createDsetoolRingPane(ff);
            cip = new ClusterinfoPane(ff);
            clusterinfopane = cip.getClusterInfoPane();
            //nodeStatusTitledPane = new NodeStatusTitledPane(ff);
            nsp = new NodeStatusTitledPane(ff);
            nodeStatusTitledPane = nsp.getNodeStatusTitledPane();
            infopane = nip.createinfoPane(ff);
            cfip = new ConfInfoPane(ff);
            confInfoPane = cfip.getConfInfoPane();
            cfsp = new CfstatsTitledPane(ff);
            cfStatsPane = cfsp.getCfstatsTitledPane();
            srtp = new SystemResourceTitledPane(ff);
            nodeResourceTitledPane = srtp.getSystemResourceTitledPane();
            tptp = new TpstatsTitledPane(ff);
            tpstatsTitledPane = tptp.getTpstatsTitledPane();

        } else {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning!");
            alert.setHeaderText("");
            alert.setContentText("No Valid Diag files!");
            alert.showAndWait();

        }
    }


    private void displayAnalysisResult() {

        border.setCenter(addScrollPane());
        grid.getChildren().removeAll(clusterinfopane,
                nodeStatusTitledPane, infopane,
                confInfoPane, cfStatsPane,
                nodeResourceTitledPane,tpstatsTitledPane);


        grid.add(clusterinfopane, 0, 0);
        grid.add(nodeStatusTitledPane, 0, 1);
        grid.add(infopane, 0, 2);
        grid.add(nodeResourceTitledPane,0,3);
        grid.add(cfStatsPane, 0, 4);
        grid.add(tpstatsTitledPane, 0, 5);
        grid.add(confInfoPane, 0, 6);

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
                nodeResourceTitledPane.setPrefWidth(arg2.doubleValue() - 15);
                tpstatsTitledPane.setPrefWidth(arg2.doubleValue() - 15);

            }
        });

    }

    private void saveAnalysisReport() {
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

        String node_resource_usage_info_report_path = srtp.save_node_resource_usage_info_report();

        if (node_resource_usage_info_report_path.equals("")
                || node_resource_usage_info_report_path.equals("error")) {
            report_status_fail_str += "Error saving report file: node_resource_usage_info.out !!\n";
        } else {
            report_status_success_str += node_resource_usage_info_report_path + "\n";
        }

        String thread_pool_stats_report_path = tptp.save_thread_pool_stats_report();

        if (thread_pool_stats_report_path.equals("")
                || thread_pool_stats_report_path.equals("error")) {
            report_status_fail_str += "Error saving report file: thread_pool_stats.out !!\n";
        } else {
            report_status_success_str += thread_pool_stats_report_path + "\n";
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

        return anchorpane;
    }

    private ScrollPane addScrollPane() {

        scrollpane.setContent(anchorpane);
        return scrollpane;
    }
}