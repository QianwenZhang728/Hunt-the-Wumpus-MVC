public class Player {
  private int arrow;
  private Cell curLoc;

  public Player(int arrow) {
    this.arrow = arrow;
    this.curLoc = null;
  }

  /**
   * Get the player's gold.
   *
   * @return number of gold coins
   */
    public int getArrow() {
      return arrow;
  }

  /**
   * Set the player's gold.
   *
   * @param arrow number of gold arrows
   */
  public void setArrow(int arrow) {
    this.arrow = arrow;
  }

  /**
   * Get the player's location.
   *
   * @return the player's location
   */
  public Cell getLocation() {
    return this.curLoc;
  }

  /**
   * set the player's location.
   *
   * @param cell the player's location
   */
  public void setLocation(Cell cell) {
    this.curLoc = cell;
  }

}
