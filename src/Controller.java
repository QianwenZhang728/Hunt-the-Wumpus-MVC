import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This class is a controller for the program.
 */
public class Controller implements ActionListener, KeyListener {
  private Maze model;
  private GUIView view;

  /**
   * Constructor for the controller.
   *
   * @param view the view
   */
  public Controller(GUIView view) {
    this.view = view;
    view.setListener(this, this);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "Start Button":
        System.out.println("start");
        this.model = new RoomMaze(view.getRowField(),view.getColField(),view.getBatsField(),
            view.getPitsField(), view.getWallField(), view.getArrowField(), view.getPlayerField());

        System.out.println("I am here");
        break;
      case "Same Game":
        model.getVisited().clear();
        model.setIfEnded(false);
        model.setIfWin(false);
        model.getPlayer().setLocation(model.getPlayerStartCell());
        model.getVisited().add(model.getPlayerStartCell());
        model.getPlayer().setArrow(model.getNumberOfArrow());

        if (model.getNumberOfPlayer() == 2) {
          model.getPlayer2().setLocation(model.getPlayerStartCell2());
          model.getVisited().add(model.getPlayerStartCell2());
          model.getPlayer2().setArrow(model.getNumberOfArrow());
        }
        model.setFirstDead();
        model.setPlayerCount();
        break;
      case "North":
        this.model.makeMove(Direction.NORTH);
        break;
      case "East":
        this.model.makeMove(Direction.EAST);
        break;
      case "South":
        this.model.makeMove(Direction.SOUTH);
        break;
      case "West":
        this.model.makeMove(Direction.WEST);
        break;
      case "Shoot":
        this.model.shoot(view.getArrowDirection(), view.getArrowStep());
        break;
    }

    checkStatus();
    view.invalidate();
    view.validate();
    view.repaint();
    view.resetFocus();
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
  }

  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_RIGHT:
        this.model.makeMove(Direction.EAST);
        System.out.println("east");
        break;
      case KeyEvent.VK_LEFT:
        this.model.makeMove(Direction.WEST);
        System.out.println("west");
        break;
      case KeyEvent.VK_UP:
        this.model.makeMove(Direction.NORTH);
        break;
      case KeyEvent.VK_DOWN:
        this.model.makeMove(Direction.SOUTH);
        break;
    }

    checkStatus();

    view.invalidate();
    view.validate();
    view.repaint();
  }

  /**
   * Check the status of the game.
   */
  private void checkStatus() {
    if (model.getIfWin()) {
      view.win();
    } else if(model.getIfEnded()) {
      view.gameOver(model);
    } else {
      view.showGrid(model);
    }
  }

}
