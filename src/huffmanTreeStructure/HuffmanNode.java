package huffmanTreeStructure;

import java.io.PrintStream;

/**
 * Abstract class for Huffman nodes that already implements the tree's structure
 * and most of the methods.
 * @author Raphael Brandis
 */
public abstract class HuffmanNode {
    protected InternalHuffmanNode parent;
    protected int weight;
    protected int number;
    protected char value;
    protected HuffmanNode leftChild;
    protected HuffmanNode rightChild;
    
    
    /**
     * Returns the node's parent.
     * @return the parent node
     */
    public InternalHuffmanNode getParent() {
        return this.parent;
    }
    
    /**
     * Sets the node's parent.
     * @param newParent the new parent node
     */
    public void setParent(InternalHuffmanNode newParent) {
        this.parent = newParent;
    }
    
    /**
     * Returns the node's weight, i.e. the weights of both of its children added up or
     * the probability of its character if it is a leaf node.
     * @return the node's weight
     */
    public int getWeight() {
        return this.weight;
    }
    
    /**
     * Increments the node's weight.
     * This method does _not_ increment the parent's weight!
     */
    public void incrementWeight() {
        this.weight++;
    }
    
    /**
     * Returns the node's number according to the numbering conventions defined by
     * Vitter.
     * @return the node's number
     */
    public int getNumber() {
        return this.number;
    }
        
    /**
     * Sets the node's number.
     * @param number the new number
     */
    public void setNumber(int number) {
        this.number = number;
    }
    
    /**
     * Returns the node's value, i.e. the character it represents.
     * @return the node's character
     */
    public char getValue() {
        return this.value;
    }
    
    /**
     * Returns the node's left child.
     * @return the node's left child
     */
    public HuffmanNode getLeftChild() {
        return this.leftChild;
    }
    
    /**
     * Returns the node's right child.
     * @return the node's right child
     */
    public HuffmanNode getRightChild() {
        return this.rightChild;
    }
    
    
    /**
     * Gets this node's sibling node, i.e. the other child of the node's parent.
     * @return the sibling node (or null if the node does not have a parent node)
     */
    public HuffmanNode getSibling() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.getLeftChild() == this ? this.parent.getRightChild() : this.parent.getLeftChild();
    }
    
    
    /**
     * Swaps this node with another node including their subtrees.
     * @param otherNode the node this node should be swapped with
     */
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
    
    
    /**
     * Prints a String representation of this tree, i.e. of the node itself and of its
     * children.
     * @param out a PrintStream the String representation should be printed to
     * @param margin a String containing the indentation spaces
     */
    public abstract void printKeysTreeOrder(PrintStream out, String margin);
}
