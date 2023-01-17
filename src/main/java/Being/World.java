package Being;
import java.io.*;

public class World {
    private int level;
    private Tile[][] map = new Tile[15][10];

    public World(int level) {
        this.level = level;
        String filename = "setting/" + Integer.toString(this.level) + ".txt";
        for(int i = 0; i < 15; i++)
        {
            for(int j = 0; j < 10; j++)
            {
                Tile tile = new Tile(i, j);
                Empty empty = new Empty(tile);
                tile.setBeing(empty);
                map[i][j] = tile;
            }
        }
        try {
            File file = new File(filename);
            InputStream fin = new FileInputStream(file);
            StringBuffer line = new StringBuffer();
            int num = fin.available();
            for(int i = 0; i < num; i++) {
                line.append((char)fin.read());
            }
            //System.out.println(line.toString());
            String[] tiles = line.toString().split("\n");
            int size = tiles.length;
            //System.out.println(size);
            for(int i = 0; i < size; i++)
            {
                //System.out.println(tiles[i]);
                Tile tile = new Tile(tiles[i]);
                this.map[tile.getX()][tile.getY()] = tile;
            }
            for(int i = 0; i < 15; i++)
            {
                for(int j = 0; j < 10; j++)
                {
                    if(map[i][j].getBeing().getName() == "Empty")
                    {
                        //System.out.println(map[i][j].getBeing().getName() + ":" + map[i][j].getBeing().getName() == "Empty");
                        if(i - 1 >= 0)
                        {
                            if(map[i - 1][j].getBeing().getName() == "Road")
                            {
                                //System.out.println(map[i - 1][j].getBeing().getName());
                                Wall wall = new Wall(map[i][j]);
                                map[i][j].setBeing(wall);
                                continue;
                            }
                        }
                        if(j - 1 >= 0)
                        {
                            if(map[i][j - 1].getBeing().getName() == "Road")
                            {
                                //System.out.println(map[i][j - 1].getBeing().getName());
                                Wall wall = new Wall(map[i][j]);
                                map[i][j].setBeing(wall);
                                continue;
                            }
                        }
                        if(i + 1 < 15)
                        {
                            if(map[i + 1][j].getBeing().getName() == "Road")
                            {
                                //System.out.println(map[i + 1][j].getBeing().getName());
                                Wall wall = new Wall(map[i][j]);
                                map[i][j].setBeing(wall);
                                continue;
                            }
                        }
                        if(j + 1 < 10)
                        {
                            if(map[i][j + 1].getBeing().getName() == "Road")
                            {
                                //System.out.println(map[i][j + 1].getBeing().getName());
                                Wall wall = new Wall(map[i][j]);
                                map[i][j].setBeing(wall);
                                continue;
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tile[][] getMap(){
        return this.map;
    }

    public int[][] mapToInt(){
        int[][] res = new int[15][10];
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 10; j++){
                if(this.map[i][j].getBeing().getName() == "Road"){
                    res[i][j] = 0;
                }
                else {
                    res[i][j] = 1;
                }
            }
        }
        return res;
    }

    public Tile findEmpty(){
        for(int i = 0 ; i < map.length; i++){
            for(int j = 0 ; j < map[i].length; j++){
                if(map[i][j].isRoad() && !map[i][j].getBeing().beOccupied()){
                    return map[i][j];
                }
            }
        }
        return null;
    }

    public Tile findEmptyExcept(Tile except){
        for(int i = 0 ; i < map.length; i++){
            for(int j = 0 ; j < map[i].length; j++){
                if(map[i][j].isRoad() && !map[i][j].getBeing().beOccupied() ){
                    if(map[i][j].getY() == except.getY() && map[i][j].getX() == except.getX()){
                        continue;
                    }
                    return map[i][j];
                }
            }
        }
        return null;
    }
}
