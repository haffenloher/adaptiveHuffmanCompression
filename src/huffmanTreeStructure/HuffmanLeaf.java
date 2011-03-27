package huffmanTreeStructure;

import java.io.PrintStream;

/**
 * @author Raphael Brandis
 */
public class HuffmanLeaf extends HuffmanNode {
    public HuffmanLeaf() {
        // this is the NYT-Node; it represents every character that has not occured yet
        this.weight = 0;
    }
    
    public HuffmanLeaf(char value, InternalHuffmanNode parent) {
        this.value = value;
        this.parent = parent;
        this.weight = 1;
    }
    
    public void printKeysTreeOrder(PrintStream out, String margin) {
        String subMargin = "";
        if (margin.length() > 0) {
            subMargin = margin.substring(0, margin.length() - 1);
        }
        out.println(margin + "--" + this.toString());
    }
    
    public String toString() {
        return value + ":" + number + ":" + weight;
    }
}
