import java.util.HashMap;
import java.util.Map;

/**
 * Class cell represents each location in the maze.
 */
public class Cell {
  private final int[] coordinates;
  private HashMap<String, Item> obstacles;
  private Map<Direction, Cell> connections;

  /**
   * Constructor of the class.
   *
   * @param x x coordinate
   * @param y y coordinate
   */
  public Cell(int x, int y) {
    coordinates = new int[2];
    this.coordinates[0] = x;
    this.coordinates[1] = y;
    this.connections = new HashMap<>();
    this.obstacles = new HashMap<>();
  }

  /**
   * Get coordinates of the cell.
   *
   * @return the coordinates
   */
  public int[] getCoordinates() {
    return this.coordinates;
  }

  /**
   * Add connect between a cell and its neighbor.
   *
   * @param d         the direction
   * @param otherCell the cell's neighbor
   */
  public void addConnection(Direction d, Cell otherCell) {
    // do some checking
    if (!this.connections.containsKey(d)) {
      // add otherCell to this
      this.connections.put(d, otherCell);
      // add this to otherCell as well
      // The enum valueOf() method helps to convert string to enum instance.
      otherCell.addConnection(Direction.valueOf(d.opposite()), this);
    }

  }

  /**
   * Set pit for the cell.
   */
  public void setPit() {
    this.obstacles.put("Pit", new Pit());
  }

  /**
   * If the cell has a pit.
   *
   * @return If the cell has a pit
   */
  public boolean ifHasPit() {
    return this.obstacles.containsKey("Pit");
  }

  /**
   * Set bat for the cell.
   */
  public void setBat() {
    this.obstacles.put("Bat", new Bat());
  }

  /**
   * If the cell has a bat.
   *
   * @return If the cell has a bat
   */
  public boolean ifHasBat() {
    return this.obstacles.containsKey("Bat");
  }

  /**
   * Set bat for the cell.
   */
  public void setWumpus() {
    this.obstacles.put("Wumpus", new Wumpus());
  }

  /**
   * If the cell has a bat.
   *
   * @return If the cell has a bat
   */
  public boolean ifHasWumpus() {
    return this.obstacles.containsKey("Wumpus");
  }

  /**
   * Get connections of a cell.
   *
   * @return all connections in a hash map
   */
  public Map<Direction, Cell> getConnections() {
    return this.connections;
  }

  /**
   * Get obstacles os a cell.
   *
   * @return obstacles in a hashmap
   */
  public HashMap<String, Item> getObstacles() {
    return this.obstacles;
  }

  @Override
  public boolean equals(Object obj) {
    // do something
    return false;
  }
  @Override
  public String toString() {
    return "coordinate: " + this.coordinates[0] + " " + this.coordinates[1];
  }
}
