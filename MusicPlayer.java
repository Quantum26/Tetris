import java.io.*;
import sun.audio.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
/**
 * Write a description of class MusicPlayer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MusicPlayer
{
    private String music = "Tetris.wav";
    private AudioStream as;
    private boolean muted = false;
    private double length;
    public MusicPlayer(String s, double l){
        music = s;
        muted = false;
        length = l;
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
    public void music(){
        if(!muted){
            AudioPlayer MGP = AudioPlayer.player;
            AudioStream BGM;
            AudioData MD;
            ContinuousAudioDataStream loop = null;
            try{
                InputStream in = new FileInputStream(music); 
                as = new AudioStream(in);
                AudioPlayer.player.start(as);
            }catch(IOException e){
                System.out.println("Baka");
            }
            
        }
    }

    public void stopMusic(){
       
        try
        {
            //don't try and do things with a null object!
            if (as != null)
            {
                AudioPlayer.player.stop(as);
            }
        }
        catch (NullPointerException e)
        {
            System.err.println(e);
        }
    }
}
