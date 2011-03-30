package main;

import huffmanTreeStructure.HuffmanTree;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;

import java.util.BitSet;
import java.util.Stack;

/**
 * This class decompresses files previosly compressed by the {@link Encoder}.
 * @author Raphael Brandis
 */
public class Decoder {
    /**
     * Takes in two filenames / -paths, reads the first file, decompresses it and
     * writes the result to the second file.
     * @param inputFilename path to a file that was previously created using the {@link Encoder} class
     * @param outputFilename the path the decompressed file should be written to
     * @throws java.io.IOException 
     * @throws java.lang.ClassNotFoundException 
     */
    public void decompressFile(String inputFilename, String outputFilename) throws IOException, ClassNotFoundException {
        // read the file and deserialize the CompressedFile object
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(inputFilename));
        CompressedFile compressedFile = (CompressedFile) inputStream.readObject();
        inputStream.close();
        
        // decompress the file and write the original UTF-8 data to the given destination
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilename), "UTF-8"));
        this.decompress(compressedFile, writer);
        writer.close();
    }
    
    private void decompress(CompressedFile input, Writer writer) throws IOException {
        BitSet bits = input.getBits();
        HuffmanTree tree = new HuffmanTree();
        Stack<Boolean> newChar = new Stack<Boolean>();
        boolean bit;
        char decodedCharacter;
        int i, j, bitsToRead = 0;
        
        
        // read the first UTF-8 character
        //determine if it is 1, 2, 3 or 4 bytes long at first.
        for (i = 0; i < 5; i++) {
            bit = bits.get(i);
            newChar.push(bit);
            if (!bit) {
                break;
            }
        }
        
        switch (i) {
            case 4:
                bitsToRead = 32;
                break;
            case 3:
                bitsToRead = 24;
                break;
            case 2:
                bitsToRead = 16;
                break;
            default:
                bitsToRead = 8;
                break;
        }
        
        // read the remaining bits and push them onto the stack
        for (i = i+1; i < bitsToRead; i++) {
            newChar.push(bits.get(i));
        }
        
        // convert the stack to a UTF-8 character, add it to the tree and write it to the file
        decodedCharacter = (char) BitTools.decodeUTF8Character(newChar);
        tree.addCharacter(decodedCharacter);
        writer.write(decodedCharacter);
        
        
        // now continue with the rest of the data and ignore the very last bit (see Encoder.compress())
        for (i = i; i < bits.length()-1; i++) {
            bit = bits.get(i);
            int nodeNumber = tree.readHuffmanCodeBit(bit);
            
            if (nodeNumber > -1) {
                if (nodeNumber == tree.getNYTNodeNumber()) {
                    // here comes a new UTF-8 character, let's read it!
                    newChar = new Stack<Boolean>();
                    // determine if it is 1, 2, 3 or 4 bytes long at first.
                    for (j = 0; j < 5; j++) {
                        bit = bits.get(++i);
                        newChar.push(bit);
                        if (!bit) {
                            break;
                        }
                    }
                    
                    switch (j) {
                        case 4:
                            bitsToRead = 32;
                            break;
                        case 3:
                            bitsToRead = 24;
                            break;
                        case 2:
                            bitsToRead = 16;
                            break;
                        default:
                            bitsToRead = 8;
                            break;
                    }
                    
                    // read the remaining bits and push them onto the stack
                    for (j = j + 1; j < bitsToRead; j++) {
                        newChar.push(bits.get(++i));
                    }
                    
                    // convert the stack to a UTF-8 character and add it to the tree
                    decodedCharacter = (char) BitTools.decodeUTF8Character(newChar);
                    tree.addCharacter(decodedCharacter);
                } else {
                    decodedCharacter = tree.decodeCharacterByNodeNumber(nodeNumber);
                }
                
                // write the decoded character to the file
                writer.write(decodedCharacter);
            }
        }
    }
}
