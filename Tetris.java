import java.util.*;
import java.awt.Color;
import sounds.APSoundClip;
import sounds.Sample;
import java.io.*;
import java.util.Scanner;

public class Tetris implements ArrowListener
{
    public static void main(String[] args)
    {
        Tetris tetris = new Tetris(); //creates a new tetris
        tetris.play(); //starts the game
    }
    int time; //base time it takes for the block to fall
    int gameTime; //level specific time for the block to fall
    private int level; //level
    private int score; //score
    private int rowsDone; //rows done per level
    private int lines; // # of rows done in total
    private boolean gotTetris; //got that tetris?
    private BoundedGrid<Block> grid; // the background
    private BlockDisplay display; // the display for tetris
    private Tetrad activeTetrad; //current tetrad being manipulated
    private Tetrad nextTetrad; //next tetrad about to fall
    private String title; //title of the display
    private int combo; // # of rows previously cleared
    private boolean tettet; // true if you cleared a tetris previously
    private boolean paused; //is teh game paused?
    private boolean game; //is the game still running
    private boolean controlsActive; //can you control the block
    private Color[][] colors; // list of colors for every block
    private long start = System.currentTimeMillis(); //internal stopwatch
    private boolean cheats = false;
    private boolean cheatCode1 = false;
    private boolean cheatCode2 = false;
    private boolean dejavu = false;
    private boolean reee = false;
    private boolean storm = false;
    private MusicPlayer music;
    private boolean sped = false;
    private long elapsed;
    private int meteors = 0;
    private int lives = 3;
    private List<Tetrad> deathrad;
    public Tetris()
    {
        grid = new BoundedGrid<Block>(20, 10); //creates a new grid
        colors = new Color[20][10]; //sets the color array to the same size as the grid
        display = new BlockDisplay(grid); //creates a new display with the grid
        display.setArrowListener(this); //sets the arrow listener
        display.setTitle("Tetris"); //sets the title to Tetris
        activeTetrad = new Tetrad(grid); //creates a new tetrad
        nextTetrad = new Tetrad(grid); //creates a second tetrad
        time = 1000; //base time is set to 1000 milliseconds or 1 second
        gameTime = 1000; //same for this time variable
        level = 1; //sets level to 1
        score = 0; //sets score to 0
        gotTetris = false; //you have not gotten a tetris yet
        rowsDone = 0; //sets rows done for this first level = 0
        lines = 0; //sets lines cleared = 0
        title = ""; //sets title to nothing
        tettet = false; //you have not gotten a tetris yet
        paused = false; //the game is not paused
        game = true; //the game is active
        controlsActive = true; //controls are active
        music = new MusicPlayer("Tetris.wav", 82000.0);
        deathrad = new ArrayList<Tetrad>();
    }

    public void upPressed()
    {
        if(!paused && game && controlsActive){
            activeTetrad.rotate(); //rotates the block when up is pressed
            display.showBlocks();
        }
    }

    public void downPressed()
    {
        if(!paused && game && controlsActive){
            activeTetrad.translate(1, 0); //moves the block down 1 square
            display.showBlocks();
            score+=1; //adds to your score
        }
    }

    public void leftPressed()
    {
        if(!paused && game && controlsActive){
            activeTetrad.translate(0, -1); //moves the block left 1 square
            display.showBlocks();
        }
    }

    public void rightPressed()
    {
        if(!paused && game && controlsActive){
            activeTetrad.translate(0, 1); //moves the block right 1 square
            display.showBlocks();
        }
    }

    public void spacePressed()
    {
        if(!paused && game && controlsActive&&!dejavu){
            while(activeTetrad.translate(1,0)){score+=2;} //adds to your score and moves the block until it is unable to move down
            gameTime = 0; //sets gameTime = 0 so it can reset the loop, thats why we have two time variables, one for a temp variable the other for the actual time being used
            display.showBlocks();
        }
    }

