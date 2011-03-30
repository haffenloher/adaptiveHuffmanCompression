package main;

import java.io.IOException;
import java.io.File;
import java.util.BitSet;

/**
 * This project implements the compression and decompression of UTF-8-encoded files
 * through adaptive Huffman coding using the FGK-algorithm defined by Faller,
 * Gallager and Knuth to maintain the coding tree.
 * @author Raphael Brandis
 * @author Patrick de Lanauze
 */
public class Main {
    /**
     * You may compress and decompress files using the following command:
     * java -jar adaptiveHuffmanCoding.jar [compress|decompress] [inputFilename] [outputFilename]
     * @param args The first argument has to be either "compress" or "decompress", the second is
     * the file that should be read and the third is the file the en- or decoding
     * result should be written to.
     * @throws java.io.IOException 
     * @throws java.lang.ClassNotFoundException 
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length < 3) {
            printUsage();
        } else {
            long start = System.currentTimeMillis();
            
            if (args[0].equals("compress")) {
                for (int i = 0; i < 10; i++) {
                    Encoder encoder = new Encoder();
                    encoder.compressFile(args[1], args[2]);
                }
            } else if (args[0].equals("decompress")) {
                for (int i = 0; i < 10; i++) {
                    Decoder decoder = new Decoder();
                    decoder.decompressFile(args[1], args[2]);
                }
            } else {
                System.err.println("Unrecognized option: " + args[0]);
                printUsage();
            }
            
            long end = System.currentTimeMillis();
            File in = new File(args[1]);
            File out = new File(args[2]);
            long inBytes = in.length();
            long outBytes = out.length();
            
            System.out.println("Process took " + (end - start) + "ms to complete.");
            System.out.println("Compression ratio: " + ((double) outBytes / (double) inBytes));
        }
    }
    
    private static void printUsage() {
        System.out.println("Adaptive Huffman compressor usage:");
        System.out.println("To compress:");
        System.out.println("./java -jar adaptiveHuffmanCoding.jar compress input.txt compressed.bin");
        System.out.println("To decompress:");
        System.out.println("./java -jar adaptiveHuffmanCoding.jar decompress compressed.bin output.txt");
    }
}
