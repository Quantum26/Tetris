import java.util.*;
/**
 * Write a description of class MoveList here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MoveList
{
    private double value;
    private List<Move> moves;
    public MoveList(List<Move> m){
        moves = m;
    }
    
    public void add(Move m){
        moves.add(m);
    }
    
    public Move get(int indx){
        return moves.get(indx);
    }
    
    public Move remove(int indx){
        return moves.remove(indx);
    }
    
    public void setValue(double val){
        value = val;
    }
    
    public double getValue(){
            return value;
    }
}
