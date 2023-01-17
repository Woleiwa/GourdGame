package Algorithm;

public class Node {
    private int x,y;
    private int parent_index;

    public Node(int x, int y, int parent_index){
        this.x = x;
        this.y = y;
        this.parent_index = parent_index;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getParent_index(){
        return this.parent_index;
    }
}
