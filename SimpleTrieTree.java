import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimpleTrieTree {
    private final int R = 256; // R - size of alphabet.
    private TrieNode root;

    private class TrieNode {
        private Integer val;
        private final TrieNode[] next = new TrieNode[R];
    }

    public SimpleTrieTree() {
        root = null;
    }

    public void put(String key, Integer val) {
        if ((key == null) || (val == null)) throw new IllegalArgumentException("key or val argument is null");
        else root = put(root, key, val);
    }

    private TrieNode put(TrieNode x, String key, Integer val) {
        if (x == null) x = new TrieNode();
        if (key.equals("")) {
            x.val = val;
            return x;
        }
        char c = key.charAt(0);
        String restkey = "";
        if (key.length() > 1) restkey = key.substring(1);
        x.next[c] = put(x.next[c], restkey, val);
        return x;
    }

    public void put_v2(String key, Integer val) {
        if (root == null) {
            root = new TrieNode();
        }
        TrieNode node = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (node.next[c] == null) {
                node.next[c] = new TrieNode();
            }
            node = node.next[c];
        }
        node.val = val;
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("key argument is null");
        TrieNode x = get(root, key);
        return x != null && x.val != null;
    }

    private TrieNode get(TrieNode x, String key) {
        if (x == null) return null;
        if (key.equals("")) return x;
        char c = key.charAt(0);
        String restKey = "";
        if (key.length() > 1) restKey = key.substring(1);
        return get(x.next[c], restKey);
    }

    public void printAllKeys() {
        collect(root, new StringBuilder(), new ArrayList<String>()).forEach(System.out::println);
    }

    public void printAllWithPrefix(String prefix) {
        TrieNode x = get(root, prefix);
        if (x == null) return;
        collectWithPrefix(x, new StringBuilder(prefix), new ArrayList<String>()).forEach(System.out::println);
    }

    public String findFirstWord() {
        if (root == null) return null;
        return findFirstWord(root, new StringBuilder());
    }

    private String findFirstWord(TrieNode x, StringBuilder prefix) {
        if (x.val != null) return prefix.toString();
        for (int i = 0; i < R; i++) {
            if (x.next[i] != null) {
                prefix.append((char) i);
                return findFirstWord(x.next[i], prefix);
            }
        }
        return null;
    }

    public String findLastWord() {
        if (root == null) return null;
        return findLastWord(root, new StringBuilder());
    }

    private String findLastWord(TrieNode x, StringBuilder prefix) {
        String lastWord = null;
        for (int i = R - 1; i >= 0; i--) {
            if (x.next[i] != null) {
                prefix.append((char) i);
                String word = findLastWord(x.next[i], prefix);
                if (word != null) return word;
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        if (x.val != null) return prefix.toString();
        return null;
    }

    public String findLongestWord() {
        if (root == null) return null;
        ArrayList<String> words = collect(root, new StringBuilder(), new ArrayList<String>());
        if (words.isEmpty()) return null;
        
        String longest = words.get(0);
        for (String word : words) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }
        return longest;
    }

    public String findShortestWord() {
        if (root == null) return null;
        ArrayList<String> words = collect(root, new StringBuilder(), new ArrayList<String>());
        if (words.isEmpty()) return null;
        
        String shortest = words.get(0);
        for (String word : words) {
            if (word.length() < shortest.length()) {
                shortest = word;
            }
        }
        return shortest;
    }

    public Iterable<String> getAllKeys() {
        return collect(root, new StringBuilder(), new ArrayList<String>());
    }

    private ArrayList<String> collect(TrieNode x, StringBuilder prefix, ArrayList<String> results) {
        if (x == null) return results;
        if (x.val != null) results.add(prefix.toString());
        for (char c = 0; c < R; c++) {
            if (x.next[c] != null) {
                prefix.append(c);
                collect(x.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        return results;
    }

    public Iterable<String> getAllKeysWithPrefix(String prefix) {
        TrieNode x = get(root, prefix);
        if (x == null) return new ArrayList<String>();
        return collectWithPrefix(x, new StringBuilder(prefix), new ArrayList<String>());
    }

    private ArrayList<String> collectWithPrefix(TrieNode x, StringBuilder prefix, ArrayList<String> results) {
        if (x == null) return results;
        if (x.val != null) results.add(prefix.toString());
        for (char c = 0; c < R; c++) {
            if (x.next[c] != null) {
                prefix.append(c);
                collectWithPrefix(x.next[c], prefix, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        return results;
    }

    public int countAllKeysWithPrefix(String prefix) {
        TrieNode x = get(root, prefix);
        if (x == null) return 0;
        return countKeysFromNode(x);
    }

    private int countKeysFromNode(TrieNode x) {
        if (x == null) return 0;
        int count = 0;
        if (x.val != null) count++;
        
        for (char c = 0; c < R; c++) {
            if (x.next[c] != null) {
                count += countKeysFromNode(x.next[c]);
            }
        }
        return count;
    }

    public void removeWord(String key) {
        if (key == null) throw new IllegalArgumentException("key argument is null");
        root = removeWord(root, key, 0);
    }

    private TrieNode removeWord(TrieNode x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            x.val = null;
        } else {
            char c = key.charAt(d);
            x.next[c] = removeWord(x.next[c], key, d + 1);
        }
        
        if (x.val != null) return x;
        for (int c = 0; c < R; c++) {
            if (x.next[c] != null) return x;
        }
        return null;
    }

    public void initFromFile(String filename) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    put(line, i++);
                }
            }
            reader.close();
            System.out.println("Successfully loaded " + i + " words from " + filename);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        
        System.out.println("\n1. Loading words from random_words.txt:");
        SimpleTrieTree fileTrie = new SimpleTrieTree();
        fileTrie.initFromFile("random_words.txt");
        
        System.out.println("\n2. Demonstrating 'contains' method with file data:");

        System.out.println("Contains 'apple': " + fileTrie.contains("apple"));
        System.out.println("Contains 'banana': " + fileTrie.contains("banana"));
        System.out.println("Contains 'nonexistentword': " + fileTrie.contains("nonexistentword"));
        

        System.out.println("\n3. Demonstrating 'printAllKeys' method with file data:");
        System.out.println("First 10 keys in ascending order from file:");
        int count = 0;
        for (String key : fileTrie.getAllKeys()) {
            System.out.println(key);
            count++;
            if (count >= 10) {
                System.out.println("... (showing only first 10 words)");
                break;
            }
        }
        
        String testPrefix = "ban";
        System.out.println("\n4. Demonstrating 'printAllWithPrefix' method with prefix '" + testPrefix + "' from file:");
        fileTrie.printAllWithPrefix(testPrefix);
        
        System.out.println("\n5. Demonstrating 'findFirstWord' method with file data:");
        System.out.println("First word in dictionary from file: " + fileTrie.findFirstWord());
        
        System.out.println("\n6. Demonstrating 'findLastWord' method with file data:");
        System.out.println("Last word in dictionary from file: " + fileTrie.findLastWord());
        
        System.out.println("\n7. Demonstrating 'findLongestWord' method with file data:");
        System.out.println("Longest word from file: " + fileTrie.findLongestWord());
        
        System.out.println("\n8. Demonstrating 'findShortestWord' method with file data:");
        System.out.println("Shortest word from file: " + fileTrie.findShortestWord());
        
        System.out.println("\n9. Demonstrating 'getAllKeys' method with file data:");
        System.out.println("First 10 keys as an Iterable from file:");
        count = 0;
        for (String key : fileTrie.getAllKeys()) {
            System.out.println(key);
            count++;
            if (count >= 10) {
                System.out.println("... (showing only first 10 words)");
                break;
            }
        }
        
        System.out.println("\n10. Demonstrating 'getAllKeysWithPrefix' method with prefix '" + testPrefix + "' from file:");
        for (String key : fileTrie.getAllKeysWithPrefix(testPrefix)) {
            System.out.println(key);
        }
        
        System.out.println("\n11. Demonstrating 'countAllKeysWithPrefix' method with prefix '" + testPrefix + "' from file:");
        System.out.println("Count: " + fileTrie.countAllKeysWithPrefix(testPrefix));
        
        System.out.println("\n12. Demonstrating 'removeWord' method with file data:");
        String wordToRemove = fileTrie.findFirstWord(); 
        if (wordToRemove != null) {
            System.out.println("Before removing '" + wordToRemove + "', contains: " + fileTrie.contains(wordToRemove));
            fileTrie.removeWord(wordToRemove);
            System.out.println("After removing '" + wordToRemove + "', contains: " + fileTrie.contains(wordToRemove));
        } else {
            System.out.println("No words available to remove.");
        }
        
        System.out.println("\nQUESTIONS ANSWERED:");
        System.out.println("1. First word in ascending (alphabetical) order: " + fileTrie.findFirstWord());
        System.out.println("2. Last word in ascending (alphabetical) order: " + fileTrie.findLastWord());
        System.out.println("3. Longest word: " + fileTrie.findLongestWord());
        System.out.println("4. Number of words starting with prefix 'ban': " + fileTrie.countAllKeysWithPrefix("ban"));
    
    }
}