package Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Bfs {

    private int[][] map;
    private int x1,y1,x2,y2;
    private List<Node> nodeQueue;
    private boolean[][] bmap;
    private int dest;

    public Bfs(int[][] map, int x1, int y1, int x2, int y2){
        this.map = map;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        bmap = new boolean[map.length][];
        for(int i = 0; i < bmap.length; i++){
            bmap[i] = new boolean[map[i].length];
            for(int j = 0; j < bmap[i].length; j++) {
                bmap[i][j] = false;
            }
        }
        nodeQueue = new ArrayList<>();
    }

    private void search(){
        Node root = new Node(x1,y1,-1);
        bmap[x1][y1] = true;
        nodeQueue.add(root);
        int index = 0;
        while (bmap[x2][y2] == false){
            Node cur = nodeQueue.get(index);
            int x = cur.getX();
            int y = cur.getY();
            int[] x_list = {x + 1, x, x - 1, x};
            int[] y_list = {y, y - 1, y, y + 1};
            for(int i = 0; i < 4; i++){
                if(x_list[i] >= 0 && x_list[i] < map.length){
                    if(y_list[i] >= 0 && y_list[i] < map[x_list[i]].length){
                        if(map[x_list[i]][y_list[i]] == 0 && !bmap[x_list[i]][y_list[i]]){
                            Node node = new Node(x_list[i],y_list[i],index);
                            nodeQueue.add(node);
                            bmap[x_list[i]][y_list[i]] = true;
                            if(x_list[i] == x2 && y_list[i] == y2) {
                                dest = nodeQueue.size() - 1;
                            }
                        }
                    }
                }
            }
            index++;
        }
    }

    public String findDirection(){
        this.search();
        String res = null;
        Node cur = nodeQueue.get(dest);
        Node pre = cur;
        while (cur.getParent_index()!= - 1){
            pre = cur;
            cur = nodeQueue.get(cur.getParent_index());
        }
        int jx = cur.getX() - pre.getX();
        int jy = cur.getY() - pre.getY();
        if(jx == 1){
            res = "left";
        }
        else if(jx == -1){
            res = "right";
        }
        else if(jy == 1){
            res = "back";
        }
        else if(jy == -1){
            res = "front";
        }
        return res;
    }
}
