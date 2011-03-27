package huffmanTreeStructure;

import java.io.PrintStream;

/**
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
    
    public void setLeftChild(HuffmanNode node) {
        this.leftChild = node;
    }
    
    public void setRightChild(HuffmanNode node) {
        this.rightChild = node;
    }
    
    public void printKeysTreeOrder(PrintStream out, String margin) {
        String subMargin = "";
        if (margin.length() > 0) {
            subMargin = margin.substring(0, margin.length() - 1);
        }
        this.rightChild.printKeysTreeOrder(out, subMargin + "  /");
        out.println(margin + "--" + this.toString());
        this.leftChild.printKeysTreeOrder(out, subMargin + "  \\");
    }
    
    public String toString() {
        return number + ":" + weight;
    }
}
