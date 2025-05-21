package game;

import java.util.ArrayList;
import java.util.Collection;

public class GameImpl implements Game {
    private final Grid grid;
    private PieceColour currentPlayer;
    
    public GameImpl(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Game size must be positive");
        }
        this.grid = new GridImpl(size);
        // White always starts first
        this.currentPlayer = PieceColour.WHITE;
    }
    
    private GameImpl(Grid grid, PieceColour currentPlayer) {
        this.grid = grid;
        this.currentPlayer = currentPlayer;
    }
    
    @Override
    public boolean isOver() {
        // Game is over if there's a winner or no more moves
        return winner() != PieceColour.NONE || getMoves().isEmpty();
    }
    
    @Override
    public PieceColour winner() {
        // Check if WHITE has won (left to right)
        if (PathFinder.leftToRight(grid, PieceColour.WHITE)) {
            return PieceColour.WHITE;
        }
        
        // Check if BLACK has won (top to bottom)
        if (PathFinder.topToBottom(grid, PieceColour.BLACK)) {
            return PieceColour.BLACK;
        }
        
        // No winner yet
        return PieceColour.NONE;
    }
    
    @Override
    public PieceColour currentPlayer() {
        return currentPlayer;
    }
    
    @Override
    public Collection<Move> getMoves() {
        Collection<Move> validMoves = new ArrayList<>();
        
        // If the game is over, return an empty list
        if (winner() != PieceColour.NONE) {
            return validMoves;
        }
        
        // Add all empty positions as valid moves
        for (int row = 0; row < grid.getSize(); row++) {
            for (int col = 0; col < grid.getSize(); col++) {
                if (grid.getPiece(row, col) == PieceColour.NONE) {
                    validMoves.add(new MoveImpl(row, col));
                }
            }
        }
        
        return validMoves;
    }
    
    @Override
    public void makeMove(Move move) {
        int row = move.getRow();
        int col = move.getCol();
        
        // Validate the move
        if (row < 0 || row >= grid.getSize() || col < 0 || col >= grid.getSize()) {
            throw new IllegalArgumentException("Position (" + row + "," + col + ") is out of bounds");
        }
        
        if (grid.getPiece(row, col) != PieceColour.NONE) {
            throw new IllegalArgumentException("Position (" + row + "," + col + ") is already occupied");
        }
        
        // Make the move
        grid.setPiece(row, col, currentPlayer);
        
        // Switch players
        currentPlayer = (currentPlayer == PieceColour.WHITE) ? PieceColour.BLACK : PieceColour.WHITE;
    }
    
    @Override
    public Grid getGrid() {
        return grid.copy();
    }
    
    @Override
    public Game copy() {
        return new GameImpl(grid.copy(), currentPlayer);
    }
}
