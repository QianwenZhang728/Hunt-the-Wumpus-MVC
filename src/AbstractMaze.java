import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

class AbstractMaze implements Maze {
  protected Player p;
  protected Player p2;
  protected Player curP;
  protected int playerCount;
  protected int firstDead;
  protected Cell playerStartCell;
  protected Cell playerStartCell2;
  protected int numberOfPlayer;
  protected int numberOfArrow;
  protected List<List<Cell>> map;
  protected ArrayList<Edge> savedWalls;
  protected boolean isEnded;
  protected boolean isWin;
  protected double batPercentage;
  protected double pitPercentage;
  protected ArrayList<Integer> roomsIndex;
  protected HashSet<Cell> visited;
  protected String loseString;


  AbstractMaze(int numRows, int numCols, double batPercentage, double pitPercentage,
               int wallNum, int arrowNumber, int numberOfPlayer) {
    // construct the map skeleton
    map = new ArrayList<>(numRows);
    for (int i = 0; i < numRows; i++) {
      List<Cell> temp = new ArrayList<>(numCols);
      for (int j = 0; j < numCols; j++) {
        temp.add(null);
      }
      map.add(temp);
    }

    // init perfect maze
    this.savedWalls = new ArrayList<>();
    this.setupPerfectMaze(numRows, numCols);

    // init status
    this.isEnded = false;
    this.isWin = false;
    this.visited = new HashSet<>();

    // set up room maze
    this.batPercentage = batPercentage * 0.01;
    this.pitPercentage = pitPercentage * 0.01;
    this.roomsIndex = new ArrayList<>();
    setupRoomMaze(wallNum, numRows, numCols);

    this.p = new Player(arrowNumber);
    this.curP = p;
    this.numberOfPlayer = numberOfPlayer;
    this.numberOfArrow = arrowNumber;
    this.playerCount = 1;
    this.firstDead = 1;
    if (this.numberOfPlayer == 2) {
      this.playerCount = 2;
      this.firstDead = 0;
      this.p2 = new Player(arrowNumber);
    }

  }

