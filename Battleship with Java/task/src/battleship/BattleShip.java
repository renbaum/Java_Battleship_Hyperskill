package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class BattleShip{
    private int size;
    private BoardPosition startPosition;
    private BoardPosition endPosition;
    List<BoardPosition> positions;

    public BattleShip(BoardPosition startPosition, BoardPosition endPosition){
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        checkPositions();
        setAllPositions();
        size = positions.size();
    }

    private boolean checkPositions(){
        if(((startPosition.getRow() != endPosition.getRow()) && (startPosition.getCol() != endPosition.getCol()))){
            throw new IllegalArgumentException("Position");
        }
        return true;
    }

    private void setAllPositions(){
        positions = new ArrayList<>();
        Set<BoardPosition> allPositions = startPosition.getAllInbetween(startPosition, endPosition);
        positions.addAll(allPositions);
    }

    public int getSize(){
        return positions.size();
    }

    public Object partsToString() {
        StringBuilder sb = new StringBuilder();
        for (BoardPosition position : positions) {
            sb.append(position.toString()).append(" ");
        }
        return sb.toString().trim();
    }

    public List<BoardPosition> getPositions(){
        return positions;
    }

    public static BattleShip Factory(int length, String input){
        String[] coords = input.split(" ");
        BoardPosition startPosition = new BoardPosition(coords[0]);
        BoardPosition endPosition = new BoardPosition(coords[1]);
        BattleShip ship = new BattleShip(startPosition, endPosition);
        if (ship.getSize() != length){
            throw new IllegalArgumentException("Length");
        }
        return ship;
    }

    public boolean shoot(BoardPosition shotPosition) {
        for(BoardPosition position : positions){
            if (position.equals(shotPosition)) {
                position.setShot();
                return true;
            }
        }
        return false;
    }

    public boolean isShipDestroyed() {
        for(BoardPosition position : positions){
            if(!position.isShot()) return false;
        }
        return true;
    }
}
