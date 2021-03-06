/*
 GameFrame är en instans av en javafx.scene.layout.BorderPane och finns uppe så 
 länge programmet körs. Den innehåller, bland annat, en instans av GameBoard samt 
 knapparna ”Nytt parti” och ”Avsluta”.
 */
package othello.interfaces;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import othello.GameGridProperty;
import participants.GetMove;

/**
 * This class is our main window.
 * @author S153298
 */
public class GameFrame{

    private final BooleanProperty gameIsOn = new SimpleBooleanProperty(true);
    private final GameBoard board = new GameBoard();
    private final BorderPane pane = new BorderPane();
    private final VBox vBox; 
    private final Button btEnd = new Button("End Game");
    private final Button btNew = new Button("New Game");
    private final Stage stage; 
    
    //private DrawnDialog dd;
    //private WinnerDialog wd;
    
    
    

    public GameFrame(Stage primaryStage, GameGridProperty gridProperty, GetMove getMove) {        
        Scene scene = new Scene(pane, 600, 500);
        primaryStage.setTitle("Othello");
        primaryStage.setScene(scene);
        this.stage = primaryStage;
        board.setGetMove(getMove);
        vBox = getVBox();
        updateView(gridProperty.getValue());
        bindViewToModel(gridProperty);
        //dd = new DrawnDialog(primaryStage);
        
    }
    
    public void bindGameOnProperty(BooleanProperty otherProperty){
        otherProperty.bindBidirectional(gameIsOn);
    }
    
    public void bindViewToModel(GameGridProperty gridProperty){
        gridProperty.addListener((observable, oldValue, newValue) -> Platform.runLater(() -> updateView(newValue)));
    }
    
    private void updateView(int[][] gameGrid){
        //System.out.println("\n\tUPDATING VIEW\t\n");
        pane.setCenter(board.createBoard(gameGrid)); 
        pane.setLeft(vBox);
    }
    
    private VBox getVBox() {
        VBox vBox = new VBox(15);
        vBox.setPadding(new Insets(15, 15, 15, 15));
        vBox.setStyle("-fx-background-color: darkgreen");
        vBox.getChildren().add(btNew);
        vBox.getChildren().add(btEnd);
        btNew.setOnAction(e -> {
            gameIsOn.set(false);
            SetUpGameDialog setup = new SetUpGameDialog(this.stage);
            setup.setUpGameDialogFrame();
        });
        btEnd.setOnAction(e -> Platform.exit());
        
        return vBox;
    }
    
    
    
    public void gameOver(){
        new DrawnDialog();
    }
    public void gameOver(String winner){
        new WinnerDialog(winner);
    }
    
    

 

    //trycker man på NewGame skall spelplanen rensas och starta på nytt.
/*
    class NewGamehandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {
            

        }
    }

    //Trycker man på EndGame skall spelet Avsluta.

    class EndGamehandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent e) {

        }
    }*/
}
