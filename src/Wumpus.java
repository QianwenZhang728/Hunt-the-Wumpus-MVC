/**
 * This class represents Wumpus.
 */
public class Wumpus implements Item {
  public Wumpus() { }

  @Override
  public void takeEffect(Maze maze) {
    maze.setIfEnded(true);
  }

  @Override
  public String getType() {
    return "Wumpus";
  }
}
