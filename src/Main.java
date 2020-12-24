/**
 * This is a main class for the program.
 */
public class Main {
  public static void main(String[] args) throws IllegalArgumentException {
      GUIView view = new GUIView("Hunt the Wumpus!");
      view.setVisible(true);

      Controller control = new Controller(view);
  }

}
