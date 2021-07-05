package binary_tree;

public class BinaryTree {

  private Node root;
  private int count;

  public void addNode(String data) {
    if (getRoot() == null) {
      root = new Node(data);
    } else {
      addTo(root, data);
    }
    count++;
  }

  public void addTo(Node node, String data) {
    if (!isContains(data)) {
      if (data.compareTo(node.getData()) < 0) {
        if (node.getLeft() == null) {
          node.setLeft(new Node(data));
        } else {
          addTo(node.getLeft(), data);
        }
      } else {
        if (node.getRight() == null) {
          node.setRight(new Node(data));
        } else {
          addTo(node.getRight(), data);
        }
      }
    }
  }

  public boolean isContains(String data) {
    return findElement(data) != null;
  }

  public Node findElement(String data) {
    Node current = root;
    Node parent = null;
    while (current != null) {
      int result = current.compareTo(data);
      if (result > 0) {
        parent = current;
        current = current.getLeft();
      } else if (result < 0) {
        parent = current;
        current = current.getRight();
      } else {
        break;
      }
    }
    return current;
  }


  public Node getRoot() {
    return root;
  }
}
