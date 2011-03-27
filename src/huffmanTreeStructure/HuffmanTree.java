package huffmanTreeStructure;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

/**
 * @author Raphael Brandis
 * @author Patrick de Lanauze
 */
public class HuffmanTree {
    private HuffmanNode root;
    
    // UTF-8 has n=1.114.112 possible characters; a Huffman tree with n leaves has n*2-1 nodes in total;
    // we have to add two nodes because of the NYT node which is also a leaf
    private final int maxNumberOfCharacters = 1114112;
    private final int rootNodeNumber = maxNumberOfCharacters * 2; // the root node gets the highest possible number
    private int lastAssignedNodeNumber = rootNodeNumber;
    private HuffmanNode[] nodes = new HuffmanNode[maxNumberOfCharacters * 2 + 1];
    
    // pointer used to go through the tree
    private HuffmanNode currentNode;
    
    private Map<Character, HuffmanLeaf> leaves = new HashMap<Character, HuffmanLeaf>();
    
    public HuffmanTree() {
        // create NYT node
        HuffmanLeaf nytNode = new HuffmanLeaf();
        this.nodes[rootNodeNumber] = nytNode;
        this.leaves.put(null, nytNode);
    }
    
    public void printKeysTreeOrder() {
        this.root.printKeysTreeOrder(System.out, "");
    }
    
    public boolean characterExists(char character) {
        return this.leaves.containsKey(character);
    }
    
    public int readHuffmanCodeBit(boolean bit) {
        int decodedCharacter = -1;
        
        if (this.currentNode == null) {
            // begin decoding a new character starting from the root node
            this.currentNode = this.root;
        }
        
        if (bit) {
            this.currentNode = this.currentNode.getRightChild();
        } else {
            this.currentNode = this.currentNode.getLeftChild();
        }
        
        if (this.currentNode instanceof HuffmanLeaf) {
            // we reached a leaf, return its number
            decodedCharacter = this.currentNode.getNumber();
            this.currentNode = null;
        }
        
        return decodedCharacter;
    }
    
    public int getNYTNodeNumber() {
        return this.lastAssignedNodeNumber;
    }
    
    public char decodeCharacterByNodeNumber(int number) {
        char decodedCharacter = this.nodes[number].getValue();
        this.incrementWeight(this.nodes[number]);
        return decodedCharacter;
    }
    
    public void addCharacter(char character) {
        HuffmanLeaf nytNode = this.leaves.get(null);
        
        // NYT node gives birth to a new internal node with the NYT node and the new character's node as children
        InternalHuffmanNode newInternalNode = new InternalHuffmanNode();
        HuffmanLeaf characterLeaf = new HuffmanLeaf(character, newInternalNode);
        
        newInternalNode.setLeftChild(nytNode);
        newInternalNode.setRightChild(characterLeaf);
        
        if (nytNode.getParent() == null) {
            // the NYT node was the root node
            this.root = newInternalNode;
        } else {
            // the NYT node is always a leftChild of its parent!
            nytNode.getParent().setLeftChild(newInternalNode);
            newInternalNode.setParent(nytNode.getParent());
        }
        
        nytNode.setParent(newInternalNode);
        
        // now write the created and modified nodes into this.nodes[] and assign the correct numbers
        this.nodes[this.lastAssignedNodeNumber] = newInternalNode;
        newInternalNode.setNumber(this.lastAssignedNodeNumber);
        
        this.nodes[--this.lastAssignedNodeNumber] = characterLeaf;
        characterLeaf.setNumber(this.lastAssignedNodeNumber);
        
        // the NYT node always has the lowest number of all nodes
        this.nodes[--this.lastAssignedNodeNumber] = nytNode;
        nytNode.setNumber(this.lastAssignedNodeNumber);
        
        this.leaves.put(character, characterLeaf);
        
        // finally update the weights of the internal nodes and reorganise the tree
        this.incrementWeight(newInternalNode);
    }
    
    public void encodeCharacter(char character, Stack<Boolean> path) {
        HuffmanLeaf leaf = this.leaves.get(character);
        this.registerPathToNode(leaf, path);
        this.incrementWeight(leaf);
    }
    
    public void encodeNYTNode(Stack<Boolean> path) {
        HuffmanNode nytNode = this.nodes[this.lastAssignedNodeNumber];
        this.registerPathToNode(nytNode, path);
    }
    
    private void incrementWeight(HuffmanNode node) {
        if (node == root) {
            // the updating process is finished.
            node.incrementWeight();
            return;
        }
        
        HuffmanNode otherNode;
        //root.printKeysTreeOrder(System.out, "");
        if (node.getSibling().getWeight() == 0) {
            otherNode = this.getHighestLeafInBlock(node);
        } else {
            otherNode = this.getHighestNodeInBlock(node);
        }
        
        if (otherNode != node) {
            node.swapWith(otherNode);
            
            int nodeNumber = node.getNumber();
            node.setNumber(otherNode.getNumber());
            this.nodes[otherNode.getNumber()] = node;
            otherNode.setNumber(nodeNumber);
            this.nodes[nodeNumber] = otherNode;
        }
        
        node.incrementWeight();
        this.incrementWeight(node.getParent());
    }
    
    // written by Patrick de Lanauze (see the implementation of the static algorithm)
    private void registerPathToNode(HuffmanNode node, Stack<Boolean> stack) {
        if (node.getParent() != null) {
            if (node == node.getParent().getLeftChild()) {
                // Then we log a false (0) since the path consists in a left branch
                stack.push(false);
            } else {
                // we log a 1 since we've taken a right branch
                stack.push(true);
            }
            
            // Call this method on the parent now
            registerPathToNode(node.getParent(), stack);
        }
    }
    
    private HuffmanNode getHighestLeafInBlock(HuffmanNode node) {
        // as this.nodes is an array of HuffmanNodes, it is necessary to use HuffmanNode
        // as the datatype here although the method gets and returns only leaves
        HuffmanNode highestLeaf = node;
        for (int i = node.getNumber() + 1; i < this.rootNodeNumber; i++) {
            if (this.nodes[i].getWeight() > node.getWeight()) {
                break;
            }
            if (this.nodes[i] instanceof HuffmanLeaf) {
                highestLeaf = this.nodes[i];
            }
        }
        return highestLeaf;
    }
    
    private HuffmanNode getHighestNodeInBlock(HuffmanNode node) {
        HuffmanNode highestNode = node;
        for (int i = node.getNumber() + 1; i < this.rootNodeNumber; i++) {
            if (this.nodes[i].getWeight() > node.getWeight()) {
                break;
            }
            highestNode = this.nodes[i];
        }
        return highestNode;
    }
}
