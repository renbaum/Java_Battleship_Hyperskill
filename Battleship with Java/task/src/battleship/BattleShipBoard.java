package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static battleship.CELL_STATE.*;

enum GAME_STATUS{
    INITIALIZE,
    BATTLE,
    WIN
}

enum CELL_STATE{
    WATER("~", 0),
    SHIP("O", 1),
    HIT("X", 2),
    MISSED("M", 3),
    DESTROYED("H", 5),
    WIN("W", 9);
    
    private int value;
    private String symbol;
    
    CELL_STATE(String symbol, int value){
        this.symbol = symbol;
        this.value = value;
    }
    
    int getValue(){
        return this.value;
    }
    
    String getSymbol(){
        return this.symbol;
    }
    
}

public class BattleShipBoard{
    CELL_STATE[][] sea = new CELL_STATE[10][10];
    Scanner sc = new Scanner(System.in);
    List<BattleShip> ships = new ArrayList<BattleShip>();
    GAME_STATUS status = GAME_STATUS.INITIALIZE;

    public BattleShipBoard(){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                sea[i][j] = WATER;
            }
        }
    }

    void printBoard(boolean fogOfWar){
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            String line = Character.toString((char)('A' + i)) + " ";
            for (int j = 0; j < 10; j++) {
                switch(sea[i][j]){
                    case WATER:
                        line += CELL_STATE.WATER.getSymbol() + " ";
                        break;
                    case SHIP:
                        if(fogOfWar) {
                            line += CELL_STATE.WATER.getSymbol() + " ";
                        }else{
                            line += CELL_STATE.SHIP.getSymbol() + " ";
                        }
                        break;
                    case HIT:
                        line += CELL_STATE.HIT.getSymbol() + " ";
                        break;
                    case MISSED:
                        line += CELL_STATE.MISSED.getSymbol() + " ";
                        break;
                    default:
                        line += "E" + " ";
                }
            }
            System.out.println(line.trim());
        }
    }

    private List<BoardPosition> getAllOccupiedCells(){
        List<BoardPosition> pos = new ArrayList<>();

        for(BattleShip ship : ships){
            pos.addAll(ship.getPositions());
        }
        return pos;
    }

    private boolean addShip(BattleShip ship){
        List<BoardPosition> positions = ship.getPositions();
        List<BoardPosition> occupiedCells = getAllOccupiedCells();

        for (BoardPosition pos : positions) {
            for(BoardPosition occupiedCell : occupiedCells){
                if(pos.near(occupiedCell)){
                    throw new IllegalArgumentException("Near a ship");
                }
            }
        }
        ships.add(ship);
        for(BoardPosition pos : positions){
            markPosition(pos, CELL_STATE.SHIP);
        }
        return true;
    }

    private void markPosition(BoardPosition pos, CELL_STATE c){
        sea[pos.getRow()][pos.getCol()] = c;
    }

    public void createShips(){
        int length;
        BattleShip ship;
        String shipType = null;

        printBoard(false);

        for(int i = 0; i < 5; i++){
            length = switch (i) {
                case 0 -> {
                    shipType = "Aircraft Carrier";
                    yield 5;
                }
                case 1 -> {
                    shipType = "Battleship";
                    yield 4;
                }
                case 2 -> {
                    shipType = "Submarine";
                    yield 3;
                }
                case 3 -> {
                    shipType = "Cruiser";
                    yield 3;
                }
                case 4 -> {
                    shipType = "Destroyer";
                    yield 2;
                }
                default -> {
                    yield 0;
                }
            };
            System.out.println();
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", shipType, length);
            boolean bOk = false;
            while (!bOk) {
                try {
                    System.out.println();
                    String coordinates = sc.nextLine();
                    ship = BattleShip.Factory(length, coordinates);
                }catch(Exception e){
                    System.out.println();
                    switch(e.getMessage()){
                        case "Length":
                            System.out.printf("Error! Wrong length of the %s! Try again:\n", shipType);
                            break;
                        case "Position":
                            System.out.println("Error! Wrong ship location! Try again:\n");
                            break;
                    }
                    continue;
                }
                try {
                    bOk = addShip(ship);
                    System.out.println();
                    printBoard(false);
                }catch(Exception e){
                    System.out.println();
                    System.out.println("Error! You placed it too close to another one. Try again:");
                }
            }
        }
        status = GAME_STATUS.BATTLE;
    }

    public CELL_STATE shoot() {
        boolean bOk = false;
        while(!bOk) {
            try {
                String input = sc.nextLine();

                BoardPosition shotPosition = new BoardPosition(input);
                for (BattleShip ship : ships) {
                    CELL_STATE retValue;
                    if (ship.shoot(shotPosition)) {
                        markPosition(shotPosition, CELL_STATE.HIT);
                        retValue=  HIT;
                        if(ship.isShipDestroyed()){
                            retValue =  DESTROYED;
                            if(allShipDestroyed()){
                                retValue =  WIN;
                            }
                        }
                        return retValue;
                    }
                }
                markPosition(shotPosition, CELL_STATE.MISSED);
            }catch(Exception e){
                System.out.println();
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                System.out.println();
                continue;
            }
            bOk = true;
        }
        return MISSED;
    }

    private boolean allShipDestroyed() {
        for(BattleShip ship : ships){
            if(!ship.isShipDestroyed()){
                return false;
            }
        }
        status = GAME_STATUS.WIN;
        return true;
    }

    public GAME_STATUS doAction(){
        GAME_STATUS result = null;

        switch(status){
            case INITIALIZE:
                createShips();
                result = GAME_STATUS.BATTLE;
                break;
            case BATTLE:
                CELL_STATE x = shoot();
                result = GAME_STATUS.BATTLE;

                switch (x) {
                    case HIT -> System.out.println("You hit a ship!");
                    case MISSED -> System.out.println("You missed!");
                    case DESTROYED -> System.out.println("You sank a ship!");
                    case WIN -> {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                        result = GAME_STATUS.WIN;
                }
            }
        }
        return result;
    }
}
