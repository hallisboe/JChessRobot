package ChessEngine.com.company;

import java.util.*;
import java.util.concurrent.*;
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
        BOARD = b;
        DATA[0] = !DATA[0];
        DATA[1] = qs;
        DATA[2] = ks;
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
        Arrays.sort(parent.children, (a, b) -> DATA[0] ? value(b) - value(a) : -(value(b) - value(a)));
        /*String output = "";
        for (int k = 0; k < 3; k++) {
            Node node = parent.children[k];
            System.out.println("Value: " + value(node));
            System.out.println("------------------------------------------------------------");
            for (byte[] col : node.position) {
                for (byte s : col) System.out.print(s + " ");
                System.out.println();
            }
            System.out.println("------------------------------------------------------------");
        }
        System.out.println(output);*/
        BOARD = parent.children[0].position;
        DATA = parent.children[0].data;
    }

}


