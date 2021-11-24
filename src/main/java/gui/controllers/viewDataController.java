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

public class viewDataController implements Initializable {


    @FXML
    private TableColumn<GameInfo, Integer> id;

    @FXML
    private TableColumn<GameInfo, String> gameType;

    @FXML
    private TableColumn<GameInfo, String> alg;

    @FXML
    private TableColumn<GameInfo, String> winner;

    @FXML
    private TableColumn<GameInfo, Integer> turns;

    @FXML
    private TableColumn<GameInfo, Double> time;

    @FXML
    private TableView<GameInfo> tableView;


    public static ObservableList<GameInfo> gameList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        id.setCellValueFactory(new PropertyValueFactory<GameInfo, Integer>("gameID"));
        gameType.setCellValueFactory(new PropertyValueFactory<GameInfo, String>("gameType"));
        alg.setCellValueFactory(new PropertyValueFactory<GameInfo, String>("algUsed"));
        winner.setCellValueFactory(new PropertyValueFactory<GameInfo, String>("gameWinner"));
        turns.setCellValueFactory(new PropertyValueFactory<GameInfo, Integer>("numTurnsToWin"));
        time.setCellValueFactory(new PropertyValueFactory<GameInfo, Double>("timeToWin"));

        tableView.setItems(gameList);
    }
}
