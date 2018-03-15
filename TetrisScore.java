
/**
 * Write a description of class TetrisScore here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TetrisScore
{
    private int level;
    private int score;
    public static void main(String[] args){
        System.out.println("Level: 1");
        System.out.println("Score: 0");
    }
    public TetrisScore(){
        level = 1;
        score = 0;
    }
    public int getScore(){
        return score;
    }
    public int getLevel(){
        return level;
    }
    public void setLevel(int l){
        level = l;
    }
    public void setScore(int s){
        score = s;
    }
    public void printScoreBoard(){
        for(int i = 0; i<10; i++)
        System.out.println();
        System.out.println("Level: " + level);
        System.out.println("Score: " + score);
    }
}
