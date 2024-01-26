import java.util.Comparator;

public class HSCompare implements Comparator<Node>{
    public int compare(Node node1, Node node2) {
        return node1.frequency - node2.frequency;
    }
}
