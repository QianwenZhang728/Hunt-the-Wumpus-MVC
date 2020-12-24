/**
 * This is room maze class.
 */
public class RoomMaze extends AbstractMaze {

  public RoomMaze(int numRows, int numCols, double batPercentage, double pitPercentage,
                  int wallNum, int arrowNumber, int numberOfPlayer) {
    super(numRows, numCols, batPercentage, pitPercentage, wallNum, arrowNumber, numberOfPlayer);
    this.setupObstacles();
    this.addPlayer();
  }

}
