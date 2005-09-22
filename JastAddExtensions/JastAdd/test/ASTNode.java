package test;

public class ASTNode extends beaver.Symbol  implements Cloneable {
  public ASTNode() {
    super();
  }

  static public boolean IN_CIRCLE = false;
  static public boolean CHANGE = false;
  static public boolean LAST_CYCLE = false;

  public static int boundariesCrossed = 0;

  static class State {
    private int[] stack;
    private int pos;
    public State() {
      stack = new int[64];
      pos = 0;
    }
    private void ensureSize(int size) {
      if(size < stack.length)
        return;
      int[] newStack = new int[stack.length * 2];
      System.arraycopy(stack, 0, newStack, 0, stack.length);
      stack = newStack;
    }
    public void push(int i) {
      ensureSize(pos+1);
      stack[pos++] = i;
    }
    public int pop() {
      return stack[--pos];
    }
    public int peek() {
      return stack[pos-1];
    }
  }
  protected static State state = new State();
  public boolean inCircle = false;
  public boolean isFinal = false;
  protected static final int REWRITE_CHANGE = 1;
  protected static final int REWRITE_NOCHANGE = 2;
  protected static final int REWRITE_INTERRUPT = 3;

  public ASTNode getChild(int i) {
    ASTNode node = getChildNoTransform(i);
    if(node.isFinal) return node;
    if(!node.mayHaveRewrite()) {
      node.isFinal = isFinal;
      return node;
    }
    if(!node.inCircle) {
      int rewriteState;
      do {
        state.push(ASTNode.REWRITE_CHANGE);
        ASTNode oldNode = node;
        oldNode.inCircle = true;
        node = node.rewriteTo();
        oldNode.inCircle = false;
        setChild(node, i);
        rewriteState = state.pop();
      } while(rewriteState == ASTNode.REWRITE_CHANGE);
      if(rewriteState == ASTNode.REWRITE_NOCHANGE) node.isFinal = isFinal;
    }
    else if(isFinal != node.isFinal) boundariesCrossed++;
    return node;
  }

  private int childIndex;
  public int getIndexOfChild(ASTNode node) {
    if(node.childIndex < getNumChild() && node == getChildNoTransform(node.childIndex))
      return node.childIndex;
    for(int i = 0; i < getNumChild(); i++)
      if(getChildNoTransform(i) == node) {
        node.childIndex = i;
        return i;
      }
    return -1;
  }

  public void addChild(ASTNode node) {
    setChild(node, getNumChild());
  }
  public ASTNode getChildNoTransform(int i) {
    return children[i];
  }
  protected ASTNode parent;
  protected ASTNode[] children;
  protected int numChildren;
  public int getNumChild() {
    return numChildren;
  }
  public void setChild(ASTNode node, int i) {
    if(children == null) {
      children = new ASTNode[i + 1];
    } else if (i >= children.length) {
      ASTNode c[] = new ASTNode[i << 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = node;
    if(i >= numChildren) numChildren = i+1;
    if(node != null) { node.setParent(this); node.childIndex = i; }
  }
  public void insertChild(ASTNode node, int i) {
    if(i > numChildren)
      throw new Error("insertChild error: can not insert child at position outside list of elements");
    if(children == null) {
      children = new ASTNode[i + 1];
      children[i] = node;
    } else {
      ASTNode c[] = new ASTNode[children.length + 1];
      System.arraycopy(children, 0, c, 0, i);
      c[i] = node;
      if(i < children.length)
        System.arraycopy(children, i, c, i+1, children.length-i);
      children = c;
    }
    numChildren++;
    if(node != null) { node.setParent(this); node.childIndex = i; }
  }

  public ASTNode insertList(List newList, int i) {
    // insert list newlist at position i and return first element in newlist
    setChild(newList.getChildNoTransform(0), i);
    for(int j = 1; j < newList.getNumChild(); j++)
      insertChild(newList.getChildNoTransform(j), ++i);
    return newList.getChildNoTransform(0);
  }
  
  public ASTNode getParent() {
    if(parent != null && parent.isFinal != isFinal) {
      boundariesCrossed++;
    }
    return parent;
  }
  public void setParent(ASTNode node) {
    parent = node;
  }
  public ASTNode rewriteTo() {
    if(state.peek() == ASTNode.REWRITE_CHANGE) {
      state.pop();
      state.push(ASTNode.REWRITE_NOCHANGE);
    }
    return this;
  }

  public boolean mayHaveRewrite() {
    return false;
  }
  
  public ASTNode copy() {
    try {
      ASTNode node = (ASTNode)clone();
      if(children != null) node.children = (ASTNode[])children.clone();
      return node;
    } catch (CloneNotSupportedException e) {
    }
    System.err.println("Error: Could not clone node of type " + getClass().getName() + "!");
    return null;
  }
  
  public ASTNode fullCopy() {
    ASTNode res = copy();
    for(int i = 0; i < getNumChild(); i++) {
      ASTNode node = getChildNoTransform(i);
      if(node != null) node = node.fullCopy();
      res.setChild(node, i);
    }
    return res;
  }


}
