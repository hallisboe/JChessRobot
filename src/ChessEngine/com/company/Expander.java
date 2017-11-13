package ChessEngine.com.company;

import java.util.Collections;

public class Expander implements Runnable {

    Node element;

    Expander(Node element) {
        this.element = element;
    }

    @Override
    public void run() {
        element.expand(1);
        if(element.children != null) Collections.addAll(ChessAI.toExpand, element.children);
    }

}
