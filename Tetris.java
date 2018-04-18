import java.util.*;
import java.awt.Color;
import java.io.*;
import java.util.Scanner;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;
public class Tetris implements ArrowListener
{
    public static void main(String[] args)
    {
        boolean playing = true;
        while(playing){
            Tetris tetris = new Tetris(); //creates a new tetris
            tetris.play(); //starts the game
            System.out.println("Would you Like to Play Again? yes/no");
            Scanner reader = new Scanner(System.in);
            String x = reader.nextLine();
            if(x.length()<1||x.substring(0,1).toLowerCase().equals("n")){
                playing = false;
                break;
            }
        }
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
    private Tetrad thirdTetrad; //extra tetrad for dodging mode
    private Tetrad tnextTetrad;//player 2's next tetrad
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
    private boolean cheatCode3 = false;
    private boolean cheatCode4 = false;
    private boolean cheatCode5 = false;
    private boolean dejavu = false;
    private boolean galaga = false;
    private boolean reee = false;
    private boolean raining = false;
    private boolean battle = false;
    private boolean bot = true;
    private MusicPlayer music;
    private boolean sped = false;
    private long elapsed;
    private int meteors = 0;
    private int lives = 1500;
    private List<Tetrad> storm;
    private Tetris other;
    private Object l1;
    private Object l2;
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
        music = new MusicPlayer("Tetris.mp3");
        music.setStopTime(new Duration(77000));
        storm = new ArrayList<Tetrad>();
        start = System.currentTimeMillis();
    }

