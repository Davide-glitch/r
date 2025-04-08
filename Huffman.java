import java.util.PriorityQueue;

public class Huffman {
    private static final int R = 256;

    private static class HufmanNode implements Comparable<HufmanNode> {
        char ch;
        int freq;
        HufmanNode left, right;

        HufmanNode(char ch, int freq, HufmanNode left, HufmanNode right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        boolean isLeaf() {
            return (left == null) && (right == null);
        }

        @Override
        public int compareTo(HufmanNode that) {
            return this.freq - that.freq;
        }
    }

    private static class CompressionResult {
        HufmanNode huffmanCodes;
        String Bits;

        public CompressionResult(String bits, HufmanNode tree) {
            this.huffmanCodes = tree;
            this.Bits = bits;
        }
    }

    static CompressionResult encode(String s) {
        char[] input = s.toCharArray();
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;
        for (int i = 0; i < R; i++)
            if (freq[i] > 0)
                System.out.println("freq " + (char) i + " " + freq[i]);
        HufmanNode root = buildHufmanTree(freq);
        String[] st = new String[R];
        printCodeTable(st, root, "");
        StringBuilder encoded = new StringBuilder();
        for (char c : input) {
            encoded.append(st[c]);
        }
        CompressionResult result = new CompressionResult(encoded.toString(), root);
        return result;
    }

    private static HufmanNode buildHufmanTree(int[] freq) {
        PriorityQueue<HufmanNode> pq = new PriorityQueue<>();
        for (char c = 0; c < R; c++)
            if (freq[c] > 0)
                pq.add(new HufmanNode(c, freq[c], null, null));
        while (pq.size() > 1) {
            HufmanNode left = pq.remove();
            HufmanNode right = pq.remove();
            HufmanNode parent = new HufmanNode('\0', left.freq + right.freq, left, right);
            pq.add(parent);
        }
        return pq.remove();
    }

    private static void printCodeTable(String[] st, HufmanNode x, String s) {
        if (!x.isLeaf()) {
            printCodeTable(st, x.left, s + '0');
            printCodeTable(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
            System.out.println("Code of " + x.ch + " is " + s);
        }
    }

    static String decode(CompressionResult encoded) {
        StringBuilder output = new StringBuilder();
        HufmanNode root = encoded.huffmanCodes;
        HufmanNode current = root;
        for (int i = 0; i < encoded.Bits.length(); i++) {
            if (encoded.Bits.charAt(i) == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
            if (current.isLeaf()) {
                output.append(current.ch);
                current = root;
            }
        }
        return output.toString();
    }

    static boolean isCode(HufmanNode root, String bitstring) {
        HufmanNode current = root;
        for (int i = 0; i < bitstring.length(); i++) {
            if (bitstring.charAt(i) == '0') {
                current = current.left;
            } else if (bitstring.charAt(i) == '1') {
                current = current.right;
            } else {
                return false;
            }
            if (current == null) {
                return false;
            }
            if (current.isLeaf() && i < bitstring.length() - 1) {
                return false;
            }
        }
        return current.isLeaf();
    }

    static String longestCode(HufmanNode root) {
        String[] longestCodeFound = {""};
        findLongestCode(root, "", longestCodeFound);
        return longestCodeFound[0];
    }

    private static void findLongestCode(HufmanNode node, String currentCode, String[] longestCodeFound) {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            if (currentCode.length() > longestCodeFound[0].length()) {
                longestCodeFound[0] = currentCode;
            }
            return;
        }
        findLongestCode(node.left, currentCode + "0", longestCodeFound);
        findLongestCode(node.right, currentCode + "1", longestCodeFound);
    }

    static char getCharWithLongestCode(HufmanNode root) {
        String[] longestCodeFound = {""};
        char[] charWithLongestCode = {'\0'};
        findCharWithLongestCode(root, "", longestCodeFound, charWithLongestCode);
        return charWithLongestCode[0];
    }

    private static void findCharWithLongestCode(HufmanNode node, String currentCode, 
                                             String[] longestCodeFound, char[] charWithLongestCode) {
        if (node == null) {
            return;
        }
        if (node.isLeaf()) {
            if (currentCode.length() > longestCodeFound[0].length()) {
                longestCodeFound[0] = currentCode;
                charWithLongestCode[0] = node.ch;
            }
            return;
        }
        findCharWithLongestCode(node.left, currentCode + "0", longestCodeFound, charWithLongestCode);
        findCharWithLongestCode(node.right, currentCode + "1", longestCodeFound, charWithLongestCode);
    }

    public static void main(String[] args) {
        testHuffmanCoding("ABRACABABRA");
        System.out.println("\n--- Testing with 'HELLO WORLD' ---");
        testHuffmanCoding("HELLO WORLD");
        System.out.println("\n--- Testing with 'MISSISSIPPI' ---");
        testHuffmanCoding("MISSISSIPPI");
        System.out.println("\n--- Testing with a longer text ---");
        testHuffmanCoding("THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG");
    }

    private static void testHuffmanCoding(String text) {
        System.out.println("Original message to be encoded: " + text);
        CompressionResult result = encode(text);
        System.out.println("Encoded message has " + result.Bits.length() + " bits");
        System.out.println("Encoded message is: " + result.Bits);
        String longest = longestCode(result.huffmanCodes);
        System.out.println("Longest code: " + longest);
        char charWithLongest = getCharWithLongestCode(result.huffmanCodes);
        System.out.println("Character with longest code: '" + charWithLongest + "'");
        System.out.println("Is longest code valid? " + isCode(result.huffmanCodes, longest));
        System.out.println("Is '01010101' a valid code? " + isCode(result.huffmanCodes, "01010101"));
        String recovered = decode(result);
        System.out.println("Decoded message is: " + recovered);
        System.out.println("Original and recovered match? " + text.equals(recovered));
    }
}