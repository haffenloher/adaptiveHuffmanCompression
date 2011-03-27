package main;

import java.io.Serializable;
import java.util.BitSet;

/**
 * A wrapper class for compressed files that (at the moment) simply contains the
 * BitSet with the compression result. It would also be possible to include a
 * Huffman tree to replace the empty starting tree that the algorithm uses by default.
 * @author Raphael Brandis
 */
public class CompressedFile implements Serializable {
    private BitSet bits;
    
    /**
     * Constructor for CompressedFiles
     * @param bits the file's contents compressed using the {@link Encoder}
     */
    public CompressedFile(BitSet bits) {
        this.bits = bits;
    }
    
    /**
     * Returns the BitSet containing the compressed data
     * @return the BitSet containing the compressed data
     */
    public BitSet getBits() {
        return this.bits;
    }
    
    /*
     public void setBits(BitSet bits) {
        this.bits = bits;
    }
     */
}
