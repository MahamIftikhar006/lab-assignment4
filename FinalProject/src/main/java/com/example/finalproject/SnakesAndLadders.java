package com.example.finalproject;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;



public class SnakesAndLadders extends Application {
    private MediaPlayer backgroundMusic;
    private MediaPlayer winSound;

    public int random;
    public Label randomResult;

    public int Circle1Position = 1;
    public int Circle2Position = 1;

    public int [][]circlePosition= new int[10][10];
    public HashMap<Integer, Integer> ladderAndSnake = new HashMap<>();

    public static final int TileSize = 60;
    public static final int width = 10;
    public static final int height = 10;

    public Circle player1;
    public Circle player2;

    public int player1Pos = 1;
    public int player2Pos = 1;

    public int player1X = 30;
    public int player1Y = 570;

    public int player2X = 30;
    public int player2Y = 570;

    public boolean player1Turn = true;
    public boolean player2Turn = true;

    public boolean gameStarted = false;
    public Button button3;

    public String player1Name;
    public String player2Name;


    private Scene loginScene(Stage primaryStage) {

        Pane loginPane = new Pane();
        loginPane.setPrefSize(400,300);
        loginPane.setStyle("-fx-background-color: black;");


        Label player1 = new Label("Player 1 Name :");
        player1.setTranslateX(50);
        player1.setTranslateY(50);
        player1.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField input1 = new TextField();
        input1.setPromptText("Player 1 Name");
        input1.setTranslateX(150);
        input1.setTranslateY(50);
        input1.setStyle("-fx-background-radius: 10; -fx-padding: 5;");

        Label player2 = new Label("Player 2 Name :");
        player2.setTranslateX(50);
        player2.setTranslateY(100);
        player2.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");


        TextField input2 = new TextField();
        input2.setPromptText("Player 2 Name");
        input2.setTranslateX(150);
        input2.setTranslateY(100);
        input2.setStyle("-fx-background-radius: 10; -fx-padding: 5;");


        Button startButton = new Button("Start Game");
        startButton.setTranslateX(150);
        startButton.setTranslateY(150);
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        startButton.setOnMouseEntered(e -> startButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));
        startButton.setOnMouseExited(e -> startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));



        startButton.setOnAction(e -> {
            player1Name = input1.getText().trim();
            player2Name = input2.getText().trim();

            if(player1Name.isEmpty() && player2Name.isEmpty()) {
                Label invalid = new Label("Both players must add names.");
                invalid.setTranslateX(150);
                invalid.setTranslateY(200);
                invalid.setTextFill(Color.RED);
                loginPane.getChildren().add(invalid);

            }
            else{
                savePlayers(player1Name, player2Name);
                primaryStage.setScene(new Scene(BuildScene(), 600, 700));
                primaryStage.setTitle("Snakes and Ladders");
            }

        });
        loginPane.getChildren().addAll(input1, player1, input2, player2, startButton);
        return new Scene(loginPane, 400, 300);

    }

    private void savePlayers(String player1Name, String player2Name){

        try(FileWriter fileWriter = new FileWriter("PlayerNames.txt",true)) {
            fileWriter.write("Player 1 : " + player1Name + "\n");
            fileWriter.write("Player 2 : " + player2Name + "\n");
            fileWriter.write("------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void resetGame(){
        player1Pos = 1;
        player2Pos = 1;
        player1X = 30;
        player1Y = 570;
        player2X = 30;
        player2Y = 570;
        player1.setTranslateX(player1X);
        player1.setTranslateY(player1Y);
        player2.setTranslateX(player2X);
        player2.setTranslateY(player2Y);
        gameStarted = false;
        button3.setText("Start Game");
    }

    Group tileGroup = new Group();

    private Parent BuildScene(){
        Pane pane = new Pane();
        pane.setPrefSize(width*TileSize, (height*TileSize)+60);
        pane.setStyle("-fx-background-color: linear-gradient(to right, #0099CC, #00CC66,Red,Orange);");
        pane.getChildren().addAll(tileGroup);
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Tile tile = new Tile(TileSize, TileSize);
                tile.setTranslateX(j * TileSize);
                tile.setTranslateY(i * TileSize);
                tileGroup.getChildren().add(tile);
                circlePosition[i][j] = j*(TileSize-30);
            }
        }


        for(int i=0;i<height;i++){
            for(int j=0; j<width; j++){
                System.out.print(circlePosition[i][j] + " ");
            }
            System.out.println();
        }

        player1 = new Circle(20);
        player1.setFill(Color.YELLOW);
        player1.setId("player1");
        player1.setTranslateX(player1X);
        player1.setTranslateY(player1Y);
        player1.setEffect(new DropShadow(20, 4, 4, Color.BLACK));

        player2 = new Circle(20);
        player2.setFill(Color.DARKGREEN);
        player2.setId("player2");
        player2.setTranslateX(player2X);
        player2.setTranslateY(player2Y);
        player2.setEffect(new DropShadow(20, 4, 4, Color.BLACK));

        Button button1 = new Button(player1Name);
        button1.setOnAction(e -> {
            if (gameStarted && player1Turn) {
                getDiceValue();
                randomResult.setText(String.valueOf(random));
                movePlayer1();
                translatePlayer(player1X, player1Y, player1);
                player1Turn = false;
                player2Turn = true;
            }
        });

        button1.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        button1.setOnMouseEntered(e -> button1.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));
        button1.setOnMouseExited(e -> button1.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));



        Button button2 = new Button(player2Name);
        button2.setOnAction(e -> {
            if (gameStarted && player2Turn) {
                getDiceValue();
                randomResult.setText(String.valueOf(random));
                movePlayer2();
                translatePlayer(player2X, player2Y, player2);
                player2Turn = false;
                player1Turn = true;
            }
        });
        button2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        button2.setOnMouseEntered(e -> button2.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));
        button2.setOnMouseExited(e -> button2.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));

        button3 = new Button("Start Game");
        button3.setOnAction(e -> {
            button3.setText("Game Started");
            gameStarted = true;
            player1X = 30;
            player1Y = 570;
            player2X = 30;
            player2Y = 570;
            player1.setTranslateX(player1X);
            player1.setTranslateY(player1Y);
            player2.setTranslateX(player2X);
            player2.setTranslateY(player2Y);

            if (backgroundMusic == null) {
                String musicFile = getClass().getResource("/Drum Jam.wav").toString();
                Media media = new Media(musicFile);
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
                backgroundMusic.play();
            }
        });
        button3.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        button3.setOnMouseEntered(e -> button3.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));
        button3.setOnMouseExited(e -> button3.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));

        Button resetButton = new Button("Reset Game");
        resetButton.setOnAction(e -> resetGame());

        resetButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;");
        resetButton.setOnMouseEntered(e -> resetButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));
        resetButton.setOnMouseExited(e -> resetButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 20;"));



        HBox buttonBox = new HBox(20);
        buttonBox.setTranslateX(60);
        buttonBox.setTranslateY(620);
        buttonBox.getChildren().addAll(button1, button3, resetButton, button2);
        buttonBox.setSpacing(60);
        Image img = new Image("Snakes.jpg");
        ImageView imgView = new ImageView(img);
        imgView.setFitWidth(600);
        imgView.setFitHeight(600);

        randomResult = new Label("0");
        randomResult.setTranslateX(300);
        randomResult.setTranslateY(650);
randomResult.setStyle("-fx-font-weight: Bold ");
        randomResult.setStyle("-fx-text-fill: white  ");
        tileGroup.getChildren().addAll(imgView, player1, player2,buttonBox,randomResult);
        return pane;
    }

    private void getDiceValue(){
        random = (int)(Math.random()*6+1);
    }

    private void translatePlayer(int x, int y,Circle circle){
        TranslateTransition animate = new TranslateTransition(Duration.millis(1000),circle);
        animate.setToX(x);
        animate.setToY(y);
        animate.setAutoReverse(false);
        animate.setCycleCount(1);
        animate.play();

    }

    private void movePlayer1() {
        player1Pos += random;

        if (player1Pos > 100) {
            player1Pos -= random;
            return;
        }


        if (ladderAndSnake.containsKey(player1Pos)) {
            player1Pos = ladderAndSnake.get(player1Pos);
        }

        updatePlayerCoordinates(player1Pos, true);

        if (player1Pos == 100) {
            randomResult.setText(player1Name + " WON!");
            button3.setText("Start Again");
            gameStarted = false;

            if (backgroundMusic != null) {
                backgroundMusic.stop();


            }
        }
    }

    private void movePlayer2() {
        player2Pos += random;

        if (player2Pos > 100) {
            player2Pos -= random;
            return;
        }


        if (ladderAndSnake.containsKey(player2Pos)) {
            player2Pos = ladderAndSnake.get(player2Pos);
        }


        updatePlayerCoordinates(player2Pos, false);


        if (player2Pos == 100) {
            randomResult.setText(player2Name + " WON!");
            button3.setText("Start Again");
            gameStarted = false;
            if (backgroundMusic != null) {
                backgroundMusic.stop();


            }
        }
    }


    private void updatePlayerCoordinates(int boardPosition, boolean isPlayer1) {

        int row = (boardPosition - 1) / 10;
        int col = (boardPosition - 1) % 10;


        if (row % 2 != 0) {
            col = 9 - col;
        }

        int newX = col * TileSize + TileSize / 2;
        int newY = 600 - (row * TileSize) - TileSize / 2;

        if (isPlayer1) {
            player1X = newX;
            player1Y = newY;
            translatePlayer(player1X, player1Y, player1);
        } else {
            player2X = newX;
            player2Y = newY;
            translatePlayer(player2X, player2Y, player2);
        }
    }

    private void setLaddersAndSnakes() {
        //ladders
        ladderAndSnake.put(5,58);
        ladderAndSnake.put(14,49);
        ladderAndSnake.put(42,60);
        ladderAndSnake.put(53,72);
        ladderAndSnake.put(64,83);
        ladderAndSnake.put(75,94);
        //Snakes
        ladderAndSnake.put(38,20);
        ladderAndSnake.put(45,7);
        ladderAndSnake.put(51,10);
        ladderAndSnake.put(76,54);
        ladderAndSnake.put(91,73);
        ladderAndSnake.put(97,61);
    }


    @Override
    public void start(Stage primaryStage) {
        setLaddersAndSnakes();
        Scene login = loginScene(primaryStage);
        primaryStage.setScene(login);
        primaryStage.setTitle("Login---Snakes and Ladders");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
}
}