    public void upPressed()
    {
        if(!paused && game && controlsActive&&!galaga){
            activeTetrad.rotate(); //rotates the block when up is pressed
            display.showBlocks();
        }else if(galaga){
            storm.add(new Tetrad(grid));
            storm.get(storm.size()-1).setShape(14);
            storm.get(storm.size()-1).SpawnTetrad();
            storm.get(storm.size()-1).translateToCol(activeTetrad.getLocations()[3].getCol());
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
        if(!paused && game && controlsActive&&!dejavu&&!galaga){
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
            music.play();
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
            music.pause();
        }
    }

    public void play()
    {
        game = true;
        activeTetrad.SpawnTetrad();
        DisplayNextTetrad();
        display.showBlocks();
        music.play();
        while (game)
        {
            for(int i = 0; i<5; i++){
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            }
            if(!paused){
                for(int i = 0; i<5; i++){
                    try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                }
                if(dejavu){
                    playDejaMode();
                }else if(galaga){
                    playGalaga();
                }else{
                    playAgainst();
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
                time = 48;
                gameTime = 48;
                sped = false;
                display.setRee(true);
                ree();
                reee = true;
            }

            if(game==false){
                break;
            }
            display.showBlocks();
            title = "Level "+level+", Score: "+score;
            display.setTitle(title);
        }
    }

    public int playTetris(){
        int x = 0;
        if(!activeTetrad.translate(1,0)){
            
            controlsActive = false;
            gameTime = time;
            if(!topRowsEmpty()){
                game = false;
                gameOver();
                return 0;
            }
            x= clearCompletedRows();
            
            activeTetrad = nextTetrad;
            MoveList m = getMovesToMake();
            activeTetrad.SpawnTetrad();
            display.showBlocks();
            if(activeTetrad.getShape()==7){
                omae();
            }

            nextTetrad = new Tetrad(grid);
            if(cheatCode1){
                nextTetrad.setShape(0);
            }else if(cheatCode2){
                nextTetrad.setShape(2);
            }
            display.showBlocks();
            DisplayNextTetrad();
            controlsActive = true;
            if(bot){
                makeMove(m);
            }
        }
        return x;
    }

    public void playGalaga(){

        boolean ntt = nextTetrad.translate(1,0);
        boolean ttt = thirdTetrad.translate(1,0);
        List<Boolean> btt = new ArrayList<Boolean>();
        for(Tetrad tet : storm){
            btt.add(0, tet.translate(-1,0));
        }
        boolean bbtt = true;
        for(Boolean boi : btt){
            if(boi.equals(false)){
                bbtt = false;
            }
        }
        if(!ntt||!ttt){
            elapsed = (long)music.getCurrentTime().toMillis();
            if(nextTetrad.isNextToSomething()||(thirdTetrad.isNextToSomething())){
                score+=20*level;
            }
            controlsActive = false;
            gameTime = time;
            int n = activeTetrad.getLocations()[0].getCol();
            if(!ntt){
                if(!nextTetrad.isOnGround()){

                    Location l = nextTetrad.getLocations()[3];
                    Location newl = new Location(l.getRow()+1, l.getCol());
                    if(grid.isValid(newl)&&grid.get(newl)!=null){
                        if(grid.get(newl).getColor().equals(Color.WHITE)){
                            lives--;
                        }
                    }else if(grid.isValid(newl)&&grid.get(newl)!=null&&
                    (grid.get(newl).getColor().equals(Color.GREEN)||
                        grid.get(newl).getColor().equals(Color.CYAN))){
                        score+=20*level;
                    }
                }else{
                    score+=10*level;
                }
                nextTetrad.removeBlocks();
                nextTetrad = new Tetrad(grid);
                nextTetrad.setShape(9);
                nextTetrad.SpawnTetrad();

                if(n>1 && n<8){
                    n += (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                }else if(n<=1){
                    n += (int)(Math.random()*2);
                }
                else if (n>=8){
                    n += -1*(int)(Math.random()*2);
                }

                nextTetrad.translateToCol(n);
            }
            if(!ttt){
                if(!thirdTetrad.isOnGround()){

                    Location l = thirdTetrad.getLocations()[3];
                    Location newl = new Location(l.getRow()+1, l.getCol());
                    if(grid.isValid(newl)&&grid.get(newl)!=null){
                        if(grid.get(newl).getColor().equals(Color.WHITE)){
                            lives--;
                        }
                    }else if(grid.isValid(newl)&&grid.get(newl)!=null&&
                    (grid.get(newl).getColor().equals(Color.GREEN)||
                        grid.get(newl).getColor().equals(Color.CYAN))){
                        score+=20*level;
                    }
                }else{
                    score+=10*level;
                }
                thirdTetrad.removeBlocks();
                thirdTetrad = new Tetrad(grid);
                thirdTetrad.setShape(9);
                thirdTetrad.SpawnTetrad();

                if(n>1 && n<8){
                    n += (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                }else if(n<=1){
                    n += (int)(Math.random()*2);
                }
                else if (n>=8){
                    n += -1*(int)(Math.random()*2);
                }

                thirdTetrad.translateToCol(n);
            }
            if(!bbtt){
                storm.remove(0).removeBlocks();
            }
            if(lives<=0){
                game = false;
                gameOver();
                return;

            }

            DisplayNextTetrad();
            controlsActive = true;
            meteors++;
            if(cheatCode3){
                meteors++;
            }
            if(meteors%10==0){
                level++;
                time-=2;
                if(time<30){
                    time = 30;
                }
            }
            score+=10*level;
            if(music.getStatus()==MediaPlayer.Status.STOPPED){
                game = false;
                win();
                return;
            }
        }
        if(game==false){
            return;
        }
        display.showBlocks();
        title = "Level "+level+", Score: "+score;
        display.setTitle(title);

    }

    public void playDejaMode(){
        if(activeTetrad.getSpawned()&&(!nextTetrad.translate(1,0)||(storm.size()>0&&storm.get(0).getSpawned()&&!storm.get(0).translate(1,0)))){
            elapsed = (long)music.getCurrentTime().toMillis();
            if(elapsed>=195000){
                //display.setRee(true);
                cheatCode3 = true;
                if(storm.size()>0&&storm.get(0).getSpawned()){
                    storm.get(0).removeBlocks();
                    storm.remove(0);
                }
                storm.add(new Tetrad(grid));
                cheatCode4 = false;
            }else if(elapsed>=158000){
                //display.setRee(false);
                cheatCode4 = true;
            }else if(elapsed>=129000&&!cheatCode5){
                cheatCode5 = true;
                nextTetrad.removeBlocks();
                storm.get(0).removeBlocks();
                storm.remove(0);
                activeTetrad.removeBlocks();
                activeTetrad.setShape(12);
                activeTetrad.SpawnTetrad();
                activeTetrad.translateToCol(9);
                thirdTetrad.setShape(11);
                thirdTetrad.SpawnTetrad();
                thirdTetrad.translateToCol(0);
                //unree();
                //reee = false;
                //display.setRee(true);
                nextTetrad = new Tetrad(grid);
                nextTetrad.setShape(9);
                nextTetrad.SpawnTetrad();
                cheatCode3 = false;
                cheatCode4 = false;
            }else if(elapsed>=92000&&!cheatCode5){
                //display.setRee(false);
                cheatCode3=false;
                cheatCode4=true;
                if(storm.size()>0&&storm.get(0).getSpawned()){
                    storm.get(0).removeBlocks();
                    storm.remove(0);
                }
                storm.add(new Tetrad(grid));
            }else if(elapsed>=64000&&!cheatCode5){
                //ree();
                //reee = true;
                //display.setRee(true);
                cheatCode3=true;
            }
            if(nextTetrad.isNextToSomething()||(storm.size()>0&&storm.get(0).getSpawned()&&storm.get(0).isNextToSomething())){
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
                return;
            }
            nextTetrad.removeBlocks();
            nextTetrad = new Tetrad(grid);
            nextTetrad.setShape(9);
            if(cheatCode4){
                nextTetrad.setShape((int)(Math.random()*7));
            }
            nextTetrad.SpawnTetrad();
            int n = activeTetrad.getLocations()[0].getCol();
            if(cheatCode5){
                n = Math.random()<0.5?(thirdTetrad.getLocations()[0].getCol()):n;
            }
            if(n>1 && n<8){
                n += (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
            }else if(n<=1){
                n += (int)(Math.random()*2);
            }
            else if (n>=8){
                n += -1*(int)(Math.random()*2);
            }

            nextTetrad.translateToCol(n);

            if(cheatCode3){
                if(storm.get(0).getSpawned()){
                    storm.get(0).removeBlocks();
                    storm.remove(0);
                }
                storm.add(new Tetrad(grid));
                storm.get(0).setShape(9);
                if(cheatCode4){
                    storm.get(0).setShape((int)(Math.random()*7));
                }
                storm.get(0).SpawnTetrad();
                int t = activeTetrad.getLocations()[0].getCol();
                if(cheatCode5){
                    t = Math.random()<0.5?(thirdTetrad.getLocations()[0].getCol()):t;
                    if(n>1 && n<8){
                        t += (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                    }else if(n<=1){
                        t += (int)(Math.random()*2);
                    }
                    else if (n>=8){
                        t += -1*(int)(Math.random()*2);
                    }
                }
                else{
                    if(n>1 && n<8){
                        t += (Math.random()<0.5)?(-1*(int)(Math.random()*4) -2):((int)(Math.random()*4) + 2);
                    }else if(n<=1){
                        t += (int)(Math.random()*4) + 2;
                    }
                    else if (n>=8){
                        t += -1*(int)(Math.random()*4) - 2;
                    }
                }
                storm.get(0).translateToCol(t); 
            }
            DisplayNextTetrad();
            controlsActive = true;
            meteors++;
            if(cheatCode3){
                meteors++;
            }
            if(meteors%10==0){
                level++;
                time-=2;
                if(time<30){
                    time = 30;
                }
            }
            if(level>=8){
                System.out.println("SEIZURE WARNING!!!!!!!!");
            }
            score+=10*level;
            if(music.getStatus()==MediaPlayer.Status.STOPPED){
                game = false;
                win();
                return;
            }
        }
        if(game==false){
            return;
        }
        display.showBlocks();
        title = "Level "+level+", Score: "+score;
        display.setTitle(title);

    }

    public void playAgainst(){
        other = new Tetris();
        other.setBot(true);
        this.setBot(false);
        battle = true;
        l1 = new Object();
        l2 = new Object();
        Thread hope = new Thread(){

                @Override
                public void run()
                {
                    while (game)
                    {
                        for(int i = 0; i<5; i++){
                            try { sleep(gameTime/10); } catch(Exception e) {}
                        }
                        if(!paused){
                            for(int i = 0; i<5; i++){
                                try { sleep(gameTime/10); } catch(Exception e) {}
                            }
                            addRows(other.playTetris());
                            other.showBlocks();
                        }

                    }
                }
            };
        hope.start();
        display.setTitle("ssh");
        while (game)
        {
            for(int i = 0; i<5; i++){
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            }
            if(!paused){
                for(int i = 0; i<5; i++){
                    try { Thread.sleep(gameTime/10); } catch(Exception e) {}
                }
                other.addRows(this.playTetris());
                display.showBlocks();
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
            if(grid.get(l)==null||!grid.get(l).getDestroyable()){
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
            if(grid.get(l)!=null&&grid.get(l).getDestroyable())
                grid.remove(l).removeSelfFromGrid();

        }
    }

    //postcondition: All completed rows have been cleared.
    public int clearCompletedRows()
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
                time-= 5;
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
        return rowsBroke;
    }

    public void addRows(int n){
        for(int i = 0; i< n; i++){
            moveUpBelow(0);
            for(int c = 0; c < grid.getNumCols(); c++){
                Location place = new Location(grid.getNumRows()-1, c);
                Block boi = new Block();boi.setColor(Color.GRAY);boi.setOriginal(Color.GRAY);
                boi.setDestroyable(false);
                boi.putSelfInGrid(grid, place);
            }
        }
        
        

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

    private void moveUpBelow(int r){
        if(r>19){
            return;
        }
        List<Location> locs = grid.getRow(r);
        for(Location l: locs){
            Location x = new Location(l.getRow()-1, l.getCol());
            if(grid.isValid(x)&&grid.get(x)==null){
                grid.remove(l).moveTo(x);
            }
        }
        moveUpBelow(r+1);
    }

    public void showBlocks(){
        display.showBlocks();
    }

    public void flashRow(int row){
        Color[] colors = new Color[10];
        for(int k = 0; k<2; k++){
            for(int i = 0; i< 10; i++){
                Location l = new Location(row, i);
                if(grid.get(l)!=null&&grid.get(l).getDestroyable()){
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
        music.setMusic("oof.mp3");
        music.play();
        title+= " You lose m8";
        display.setTitle(title);
        DisplayNextTetrad();
    }

    public void win(){
        System.out.println("YOU WIN! \nYour Score was: " + score);
        title+=" YOU A WINNER";
        display.setTitle(title);
    }

    private void ree(){
        List<Location> locs = grid.getOccupiedLocations();
        display.setRee(true);
        for(Location l : locs){
            grid.get(l).setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));

        }
    }

    private void omae(){
        Duration temp = music.getCurrentTime();
        String tempName = music.getMusic();
        music.stopMusic();
        music.setMusic("omae.mp3");
        music.setStopTime(new Duration(2450));
        music.setAutoPlay(false);
        music.setCycleCount(1);
        music.play();
        try{
            Thread.sleep(2450);
        }catch(Exception e){
        }
        music.stopMusic();
        if(display!=null){
            display.showBlocks();
        }
        music.setStartTime(new Duration(2700));
        music.setStopTime(new Duration(3490));
        music.setCycleCount(1);
        music.play();
        try{
            Thread.sleep(790);
        }catch(Exception e){
        }
        music.stopMusic();
        music.setMusic(tempName);
        music.setStartTime(temp);
        music.play();
        music.setStartTime(Duration.ZERO);
    }

    private void unree(){
        List<Location> locs = grid.getOccupiedLocations();
        display.setRee(false);
        for(Location l : locs){
            grid.get(l).setColor(grid.get(l).getOriginal());

        }
    }
    
    public void setBot(boolean b){
        bot = b;
    }

    public void DisplayNextTetrad(){
        for(int i = 0; i<16; i++)
            System.out.println();
        if(!dejavu&&!galaga){
            System.out.println("Level: " + level + "\nLines: " + lines + "\nScore: " + score + 
                "\nNext:\n");
        }else{
            if(level>=14){
                System.out.println("GET READY FOR DOUBLE BLOCK (A and D keys)");
            }
            System.out.print("\nLevel: " + level + "\nLives: ");
            if(lives>5){
                System.out.print(lives);
            }else{
                for(int i = 0; i<lives; i++)
                    System.out.print("[]");
            }
            System.out.print("\nScore: " + score + 
                "\nNext:\n");
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
        System.out.println(music.getStatus());
        System.out.println(Thread.activeCount());
    }

    public void stockStorm(){
        for(int i = 0; i<4; i++){
            Tetrad temp = new Tetrad(grid);
            temp.setShape(9);
            temp.translate(0,((Math.random()<0.5)?((int)(Math.random()*6)):(-1*(int)(Math.random()*6))));
            storm.add(temp);
        }
    }   

    public void onePressed(){
        music.stopMusic();
        music.setMusic("Tetris.mp3");
        music.setStopTime(new Duration(77000));
        music.play();
    }

    public void twoPressed(){
        music.stopMusic();
        music.setMusic("dankTetris.mp3");
        music.play();
    }

    public void threePressed(){
        music.stopMusic();
        music.setMusic("oof.mp3");
        music.play();
    }

    public void fourPressed(){
        music.stopMusic();
        music.setMusic("omae.mp3");
        start = System.currentTimeMillis();
        music.play();
    }

    public void sPressed(){
        if(battle){
            other.downPressed();
        }else if(!dejavu){
            if(cheats){
                sped = true;
                music.stopMusic();
                music.setMusic("90s.mp3");
                music.play();
                level = 1990;
                time = 300;
                gameTime = 0;
                start = System.currentTimeMillis();
                elapsed = 0;
            }else{
                music.stopMusic();
                music.setMusic("90s.mp3");
                music.play();
                start = System.currentTimeMillis();
            }
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
    }

    public void aPressed(){
        if(battle){
            other.leftPressed();
        }else if(!paused && game && controlsActive && cheatCode5){
            if(thirdTetrad.getSpawned()){
                thirdTetrad.translate(0, -1); //moves the block left 1 square
                display.showBlocks();
            }
        }
    }

    public void dPressed(){
        if(battle){
            other.rightPressed();
        }else if(!paused && game && controlsActive && cheatCode5){
            if(thirdTetrad.getSpawned()){
                thirdTetrad.translate(0, 1); //moves the block right 1 square
                display.showBlocks();
            }
        }else if(cheats){
            music.stopMusic();
            music.setMusic("dejavu.mp3");
            music.play();
            dejavu = true;
            for(int i = 0; i< grid.getNumRows(); i++){
                clearRow(i);
            }
            if(dejavu){

                nextTetrad = new Tetrad(grid);
                activeTetrad = new Tetrad(grid);
                thirdTetrad = new Tetrad(grid);
                storm.add(new Tetrad(grid));
                storm.get(0).setShape(9);
                nextTetrad.setShape(9);
                activeTetrad.setShape(10);
                thirdTetrad.setShape(10);
                DisplayNextTetrad();
                start = System.currentTimeMillis();
                time = 50;
                gameTime = time;
                activeTetrad.SpawnTetrad();
                nextTetrad.SpawnTetrad();
                int n = activeTetrad.getLocations()[0].getCol();
                n = (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                nextTetrad.translateToCol(n);
            }
        }else{
            music.stopMusic();
            music.setMusic("dejavu.mp3");
            music.play();
        }
    }

    public void gPressed(){
        if(cheats){
            music.stopMusic();
            music.setMusic("Fingerdash.mp3");
            music.play();
            galaga = true;
            for(int i = 0; i< grid.getNumRows(); i++){
                clearRow(i);
            }
            if(galaga){

                nextTetrad = new Tetrad(grid);
                activeTetrad = new Tetrad(grid);
                thirdTetrad = new Tetrad(grid);
                nextTetrad.setShape(9);
                activeTetrad.setShape(10);
                thirdTetrad.setShape(9);
                DisplayNextTetrad();
                start = System.currentTimeMillis();
                time = 75;
                gameTime = time;
                activeTetrad.SpawnTetrad();
                Color[] c =new Color[]{Color.WHITE,Color.WHITE,Color.WHITE,Color.CYAN};
                activeTetrad.setColor(c);
                nextTetrad.SpawnTetrad();
                int n = activeTetrad.getLocations()[0].getCol();
                n = (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                nextTetrad.translateToCol(n);
                thirdTetrad.SpawnTetrad();
                n = activeTetrad.getLocations()[0].getCol();
                n = (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));
                thirdTetrad.translateToCol(n);
            }
        }
    }

    public void wPressed(){
        if(battle){
            other.upPressed();
        }
    }

    public void qPressed(){
        if(battle){
            other.spacePressed();
        }
    }

    public void tPressed(){
        if(cheats)
            nextTetrad.setShape(8);
        DisplayNextTetrad();
    }

    /**AI CODE*/
    public int getColHeight(int col, BoundedGrid<Block> g){
        int i = 0;
        while(i<g.getNumRows()){
            Location l = new Location (i, col);
            if(g.get(l)==null){
                i++;
            }else{
                break;
            }
        }
        int sum = g.getNumRows()-i;
        return sum;
    }

    public int getAggHeight(BoundedGrid<Block> g){
        int sum = 0;
        for(int i = 0; i<g.getNumCols(); i++){

            sum+=getColHeight(i, g);
        }

        return sum;
    }

    public int getCompletedLines(BoundedGrid<Block> g){
        int sum = 0;
        for(int i = 19; i > 0; i--){
            boolean full = true;
            for(int k = 0; k < 10; k++){
                Location l = new Location(i, k);
                if(g.get(l)==null){
                    full = false;
                }
            }

            if(full){
                sum++;
            }
        }
        return sum;
    }

    public int getHoles(BoundedGrid<Block> g){
        int sum = 0;
        List<Location> locs = g.getOccupiedLocations();
        for(Location l: locs){
            Location down = new Location(l.getRow()+1, l.getCol());
            if(g.isValid(down)){
                if(g.get(down)==null){
                    sum++;
                }
            }
        }
        return sum;
    }

    public int getBumpiness(BoundedGrid<Block> g){
        int sum = 0;
        for(int i = 0; i<g.getNumCols()-1; i++){
            sum+=Math.abs(getColHeight(i, g)-getColHeight(i+1, g));
        }   
        return sum;
    }

    public boolean getHeavySide(BoundedGrid<Block> g){//true is left, false is right
        int left = 0;
        int right = 0;
        for(int i = 0; i<g.getNumCols()/2; i++){
            left+=getColHeight(i, g);
        } 
        for(int i = g.getNumCols()/2; i<g.getNumCols(); i++){
            right+=getColHeight(i, g);
        }   

        return left>right;
    }

    public double getGridScore(BoundedGrid<Block> g){//returns AI-based value of grid
        double a = getAggHeight(g)*-0.510066;
        double b = getCompletedLines(g)*0.760666;
        for(int i = 0; i<g.getNumCols(); i++){
            if(getColHeight(i, g)>10){
                b*=2;
                break;
            }
        }
        double c = getHoles(g)*-0.35663;
        double d = getBumpiness(g)*-0.184483;
        return a+b+c+d;
    }

    public MoveList getMovesToMake(){

        List<MoveList> movesList = new ArrayList<MoveList>();

        for(int rot = 0; rot< 4; rot++){
            BoundedGrid<Block> temp = grid.getEquivGrid();
            MoveList tmove;
            List<Move> actions = new ArrayList<Move>();
            Tetrad active = new Tetrad(temp);
            active.setShape(activeTetrad.getShape());
            active.SpawnTetrad();
            active.translate(2,0);
            actions.add(Move.DOWN);actions.add(Move.DOWN);
            for(int i = 0; i<rot; i++){
                active.rotate(); 
                actions.add(Move.UP);    
            }
            int x = (getHeavySide(temp))?1:-1;
            while(active.translate(0,x)){
                actions.add((x==-1)?Move.LEFT:Move.RIGHT);
            }
            Location[] testpos = active.removeBlocks();
            Tetrad test = new Tetrad(temp);

            while(Location.posValid(temp, testpos, 0,x*-1)){

                test = new Tetrad(temp, testpos, Color.RED);
                test.SpawnTetrad();

                while(test.translate(1,0)){}
                actions.add(Move.SPACE);

                movesList.add(new MoveList(actions, getGridScore(temp)).getEquivMoveList());
                actions.remove(actions.size()-1);
                test.removeBlocks();
                Location[] newPos = new Location[testpos.length];

                if(Location.posValid(temp, testpos, 0,x*-1)){
                    newPos = Location.moveLocsOver(testpos, 0,x*-1);
                }

                active = new Tetrad(temp, newPos, Color.RED);
                active.SpawnTetrad();

                actions.add((x==1)?Move.LEFT:Move.RIGHT);
                testpos = active.removeBlocks();

            }

            test = new Tetrad(temp, testpos, Color.RED);
            test.SpawnTetrad();

            while(test.translate(1,0)){}
            actions.add(Move.SPACE);

            movesList.add(new MoveList(actions, getGridScore(temp)).getEquivMoveList());
            actions.remove(actions.size()-1);
            test.removeBlocks();

        }
        Collections.sort(movesList, new Comparator<MoveList>(){
                public int compare(MoveList a, MoveList b){
                    if(a.getValue()-b.getValue()==0){
                        return 0;
                    }
                    return (a.getValue()>b.getValue())? -1:1;
                }
            });

        return movesList.get(0);
    }

    public void makeMove(MoveList moves){
        Move[] plz = new Move[]{Move.DOWN,Move.DOWN,Move.LEFT,Move.UP,Move.SPACE}; 
        for(Move m: moves.getList()){
            switch(m){
                case UP:
                upPressed();
                break;
                case DOWN:
                downPressed();
                break;
                case LEFT:
                leftPressed();
                break;
                case RIGHT:
                rightPressed();
                break;
                case SPACE:
                spacePressed();
                break;

            }
            display.showBlocks();
            try{Thread.sleep(gameTime/10);}catch(Exception e){};

        }
    }
}