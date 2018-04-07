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
    private boolean raining = false;
    private MusicPlayer music;
    private boolean sped = false;
    private long elapsed;
    private int meteors = 0;
    private int lives = 3;
    private List<Tetrad> storm;
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
        storm = new ArrayList<Tetrad>();
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
            display.showBlocks();//shows the block again
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
            display.showBlocks();//show the blocks again
        }
    }

    public void play()
    {
        game = true;//sets the fact that you aer still going to true
        activeTetrad.SpawnTetrad();//spawns the active tetrad
        DisplayNextTetrad();//displays information about your next tetrad on the screen

        music.music();//starts the music
        while (game)//while the game is being played
        {
            elapsed = System.currentTimeMillis()-start;//elapsed time since game started
            if(elapsed%music.getLength()>=1000&&elapsed>=music.getLength() && !music.getMuted()){//if the song is over
                music.music();//plays the music again
                start = System.currentTimeMillis();//resets the start time of the music
            }
            for(int i = 0; i<5; i++){
                try { Thread.sleep(gameTime/10); } catch(Exception e) {}//waits for amount of time
                elapsed = System.currentTimeMillis()-start;//gathers elapsed time since game started
            }
            if(!paused){//if the game is not paused
                
                for(int i = 0; i<5; i++){
                    try { Thread.sleep(gameTime/10); } catch(Exception e) {}//completes one interval for block to fall due to gravity
                    
                    elapsed = System.currentTimeMillis()-start;//elapsed time since game started
                }
                if(!dejavu){//if you are in normal mode
                    if(!activeTetrad.translate(1,0)){//translates tetrad down if it can, for gravity
                        controlsActive = false;//deactivates controls so game can deal with what needs to happen properly
                        gameTime = time;//resets time for block to fall
                        if(!topRowsEmpty()){//if the top rows are not empty
                            game = false;//the game is over
                            gameOver();//indicates to player game is over
                            break;//stops game
                        }
                        clearCompletedRows();//clears all rows that are complete
                        activeTetrad = nextTetrad;//sets new tetrad to what was next
                        activeTetrad.SpawnTetrad();//spawns the new tetrad
                        nextTetrad = new Tetrad(grid);//makes the next tetrad a new tetrad
                        if(cheatCode1){//if you have a cheatcode
                            nextTetrad.setShape(0);//gives you automatic "I" block
                        }else if(cheatCode2){//another cheatcode
                            nextTetrad.setShape(2);//gives you an "O" block
                        }
                        DisplayNextTetrad();//displays information
                        controlsActive = true;//now the user gets their controls back
                    }
                }else{//in the dejavu game mode
                    gameTime = time;
                    if(!nextTetrad.translate(1,0)){//nextTetrad is a meteor, drops meteor if possible
                        if(elapsed>=64000 && reee){//if the beat drop is here
                            ree();//activates intense display mode
                            reee = !reee;//sets boolean for intense display mode to not
                        }
                        if(nextTetrad.isNextToSomething()){//if the meteor dropped next to you
                            score+=20*level;//get extra points
                        }
                        controlsActive = false;//to deal with block deactivaes controls
                        gameTime = time;//resets time
                        if(!nextTetrad.isOnGround()){//if the meteor fell on you
                            lives--;//lose one life
                        }
                        if(lives<=0){//if you dont have any lives
                            game = false;//game is over
                            gameOver();//display the game is over
                            break;//stop the game
                        }
                        nextTetrad.removeBlocks();//removes the meteor once it hits the ground
                        nextTetrad = new Tetrad(grid);//makes a new tetrad
                        nextTetrad.setShape(9);//sets the new tetrad to a meteor
                        nextTetrad.SpawnTetrad();//spawns the meteor
                        int n = activeTetrad.getLocations()[0].getCol();//gets the column your block is in
                        n += (Math.random()<0.5)?(-1*(int)(Math.random()*2)):((int)(Math.random()*2));//translates the meteor so it is above your block
                        nextTetrad.translateToCol(n);//translates the meteor to above you
                        DisplayNextTetrad();//displays game information
                        controlsActive = true;//user gets controls back
                        meteors++;//amount of meteors cleared goes up
                        if(meteors%10==0){//if you have completed a multiple of 10 meteors
                            level++;//level up
                            time-=2;//game gets faster
                            if(time<20){//caps time to 20, way too fast after that
                                time = 20;//time is 20
                            }
                        }
                        if(level>=8){//intense display starts here
                            System.out.println("SEIZURE WARNING!!!!!!!!");//hopefully you wont, but need the warning for a seizure
                        }
                        score+=20*level;//increments score, multiplier of level you are on
                    }
                }
                if(game==false){//if the game is over
                    break;//stop the game if you havent already
                }
                display.showBlocks();//show information in console
                title = "Level "+level+", Score: "+score;//show basic information in title of window
                display.setTitle(title);//sets the title to basic information
                if(reee)//if you want intense display mode
                    ree();//activates intense game mode(dont do it)
            }
            if(elapsed >=24000 && sped){//if beat drop for "running in the 90s" mode
                time = 25;//very fast game
                gameTime = 25;//sets drop speed to way too fast
                sped = false;//sets this script to completed
                display.setRee(true);//sets intense display mode
            }
        }
    }

    //precondition:  0 <= row < number of rows
    //postcondition: Returns true if every cell in the
    //               given row is occupied;
    //               returns false otherwise.
    private boolean isCompletedRow(int row)
    {
        boolean full = true;//row is full until proven not
        for(int i = 0; i < 10; i++){//for 10 blocks in a row
            Location l = new Location(row, i);//makes temporary location to check
            if(grid.get(l)==null){//if there is nothing in that location on the grid
                full = false;//the row is not full
            }
        }
        return full;//returns if the row is full
    }

    //precondition:  0 <= row < number of rows;
    //               given row is full of blocks
    //postcondition: Every block in the given row has been
    //               removed, and every block above row
    //               has been moved down one row.
    private void clearRow(int row)
    {
        for(int i = 0; i<10; i++){//for all the blocks in a row
            Location l = new Location(row, i);//temporary location to delete
            if(grid.get(l)!=null)//if there is something in the locatoin
                grid.remove(l).removeSelfFromGrid();//remove it

        }
    }

    //postcondition: All completed rows have been cleared.
    private void clearCompletedRows()
    {
        int rowsBroke = 0;//how many rows have you broken
        for(int i = 19; i > 0; i--){//goes through all 20 rows
            if(isCompletedRow(i)){//if it is a completed row
                flashRow(i);//flash the row to indicate it is broken
                clearRow(i);//clear the row
                moveDownAbove(i);//move everything above the row down
                i = 20;//reset line to 20 to recheck everything
                rowsBroke++;//you broke one more line
                rowsDone++;//you have completed one more line
                lines++;//you have completed one more line
            }

        }

        int scor = 0;//score to add
        if(rowsBroke == 1){//if you only broke one row
            scor = 100*level;//100 points for row, multipled by level
            tettet = false;//did not get tetris
            combo++;//combo goes up
        }else if(rowsBroke == 2){//if you broke two rows
            scor = 300*level;//300 points
            tettet = false;//did not get tetris
            combo++;//ups combo
        }else if(rowsBroke == 3){//if you got 3 rows
            scor = 500*level;//500 points 
            tettet = false;//did not get tetris
            combo++;//combo goes up
        }else if(rowsBroke == 4 && tettet){//if you have a tetris before this and just got a tetris
            scor = 1600*level;//1600 points 
            tettet = true;//you got a tetris
            combo++;//combo goes up
        }else if(rowsBroke == 4){//if you only got a tetris
            scor = 800*level;//800 points
            tettet = true;//you have a tetris
            combo++;//combo goes up
        }else{
            tettet = false;//you did not get a tetris
            combo = 0;//combo is broken
        }
        if(combo>0){//if you have a combo
            scor += 50*combo*level;//extra points for the combo
        }
        score+= scor;//increments your score
        if(rowsDone>=10){//if more than 10 rows are completed
            if(time>200){//if gametime is more than 200
                level++;//level goes up
                time-= 100;//time goes down by a 10th of a second
                rowsDone -= 10;//rowsdone since leveling up goes down by 10
            }else if (time>75){//if time is less than 200 but more than 75
                level++;//level up
                time-= 25;//only go down by 0.025 of a second
                rowsDone -= 10;//rows done since leveling up goes down by 10
            }else if(level>=1999){//if running in the 90s mode or you somehow made it to this level
                level=1990;//you cant pass 2000 sorry and you must continue running in the 90s
                rowsDone -=10;//rows done since leveling up goes down by 10
            }else{//if you are already at an insanely fast speed
                //speed cant go up anymore
                level++;//level up
                rowsDone -= 10;//rows done since leveling up goes down by 10
            }
        }
        display.showBlocks();//shows blocks after rows cleared
    }

    private void moveDownAbove(int r){
        if(r<1){//if it is top row
            return;//dont do anything
        }
        List<Location> locs = grid.getRow(r);//get locations of the row specified
        for(Location l: locs){//for all of those locations
            Location x = new Location(l.getRow()+1, l.getCol());//new locaiton to go to
            if(grid.isValid(x)&&grid.get(x)==null){//if block can move down
                grid.remove(l).moveTo(x);//moves block down
            }
        }
        moveDownAbove(r-1);//uses recursion to do this to all the rows above current row
    }

    public void flashRow(int row){
        Color[] colors = new Color[10];//keeps original colors
        for(int k = 0; k<2; k++){//does this process twice
            for(int i = 0; i< 10; i++){//for the entire row
                Location l = new Location(row, i);//location to look at
                if(grid.get(l)!=null){//if something is there
                    colors[i] = grid.get(l).getColor();//sets original color in the array storing original colors
                    grid.get(l).setColor(new Color(0, 0, 0));//sets color to black to simulate flashing
                }

            }
            display.showBlocks();//shows what you display should look like
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}//waits for a little bit
            for(int i = 0; i< 10; i++){//for all of the locations
                Location l = new Location(row, i);//current location to look at
                if(colors[i]!=null && grid.get(l)!=null)//if something is there
                    grid.get(l).setColor(colors[i]);//set back to its original color

            }
            display.showBlocks();//shows what blocks should look like
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}//waits a little
        }
    }

    //returns true if top two rows of the grid are empty (no blocks), false otherwise
    private boolean topRowsEmpty()
    {
        boolean top = true;
        boolean atop = true;
        for(int i = 0; i<10; i++){//looks at top row
            Location l = new Location(0, i);//location in top row
            if(grid.get(l)!=null){//if something is there
                top = false;//top row is not empty
            }
        }
        for(int i = 0; i<10; i++){//for all elements in second to top row
            Location l = new Location(1, i);//looks at second to top row
            if(grid.get(l)!=null){//if something is there
                atop = false;//second top row is not empty
            }
        }
        return top && atop;//returns if both rows are empty
    }

    private void gameOver(){
        for(int r = 0; r < 20; r++){//for all of the rows in the grid
            for(int c = 0; c < 10; c++){//for all of the columns
                Location l = new Location (r, c);//location to look at
                if(grid.get(l)!=null){//if something is there
                    grid.get(l).setColor(Color.RED);//set it to red, to indicate you died

                }
            }
        }
        display.showBlocks();//shows blocks
        music.stopMusic();//stops music
        title+= " You lose m8";//tells you you lost in title
        display.setTitle(title);//resets title
        DisplayNextTetrad();//displays information
    }

    private void ree(){//this should not exist
        List<Location> locs = grid.getOccupiedLocations();//for all the locaitons
        display.setRee(true);//sets display to go into intense display mode
        for(Location l : locs){//for all of the locations
            grid.get(l).setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
            //sets block to a random color
        }
    }

    private void unree(){//stop the seizure 
        List<Location> locs = grid.getOccupiedLocations();//for all of the locations
        display.setRee(false);//sets display back to normal
        for(Location l : locs){//for all the locations
            grid.get(l).setColor(grid.get(l).getOriginal());//sets blocks to original color

        }
    }

    public void DisplayNextTetrad(){//information about level
        for(int i = 0; i<16; i++)
            System.out.println();//prints extra lines so information is next to display
        if(!dejavu){//if not in deja vu mode
            System.out.println("Level: " + level + "\nLines: " + lines + "\nScore: " + score + 
                "\nNext:");//displays tetris game information
        }else{//if you are in deja vu mode
            System.out.print("\nLevel: " + level + "\nLives: ");//shows lives left
            for(int i = 0; i<lives; i++)
                System.out.print("[]");//indicates how many lives are left
            System.out.print("\nScore: " + score + //shows score
                "\nNext:");
        }
        if(nextTetrad.getShape()==0){//for all the shapes you can have, shows what they are
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
            System.out.println("ya done mate\n\n\n");//smiley  face death tetrad
        }else{
            System.out.println("Not real block");//if you have an extra block that should not be in the game
        }

        if(sped){//if you are in fast mode
            System.out.println("SEIZURE WARNING");//you might get a seizure
        }
    }

    public void stockStorm(){//does nothing as of now
        for(int i = 0; i<4; i++){
            Tetrad temp = new Tetrad(grid);//new tetrad
            temp.setShape(9);//sets to a meteor
            temp.translate(0,((Math.random()<0.5)?((int)(Math.random()*6)):(-1*(int)(Math.random()*6))));// translates some amount
            storm.add(temp);//adds to storm
        }
    }   

    public void onePressed(){
        music.stopMusic();//stops current music
        music = new MusicPlayer("Tetris.wav", 82000.0);//plays tetris
        start = System.currentTimeMillis();//start of song is current time
        music.music();//plays music
    }

    public void twoPressed(){
        music.stopMusic();//stops current music
        music = new MusicPlayer("Boosted.wav", 82000.0);//plays a song you do not want to hear
        start = System.currentTimeMillis();//start of song is current time
        music.music();//plays music
    }

    public void threePressed(){//plays song dejavu
        music.stopMusic();
        music = new MusicPlayer("dejavu.wav", 273000.0);
        start = System.currentTimeMillis();
        music.music();
    }

    public void fourPressed(){//plays some electric song ian likes
        music.stopMusic();
        music = new MusicPlayer("Electric.wav", 191000.0);
        start = System.currentTimeMillis();
        music.music();
    }

    public void sPressed(){//activates running in the 90s mode
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
