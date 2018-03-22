import java.util.*;
import java.awt.Color;
import sounds.APSoundClip;
import sounds.Sample;
import sun.audio.*;
import java.io.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Tetris implements ArrowListener
{
    public static void main(String[] args)
    {
        Tetris tetris = new Tetris();
        tetris.play();
    }
    int time;
    int gameTime;
    private int level;
    private int score;
    private int rowsDone;
    private boolean gotTetris;
    private BoundedGrid<Block> grid;
    private BlockDisplay display;
    private TetradV2 activeTetrad;
    private TetradV2 nextTetrad;
    private String title;
    private int combo;
    private boolean tettet;
    private boolean paused;
    private boolean game;
    private Color[][] colors;

    public Tetris()
    {
        grid = new BoundedGrid<Block>(20, 10);
        colors = new Color[20][10];
        display = new BlockDisplay(grid);
        display.setArrowListener(this);
        display.setTitle("Tetris");
        activeTetrad = new TetradV2(grid);
        nextTetrad = new TetradV2(grid);
        time = 1000;
        gameTime = 1000;
        level = 1;
        score = 0;
        gotTetris = false;
        rowsDone = 0;
        title = "";
        tettet = false;
        paused = false;
        game = true;
    }

    public void upPressed()
    {
        if(!paused && game){
            activeTetrad.rotate();
            display.showBlocks();
        }
    }

    public void downPressed()
    {
        if(!paused && game){
            activeTetrad.translate(1, 0);
            display.showBlocks();
            score+=1;
        }
    }

    public void leftPressed()
    {
        if(!paused && game){
            activeTetrad.translate(0, -1);
            display.showBlocks();
        }
    }

    public void rightPressed()
    {
        if(!paused && game){
            activeTetrad.translate(0, 1);
            display.showBlocks();
        }
    }

    public void spacePressed()
    {
        if(!paused && game){
            while(activeTetrad.translate(1,0)){score+=2;}
            gameTime = 0;
            display.showBlocks();
        }
    }

    public void escPressed()
    {
        if(paused){
            paused = false;
            System.out.println("unpause");
            for(int r = 0; r < 20; r++){
                for(int c = 0; c < 10; c++){
                    Location l = new Location (r, c);
                    if(grid.get(l)!=null&&colors[r][c]!=null){
                        grid.get(l).setColor(colors[r][c]);
                        colors[r][c]=null;
                    }
                }
            }
            display.showBlocks();
        }else{
            paused = true;
            System.out.println("pause");
            for(int r = 0; r < 20; r++){
                for(int c = 0; c < 10; c++){
                    Location l = new Location (r, c);
                    if(grid.get(l)!=null){
                        colors[r][c]=(grid.get(l).getColor());
                        grid.get(l).setColor(Color.BLACK);
                    }
                }
            }
            display.showBlocks();
        }
    }

    public void play()
    {
        game = true;
        activeTetrad.SpawnTetrad();
        DisplayNextTetrad();
        //music();
        while (game)
        {
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}

            if(!paused){
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}

                if(!activeTetrad.translate(1,0)){
                    gameTime = time;
                    Location[] l = activeTetrad.getLocations();
                    if(!topRowsEmpty()){
                        game = false;
                        gameOver();
                        break;
                    }
                    clearCompletedRows();
                    activeTetrad = nextTetrad;
                    activeTetrad.SpawnTetrad();
                    nextTetrad = new TetradV2(grid);
                    DisplayNextTetrad();
                }
                if(game==false){
                    break;
                }
                display.showBlocks();
                title = "Level "+level+", Score: "+score;
                display.setTitle(title);
                //ree();
            }

        }
    }

    //precondition:  0 <= row < number of rows
    //postcondition: Returns true if every cell in the
    //               given row is occupied;
    //               returns false otherwise.
    private boolean isCompletedRow(int row)
    {
        boolean full = true;
        for(int i = 0; i < 10; i++){
            Location l = new Location(row, i);
            if(grid.get(l)==null){
                full = false;
            }
        }
        return full;
    }

    //precondition:  0 <= row < number of rows;
    //               given row is full of blocks
    //postcondition: Every block in the given row has been
    //               removed, and every block above row
    //               has been moved down one row.
    private void clearRow(int row)
    {
        for(int i = 0; i<10; i++){
            Location l = new Location(row, i);
            if(grid.get(l)!=null)
                grid.remove(l).removeSelfFromGrid();

        }
    }

    //postcondition: All completed rows have been cleared.
    private void clearCompletedRows()
    {
        int rowsBroke = 0;
        for(int i = 19; i > 0; i--){
            if(isCompletedRow(i)){
                flashRow(i);
                clearRow(i);
                moveDownAbove(i);
                i = 20;
                rowsBroke++;
                rowsDone++;
            }

        }

        int scor = 0;
        if(rowsBroke == 1){
            scor = 100*level;
            tettet = false;
        }else if(rowsBroke == 2){
            scor = 300*level;
            tettet = false;
        }else if(rowsBroke == 3){
            scor = 500*level;
            tettet = false;
        }else if(rowsBroke == 4 && tettet){
            scor = 1200*level;
            tettet = true;
        }else if(rowsBroke == 4){
            scor = 800*level;
            tettet = true;
        }
        if(combo>0){
            scor += 50*combo*level;
        }
        score+= scor;
        if(rowsDone>=10){
            if(time>200){
                level++;
                time-= 100;
                rowsDone -= 10;
            }else if (time>50){
                level++;
                time-= 25;
                rowsDone -= 10;
            }else{
                level++;
                rowsDone -= 10;
            }
        }
        display.showBlocks();
    }

    private void moveDownAbove(int r){
        if(r<1){
            return;
        }
        List<Location> locs = grid.getRow(r);
        for(Location l: locs){
            Location x = new Location(l.getRow()+1, l.getCol());
            if(grid.isValid(x)&&grid.get(x)==null){
                grid.remove(l).moveTo(x);
            }
        }
        moveDownAbove(r-1);
    }

    public void flashRow(int row){
        Color[] colors = new Color[10];
        for(int k = 0; k<2; k++){
            for(int i = 0; i< 10; i++){
                Location l = new Location(row, i);
                if(grid.get(l)!=null){
                    colors[i] = grid.get(l).getColor();
                    grid.get(l).setColor(new Color(0, 0, 0));
                }

            }
            display.showBlocks();
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            for(int i = 0; i< 10; i++){
                Location l = new Location(row, i);
                if(colors[i]!=null && grid.get(l)!=null)
                    grid.get(l).setColor(colors[i]);

            }
            display.showBlocks();
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
        }
    }

    //returns true if top two rows of the grid are empty (no blocks), false otherwise
    private boolean topRowsEmpty()
    {
        boolean top = true;
        boolean atop = true;
        for(int i = 0; i<10; i++){
            Location l = new Location(0, i);
            if(grid.get(l)!=null){
                top = false;
            }
        }
        for(int i = 0; i<10; i++){
            Location l = new Location(1, i);
            if(grid.get(l)!=null){
                atop = false;
            }
        }
        return top && atop;
    }

    private void gameOver(){
        for(int r = 0; r < 20; r++){
            for(int c = 0; c < 10; c++){
                Location l = new Location (r, c);
                if(grid.get(l)!=null){
                    grid.get(l).setColor(Color.RED);
                }
            }
        }
        title+= " You lose m8";
        display.setTitle(title);
    }

    private void ree(){
        List<Location> locs = grid.getOccupiedLocations();
        display.setRee();
        for(Location l : locs){
            grid.get(l).setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));

        }
    }

    public static void music(){
        /**AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;
        ContinuousAudioDataStream loop = null;
        try{
        FileInputStream fileInputStream = new FileInputStream("Tetris.mp3");
        BGM = new AudioStream(fileInputStream);
        MD = BGM.getData();
        loop = new ContinuousAudioDataStream(MD);
        System.out.println("Oof");
        }catch(IOException e){
        }
        MGP.start(loop);
         */
        String bip = "Tetris.mp3";
        Media hit = new Media(new File(bip).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
    public void DisplayNextTetrad(){
        for(int i = 0; i<20; i++)
        System.out.println();
        System.out.println("Next:");
        if(nextTetrad.getShape()==0){
            for(int i = 0; i<4; i++)
            System.out.println(" []");
        }else if(nextTetrad.getShape()==1){
            System.out.println(" [][][] \n   []");
        }else if(nextTetrad.getShape()==2){
            System.out.println(" [][] \n [][]");
        }else if(nextTetrad.getShape()==3){
            System.out.println(" [] \n [] \n [][]");
        }else if(nextTetrad.getShape()==4){
            System.out.println("   [] \n   [] \n [][]"); 
        }else if(nextTetrad.getShape()==6){
            System.out.println(" [][] \n   [][]"); 
        }else if(nextTetrad.getShape()==5){
            System.out.println("   [][] \n [][]"); 
        }
    }
}

