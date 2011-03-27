package huffmanTreeStructure;

import java.io.PrintStream;

/**
 * @author Raphael Brandis
 */
public abstract class HuffmanNode {
    protected InternalHuffmanNode parent;
    protected int weight;
    protected int number;
    protected char value;
    protected HuffmanNode leftChild;
    protected HuffmanNode rightChild;
    
    
    public InternalHuffmanNode getParent() {
        return this.parent;
    }
    
    public void setParent(InternalHuffmanNode node) {
        this.parent = node;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public void incrementWeight() {
        this.weight++;
    }
    
    public int getNumber() {
        return this.number;
    }
        
    public void setNumber(int number) {
        this.number = number;
    }
    
    public char getValue() {
        return this.value;
    }
    
    public HuffmanNode getLeftChild() {
        return this.leftChild;
    }
    
    public HuffmanNode getRightChild() {
        return this.rightChild;
    }
    
    
    public HuffmanNode getSibling() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.getLeftChild() == this ? this.parent.getRightChild() : this.parent.getLeftChild();
    }
    
    
    public void swapWith(HuffmanNode otherNode) {
        boolean isOtherNodeLeftChild = (otherNode == otherNode.getParent().getLeftChild());
        
        if (this == this.parent.getLeftChild()) {
            this.parent.setLeftChild(otherNode);
        } else {
            this.parent.setRightChild(otherNode);
        }
        
        if (isOtherNodeLeftChild) {
            otherNode.getParent().setLeftChild(this);
        } else {
            otherNode.getParent().setRightChild(this);
        }
        
        InternalHuffmanNode myParent = this.parent;
        this.parent = otherNode.getParent();
        otherNode.setParent(myParent);
    }
    
    
    public abstract void printKeysTreeOrder(PrintStream out, String margin);
}
