import java.io.*;
import sun.audio.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;
/**
 * Write a description of class MusicPlayer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MusicPlayer
{

    private String music = "Tetris.mp3";
    private boolean muted = false;
    private double length;
    JFXPanel fxPanel;
    Media hit;
    MediaPlayer mediaPlayer;
    public MusicPlayer(String s){
        music = s;
        muted = false;
        fxPanel = new JFXPanel();
        hit = new Media(new File(music).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
    }

    public void setMusic(String s, double l){
        music = s;
        length = l;
    }

    public void setMuted(boolean m){
        muted = m;
    }

    public String getMusic(){
        return music;
    }

    public boolean getMuted(){
        return muted;
    }

    public double getLength(){
        return length;
    }

    public void play(){
        if(!muted){
            /**
            try{
            InputStream in = new FileInputStream(music); 
            as = new AudioStream(in);
            AudioPlayer.player.start(as);
            }catch(IOException e){
            System.out.println("Baka");
            }*/

            mediaPlayer.play();
            int x = 0;
        }
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public void stopMusic(){

        mediaPlayer.stop();
    }
}