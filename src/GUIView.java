import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
import javax.swing.*;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Map;

/**
 * This is a view class extends JFrame.
 */
public class GUIView extends JFrame {
  private JPanel entryPanel;
  private JPanel gamePanel;
  private JTextField rowField;
  private JLabel rowLabel;
  private JTextField columnField;
  private JLabel columnLabel;
  private JTextField wallField;
  private JLabel wallNumber;
  private JTextField playerField;
  private JLabel playerNumber;
  private JTextField batsField;
  private JTextField pitsField;
  private JLabel batsPercentage;
  private JLabel pitsPercentage;
  private JButton startButton;
  private JTextField arrowField;
  private JLabel arrowNumber;
  private JButton northButton;
  private JButton eastButton;
  private JButton shootButton;
  private JButton westButton;
  private JButton southButton;
  private JTextField arrowStepField;
  private JLabel arrowStepLabel;
  private JTextField shootDirectionField;
  private JLabel shootDirectionLabel;
  private JButton sameGameButton;

  /**
   * Constructor of the view class.
   *
   * @param title a string
   */
  public GUIView(String title) {
    super(title);
    gamePanel = new JPanel();

    gamePanel.setBackground(Color.LIGHT_GRAY);
    this.add(entryPanel, BorderLayout.WEST);
//    this.add(gamePanel, BorderLayout.CENTER);

    JScrollPane scrollPane = new JScrollPane(gamePanel);
    scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
    scrollPane.setBounds(30, 20,400, 400);
    JPanel contentPane = new JPanel(null);
    contentPane.setPreferredSize(new Dimension(500, 400));
    contentPane.add(scrollPane);
    this.add(contentPane, BorderLayout.EAST);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    int height = screenSize.height * 2 / 3;
    int width = screenSize.width * 2 / 3;

    this.setMinimumSize(new Dimension(width, height));

//    this.setContentPane(entryPanel);
    this.pack();

  }

  /**
   * Get the room icon.
   *
   * @param ways a hashmap
   *
   * @return a JLabel
   */
  public JLabel getRoomIcon(Map<Direction, Cell> ways) {
    JLabel room = new JLabel();
    if (ways.size() == 4) {
      room = new JLabel(new ImageIcon("res/roombase-4.png"));
    } else if (ways.size() == 3) {
      ImageIcon icon = new ImageIcon("res/roombase-3.png");
      if (!ways.containsKey(Direction.NORTH)){
        room = new JLabel(icon);
      } else if (!ways.containsKey(Direction.EAST)) {
        room = new JLabel(new RotatedIcon(icon, 90.0));
      } else if (!ways.containsKey(Direction.SOUTH)) {
        room = new JLabel(new RotatedIcon(icon, 180.0));
      } else if (!ways.containsKey(Direction.WEST)) {
        room = new JLabel(new RotatedIcon(icon, 270.0));
      }
    } else if ( ways.size() == 2) {
      ImageIcon icon1 = new ImageIcon("res/hallway-straight.png");
      ImageIcon icon2 = new ImageIcon("res/hallway.png");
      if ((ways.containsKey(Direction.EAST)) && (ways.containsKey(Direction.WEST))) {
        room = new JLabel(icon1);
      } else if ((ways.containsKey(Direction.NORTH)) && (ways.containsKey(Direction.SOUTH))) {
        room = new JLabel(new RotatedIcon(icon1, 90.0));
      } else if ((ways.containsKey(Direction.SOUTH)) && (ways.containsKey(Direction.EAST))) {
        room = new JLabel(icon2);
      } else if ((ways.containsKey(Direction.SOUTH)) && (ways.containsKey(Direction.WEST))) {
        room = new JLabel(new RotatedIcon(icon2, 90.0));
      } else if ((ways.containsKey(Direction.NORTH)) && (ways.containsKey(Direction.WEST))) {
        room = new JLabel(new RotatedIcon(icon2, 180.0));
      } else if ((ways.containsKey(Direction.NORTH)) && (ways.containsKey(Direction.EAST))) {
        room = new JLabel(new RotatedIcon(icon2, 270.0));
      }
    } else if (ways.size() == 1) {
      ImageIcon icon = new ImageIcon("res/roombase-1.png");
      if (ways.containsKey(Direction.EAST)){
        room = new JLabel(icon);
      } else if (ways.containsKey(Direction.SOUTH)) {
        room = new JLabel(new RotatedIcon(icon, 90.0));
      } else if (ways.containsKey(Direction.WEST)) {
        room = new JLabel(new RotatedIcon(icon, 180.0));
      } else if (ways.containsKey(Direction.NORTH)) {
        room = new JLabel(new RotatedIcon(icon, 270.0));
      }
    } else {
      room = new JLabel(new ImageIcon("res/roombase-0.png"));
    }
    return room;
  }

