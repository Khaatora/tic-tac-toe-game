package com.example.tictactoegama.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;

/**
 * VideoController class to manage media playback.
 * Author: Mohamed Fekry Khedr
 */
public class VideoController implements Initializable {

    @FXML
    private MediaView myMediaView;

    private File file;
    private Stage stage;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Scene previousScene;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
    }

    public void setStageAndPreviousScene(Stage stage, Scene previousScene) {
        this.stage = stage;
        this.previousScene = previousScene;

        // Register the window close request handler
        stage.setOnCloseRequest(this::handleWindowCloseRequest);
    }

    public void setVideoPath(String videoPath) {
        file = new File(videoPath);
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        myMediaView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
    }

    private void handleWindowCloseRequest(WindowEvent event) {
        // Stop media playback
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        // Prevent the default close operation
        event.consume();

        // Switch back to the previous scene
        if (stage != null && previousScene != null) {
            stage.setScene(previousScene);
        }
    }
}