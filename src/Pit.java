/**
 * This class represents pits.
 */
public class Pit implements Item {
  public Pit() { }

  @Override
  public void takeEffect(Maze maze) {
    maze.setIfEnded(true);
  }

  @Override
  public String getType() {
    return "Pit";
  }
}
