package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import minesweeper.MineSweeper;
import java.net.URL;

import java.io.File;

public class MineSweeperApplication extends Application {

    private MineSweeper mineSweeper;
    private Button[][] buttons;
    private Button newButton;
    private Button exitButton;
    private Label flagsLabel;
    private Label cellsLabel;
    private Label outcomeLabel;
    private ImageView WIN, LOSE, ON_GOING, BOMB;
    private MenuBar menuBar;
    private Menu menuDifficulty, menuOptions;
    private MenuItem easy, medium, hard;
    private HBox hbox;
    private BorderPane borderPane;
    private Scene scene;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        createImageViews();
        primaryStage.setTitle("MineSweeper");
        this.primaryStage = primaryStage;
        mineSweeper = new MineSweeper();
        initialize();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createImageViews() {

        URL url = getClass().getResource("resources/win.png");
        Image imageWin = new Image(url.toString());
        WIN = new ImageView(imageWin);
        url = getClass().getResource("resources/lose.png");
        Image imageLose = new Image(url.toString());
        LOSE = new ImageView(imageLose);
        url = getClass().getResource("resources/on_going.png");
        Image imageOnGoing = new Image(url.toString());
        ON_GOING = new ImageView(imageOnGoing);
        url = getClass().getResource("resources/bomb.png");
        Image imageBomb = new Image(url.toString());
        BOMB = new ImageView(imageBomb);
    }

    private void initialize() {
        scene = new Scene(getBoard());
        createMenus();
        ((BorderPane)scene.getRoot()).setTop(menuBar);
        primaryStage.setScene(scene);
    }

    private void createMenus() {

        menuBar = new MenuBar();
        menuOptions = new Menu("Options");
        menuDifficulty = new Menu("Difficulty");
        easy = new MenuItem("Easy");
        medium = new MenuItem("Medium");
        hard = new MenuItem("Hard");
        easy.setOnAction(new EventHandler <ActionEvent> () {
            @Override
            public void handle(ActionEvent e) {
                mineSweeper.setRows(10);
                mineSweeper.setColumns(10);
                mineSweeper.setMines(10);
                mineSweeper.resetGame();
                initialize();
                repaintAll();
            }
        });
        medium.setOnAction(new EventHandler <ActionEvent> () {
            @Override
            public void handle(ActionEvent e) {
                mineSweeper.setRows(16);
                mineSweeper.setColumns(16);
                mineSweeper.setMines(40);
                mineSweeper.resetGame();
                initialize();
                repaintAll();
            }
        });

        hard.setOnAction(new EventHandler <ActionEvent> () {
            @Override
            public void handle(ActionEvent e) {
                mineSweeper.setRows(30);
                mineSweeper.setColumns(16);
                mineSweeper.setMines(99);
                mineSweeper.resetGame();
                initialize();
                repaintAll();
            }
        });
        menuDifficulty.getItems().addAll(easy, medium, hard);
        menuOptions.getItems().addAll(menuDifficulty);
        menuBar.getMenus().addAll(menuDifficulty);
    }

    private BorderPane getBoard() {

        BorderPane borderPane = new BorderPane();
        BorderPane innerBorderPane = new BorderPane();
        innerBorderPane.setTop(createControlPane());
        innerBorderPane.setCenter(createGridPane());
        borderPane.setCenter(innerBorderPane);
        return borderPane;
    }

    private BorderPane createControlPane() {

        hbox = new HBox(7);
        hbox.setPadding(new Insets(15, 5, 5, 15));
        newButton = new Button("New game");
        newButton.setOnAction(new EventHandler <ActionEvent> () {

            @Override
            public void handle(ActionEvent event) {

                mineSweeper.resetGame();
                repaintAll();
            }
        });
        exitButton = new Button("Exit");
        exitButton.setOnAction(new EventHandler <ActionEvent> () {

            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });

        flagsLabel = new Label("0/" + mineSweeper.getMines() + " flags");
        cellsLabel = new Label("0/" + mineSweeper.getRows() * mineSweeper.getColumns() + " cells");
        outcomeLabel = new Label("");
        hbox.getChildren().addAll(newButton, flagsLabel, cellsLabel,
                                  outcomeLabel, exitButton);
        newButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        exitButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        outcomeLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        cellsLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        flagsLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        hbox.setHgrow(newButton, Priority.ALWAYS);
        hbox.setHgrow(flagsLabel, Priority.ALWAYS);
        hbox.setHgrow(cellsLabel, Priority.ALWAYS);
        hbox.setHgrow(outcomeLabel, Priority.ALWAYS);
        hbox.setHgrow(exitButton, Priority.ALWAYS);
        borderPane = new BorderPane();
        borderPane.setBottom(hbox);
        return borderPane;
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15, 15, 15, 15));
        grid.setHgap(5);
        grid.setVgap(5);
        buttons = new Button[mineSweeper.getRows()][mineSweeper.getColumns()];
        for (int i = 0; i < mineSweeper.getRows(); ++i) {
            for (int j = 0; j < mineSweeper.getColumns(); ++j) {

                buttons[i][j] = new ButtonCell(i, j);
                buttons[i][j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                buttons[i][j].setOnMousePressed(new EventHandler <MouseEvent> () {

                    @Override
                    public void handle(MouseEvent event) {

                        if (!mineSweeper.gameOver()) {
                            ButtonCell cell = (ButtonCell)event.getSource();
                            if (event.getButton() == MouseButton.PRIMARY ) {
                                mineSweeper.enterCell(cell.getRow(), cell.getColumn());
                            }
                            else if (event.getButton() == MouseButton.SECONDARY) {
                                // flag cell
                                mineSweeper.toggleFlag(cell.getRow(), cell.getColumn());
                            }
                        }
                        repaintAll();
                    }
                });
                grid.add(buttons[i][j], i, j);
            }
        }
        addConstraints(grid);
        return grid;
    }

    private void addConstraints(GridPane grid) {

        for (int i = 0; i < buttons.length; ++i) {
            RowConstraints row = new RowConstraints();
            row.setVgrow(Priority.ALWAYS);
            grid.getRowConstraints().add(row);
            ColumnConstraints column = new ColumnConstraints();
            column.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(column);
        }
    }

    private void repaintAll() {

        repaintBoard();
        flagsLabel.setText(mineSweeper.getFlagged() + "/" +
                           mineSweeper.getMines() + " flags");
        cellsLabel.setText(mineSweeper.getCellsExplored() + "/" +
                           mineSweeper.getTotalCells() + " cells");
        if (mineSweeper.gameOver()) {
            outcomeLabel.setGraphic(mineSweeper.win() ? WIN : LOSE);
        }
        else {
            outcomeLabel.setGraphic(ON_GOING);
        }
    }

    private void repaintBoard() {
        for (int row = 0; row < mineSweeper.getRows(); ++row) {
            for (int col = 0 ; col < mineSweeper.getColumns(); ++col) {
                String text = mineSweeper.getCellText(row, col);
                if(text.equals("X") && buttons[row][col].getGraphic() == null) {
                    
                    for (int row1 = 0; row1 < mineSweeper.getRows(); ++row1) {
                        for (int col1 = 0 ; col1 < mineSweeper.getColumns(); ++col1) {
                            if(mineSweeper.getCellText(row1, col1).equals("X")) {
                                buttons[row1][col1].setGraphic(BOMB);
                                buttons[row1][col1].setText(null);
                                BOMB.setFitHeight(10);
                                BOMB.setFitWidth(10);
                            }

                        }
                    }
                }
                else if(buttons[row][col].getGraphic() == null)
                    buttons[row][col].setText(text);
            }
        }
    }
}
