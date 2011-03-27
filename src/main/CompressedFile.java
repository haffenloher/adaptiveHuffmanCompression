package main;

import java.io.Serializable;
import java.util.BitSet;

/**
 * @author Raphael Brandis
 */
public class CompressedFile implements Serializable {
    private BitSet bits;
    
    public CompressedFile(BitSet bits) {
        this.bits = bits;
    }
    
    public BitSet getBits() {
        return this.bits;
    }
    
    /*
     public void setBits(BitSet bits) {
        this.bits = bits;
    }
     */
}