  /**
   * Get the pit icon.
   *
   * @param cell a cell
   *
   * @return a JLabel
   */
  public JLabel getPitsIcon(Cell cell, Maze model) {
    JLabel ob = null;
    if (cell.ifHasPit()) {
      ob = new JLabel(new ImageIcon("res/slime-pit.png"));
    } else {
      for (Direction direction : cell.getConnections().keySet()) {
        Cell next = model.cellNextCave(direction, cell);
        if (next.ifHasPit()) {
          ob = new JLabel(new ImageIcon("res/slime-pit-nearby.png"));
        }
      }
    }
    return ob;
  }

  /**
   * Get the wumpus icon.
   *
   * @param cell a cell
   *
   * @return a JLabel
   */
  public JLabel getWumpusIcon(Cell cell, Maze model) {
    JLabel wu = null;
    if (cell.ifHasWumpus()) {
      wu = new JLabel(new ImageIcon("res/wumpus.png"));
    } else {
      for (Direction direction : cell.getConnections().keySet()) {
        Cell next = model.cellNextCave(direction, cell);
        if (next.ifHasWumpus()) {
          wu = new JLabel(new ImageIcon("res/wumpus-nearby.png"));
        }
      }
    }
    return wu;
  }

  /**
   * Get the bat icon.
   *
   * @param cell a cell
   *
   * @return a JLabel
   */
  public JLabel getBatIcon(Cell cell) {
    JLabel bat = null;
    if (cell.ifHasBat()) {
      bat = new JLabel(new ImageIcon("res/superbat.png"));
    }
    return bat;
  }

  /**
   * Get the player icon.
   *
   * @param cell a cell
   *
   * @return a JLabel
   */
  public JLabel getPlayerIcon(Maze model, Cell cell) {
    JLabel player = null;
    JLabel target = new JLabel((new ImageIcon("res/target.png")));
    if (cell == model.getPlayerLocation()) {
      player = new JLabel(new ImageIcon("res/player.png"));
      player.setLayout(new BorderLayout());
      if (model.getCurPlayer() == model.getPlayer()) {
        player.add(target);
      }
    }
    if (model.getNumberOfPlayer() == 2 && cell == model.getPlayer2Location()) {
      player = new JLabel(new ImageIcon("res/player.png"));
      player.setLayout(new BorderLayout());
      if (model.getCurPlayer() == model.getPlayer2()) {
        player.add(target);
      }
    }
    return player;
  }

