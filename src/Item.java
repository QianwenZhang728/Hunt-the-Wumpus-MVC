import java.util.Set;

/**
 * This is an interface for item class.
 */
public interface Item {
  /**
   * Calculate the number of coins after taking effect of the item.
   *
   * @param maze the maze used by the game
   *
   * @return  number of gold coins when the player leaves the room
   */
  void takeEffect(Maze maze);

  /**
   * Get the type of the item.
   *
   * @return  the type of the item
   */
  String getType();



}
