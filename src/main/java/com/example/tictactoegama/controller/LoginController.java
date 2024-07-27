package com.example.tictactoegama.controller;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


import com.example.tictactoegama.Api.Client;
import com.example.tictactoegama.models.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.JSONObject;

/**
 *
 * @author Mohamed Fekry Khedr
 */
public class LoginController {

    @FXML
    private PasswordField passwordtxt;
    @FXML
    private TextField userNametxt;

    @FXML
    private Button registerBtn;
    @FXML
    private Button loginBtn;

    static Client client;

    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        // Handles register button click, opens the register screen
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/tictactoegama/views/Register.fxml"));
        Scene registerScene = new Scene(root);
        stage.setScene(registerScene);
        stage.show();
    }

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        // Handles login button click, validates input and communicates with the server
        String password = passwordtxt.getText();
        String username = userNametxt.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        User player = new User(username, password);
        String playerJson = player.toString(); // Convert Player object to JSON string

        System.out.println("Player JSON: " + playerJson); // Log the JSON string

        try {
            if (client != null && client.isConnected()) {
                Socket socket = client.getSocket();
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // Create a JSON object for the login request
                JSONObject request = new JSONObject();
                request.put("RequestType", "Login");
                request.put("User", playerJson);

                System.out.println("Request JSON: " + request.toString()); // Log the request JSON

                dos.writeUTF(request.toString()); // Send the JSON request to the server
                System.out.println("Data sent to server successfully."); // Log success message
            } else {
                showAlert("Error", "Not connected to server.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to communicate with server.");
        }
    }

    private void showAlert(String title, String message) {
        // Shows an alert with the provided title and message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}