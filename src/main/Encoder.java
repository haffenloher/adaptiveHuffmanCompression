package main;

import huffmanTreeStructure.HuffmanTree;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;

import java.util.BitSet;
import java.util.Stack;

/**
 * @author Raphael Brandis
 * @author Patrick de Lanauze
 */
public class Encoder {
    public void compressFile(String inputFilename, String outputFilename) throws IOException {
        // read the file and compress it using the FGK algorithm
        Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilename), "UTF-8"));
        CompressedFile file = this.compress(reader);
        reader.close();
        
        // serialize the CompressedFile object and write it to the given destination
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(outputFilename));
        output.writeObject(file);
        output.close();
    }
    
    // adapted from Patrick de Lanauze's HuffmanTreeWriter.write()-method for the static algorithm
    private CompressedFile compress(Reader input) throws IOException {
        HuffmanTree tree = new HuffmanTree();
        BitSet bits = new BitSet();
        
        // Iterate over the chars in the reader
        int value = input.read();
        int i = 0;
        
        while (value != -1) {
            // Parse the character
            char c = (char) value;
            Stack<Boolean> path = new Stack<Boolean>();
            
            if (tree.characterExists(c)) {
                tree.encodeCharacter(c, path);
            } else {
                // this will send the current code for the NYT node first and the UTF8-encoded character afterwards
                BitTools.encodeUTF8Character(value, path);
                tree.encodeNYTNode(path);
                // finally add the character to the tree and update the tree structure
                tree.addCharacter(c);
            }
            
            // Now we have all the results
            // Iterate over them and add to the bitset
            while (!path.isEmpty()) {
                bits.set(i++, path.pop());
            }
            
            // Prepare the next character
            value = input.read();
        }
        
        // add a last 1 to the BitSet (the Decoder ignores it). I experienced that
        // otherwise trailing 0-bits get lost in the serializing process so that
        // sometimes one character is missing at the end of the decoded file.
        bits.set(i + 1, true);
        
        return new CompressedFile(bits);
    }
}
