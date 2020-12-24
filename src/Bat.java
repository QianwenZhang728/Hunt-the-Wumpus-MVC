import java.util.Random;

/**
 * This class represents super bats.
 */
public class Bat implements Item {
  public Bat() {}

  @Override
  public void takeEffect(Maze maze) {
    Random rand = new Random();
    int temp = rand.nextInt(maze.getRoomsIndex().size());
    int r = maze.getRoomsIndex().get(temp) / maze.getMap().get(0).size();
    int c = maze.getRoomsIndex().get(temp) % maze.getMap().get(0).size();
    Cell next = maze.getMap().get(r).get(c);
    maze.getCurPlayer().setLocation(next);
    maze.getVisited().add(next);
    maze.demonstrate("");
  }

  @Override
  public String getType() {
    return "Bat";
  }
}
