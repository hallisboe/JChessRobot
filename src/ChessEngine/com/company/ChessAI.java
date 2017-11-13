package ChessEngine.com.company;

import ChessEngine.com.company.board.Possible;
import ChessEngine.com.company.board.Value;
import ChessEngine.com.company.lookup.Pieces;

import java.util.*;
import java.util.concurrent.*;
import static ChessEngine.com.company.Save.*;
import static ChessEngine.com.company.MinMax.*;


public class ChessAI {

    static ExecutorService ES;

    static Queue<Node> toExpand;
    static byte[][] BOARD;
    static boolean[] DATA = {true, true, true, true, true};

    static char[] chars = new char[] { '/', '-', '\\', '/', '-', '\\'};

    static Node parent;

    public byte[][] getBoard(){
        return this.BOARD;
    }

    public void setBoard(byte[][] b,boolean qs, boolean ks){
        this.BOARD = b;
        this.DATA[0] = !DATA[0];
        this.DATA[1] = qs;
        this.DATA[2] = ks;
    }

    public static void iterate() throws InterruptedException {
        parent = new Node(BOARD, DATA);
        ES = Executors.newFixedThreadPool(8);
        toExpand = new LinkedBlockingQueue();
        toExpand.add(parent);
        long time = System.currentTimeMillis();
        int i = 0;
        while (i < 15000) {
            while (toExpand.size() == 0) Thread.sleep(10);
            ES.execute(new Expander(toExpand.poll()));
            i++;
        }
        System.out.println(i + " operations started. Waiting for completion... ");
        ES.shutdown();
        System.out.print("/");
        while (!ES.awaitTermination(10, TimeUnit.MILLISECONDS)) {
            System.out.print("\r");
            System.out.print(chars[i++ % 3]);
        }
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("RESULT:");
        System.out.println("------------------------------------------------------------");
        System.out.println("Total time: " + (System.currentTimeMillis() - time));
        System.out.println("End-nodes: " + toExpand.size());
        System.out.println("------------------------------------------------------------");
        ArrayList<Node> temp = new ArrayList<Node>();
        Collections.addAll(temp, parent.children);
        Collections.shuffle(temp);
        System.out.println(temp);
        parent.children = temp.toArray(new Node[temp.size()]);
        //Arrays.sort(parent.children, (a, b) -> DATA[0] ? find(b) - find(a) : -(find(b) - find(a)));
        Arrays.sort(parent.children, new NodeComparator());
        String output = "";
        for (int k = 0; k < 3; k++) {
            Node node = parent.children[k];
            System.out.println("Value: " + find(node));
            System.out.println("------------------------------------------------------------");
            for (byte[] col : node.position) {
                for (byte s : col) System.out.print(s + " ");
                System.out.println();
            }
            System.out.println("------------------------------------------------------------");
        }
        System.out.println(output);

        BOARD = parent.children[0].position;
        DATA = parent.children[0].data;
    }

    private static class NodeComparator implements Comparator<Node>{
        public int compare(Node a, Node b){
            return DATA[0]? find(b) - find(a) : -(find(b) - find(a));
        }
    }

}


