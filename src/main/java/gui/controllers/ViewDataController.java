package gui.controllers;
import dataCollection.GameInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ViewDataController implements Initializable {


    @FXML
    private TableColumn<GameInfo, String> gameType;

    @FXML
    private TableColumn<GameInfo, String> alg;

    @FXML
    private TableColumn<GameInfo, String> side;

    @FXML
    private TableColumn<GameInfo, String> winner;

    @FXML
    private TableColumn<GameInfo, Integer> turns;

    @FXML
    private TableView<GameInfo> tableView;

    public static ObservableList<GameInfo> gameList = FXCollections.observableArrayList();
    public static ObservableList<GameInfo> aiAiGameList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        gameType.setCellValueFactory(new PropertyValueFactory<GameInfo, String>("gameType"));
        alg.setCellValueFactory(new PropertyValueFactory<GameInfo, String>("algUsed"));
        side.setCellValueFactory(new PropertyValueFactory<GameInfo, String>("algSide"));
        winner.setCellValueFactory(new PropertyValueFactory<GameInfo, String>("gameWinner"));
        turns.setCellValueFactory(new PropertyValueFactory<GameInfo, Integer>("numTurnsToWin"));

        tableView.setItems(gameList);
    }
}
