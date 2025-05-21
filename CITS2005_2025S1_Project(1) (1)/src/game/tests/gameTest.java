package game.tests;

import java.util.Collection;
import game.*;

public class GameTest extends Test {
    public static void main(String[] args) {
        // Test constructor with invalid size
        boolean caught = false;
        try {
            new GameImpl(0);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        expect(true, caught);

        // Test constructor with valid size
        caught = false;
        try {
            new GameImpl(5);
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        expect(false, caught);
        
        // Test initial game state
        Game game = new GameImpl(5);
        expect(false, game.isOver());
        expect(PieceColour.NONE, game.winner());
        expect(PieceColour.WHITE, game.currentPlayer());
        
        // Test getMoves on empty grid
        Collection<Move> moves = game.getMoves();
        expect(25, moves.size()); // 5x5 grid = 25 possible moves
        
        // Test getGrid returns a copy
        Grid grid = game.getGrid();
        expect(5, grid.getSize());
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                expect(PieceColour.NONE, grid.getPiece(r, c));
            }
        }
        
        // Test makeMove
        Move move = new MoveImpl(2, 2);
        game.makeMove(move);
        // Current player should now be BLACK
        expect(PieceColour.BLACK, game.currentPlayer());
        // Position (2,2) should be WHITE
        Grid updatedGrid = game.getGrid();
        expect(PieceColour.WHITE, updatedGrid.getPiece(2, 2));
        
        // Test makeMove with invalid positions
        caught = false;
        try {
            game.makeMove(new MoveImpl(2, 2)); // Already occupied
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        expect(true, caught);
        
        caught = false;
        try {
            game.makeMove(new MoveImpl(-1, 3)); // Out of bounds
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        expect(true, caught);
        
        caught = false;
        try {
            game.makeMove(new MoveImpl(5, 3)); // Out of bounds
        } catch (IllegalArgumentException e) {
            caught = true;
        }
        expect(true, caught);
        
        // Test getMoves after placing a piece
        moves = game.getMoves();
        expect(24, moves.size()); // 25 - 1 = 24 moves remaining
        
        // Test game copy
        Game gameCopy = game.copy();
        expect(PieceColour.BLACK, gameCopy.currentPlayer());
        Grid copyGrid = gameCopy.getGrid();
        expect(PieceColour.WHITE, copyGrid.getPiece(2, 2));
        // Make a move on the copy and verify it doesn't affect the original
        gameCopy.makeMove(new MoveImpl(0, 0));
        expect(PieceColour.BLACK, game.currentPlayer()); // Original game unchanged
        expect(PieceColour.WHITE, gameCopy.currentPlayer()); // Copy player changed
        
        // Test winner detection - WHITE wins (left to right)
        game = new GameImpl(5);
        // Create a winning path for WHITE from left to right
        game.makeMove(new MoveImpl(0, 0)); // WHITE
        game.makeMove(new MoveImpl(0, 1)); // BLACK
        game.makeMove(new MoveImpl(1, 1)); // WHITE
        game.makeMove(new MoveImpl(1, 0)); // BLACK
        game.makeMove(new MoveImpl(2, 2)); // WHITE
        game.makeMove(new MoveImpl(2, 0)); // BLACK
        game.makeMove(new MoveImpl(3, 3)); // WHITE
        game.makeMove(new MoveImpl(3, 0)); // BLACK
        game.makeMove(new MoveImpl(4, 4)); // WHITE
        // Check if WHITE wins
        expect(true, game.isOver());
        expect(PieceColour.WHITE, game.winner());
        
        // Test winner detection - BLACK wins (top to bottom)
        game = new GameImpl(5);
        // Create a winning path for BLACK from top to bottom
        game.makeMove(new MoveImpl(0, 1)); // WHITE
        game.makeMove(new MoveImpl(0, 0)); // BLACK
        game.makeMove(new MoveImpl(0, 2)); // WHITE
        game.makeMove(new MoveImpl(1, 0)); // BLACK
        game.makeMove(new MoveImpl(0, 3)); // WHITE
        game.makeMove(new MoveImpl(2, 0)); // BLACK
        game.makeMove(new MoveImpl(0, 4)); // WHITE
        game.makeMove(new MoveImpl(3, 0)); // BLACK
        game.makeMove(new MoveImpl(1, 4)); // WHITE
        game.makeMove(new MoveImpl(4, 0)); // BLACK
        // Check if BLACK wins
        expect(true, game.isOver());
        expect(PieceColour.BLACK, game.winner());
        
        // Test draw condition
        game = new GameImpl(2); // 2x2 grid for easier testing
        // Fill the grid without a winner
        game.makeMove(new MoveImpl(0, 0)); // WHITE
        game.makeMove(new MoveImpl(0, 1)); // BLACK
        game.makeMove(new MoveImpl(1, 1)); // WHITE
        game.makeMove(new MoveImpl(1, 0)); // BLACK
        // Check if it's a draw
        expect(true, game.isOver());
        expect(PieceColour.NONE, game.winner());
        expect(0, game.getMoves().size());
        
        // Test that getMoves returns an empty collection when there's a winner
        game = new GameImpl(3);
        // WHITE wins
        game.makeMove(new MoveImpl(0, 0)); // WHITE
        game.makeMove(new MoveImpl(0, 1)); // BLACK
        game.makeMove(new MoveImpl(1, 1)); // WHITE
        game.makeMove(new MoveImpl(1, 0)); // BLACK
        game.makeMove(new MoveImpl(2, 2)); // WHITE
        // WHITE has won, so getMoves should return empty list
        expect(true, game.isOver());
        expect(PieceColour.WHITE, game.winner());
        expect(0, game.getMoves().size());

        checkAllTestsPassed();
    }
}
