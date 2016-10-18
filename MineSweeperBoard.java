import java.util.Random;

/**
 *  Representation of a minesweepr game board.
 *  <p>This is the model class in the MVC architecture.</p>
 *  @author Jeroen Keppens, King's College London
 */
public class MineSweeperBoard {
  private static Random random = new Random();
  Cell[][] cell;
  int rows = 10;
  int cols = 10;
  int mines = 10;
  int flags = 0;
  int completion = 0;
  boolean finished = false;
  boolean won = false;

  /**
   *  Produces the number of rows in the board.
   *  @return the number of rows.
   */
  public int getRows() {
    return rows;
  }

  /**
   *  Produces the number of columns in the board.
   *  @return the number of columns.
   */
  public int getCols() {
    return cols;
  }

  private void incrementNeighbourAdjacents(int row, int col) {
    if (row>0) cell[row-1][col].incrementAdjacent();
    if (row<rows-1) cell[row+1][col].incrementAdjacent();
    if (col>0) cell[row][col-1].incrementAdjacent();
    if (col<cols-1) cell[row][col+1].incrementAdjacent();
    if ((row>0) && (col>0)) cell[row-1][col-1].incrementAdjacent();
    if ((row>0) && (col<cols-1)) cell[row-1][col+1].incrementAdjacent();
    if ((row<rows-1) && (col>0)) cell[row+1][col-1].incrementAdjacent();
    if ((row<rows-1) && (col<cols-1)) cell[row+1][col+1].incrementAdjacent();
  }

  private void revealNeighbours(int row, int col) {
    if (row>0) reveal(row-1,col);
    if (row<rows-1) reveal(row+1,col);
    if (col>0) reveal(row,col-1);
    if (col<cols-1) reveal(row,col+1);
    if ((row>0) && (col>0)) reveal(row-1,col-1);
    if ((row>0) && (col<cols-1)) reveal(row-1,col+1);
    if ((row<rows-1) && (col>0)) reveal(row+1,col-1);
    if ((row<rows-1) && (col<cols-1)) reveal(row+1,col+1);
  }

  private void checkCompletion() {
    if (!finished && (completion==(rows*cols))) {
      won = true;
      finished = true;
    }
  }

  /**
   *  Resets the game board.
   *  <p>This method generate a new game based on the current specification of the
   *  game board.  Random mines are added to the game board until the number of
   *  mines equals the <tt>mines</tt> parameter.  The number of mines
   *  adjacent to each cell is calculated in this process.</p>
   */
  public void newGame() {
    cell = new Cell[rows][cols];
    for(int i=0; i<rows; i++) {
      for(int j=0; j<cols; j++) {
        cell[i][j] = new Cell();
      }
    }
    int mineCount = 0;
    while(mineCount<mines) {
      int row=random.nextInt(rows);
      int col=random.nextInt(cols);
      if (!cell[row][col].hasMine()) {
        cell[row][col].mine();
        mineCount++;
        incrementNeighbourAdjacents(row,col);
      }
    }
    flags = 0;
    completion = 0;
    finished = false;
    won = false;
  }

  /**
   *  Indicates whether a cell with given coordinates contains a mine.
   *  @param row the given row coordinates.
   *  @param col the given column coordinate.
   *  @return true iff the cell contains a mine.
   */
  public boolean hasMine(int row, int col) {
    return cell[row][col].hasMine();
  }

  /**
   *  Indicates whether a cell with given coordinates contains a flag.
   *  @param row the given row coordinates.
   *  @param col the given column coordinate.
   *  @return true iff the cell contains a flag.
   */
  public boolean hasFlag(int row, int col) {
    return cell[row][col].hasFlag();
  }

  /**
   *  Plants a flag at the cell with the given coordinates.
   *  @param row the given row coordinate.
   *  @param col the given column coordinate.
   */
  public void flag(int row, int col) {
    if (finished) return;
    if ((flags<mines) && (cell[row][col].blank())) {
      cell[row][col].flag();
      completion++;
      flags++;
      checkCompletion();
    }
  }

  /**
   *  Removes a flag from the cell with the given coordinates.
   *  @param row the given row coordinate.
   *  @param col the given column coordinate.
   */
  public void unflag(int row, int col) {
    if (finished) return;
    if ((cell[row][col].hasFlag()) && (!cell[row][col].revealed())) {
      cell[row][col].unflag();
      completion--;
      flags--;
    }
  }

  /**
   *  Determines whether the cell with the given coordinates was revealed.
   *  @param row the given row coordinate.
   *  @param col the given column coordinate.
   *  @return true iff the cell at the given coordinate was revealed by the player.
   */
  public boolean revealed(int row, int col) {
    return cell[row][col].revealed();
  }

  /**
   *  Reveal the contents of the cell with the given coordinates.
   *  <p>Note that revealing the contents of a cell containing a mine will
   *  cause the game to end with a loss.  If the cell does not contain a mine
   *  and it has no adjecent mines, all adjacent cells are revealed as well.
   *  If this is the last cell to be revealed, the game ends with a win.
   *  @param row the given row coordinate.
   *  @param col the given column coordinate.
   */
  public void reveal(int row, int col) {
    if (finished) return;
    if (cell[row][col].blank()) {
      cell[row][col].reveal();
      completion++;
      if (cell[row][col].hasMine()) {
        finished = true;
        won = false;
      } else if (cell[row][col].noAdjacentMines()) {
        revealNeighbours(row,col);
      }
      checkCompletion();
    }
  }

  /**
   *  Retrieves the number of mines adjacent to the cell with the given coordinates.
   *  @param row the given row coordinate.
   *  @param col the given column coordinate.
   *  @return the number of adjacent mines.
   */
  public int adjacent(int row, int col) {
    return cell[row][col].getAdjacent();
  }

  /**
   *  Retrieves the number of flags available for flagging cells.
   *  @return the number of available flags.
   */
  public int getFlags() {
    return flags;
  }

  /**
   *  Retrieves the number of mines on the board.
   *  @return the number of mines on the board.
   */
  public int getMines() {
    return mines;
  }

  /**
   *  Retrieves the number of cells on the board that have been flagged or
   *  revealed.
   *  @return the cells flagged or revealed by the player.
   */
  public int getCompletion() {
    return completion;
  }

  /**
   *  Retrieves the number of cells on the board.
   *  @return the number of cells on the board.
   */
  public int getMaxCompletion() {
    return (rows * cols);
  }

  /**
   *  Checks whether the game was won.
   *  @return true iff the game is finished and was won.
   */
  public boolean hasWon() {
    return (finished && won);
  }

  /**
   *  Checks whether the game was lost.
   *  @return true iff the game is finished and was lost.
   */
  public boolean hasLost() {
    return (finished && !won);
  }

  /**
   *  Produces a text visualisation of the board contents.
   *  @return a String representing the board contents.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for(int i=0; i<rows; i++) {
      for(int j=0; j<cols; j++) {
        sb.append(cell[i][j]);
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  /**
   *  Construct a new board with the default paramters.
   */
  public MineSweeperBoard() {
    super();
    newGame();
  }
}