  /**
   * Show the maze.
   *
   * @param model the maze
   */
  public void showGrid(Maze model) {
//    model.

    int numRows = model.getMap().size();
    int numCols = model.getMap().get(0).size();
    List<List<Cell>> cells = model.getMap();
    System.out.println("" + String.valueOf(numRows) + " " + String.valueOf(numCols));
    gamePanel.removeAll();
    gamePanel.setLayout(new GridLayout(numRows, numCols));
    gamePanel.setSize(numRows * 200, numCols*200);

    for( int i = 0; i < numRows; i++) {
      for(int j = 0; j < numCols; j++) {
        Cell cell = cells.get(i).get(j);
        Map<Direction, Cell> ways;
        ways = cell.getConnections();
        JLabel room = this.getRoomIcon(ways);
        room.setLayout(new BorderLayout());

        if (cell.getConnections().size() != 2) {
          JLabel pit = this.getPitsIcon(cell, model);
          JLabel wumpus = this.getWumpusIcon(cell, model);
          JLabel bat = this.getBatIcon(cell);
          JLabel player = this.getPlayerIcon(model, cell);

          if (pit != null) {
            pit.setLayout(new BorderLayout());
            room.add(pit);
          }

          if (wumpus != null) {
            wumpus.setLayout(new BorderLayout());
            if (pit == null) {
              room.add(wumpus);
            } else {
              pit.add(wumpus);
            }
          }

          if (bat != null) {
            bat.setLayout(new BorderLayout());
            if (pit == null && wumpus == null) {
              room.add(bat);
            } else if (pit != null && wumpus != null) {
              wumpus.add(bat);
            } else if (pit != null) {
              pit.add(bat);
            } else {
              wumpus.add(bat);
            }
          }

          if (player != null) {
            if (bat != null) {
              bat.add(player);
            } else  {
              //  (bat == null)
              if (pit == null && wumpus == null) {
                room.add(player);
              } else if (pit != null && wumpus != null) {
                wumpus.add(player);
              } else if (pit != null) {
                pit.add(player);
              } else {
                wumpus.add(player);
              }
            }
          }
        }
//        JPanel canvas = new JPanel();
//        canvas.add(room);
        if (model.getVisited().contains(cell)) {
          room.setVisible(true);
        } else {
          room.setVisible(false);
        }
        gamePanel.add(room);
      }
    }
  }

  /**
   * Main function for this view.
   *
   * @param args the arguments
   */
  public static void main(String args[]) {
    JFrame window = new GUIView("Hunt the Wumpus!");
    window.setVisible(true);

  }

  /**
   * Set all kinds of listeners.
   *
   * @param actionListener an action listener
   *
   * @param keyListener    a key listener
   */
  public void setListener(ActionListener actionListener, KeyListener keyListener) {
    startButton.addActionListener(actionListener);
    northButton.addActionListener(actionListener);
    southButton.addActionListener(actionListener);
    eastButton.addActionListener(actionListener);
    westButton.addActionListener(actionListener);
    shootButton.addActionListener(actionListener);
    sameGameButton.addActionListener(actionListener);
    this.addKeyListener(keyListener);
  }

  /**
   * Reset focus to the frame.
   */
  public void resetFocus() {
    this.setFocusable(true);
    this.requestFocus();
  }

  /**
   * Get the number of rows of the maze.
   *
   * @return the number
   */
  public int getRowField() {
    return Integer.parseInt(this.rowField.getText());
  }

  /**
   * Get the number of columns of the maze.
   *
   * @return the number
   */
  public int getColField() {
    return Integer.parseInt(this.columnField.getText());
  }

  /**
   * Get the number of walls of the maze.
   *
   * @return the number
   */
  public int getWallField() {
    return Integer.parseInt(this.wallField.getText());
  }

  /**
   * Get the number of players of the maze.
   *
   * @return the number
   */
  public int getPlayerField() {
    return Integer.parseInt(this.playerField.getText());
  }

  /**
   * Get the percentage of pits of the maze.
   *
   * @return the number
   */
  public int getPitsField() {
    return Integer.parseInt(this.pitsField.getText());
  }

  /**
   * Get the percentage of bats of the maze.
   *
   * @return the number
   */
  public int getBatsField() {
    return Integer.parseInt(this.batsField.getText());
  }

  /**
   * Get the number of arrows for the players.
   *
   * @return the number
   */
  public int getArrowField() {
    return Integer.parseInt(this.arrowField.getText());
  }

  /**
   * Get the number of rooms for the arrow.
   *
   * @return the number
   */
  public int getArrowStep() {
    return Integer.parseInt(this.arrowStepField.getText());
  }

  /**
   * Get the direction of arrows.
   *
   * @return the number
   */
  public Direction getArrowDirection() {
    return Direction.valueOf(this.shootDirectionField.getText());
  }

  /**
   * The view displayed when players lose.
   *
   * @param model the maze
   */
  public void gameOver(Maze model) {
    gamePanel.removeAll();
    gamePanel.setBackground(Color.GRAY);
    gamePanel.add(new Label(model.getLoseString()));
  }

  /**
   * The view displayed when players win.
   */
  public void win() {
    gamePanel.removeAll();
    gamePanel.setBackground(Color.PINK);
    gamePanel.add(new Label("You Win!"));
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
  }
}
