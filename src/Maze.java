import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Interface for class maze.
 */
public interface Maze {
  /**
   * Produce the player's location.
   *
   * @return the player's location
   */
  Cell getPlayerLocation();

  Cell getPlayer2Location();

  /**
   * Produce the player's arrow count.
   *
   * @return the player's arrow count
   */
  int getPlayerArrow();

  /**
   * Just for test purpose.
   *
   * @return the maze skeleton
   */
  List<List<Cell>> getMap();

  /**
   * Produce possible moves for the player.
   *
   * @return a list of directions
   */
  Set<Direction> possibleMove();

  /**
   * Make the player move by specifying a direction.
   *
   * @param d specified direction
   */
  void makeMove(Direction d);

  /**
   * Demonstrate a player navigating within the maze.
   *
   * @param prev the previous status
   *
   * @return a string demonstrate a player navigating within the maze
   */
  String demonstrate(String prev);

  /**
   * Check if neighbor caves contain a bit or a Wumpus.
   *
   * @param cur the current cave
   *
   * @return the obstacles in neighbor
   */
  String checkNeighbors(Cell cur);

  /**
   * Get the next cave from the current cave given a direction.
   *
   * @param d the direction
   *
   * @return the next cave
   */
  Cell nextCave(Direction d, String mode);

  Cell cellNextCave(Direction d, Cell cell);
  /**
   * Shoot according to the given direction and steps.
   *
   * @param d
   * @param step
   */
  void shoot(Direction d, int step);

  /**
   * Get if the player loses the game.
   *
   * @return if the player loses the game
   */
  boolean getIfEnded();

  /**
   * Get if the player wins the game.
   *
   * @return if the player wins the game
   */
  boolean getIfWin();

  /**
   * Get the indexes of caves in the maze.
   *
   * @return an array list of indexes
   */
  ArrayList<Integer> getRoomsIndex();

  /**
   * Get the player in the maze.
   */
  Player getPlayer();

  /**
   * Get the player in the maze.
   */
  Player getPlayer2();

  /**
   * Get the player in the maze.
   */
  Player getCurPlayer();

  /**
   * The player loses the game.
   *
   * @param b if the player loses the game, b is true.
   */
  void setIfEnded(boolean b);

  /**
   * The player wins the game.
   *
   * @param b if the player wins the game, b is true.
   */
  void setIfWin(boolean b);

  /**
   * Get visited cells.
   */
  Set<Cell> getVisited();

  /**
   * Get a player's start position.
   */
  Cell getPlayerStartCell();

  /**
   * Get a player's start position.
   */
  Cell getPlayerStartCell2();

  /**
   * Get the description of death.
   *
   * @return a string
   */
  String getLoseString();

  /**
   * Get the number of players.
   *
   * @return the number of players
   */
  int getNumberOfPlayer();

  /**
   * Toggle between two players.
   *
   * @return player
   */
  Player togglePlayer();

  /**
   * Get the number of arrows of a player at the beginning of the game.
   *
   * @return number of arrows
   */
  int getNumberOfArrow();

  /**
   * Set current number of players in the game.
   */
  void setPlayerCount();

  /**
   * Set if the first player has dead.
   */
  void setFirstDead();
}
