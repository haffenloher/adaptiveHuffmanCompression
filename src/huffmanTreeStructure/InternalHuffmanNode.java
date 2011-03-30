package huffmanTreeStructure;

import java.io.PrintStream;

/**
 * This class implements the internal nodes used in a Huffman tree. They have two
 * {@link HuffmanNode}s as children and their weight equals the weight of both
 * children added up.
 * @author Raphael Brandis
 */
public class InternalHuffmanNode extends HuffmanNode {
    /*
    public void InternalHuffmanNode(HuffmanNode leftChild, HuffmanNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
    
    public int getWeight() {
        return this.leftChild.getWeight() + this.rightChild.getWeight();
    }
    */
    
    /**
     * Sets the node's left child.
     * @param newChild the node that should become the new left child
     */
    public void setLeftChild(HuffmanNode newChild) {
        this.leftChild = newChild;
    }
    
    /**
     * Sets the node's right child.
     * @param newChild the node that should become the new right child
     */
    public void setRightChild(HuffmanNode newChild) {
        this.rightChild = newChild;
    }
    
    /**
     * Prints a String representation of this tree, i.e. of the node itself and of its
     * children.
     * @param out a PrintStream the String representation should be printed to
     * @param margin a String containing the indentation spaces
     */
    public void printKeysTreeOrder(PrintStream out, String margin) {
        String subMargin = "";
        if (margin.length() > 0) {
            subMargin = margin.substring(0, margin.length() - 1);
        }
        this.rightChild.printKeysTreeOrder(out, subMargin + "  /");
        out.println(margin + "--" + this.toString());
        this.leftChild.printKeysTreeOrder(out, subMargin + "  \\");
    }
    
    /**
     * Gives back a String representation of the node itself (without its children)
     * @return number and weight of the node as a String
     */
    public String toString() {
        return number + ":" + weight;
    }
}
