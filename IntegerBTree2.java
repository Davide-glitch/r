import java.util.LinkedList;
import java.util.Queue;

interface Btree {
    void constructTreeFromFigure();
    void displayLevels();
}

public class IntegerBTree2 implements Btree {

    private final int T;

    class BTreeNode {
        int n;
        Integer key[] = new Integer[2 * T - 1];
        BTreeNode child[] = new BTreeNode[2 * T];
        boolean leaf = true;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(" [ ");
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    sb.append(" "); 
                }
                sb.append(key[i]);
            }
            sb.append(" ] ");
            return sb.toString();
        }
    }

    private BTreeNode root;

    public IntegerBTree2(int t) {
        T = t;
        root = null;
    }

    @Override
    public void constructTreeFromFigure() {
        root = new BTreeNode();
        root.n = 5;
        root.leaf = false;
        root.key[0] = 5;
        root.key[1] = 12;
        root.key[2] = 16;
        root.key[3] = 23;
        root.key[4] = 30;

        BTreeNode child1 = new BTreeNode();
        child1.n = 3;
        child1.leaf = true;
        child1.key[0] = 1;
        child1.key[1] = 2;
        child1.key[2] = 3;

        BTreeNode child2 = new BTreeNode();
        child2.n = 5;
        child2.leaf = true;
        child2.key[0] = 6;
        child2.key[1] = 7;
        child2.key[2] = 9;
        child2.key[3] = 10;
        child2.key[4] = 11;

        BTreeNode child3 = new BTreeNode();
        child3.n = 2;
        child3.leaf = true;
        child3.key[0] = 13;
        child3.key[1] = 14;

        BTreeNode child4 = new BTreeNode();
        child4.n = 3;
        child4.leaf = true;
        child4.key[0] = 18;
        child4.key[1] = 19;
        child4.key[2] = 20;

        BTreeNode child5 = new BTreeNode();
        child5.n = 2;
        child5.leaf = true;
        child5.key[0] = 24;
        child5.key[1] = 26;

        BTreeNode child6 = new BTreeNode();
        child6.n = 2;
        child6.leaf = true;
        child6.key[0] = 32;
        child6.key[1] = 36;

        root.child[0] = child1;
        root.child[1] = child2;
        root.child[2] = child3;
        root.child[3] = child4;
        root.child[4] = child5;
        root.child[5] = child6;
    }

    private class QueuePair {
        BTreeNode node;
        int level;

        QueuePair(BTreeNode node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    @Override
    public void displayLevels() {
        if (root == null) {
            System.out.println("Empty tree");
            return;
        }

        Queue<QueuePair> q = new LinkedList<>();
        System.out.println("B Tree displayed on levels: ");
        BTreeNode x = root;
        int oldLevel = 0;
        int level;

        q.add(new QueuePair(x, oldLevel));

        while (!q.isEmpty()) {
            QueuePair p = q.remove();
            x = p.node;
            level = p.level;

            if (level > oldLevel) {
                System.out.println();
                oldLevel = level;
            }
            System.out.print(x.toString());
            if (!x.leaf) {
                for (int i = 0; i <= x.n; i++) {
                    BTreeNode y = (x.child)[i];
                    q.add(new QueuePair(y, level + 1));
                }
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        IntegerBTree2 btree = new IntegerBTree2(3);
        btree.constructTreeFromFigure();
        System.out.println("Constructed B-Tree of mindegree t=3 as shown in the figure:");
        btree.displayLevels();
    }
}