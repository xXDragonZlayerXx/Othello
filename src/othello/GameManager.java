/*
GameManager är den som administrerar pågående parti. GameManagerns uppgift är enkel.
Den skall gång på gång, omväxlande, be de två spelarna att ange sitt nästa drag. 
Den skall också tala om för spelarna om de vunnit eller gjort ett felaktigt drag.
Eftersom GameManagern inte skall veta vilken typ av spelare den har att göra 
med skall dynamisk bindning användas vid anrop av spelarnas operationer.
 */
package othello;

import java.util.Scanner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import participants.Player;

/**
 *
 * @author S153298
 */
public class GameManager {
    private GameGrid board;
    private Scanner in;
    private Player[] playerList;
    
    private ObjectProperty<Move> playerMadeMove = new SimpleObjectProperty<>(); //Property som vi skickar med varje gång en spelare ska göra ett move (dvs vi anropar getmove) i getmove så har vi en set, vilket aktiverar vår lyssnare i run()
    
    private int currentPlayer = 0;
    
    /*
            TODO
            - Run new turn
                - Select next player
                - Prompt new move until one is legal
                - Make move on board
            - Run match that makes new turns until someone wins or loses
                - New turn
                - Continue making new turn when a move is succesfully made in the GameGrid (board).
    
            - Run method gameOver when someone lost or won or draw etc.
            
    */

    public GameManager(Player white, Player black){
        board = new GameGrid();
        in = new Scanner(System.in);
        playerList = new Player[2];
        playerList[0] = white;
        playerList[1] = black;
    }
    
    public void run(){
        
        this.playerMadeMove.addListener(e -> {                                  //varje gång getMove anropas så aktiveras set(se player-klassen), vilket lyssnaren ser.
            Move move = this.playerMadeMove.get();                              //Inuti får vi ett move-objekt genom get (som fick det från set)
            if(!board.isLegalMove(move)){                                    
                System.out.println("Move was not legal! Try again.");
                playerList[currentPlayer].getMove(board.getLegalMoves(playerList[currentPlayer]), playerMadeMove);       //ifall det inte gick så anropar vi getMove, vilket anropar set, som tar oss tillbaka till lyssnaren
            }
            else{
                board.addToGrid(move);
                board.printBoard();
                advanceTurn();
            }
        });
        
        board.printBoard();
        playerList[currentPlayer].getMove(board.getLegalMoves(playerList[currentPlayer]), playerMadeMove);          //Sätts längst ner efter lyssnaren, så att lyssnaren ska ha tid att fästa sig. Annars ifall det här kommer först så kan getmove hinna exekvera klart på sin egen tråd innan lyssnaren har satt sig, och då har den inga instruktioner och set() triggar inte något.
    }
    
    
    private void advanceTurn(){
        if(!board.checkForLegalMoves()){
            gameOver();
            System.exit(0);
        }   
        nextTurn();
        if(!(board.checkForLegalMoves(playerList[currentPlayer])))
           nextTurn();
        playerList[currentPlayer].getMove(board.getLegalMoves(playerList[currentPlayer]), playerMadeMove);
    }
    
    
    private void nextTurn(){
        currentPlayer = (currentPlayer + 1) % 2;
    }
    
    private void gameOver(){
        System.out.println("Player 1: " + board.getScore(1));
        System.out.println("Player 2: " + board.getScore(2));
        System.out.println("Winner: " + board.getWinner());
    }
    
}