    public void escPressed()
    {
        if(paused){ //if the game is currently paused
            paused = false; //the game is no longer paused
            System.out.println("unpause"); //tells the player the game is unpaused
            for(int r = 0; r < 20; r++){ //for each block
                for(int c = 0; c < 10; c++){
                    Location l = new Location (r, c);
                    if(grid.get(l)!=null&&colors[r][c]!=null){
                        grid.get(l).setColor(colors[r][c]);
                        colors[r][c]=null;
                    }
                    //resets the color of each block
                }
            }
            activeTetrad.resetColor(); //resets the color of the active tetrad
            display.showBlocks();
        }else{ //if the game isnt paused
            paused = true; //pause the game
            System.out.println("pause"); //tell the player the game is paused
            for(int r = 0; r < 20; r++){
                for(int c = 0; c < 10; c++){ //for each block
                    Location l = new Location (r, c);
                    if(grid.get(l)!=null){
                        colors[r][c]=(grid.get(l).getColor());
                        grid.get(l).setColor(Color.BLACK);
                        //sets the color of each block to black
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

        music.music();
        while (game)
        {
            elapsed = System.currentTimeMillis()-start;
            if(elapsed%music.getLength()>=1000&&elapsed>=music.getLength() && !music.getMuted()){
                music.music();
                start = System.currentTimeMillis();
            }
            for(int i = 0; i<5; i++){
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                elapsed = System.currentTimeMillis()-start;
            }
            if(!paused){
                for(int i = 0; i<5; i++){
                    try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                    elapsed = System.currentTimeMillis()-start;
                }
                if(!dejavu){
                    if(!activeTetrad.translate(1,0)){
                        controlsActive = false;
                        gameTime = time;
                        if(!topRowsEmpty()){
                            game = false;
                            gameOver();
                            break;
                        }
                        clearCompletedRows();
                        activeTetrad = nextTetrad;
                        activeTetrad.SpawnTetrad();
                        nextTetrad = new Tetrad(grid);
                        if(cheatCode1){
                            nextTetrad.setShape(0);
                        }else if(cheatCode2){
                            nextTetrad.setShape(2);
                        }
                        DisplayNextTetrad();
                        controlsActive = true;
                    }
                }else{
                    gameTime = time;
                    if(!nextTetrad.translate(1,0)){
                        if(elapsed>=64000 && reee){
                            ree();
                            reee = !reee;
                        }
                        if(nextTetrad.isNextToSomething()){
                            score+=20*level;
                        }
                        controlsActive = false;
                        gameTime = time;
                        if(!nextTetrad.isOnGround()){
                            lives--;
                        }
                        if(lives<=0){
                            game = false;
                            gameOver();
                            break;
                        }
                        nextTetrad.removeBlocks();
                        nextTetrad = new Tetrad(grid);
                        nextTetrad.setShape(9);
                        nextTetrad.SpawnTetrad();
                        int n = activeTetrad.getLocations()[0].getCol();
                        n += (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                        nextTetrad.translateToCol(n);
                        controlsActive = true;
                        meteors++;
                        if(meteors%10==0){
                            level++;
                            time-=2;
                            if(time<20){
                                time = 20;
                            }
                        }
                        if(level>=8){
                            System.out.println("SEIZURE WARNING!!!!!!!!");
                        }
                        score+=20*level;
                    }
                }
                if(game==false){
                    break;
                }
                display.showBlocks();
                title = "Level "+level+", Score: "+score;
                display.setTitle(title);
                if(reee)
                    ree();
            }
            if(elapsed >=24000 && sped){
                time = 25;
                gameTime = 25;
                sped = false;
                display.setRee(true);
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
                lines++;
            }

        }

        int scor = 0;
        if(rowsBroke == 1){
            scor = 100*level;
            tettet = false;
            combo++;
        }else if(rowsBroke == 2){
            scor = 300*level;
            tettet = false;
            combo++;
        }else if(rowsBroke == 3){
            scor = 500*level;
            tettet = false;
            combo++;
        }else if(rowsBroke == 4 && tettet){
            scor = 1600*level;
            tettet = true;
            combo++;
        }else if(rowsBroke == 4){
            scor = 800*level;
            tettet = true;
            combo++;
        }else{
            tettet = false;
            combo = 0;
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
            }else if (time>75){
                level++;
                time-= 25;
                rowsDone -= 10;
            }else if(level>=1999){
                level=1990;
                rowsDone -=10;
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
        display.showBlocks();
        music.stopMusic();
        title+= " You lose m8";
        display.setTitle(title);
        DisplayNextTetrad();
    }

    private void ree(){
        List<Location> locs = grid.getOccupiedLocations();
        display.setRee(true);
        for(Location l : locs){
            grid.get(l).setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));

        }
    }

    private void unree(){
        List<Location> locs = grid.getOccupiedLocations();
        display.setRee(false);
        for(Location l : locs){
            grid.get(l).setColor(grid.get(l).getOriginal());

        }
    }

    public void DisplayNextTetrad(){
        for(int i = 0; i<16; i++)
            System.out.println();
        if(!dejavu){
            System.out.println("Level: " + level + "\nLines: " + lines + "\nScore: " + score + 
                "\nNext:");
        }else{
            System.out.print("\nLevel: " + level + "\nLives: ");
            for(int i = 0; i<lives; i++)
                System.out.print("[]");
            System.out.print("\nScore: " + score + 
                "\nNext:");
        }
        if(nextTetrad.getShape()==0){
            for(int i = 0; i<4; i++)
                System.out.println(" []");
        }else if(nextTetrad.getShape()==1){
            System.out.println(" [][][] \n   [] \n\n");
        }else if(nextTetrad.getShape()==2){
            System.out.println(" [][] \n [][] \n \n");
        }else if(nextTetrad.getShape()==3){
            System.out.println(" [] \n [] \n [][]\n");
        }else if(nextTetrad.getShape()==4){
            System.out.println("   [] \n   [] \n [][]\n"); 
        }else if(nextTetrad.getShape()==6){
            System.out.println(" [][] \n   [][]\n\n"); 
        }else if(nextTetrad.getShape()==5){
            System.out.println("   [][] \n [][]\n\n"); 
        }else if(nextTetrad.getShape()==7){
            System.out.println("ya done mate\n\n\n");
        }else{
            System.out.println("You dirty cheater");
        }

        if(sped){
            System.out.println("SEIZURE WARNING");
        }
    }

    public void stockStorm(){
        for(int i = 0; i<4; i++){
            Tetrad temp = new Tetrad(grid);
            temp.setShape(9);
            temp.translate(0,((Math.random()<0.5)?((int)(Math.random()*6)):(-1*(int)(Math.random()*6))));
            deathrad.add(temp);
        }
    }   

    public void onePressed(){
        music.stopMusic();
        music = new MusicPlayer("Tetris.wav", 82000.0);
        start = System.currentTimeMillis();
        music.music();
    }

    public void twoPressed(){
        music.stopMusic();
        music = new MusicPlayer("Boosted.wav", 82000.0);
        start = System.currentTimeMillis();
        music.music();
    }

    public void threePressed(){
        music.stopMusic();
        music = new MusicPlayer("dejavu.wav", 273000.0);
        start = System.currentTimeMillis();
        music.music();
    }

    public void fourPressed(){
        music.stopMusic();
        music = new MusicPlayer("Electric.wav", 191000.0);
        start = System.currentTimeMillis();
        music.music();
    }

    public void sPressed(){
        if(cheats){
            sped = true;
            music.stopMusic();
            music = new MusicPlayer("90s.wav", 284000);
            music.music();
            level = 1990;
            time = 300;
            gameTime = 0;
            start = System.currentTimeMillis();
            elapsed = 0;
        }else{
            music.stopMusic();
            music = new MusicPlayer("90s.wav", 284000);
            music.music();
            start = System.currentTimeMillis();
        }
    }

    public void iPressed(){
        if(cheats){
            while(nextTetrad.getShape()!=2)
                nextTetrad = new Tetrad(grid);
            cheatCode1 = false;
            DisplayNextTetrad();
            cheatCode2 = !cheatCode2;
        }
    }

    public void oPressed(){
        if(cheats){
            nextTetrad = new Tetrad(grid);
            DisplayNextTetrad();
        }
    }

    public void pPressed(){
        if(cheats){
            nextTetrad.setShape(0);
            DisplayNextTetrad();
            cheatCode1 = !cheatCode1;
            cheatCode2 = false;
        }
    }

    public void rPressed(){
        if(reee){
            display.setRee(false);
            display.showBlocks();
            unree();
        }
        reee = !reee;
    }

    public void cPressed(){
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter in code");
        String input = reader.nextLine();
        if(input.equals("oof"))
            cheats = true;
    }

    public void mPressed(){
        music.setMuted(!music.getMuted());
        if(music.getMuted())
            music.stopMusic();
        else{
            music.music();
            start = System.currentTimeMillis();
        }
    }

    public void dPressed(){
        if(cheats){
            music.stopMusic();
            music = new MusicPlayer("dejavu.wav", 264000);
            music.music();
            dejavu =!dejavu;
            for(int i = 0; i< grid.getNumRows(); i++){
                clearRow(i);
            }
            if(dejavu){

                nextTetrad = new Tetrad(grid);
                activeTetrad = new Tetrad(grid);
                nextTetrad.setShape(9);
                activeTetrad.setShape(10);
                DisplayNextTetrad();
                start = System.currentTimeMillis();
                time = 50;
                activeTetrad.SpawnTetrad();
                nextTetrad.SpawnTetrad();
                int n = activeTetrad.getLocations()[0].getCol();
                n = (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                nextTetrad.translateToCol(n);
            }else{
                nextTetrad = new Tetrad(grid);
                activeTetrad = new Tetrad(grid);
                activeTetrad.SpawnTetrad();
                DisplayNextTetrad();
                time = 1000;
            }
        }else{
            music.stopMusic();
            music = new MusicPlayer("dejavu.wav", 264000);
            music.music();
            start = System.currentTimeMillis();
        }
    }

    public void tPressed(){
        if(cheats)
            nextTetrad.setShape(8);
        DisplayNextTetrad();
    }
}
