package ChessEngine.com.company;

class MinMax {

    /*
    Starer minmax søk gitt node.
     */
    static int value(Node node) {
        return value(node, Integer.MIN_VALUE, Integer.MAX_VALUE, node.data[0]);
    }

    /*
    Søker gitt Node, alpha, beta og tur.
     */
    private static int value(Node node, int alpha, int beta, boolean player) {
        if (node.children == null) {
            return node.value();
        }
        if(node.unchanged) {
            return node.last_value;
        }
        if (player) {
            int v = Integer.MIN_VALUE;
            for(Node n : node.children) {
                v = Math.max(v, value(n, alpha, beta, false));
                if (v > alpha) {
                    alpha = v;
                }
                if (beta <= alpha) {
                    break;
                }
            }
            node.unchanged = true;
            node.last_value = alpha;
            return alpha;
        } else {
            int v = Integer.MAX_VALUE;
            for(Node n : node.children) {
                v = Math.min(v, value(n, alpha, beta, true));
                if(v < beta) {
                    beta = v;
                }
                if (beta <= alpha) {
                    break;
                }
            }
            node.unchanged = true;
            node.last_value = beta;
            return beta;
        }
    }

}
