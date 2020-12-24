/**
 * This is a tree structure to model the "set" of each tree
 * that is used in Kruskal to build the graph.
 */
public class Tree {
  private Tree parent;

  public Tree() {
    this.parent = null;
  }

  /**
   * Get the root of a tree.
   *
   * @return the root
   */
  public Tree root() {
    return parent != null ? parent.root() : this;
  }

  /**
   * Get if a tree has been connected to another tree.
   *
   * @param tree the tree
   *
   * @return true or false
   */
  public boolean isConnected(Tree tree) {
    return this.root() == tree.root();
  }

  /**
   * Connect a tree to another tree.
   *
   * @param tree a tree
   */
  public void connect(Tree tree) {
    tree.root().setParent(this);
  }

  /**
   * Set the parent of a tree.
   *
   * @param parent a tree node
   */
  public void setParent(Tree parent) {
    this.parent = parent;
  }
}
