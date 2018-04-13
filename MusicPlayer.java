import java.io.*;
import sun.audio.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;
import javafx.util.Duration;
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
        length = mediaPlayer.getTotalDuration().toMillis();
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(mediaPlayer.INDEFINITE);
    }

    public void setMusic(String s){
        music = s;
        stopMusic();
        hit = new Media(new File(music).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        length = mediaPlayer.getTotalDuration().toMillis();
    }

    public void setMuted(boolean m){
        muted = m;
        mediaPlayer.setMute(m);
    }

    public String getMusic(){
        return music;
    }
    
    public String getStatus(){
        return ""+mediaPlayer.getStatus();
    }

    public boolean getMuted(){
        return muted;
    }

    public double getLength(){
        return length;
    }
    public void setStopTime(Duration x){
        mediaPlayer.setStopTime(x);
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
        }
    }
    public void pause(){
        mediaPlayer.pause();
    }
    public void stopMusic(){

        mediaPlayer.stop();
    }
}