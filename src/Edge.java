/**
 * A class represents the edge between two connected cells.
 * x,y are coordinates of where the edge starts, and direction is the direction to which it points.
 */
public class Edge {
  private final int x;
  private final int y;
  private final Direction direction;

  /**
   * The constructor for this class.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   * @param direction the direction
   */
  public Edge(int x, int y, Direction direction) {
    this.x = x;
    this.y = y;
    this.direction = direction;
  }

  /**
   * Get the start point's x coordinate of this edge.
   *
   * @return x coordinate
   */
  public int getX() {
    return this.x;
  }

  /**
   * Get the start point's y coordinate of this edge.
   *
   * @return y coordinate
   */
  public int getY() {
    return this.y;
  }

  /**
   * Get the direction to which it points.
   *
   * @return direction
   */
  public Direction getDirection() {
    return this.direction;
  }
}
