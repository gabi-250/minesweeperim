/**
 *  Representation of a single cell in the game board.
 *  @author Jeroen Keppens, King's College London
 */
public class Cell {
  boolean mine = false;
  boolean flag = false;
  boolean revealed = false;
  int adjacent = 0;

  /**
   *  Place a mine in this cell.
   */
  public void mine() {
    mine = true;
  }

  /**
   *  Identify whether this cell contains a mine.
   *  @return true iff this cell contains a mine.
   */
  public boolean hasMine() {
    return mine;
  }

  /**
   *  Place a flag in this cell.
   */
  public void flag() {
    flag = true;
  }

  /**
   *  Remove a flag from this cell.
   */
  public void unflag() {
    flag = false;
  }

  /**
   *  Identify whether this cell contains a flag.
   *  @return true iff this cell contains a flag.
   */
  public boolean hasFlag() {
    return flag;
  }

  /**
   *  Change the status of this cell to reveal.
   */
  public void reveal() {
    revealed = true;
  }

  /**
   *  Identify whether this cell is revealed.
   *  @return true iff this cell is revealed.
   */
  public boolean revealed() {
    return revealed;
  }

  /**
   *  Identify whether this cell has been processed by the player.
   *  A cell is processed by the player if it has been clicked by the player,
   *  e.g. it was clicked in the game board display, or it has been flagged
   *  by the player.
   *  @return true iff this cell has neither been flagged nor been revealed.
   */
  public boolean blank() {
    return !flag && !revealed;
  }

  /**
   *  Sets the number of adjacent cells that contain mines.
   *  @param adjacent the number of adjacent mines.
   */
  public void setAdjacent(int adjacent) {
    this.adjacent = adjacent;
  }

  /**
   *  Increases the number of adjacent mines by 1.
   */
  public void incrementAdjacent() {
    adjacent++;
  }

  /**
   *  Specifies the number of adjacent cells that contain mines.
   *  @return the number of adjacent mines.
   */
  public int getAdjacent() {
    return adjacent;
  }

  /**
   *  Indicates whether there are adjacent mines.
   *  @return true iff there are not adjacent mines.
   */
  public boolean noAdjacentMines() {
    return (adjacent == 0);
  }

  /**
   *  Returns a text representation of the cell.
   *  @return "F" if the cell was flagged, "M" if the cell is not flagged but
   *  contains a mine, or a number indicating the number of adjacent mines if
   *  the cell is neither flagged nor mined.
   */
  public String toString() {
    if (hasFlag()) {
      return "F";
    } else if (hasMine()) {
      return "M";
    } else {
      return Integer.toString(getAdjacent());
    }
  }
}
