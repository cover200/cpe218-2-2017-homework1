public class Node {
    char x;
    Node left ,right;

    public Node(char _x) {
        x = _x;
        left = right = null;
    }
    @Override
    public String toString() {
        return String.valueOf(x);
    }
}
