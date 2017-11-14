package ChessEngine.com.company;

import java.util.ArrayList;
import java.util.Collections;

public class Expander implements Runnable {

    Node element;

    Expander(Node element) {
        this.element = element;
    }

    /*
    Expander et node
     */
    @Override
    public void run() {
        element.expand(1);
        if(element.children != null) {
            ArrayList<Node> c = element.getAllChildren();
            Collections.shuffle(c);
            Collections.addAll(ChessAI.toExpand,  c.toArray(new Node[c.size()]));
        }
    }

}
