public class TrieTree {
    private class Node {
        private Integer val;
        private final Node[] next;
        
        public Node() {
            this.val = null;
            this.next = new Node[26];
        }
    }
    
    private Node root;
    
    public TrieTree() {
        root = null;
    }
    
    public void buildFromDiagram() {
        root = new Node();
        
        Node tNode = new Node();
        root.next['t' - 'a'] = tNode;
        
        Node oNode = new Node();
        tNode.next['o' - 'a'] = oNode;
        oNode.val = 7;
        
        Node eNode = new Node();
        tNode.next['e' - 'a'] = eNode;
        
        Node aNode = new Node();
        eNode.next['a' - 'a'] = aNode;
        aNode.val = 3;
        
        Node dNode = new Node();
        eNode.next['d' - 'a'] = dNode;
        dNode.val = 4;
        
        Node nNode = new Node();
        eNode.next['n' - 'a'] = nNode;
        nNode.val = 12;
        
        Node iNode = new Node();
        root.next['i' - 'a'] = iNode;
        iNode.val = 11;
        
        Node inNode = new Node();
        iNode.next['n' - 'a'] = inNode;
        inNode.val = 5;
        
        Node innNode = new Node();
        inNode.next['n' - 'a'] = innNode;
        innNode.val = 9;
    }
    
    public void printAllWords() {
        System.out.println("Words in the Trie:");
        printAllWords(root, new StringBuilder());
    }
    
    private void printAllWords(Node x, StringBuilder prefix) {
        if (x == null) return;
        
        if (x.val != null) {
            System.out.println("(" + prefix.toString() + ", " + x.val + ")");
        }
        
        for (char c = 'a'; c <= 'z'; c++) {
            int index = c - 'a';
            if (x.next[index] != null) {
                prefix.append(c);
                printAllWords(x.next[index], prefix);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }
    
    public static void main(String[] args) {
        TrieTree trie = new TrieTree();
        trie.buildFromDiagram();
        trie.printAllWords();
    }
}