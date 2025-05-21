package game;

public class GridImpl implements Grid {
    private final int size;
    private final PieceColour[][] grid;
    
    public GridImpl(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Grid size must be positive");
        }
        this.size = size;
        this.grid = new PieceColour[size][size];
        
        // Initialize all positions to NONE
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = PieceColour.NONE;
            }
        }
    }
    
    private GridImpl(int size, PieceColour[][] gridData) {
        this.size = size;
        this.grid = new PieceColour[size][size];
        
        // Copy grid data
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                grid[row][col] = gridData[row][col];
            }
        }
    }
    
    @Override
    public int getSize() {
        return size;
    }
    
    @Override
    public PieceColour getPiece(int row, int col) {
        validatePosition(row, col);
        return grid[row][col];
    }
    
    @Override
    public void setPiece(int row, int col, PieceColour piece) {
        validatePosition(row, col);
        if (piece == null) {
            throw new IllegalArgumentException("Piece colour cannot be null");
        }
        grid[row][col] = piece;
    }
    
    @Override
    public Grid copy() {
        return new GridImpl(size, grid);
    }
    
    private void validatePosition(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Position (" + row + "," + col + ") is out of bounds");
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                PieceColour piece = grid[row][col];
                switch (piece) {
                    case NONE:
                        sb.append('.');
                        break;
                    case WHITE:
                        sb.append('W');
                        break;
                    case BLACK:
                        sb.append('B');
                        break;
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
