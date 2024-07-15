package battleship;


import java.util.Scanner;

public class Main {

    BattleShipBoard[] player;
    int currentPlayer = 0;
    int opponentPlayer = 1;
    Scanner scanner = new Scanner(System.in);

    public Main() {
        player = new BattleShipBoard[2];
        player[0] = new BattleShipBoard();
        player[1] = new BattleShipBoard();
    }

    private void switchPlayer(){
        System.out.println();
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        opponentPlayer = currentPlayer;
        currentPlayer = (currentPlayer + 1) % 2;
    }

    private void startGame() {
        GAME_STATUS result = null;
        while(result != GAME_STATUS.WIN) {
            switch (player[currentPlayer].status) {
                case INITIALIZE -> {
                    System.out.printf("Player %d, place your ships on the game field\n", currentPlayer + 1);
                    System.out.println();
                    result = player[currentPlayer].doAction();
                }
                case BATTLE -> {
                    player[opponentPlayer].printBoard(true);
                    System.out.println("---------------------");
                    player[currentPlayer].printBoard(false);
                    System.out.println();
                    System.out.printf("Player %d, it's your turn:", currentPlayer + 1);
                    System.out.println();
                    result = player[opponentPlayer].doAction();
                }
            }
            if(result != GAME_STATUS.WIN) {
                switchPlayer();
            }
        }
    }

    public static void main(String[] args) {

        Main game = new Main();
        game.startGame();
    }
}
