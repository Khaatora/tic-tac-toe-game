package com.example.tictactoegama.controller;

import com.example.tictactoegama.interfaces.AIMoodOption;
import com.example.tictactoegama.models.PlayBoard;
import com.example.tictactoegama.models.Player;
import com.example.tictactoegama.models.VideoViewHandler;
import com.example.tictactoegama.views.SymbolSelectionDialog;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class OnlineGameController {
    private static boolean flag;
    @FXML
    private Text gameStatus;

    @FXML
    private GridPane gameGrid;
    @FXML
    private String currentPlayer;
    @FXML
    Button replayBtn;
    @FXML
    private Line winnerLine;
    @FXML
    static Label playerXNametxt;
    @FXML
    static Label playerONametxt;
    @FXML
    static Label OScoreLabel;
    @FXML
    static Label XScoreLabel;
    @FXML
    static int playerScore, computerScore;
    static String globalPlayerSymbol;
    private Scene originalGameScene;
    private boolean isDraw;
    private String computerSymbol;
    private PlayBoard playBoard;
    private boolean gameEnded;
    private VideoViewHandler videoViewHandler;
    private static AIMoodOption aiMoodOption;

    public static void setPlayers(boolean flag,Player user, Player opponent){
        OnlineGameController.flag = !flag;
        XScoreLabel.setText(""+user.getScore());
        playerXNametxt.setText(user.getUsername());
        OScoreLabel.setText(""+opponent.getScore());
        playerONametxt.setText(opponent.getUsername());
    }

    @FXML
    public void initialize() {
        playBoard = new PlayBoard();
        gameEnded = false;
        videoViewHandler = new VideoViewHandler();

    }



    @FXML
    private void handleButtonClick(ActionEvent event) {
        if (gameEnded || flag) {
            disableButtons();
        }

        Button clickedButton = (Button) event.getSource();
        if (clickedButton.getText().isEmpty()) {
            int row = GridPane.getRowIndex(clickedButton);
            int col = GridPane.getColumnIndex(clickedButton);

            try {
                processPlayerMove(clickedButton, row, col);
            } catch (IOException e) {
                System.out.println("wtf");
                throw new RuntimeException(e);
            }

            if (!gameEnded) {
                PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
                pause.setOnFinished(e -> processComputerMove());
                pause.play();
            }
        }
    }

    private synchronized void OpponentStart(){
        processComputerMove();

    }

    @FXML
    public void handleGoBack(ActionEvent event) throws IOException {
        computerScore = 0;
        playerScore = 0;
        Parent optionPageParent = FXMLLoader.load(getClass().getResource("/com/example/tictactoegama/views/ListOfAvailablePlayers.fxml"));
        Scene optionPageScene = new Scene(optionPageParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(optionPageScene);
        window.show();
    }

    @FXML
    public void handleReplay(ActionEvent event) throws IOException {
        if (currentPlayer == globalPlayerSymbol) {
            playerScore += 1;
        } else {
            computerScore += 1;
        }

        Parent gamePageParent = FXMLLoader.load(getClass().getResource("/com/example/tictactoegama/views/gama-page.fxml"));
        Scene gamePageScene = new Scene(gamePageParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(gamePageScene);
        window.show();
    }

    private void processPlayerMove(Button clickedButton, int row, int col) throws IOException {
        clickedButton.setText(currentPlayer);
        updateButtonStyle(clickedButton, currentPlayer);
        int flag = playBoard.play(row, col, currentPlayer.charAt(0));
        if (flag == 1) {
            endGame(currentPlayer);
        }
        if (flag == 0) {
            endGame("draw");
        }
    }

    private void processComputerMove() {
        int flag = aiMoodOption.makeMove(playBoard, computerSymbol.charAt(0));
        if (flag == 1) {
            endGame(computerSymbol);
        } else if (flag == 0) {
            endGame("draw");
        } else if (flag == 10) {
            endGame(currentPlayer);
        }
        updateBoardUI();
    }


    private void updateButtonStyle(Button button, String symbol) {
        if ("X".equals(symbol)) {
            button.setStyle(
                    "-fx-background-color: rgba(70, 163, 255, 0.2); " +
                            "-fx-text-fill: #46A3FF; " +
                            "-fx-font-size: 36px; " +
                            "-fx-border-color: #E3E3E3;"
            );
        } else if ("O".equals(symbol)) {
            button.setStyle(
                    "-fx-background-color: rgba(255, 130, 126, 0.2); " +
                            "-fx-text-fill: #FF827E; " +
                            "-fx-font-size: 36px; " +
                            "-fx-border-color: #E3E3E3;"
            );
        }
    }

    private void updateBoardUI() {
        char[][] board = playBoard.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                char symbol = board[i][j];
                if (symbol != '_') {
                    Button button = (Button) getNodeByRowColumnIndex(i, j);
                    if (button != null) {
                        button.setText(String.valueOf(symbol));
                        updateButtonStyle(button, String.valueOf(symbol));
                    }
                }
            }
        }
    }


    private void endGame(String winner) {

        String videoPath = "";
        if (winner.equals(currentPlayer)) {
            videoPath = "src/main/resources/com/example/tictactoegama/videos/video_win.mp4";

        } else if (winner.equals(computerSymbol)) {
            videoPath = "src/main/resources/com/example/tictactoegama/videos/video_fail.mp4";
        } else if (winner.equals("draw")) {
            videoPath =
                    "src/main/resources/com/example/tictactoegama/videos/video_draw1.mp4";
        } else {
            return;
        }
        disableButtons();
        gameEnded = true;
        drawWinnerLine(playBoard.getWinningTiles());
        winnerLine.setVisible(true);
        replayBtn.setVisible(true);
        final String finalVideoPath = videoPath;
        PauseTransition delay = new PauseTransition(Duration.seconds(1));
        delay.setOnFinished(event -> {

            Stage stage = new Stage();
            videoViewHandler.showVideoView(stage, finalVideoPath);
            stage.show();
        });
        delay.play();
    }


    private void disableButtons() {
        for (Node node : gameGrid.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
    }

    private void enableButtons() {
        for (Node node : gameGrid.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        }
    }


    private void drawWinnerLine(int[][] winningTiles) {
        if (winningTiles == null || winningTiles.length == 0) return;

        // Get the starting and ending nodes of the winning line
        Node startNode = getNodeByRowColumnIndex(winningTiles[0][0], winningTiles[0][1]);
        Node endNode = getNodeByRowColumnIndex(winningTiles[2][0], winningTiles[2][1]);

        if (startNode != null && endNode != null) {
            // Get the position of the GridPane
            Bounds gridPaneBounds = gameGrid.localToScene(gameGrid.getBoundsInLocal());

            // Calculate the center points of the start and end nodes relative to the GridPane
            double startX = startNode.getBoundsInParent().getMinX() + startNode.getBoundsInParent().getWidth() / 2;
            double startY = startNode.getBoundsInParent().getMinY() + startNode.getBoundsInParent().getHeight() / 2;
            double endX = endNode.getBoundsInParent().getMinX() + endNode.getBoundsInParent().getWidth() / 2;
            double endY = endNode.getBoundsInParent().getMinY() + endNode.getBoundsInParent().getHeight() / 2;

            // Adjust the winnerLine coordinates relative to the GridPane
            winnerLine.setStartX(gridPaneBounds.getMinX() + startX);
            winnerLine.setStartY(gridPaneBounds.getMinY() + startY);
            winnerLine.setEndX(gridPaneBounds.getMinX() + endX);
            winnerLine.setEndY(gridPaneBounds.getMinY() + endY);
            winnerLine.setVisible(true);
        }
    }


    private Node getNodeByRowColumnIndex(final int row, final int column) {
        for (Node node : gameGrid.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }
}