  /**
   * Initialize cells for a perfect maze, including items in cells.
   */
  protected void setupPerfectMaze(int numRows, int numCols) {
    // add cells to the maze
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        Cell cell = new Cell(i, j);
        map.get(i).set(j, cell);
      }
    }

    // add connections between cells
    this.savedWalls = this.connect(numRows, numCols);
  }

  /**
   * Add connections between cells in the maze.
   *
   * @param numRows number of rows of the maze
   * @param numCols number of columns of the maze
   *
   * @return saved walls
   */
  protected ArrayList<Edge> connect(int numRows, int numCols) throws IllegalStateException {
    // Initialize each cell of the maze as a tree.
    ArrayList<ArrayList<Tree>> trees = new ArrayList<>();
    for (int i = 0; i < numRows; i++) {
      ArrayList<Tree> tmp = new ArrayList<>();
      for (int j = 0; j < numCols; j++) {
        tmp.add(new Tree());
      }
      trees.add(tmp);
    }

    // Save all edges of the skeleton in a list
    ArrayList<Edge> edges = new ArrayList<>();
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        if (i > 0) {
          edges.add(new Edge(i, j, Direction.NORTH));
        }
        if (j < numCols - 1) {
          edges.add(new Edge(i, j, Direction.EAST));
        }
      }
    }

    // Randomize the edges.
    for (int i = 0; i < edges.size(); i++) {
      Random rand = new Random();
      int j = rand.nextInt(edges.size());
      Edge e1 = edges.get(i);
      Edge e2 = edges.get(j);
      edges.set(i, e2);
      edges.set(j, e1);
    }

    // Connect cells.
    while (edges.size() > 0) {
      Edge e = edges.remove(0);
      int y = e.getY();
      int x = e.getX();
      Direction d = e.getDirection();
      int[] neighbor = this.getNeighbor(numRows, numCols, d, x, y);
      int nx = neighbor[0];
      int ny = neighbor[1];

      Tree tree1 = trees.get(x).get(y);
      Tree tree2 = trees.get(nx).get(ny);

      // Connect two trees.
      if (!tree1.isConnected(tree2)) {
        tree1.connect(tree2);
        this.map.get(x).get(y).addConnection(d, this.map.get(nx).get(ny));
      } else {
        savedWalls.add(e);
      }
    }

    if (savedWalls.size() == numCols * (numRows - 1) + numRows * (numCols - 1)
        - numRows * numCols + 1) {
      return savedWalls;
    } else {
      throw new IllegalStateException("Wrong state of saved walls.");
    }

  }

  protected void setupObstacles() {
    // get index of rooms
    int numRows = this.map.size();
    int numCols = this.map.get(0).size();
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        if (map.get(i).get(j).getConnections().size() != 2) {
          this.roomsIndex.add(j + i * numCols);
        }
      }
    }

    // generate random numbers that represents the locations of pits and bats.
    Set<Integer> pitIndex = new HashSet<>();
    Set<Integer> batIndex = new HashSet<>();
    this.generateRandom(pitIndex, batIndex, this.roomsIndex);

    // add pits, bats and Wumpus to rooms
    this.addObstacles(pitIndex, batIndex, numRows, numCols);
  }

  /**
   * Generate random numbers that represents the locations of pits and bats.
   *
   * @param pitIndex indexes for pits
   * @param batIndex indexes for bats
   * @param roomsIndex indexes of rooms
   */
  protected void generateRandom(Set<Integer> pitIndex, Set<Integer> batIndex,
                                ArrayList<Integer> roomsIndex) {
    int numRooms = roomsIndex.size();
    Random rand = new Random();

    while (pitIndex.size() < numRooms * this.pitPercentage) {
      int tmp = rand.nextInt(roomsIndex.size());
      pitIndex.add(roomsIndex.get(tmp));
    }
    while (batIndex.size() < numRooms * this.batPercentage) {
      int tmp = rand.nextInt(roomsIndex.size());
      batIndex.add(roomsIndex.get(tmp));
    }

  }

  /**
   * Add cells to this maze.
   *
   * @param pitIndex index for pit
   * @param batIndex index for bat
   * @param numRows number of rows of the maze
   * @param numCols number of columns of the maze
   */
  protected void addObstacles(Set<Integer> pitIndex, Set<Integer> batIndex,
                              int numRows, int numCols) {
    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        if (pitIndex.contains(i * numCols + j)) {
          map.get(i).get(j).setPit();
        }
        if (batIndex.contains(i * numCols + j)) {
          map.get(i).get(j).setBat();
        }
      }
    }

    Random rand = new Random();
    int tmp = rand.nextInt(this.roomsIndex.size());
    int index = this.roomsIndex.get(tmp);
    map.get(index / numCols).get(index % numCols).setWumpus();
  }


  /**
   * Build additional connections between cells based on room maze.
   *
   * @param wallNum the number of walls remaining
   * @param numRows the number of rows
   * @param numCols the number of columns
   *
   * @throws IllegalArgumentException illegal wall numbers
   */
  protected void setupRoomMaze(int wallNum, int numRows, int numCols)
      throws IllegalArgumentException {
    if (wallNum >= this.savedWalls.size() || wallNum < 0) {
      throw new IllegalArgumentException("Illegal number of walls.");
    }

    while(this.savedWalls.size() > wallNum) {
      Edge e = savedWalls.remove(0);
      int x = e.getX();
      int y = e.getY();
      Direction d = e.getDirection();
      int[] neighbor = this.getNeighbor(numRows, numCols, d, x, y);
      int nx = neighbor[0];
      int ny = neighbor[1];

      // Connect two trees.
      this.map.get(x).get(y).addConnection(d, this.map.get(nx).get(ny));
    }
  }

  /**
   * Add a player to a random cave.
   */
  protected void addPlayer() {
    int numCols = map.get(0).size();
    Random rand = new Random();
    int tmp = rand.nextInt(this.roomsIndex.size());
    int index = this.roomsIndex.get(tmp);
    while (map.get(index / numCols).get(index % numCols).getObstacles().size() != 0) {
      tmp = rand.nextInt(this.roomsIndex.size());
      index = this.roomsIndex.get(tmp);
    }
    this.p.setLocation(map.get(index / numCols).get(index % numCols));
    this.playerStartCell = this.getPlayerLocation();
    this.visited.add(this.getPlayerLocation());

    if (this.numberOfPlayer == 2) {
      tmp = rand.nextInt(this.roomsIndex.size());
      index = this.roomsIndex.get(tmp);
      while (map.get(index / numCols).get(index % numCols).getObstacles().size() != 0
          && map.get(index / numCols).get(index % numCols) != this.p.getLocation()) {
        tmp = rand.nextInt(this.roomsIndex.size());
        index = this.roomsIndex.get(tmp);
      }
      this.p2.setLocation(map.get(index / numCols).get(index % numCols));
      this.playerStartCell2 = this.p2.getLocation();
      this.visited.add(this.p2.getLocation());

    }
  }

  /**
   * Get coordinates of neighbor for a cell according to its given direction.
   *
   * @param numRows number of rows of the maze
   * @param numCols number of columns of the maze
   * @param d       the given direction
   * @param x       current x coordinate
   * @param y       current y coordinate
   * @return        neighbor's location
   */
  protected int[] getNeighbor(int numRows, int numCols, Direction d, int x, int y) {
    int[] res = new int[2];
    if (d == Direction.NORTH && x != 0) {
      res[0] = x - 1;
      res[1] = y;
      return res;
    } else if (d == Direction.SOUTH && x != numRows - 1) {
      res[0] = x + 1;
      res[1] = y;
      return res;
    } else if (d == Direction.EAST && y != numCols - 1) {
      res[0] = x;
      res[1] = y + 1;
      return res;
    } else if (d == Direction.WEST && y != 0) {
      res[0] = x;
      res[1] = y - 1;
      return res;
    } else {
      return null;
    }
  }


  @Override
  public Cell getPlayerLocation() {
    return p.getLocation();
  }

  public Cell getPlayer2Location() {
    return p2.getLocation();
  }

  public Cell getCurPlayerLocation() {
    return curP.getLocation();
  }
  @Override
  public Cell getPlayerStartCell() {return this.playerStartCell;}


  @Override
  public Cell getPlayerStartCell2() {return this.playerStartCell2;}

  @Override
  public int getPlayerArrow() {
    return p.getArrow();
  }

  public int getCurPlayerArrow() {
    return curP.getArrow();
  }

  @Override
  public List<List<Cell>> getMap() {
    return this.map;
  }

  @Override
  public Set<Direction> possibleMove() {
    return this.curP.getLocation().getConnections().keySet();
  }

  @Override
  public void shoot(Direction d, int step) {
    this.curP.setArrow(this.curP.getArrow() - 1);
    shootArrow(d, step, curP.getLocation());
    if (this.curP.getArrow() == 0) {
      this.playerCount -= 1;
      if (this.playerCount == 0) {
        this.isEnded = true;
        this.loseString = "Run out of arrows! You lose!";
      }
    }
    this.togglePlayer();
  }

  /**
   * Helper method for shoot.
   *
   * @param d             the direction
   * @param step          the steps
   * @param arrowPosition the current arrow position
   */
  protected void shootArrow(Direction d, int step, Cell arrowPosition) {
    if (step == 0) {
      if (arrowPosition.ifHasWumpus()) {
        this.isWin = true;
        System.out.println("You took the Wumpus out! You win!");
      }
      return;
    }
    if (!arrowPosition.getConnections().containsKey(d)) {
      System.out.println("Arrow hit walls!");
      return;
    }

    Direction prev = d;
    Cell next = arrowPosition.getConnections().get(d);

    while (next.getConnections().size() == 2) {
      for (Direction direction : next.getConnections().keySet()) {
        if (!direction.toString().equals(prev.opposite())) {
          prev = direction;
          next = next.getConnections().get(direction);
          break;
        }
      }
    }
    shootArrow(prev, step - 1, next);
  }

  @Override
  public Cell nextCave(Direction d, String mode) {
    Cell cur = this.curP.getLocation();
    Cell next = cur.getConnections().get(d);
    Direction prev = d;
    if (mode == "move") {
      this.visited.add(next);
    }
    while (next.getConnections().size() == 2) {
      for (Direction direction : next.getConnections().keySet()) {
        if (!direction.toString().equals(prev.opposite())) {
          next = next.getConnections().get(direction);
          prev = direction;
          if (mode == "move") {
            this.visited.add(next);
          }
          break;
        }
      }
    }
    return next;
  }

  @Override
  public Cell cellNextCave(Direction d, Cell cell) {
    Cell next = cell.getConnections().get(d);

    Direction prev = d;
    while (next.getConnections().size() == 2) {
      for (Direction direction : next.getConnections().keySet()) {
        if (!direction.toString().equals(prev.opposite())) {
          next = next.getConnections().get(direction);
          prev = direction;
          break;
        }
      }
    }
    return next;
  }

  @Override
  public String checkNeighbors(Cell cur) {
    String status = "";
    boolean isWumpus = false;
    boolean isDraft = false;
    for (Direction direction : cur.getConnections().keySet()) {
      Cell next = nextCave(direction, "check");
      if (next.ifHasWumpus()) {
        isWumpus = true;
      }
      if (next.ifHasPit()) {
        isDraft = true;
      }
    }

    if (isDraft) {
      status += "You feel a draft\n";
    }
    if (isWumpus) {
      status += "You feel a Wumpus\n";
    }
    return status;
  }

  @Override
  public void makeMove(Direction d) {
    Cell cur = this.curP.getLocation();
    Cell next;
    next = nextCave(d, "move");
    this.curP.setLocation(next);
    demonstrate("");
    this.visited.add(next);
    this.visited.add(this.curP.getLocation());
    if (this.numberOfPlayer == 2) {
      this.togglePlayer();
    }
  }

  @Override
  public String demonstrate(String prev) {
    String status = prev;

    if (this.curP.getLocation().ifHasWumpus()) {
      this.playerCount -= 1;
      if (this.playerCount == 0) {
        this.loseString = "Chomp, Chomp, Chomp, thanks for feeding the Wumpus!\n";
        this.isEnded = true;
      }
    } else if (this.curP.getLocation().ifHasBat()) {
      Random rand = new Random();
      int temp = rand.nextInt(2);
      if (temp == 1) {
        this.loseString = "You are grabbed by a super bat!\n";
        this.curP.getLocation().getObstacles().get("Bat").takeEffect(this);
      } else {
        this.loseString = "You dodged super bats!\n";
        if (this.curP.getLocation().ifHasPit()) {
          this.playerCount -= 1;
          if (this.playerCount == 0) {
            this.loseString += "But You are falling to death!\n";
            this.isEnded = true;
          }
        }
      }
    } else if (this.curP.getLocation().ifHasPit()) {
      this.playerCount -= 1;
      if (this.playerCount == 0) {
        this.loseString = "You are falling to death!\n";
        this.isEnded = true;
      }
    }
    return status;
  }

  @Override
  public ArrayList<Integer> getRoomsIndex() {
    return this.roomsIndex;
  }

  @Override
  public boolean getIfEnded() {
    return this.isEnded;
  }

  @Override
  public boolean getIfWin() {
    return this.isWin;
  }

  @Override
  public Player getPlayer() {
    return this.p;
  }

  @Override
  public Player getPlayer2() {
    return this.p2;
  }

  @Override
  public Player getCurPlayer() {
    return this.curP;
  }

  @Override
  public void setIfEnded(boolean b) {
    this.isEnded = b;
  }

  @Override
  public void setIfWin(boolean b) {
    this.isWin = b;
  }

  @Override
  public Set<Cell> getVisited() { return this.visited;}

  @Override
  public String getLoseString() { return this.loseString;}

  @Override
  public int getNumberOfPlayer() {
    return this.numberOfPlayer;
  }

  @Override
  public int getNumberOfArrow() {
    return this.numberOfArrow;
  }

  public void setPlayerCount() {
    this.playerCount = this.numberOfPlayer;
  }
  public void setFirstDead() {
    if (this.numberOfPlayer == 2) {
      this.firstDead = 0;
    } else {
      this.firstDead = 1;
    }
  }

  @Override
  public Player togglePlayer() {
    if (this.playerCount == 2) {
      if (curP == p) {
        curP = p2;
      } else { // curP == p2
        curP = p;
      }
    } else if (this.firstDead == 0){
      if (curP == p) {
        curP = p2;
      } else { // curP == p2
        curP = p;
      }
      this.firstDead = 1;
    } else {

    }
    return curP;
  }
}
