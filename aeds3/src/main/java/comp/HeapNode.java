package comp;

public class HeapNode implements Comparable<HeapNode> {

    private MovieObj movieObj;
    private int weight;

    public HeapNode(MovieObj movieObj) {
        this.movieObj = movieObj;
        this.weight = 0;
    }

    public HeapNode(MovieObj movieObj, int weight) {
        this.movieObj = movieObj;
        this.weight = weight;
    }

    @Override
    public int compareTo(HeapNode heapNode) {
        if (this.weight != heapNode.weight) {
            return this.weight - heapNode.weight;
        } else {
            return this.movieObj.getId() - heapNode.movieObj.getId();
        }
    }

    public MovieObj getMovieObj() {
        return movieObj;
    }

    public void setMovieObj(MovieObj movieObj) {
        this.movieObj = movieObj;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
