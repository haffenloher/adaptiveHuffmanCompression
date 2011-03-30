package huffmanTreeStructure;

import java.util.Map;
import java.util.HashMap;
import java.util.Stack;

/**
 * This class manages the Huffman tree, performs multiple operations on it and
 * maintains its structure (especially the sibling property) using the FGK
 * algorithm.
 * @author Raphael Brandis
 * @author Patrick de Lanauze
 */
public class HuffmanTree {
    private HuffmanNode root;
    
    // UTF-8 has n=1.114.112 possible characters; a Huffman tree with n leaves has n*2-1 nodes in total;
    // we have to add two nodes because of the NYT node which is also a leaf
    private final int maxNumberOfCharacters = 1114112;
    private HuffmanNode[] nodes = new HuffmanNode[maxNumberOfCharacters * 2 + 1];
    private Map<Character, HuffmanLeaf> leaves = new HashMap<Character, HuffmanLeaf>();
    private int lastAssignedNodeNumber;
    
    // pointer used to go through the tree
    private HuffmanNode currentNode;
    
    /**
     * This constructor creates an (almost) empty Huffman tree with a NYT node as the
     * root.
     */
    public HuffmanTree() {
        // create NYT node
        HuffmanLeaf nytNode = new HuffmanLeaf();
        // the root node gets the highest possible number
        this.nodes[maxNumberOfCharacters * 2] = nytNode;
        lastAssignedNodeNumber = maxNumberOfCharacters * 2;
        this.leaves.put(null, nytNode);
    }
    
    /**
     * Checks if the given character is already in the tree.
     * @param character the character to search for
     * @return true if the character was found, otherwise false
     */
    public boolean characterExists(char character) {
        return this.leaves.containsKey(character);
    }
    
    /**
     * Adds a character to the tree and uses the FGK algorithm to reorganize the tree
     * @param character the character that should be added to the tree (its weight will be 1)
     */
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
        
        // finally update the weights of the internal nodes and reorganize the tree
        this.incrementWeight(newInternalNode);
    }
    
    /**
     * Takes in a character, computes its Huffman code, increments the weight of the
     * character's leaf and recomputes the tree using the FGK algorithm.
     * @param character the character to encode
     * @param path a stack the path to the character should be pushed on
     */
    public void encodeCharacter(char character, Stack<Boolean> path) {
        HuffmanLeaf leaf = this.leaves.get(character);
        this.registerPathToNode(leaf, path);
        this.incrementWeight(leaf);
    }
    
    /**
     * Gets the path from the root to the NYT node.
     * @param path a stack the path to the NYT node should be pushed on
     */
    public void encodeNYTNode(Stack<Boolean> path) {
        HuffmanNode nytNode = this.nodes[this.lastAssignedNodeNumber];
        this.registerPathToNode(nytNode, path);
    }
    
    /**
     * Reads one bit and moves the internal node pointer to the left (if the given bit
     * is 0 / false) or to the right child (if the bit is 1 / true) of the node the
     * pointer currently points at. If it reaches a leaf, its node number is returned
     * and the pointer is resetted.
     * @param bit one Huffman code bit in boolean form
     * @return if a leaf is reached: its number, if not: -1
     */
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
    
    /**
     * Returns the node number of the NYT node.
     * @return the NYT node's number
     */
    public int getNYTNodeNumber() {
        return this.lastAssignedNodeNumber;
    }
    
    /**
     * Takes in a node number, returns the corresponding character, increments the
     * weight of the character's leaf and recomputes the tree using the FGK algorithm.
     * @param number a node number (pointing to a leaf, usually returned by {@link readHuffmanCodeBit(boolean)})
     * @return the decoded character
     */
    public char decodeCharacterByNodeNumber(int number) {
        char decodedCharacter = this.nodes[number].getValue();
        this.incrementWeight(this.nodes[number]);
        return decodedCharacter;
    }
    
    /**
     * Prints a String representation of the tree to the console output.
     */
    public void printKeysTreeOrder() {
        this.root.printKeysTreeOrder(System.out, "");
    }
    
    // increments the weight of the given node by 1 and updates the tree
    private void incrementWeight(HuffmanNode node) {
        if (node == root) {
            // the updating process is finished
            node.incrementWeight();
            return;
        }
        
        // reorganize the tree according to the FGK algorithm before actually
        // incrementing the weight of the given node
        HuffmanNode otherNode;
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
        
        // increment the node's weight by 1 and do the same process with its parent node
        node.incrementWeight();
        this.incrementWeight(node.getParent());
    }
    
    // written by Patrick de Lanauze (see his implementation of the static algorithm)
    // pushes the path to the given node onto the given stack
    private void registerPathToNode(HuffmanNode node, Stack<Boolean> path) {
        if (node.getParent() != null) {
            if (node == node.getParent().getLeftChild()) {
                // Then we log a false (0) since the path consists in a left branch
                path.push(false);
            } else {
                // we log a 1 since we've taken a right branch
                path.push(true);
            }
            
            // Call this method on the parent now
            registerPathToNode(node.getParent(), path);
        }
    }
    
    // returns the leaf with the highest number and the same weight as the given node
    private HuffmanNode getHighestLeafInBlock(HuffmanNode node) {
        // as this.nodes is an array of HuffmanNodes, it is necessary to use HuffmanNode
        // as the datatype here although the method gets and returns only leaves
        HuffmanNode highestLeaf = node;
        for (int i = node.getNumber() + 1; i < this.root.getNumber(); i++) {
            if (this.nodes[i].getWeight() > node.getWeight()) {
                break;
            }
            if (this.nodes[i] instanceof HuffmanLeaf) {
                highestLeaf = this.nodes[i];
            }
        }
        return highestLeaf;
    }
    
    // returns the node with the highest number and the same weight as the given node
    private HuffmanNode getHighestNodeInBlock(HuffmanNode node) {
        HuffmanNode highestNode = node;
        for (int i = node.getNumber() + 1; i < this.root.getNumber(); i++) {
            if (this.nodes[i].getWeight() > node.getWeight()) {
                break;
            }
            highestNode = this.nodes[i];
        }
        return highestNode;
    }
}
