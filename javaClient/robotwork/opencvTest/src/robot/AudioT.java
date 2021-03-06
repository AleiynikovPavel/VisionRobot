package robot;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class Setting {
    public static File soundFile7 = new File("/home/user/Downloads/start.wav");
    public static File soundFile1 = new File("/home/user/Downloads/catch1.wav");
    public static File soundFile2 = new File("/home/user/Downloads/catch2.wav");
    public static File soundFile3 = new File("/home/user/Downloads/catch3.wav");
    public static File soundFile4 = new File("/home/user/Downloads/search1.wav");
    public static File soundFile5 = new File("/home/user/Downloads/search2.wav");
    public static File soundFile6 = new File("/home/user/Downloads/search3.wav");
    
    public static File file(Boolean state, int i)
    {
        File f = null;
        if (!state)
        { 
           
            switch(i)
            {
                case 0:
                    
                    f = soundFile4;
                    
                    break;
                case 1:
              
                    f = soundFile5;
                    break;
                case 2:
             
                    f = soundFile6;
                    break;
            }
        } else {
            switch(i)
            {
                case 0:
                    f = soundFile1;
                    break;
                case 1:
                    f = soundFile2;
                    break;
                case 2:
                    f = soundFile3;
                    break;
            }
        }
        return f;
    }
}

public class AudioT extends Thread
{
        Setting setting;
        Clip clip;
        static int count = 0;
        boolean state = false;
        
       
    public AudioT(Boolean s, int c) {
        state = s;
        count = c;
        }


    public void run()  
    {
            try {

                Random rnd = new Random(System.currentTimeMillis());
                int number = rnd.nextInt(count);
            AudioInputStream ais = AudioSystem.getAudioInputStream(Setting.file(state, number));

            clip = AudioSystem.getClip(null);

            clip.open(ais);

            clip.setFramePosition(0); 
            clip.start();

            Thread.sleep(clip.getMicrosecondLength() / 1000);
            clip.stop();
            clip.close();

            Thread.sleep(1000);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        } catch (InterruptedException exc) {
        }
    }
}
