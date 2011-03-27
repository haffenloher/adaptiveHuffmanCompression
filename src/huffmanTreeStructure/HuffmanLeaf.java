package huffmanTreeStructure;

import java.io.PrintStream;

/**
 * This class implements the leaves used in a Huffman tree. Leaves are nodes
 * without any children; in a Huffman tree they represent characters and their
 * probability.
 * @author Raphael Brandis
 */
public class HuffmanLeaf extends HuffmanNode {
    /**
     * Constructor for the NYT node which represents every character that has
     * not occured yet and therefore has the weight 0
     */
    public HuffmanLeaf() {
        this.weight = 0;
    }
    
    /**
     * Constructor for Huffman leaves representing a single character
     * @param value the character this leaf represents
     * @param parent the parent InternalHuffmanNode
     */
    public HuffmanLeaf(char value, InternalHuffmanNode parent) {
        this.value = value;
        this.parent = parent;
        this.weight = 1;
    }
    
    /**
     * Prints a String representation of the leaf. This method is called by the leaf's
     * parent node.
     * @param out a PrintStream the String representation should be printed to
     * @param margin a String containing the indentation spaces
     */
    public void printKeysTreeOrder(PrintStream out, String margin) {
        String subMargin = "";
        if (margin.length() > 0) {
            subMargin = margin.substring(0, margin.length() - 1);
        }
        out.println(margin + "--" + this.toString());
    }
    
    /**
     * Gives back a String representation of the leaf
     * @return value, number and weight of the leaf in a String
     */
    public String toString() {
        return value + ":" + number + ":" + weight;
    }
}
