package robot;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/* 
 * For version serverHardware2.py
 */
public class Main{
    
    static Clip clip;
    
    static AudioT audioT;
	
	static int patrolWaitFrames = 0;
	static int FRAMES_TO_PATROL = 10; //speed
	static final int CENTERX = 90;
	static final int CENTERY = 90;
	static final int LEFT = 30;
	static final int RIGHT = 150;
	static final int TOP = 150;
	static final int BOT = 30;
	static final int STEP = 30; // viewing angle of the camera
	
	static int[][] patrolWay = {
	                         {CENTERX,        CENTERY},
	                         {CENTERX-STEP,	  CENTERY},
							 {CENTERX+STEP,   CENTERY},
							 
							 {CENTERX,        CENTERY+STEP},
							 {CENTERX-STEP,   CENTERY+STEP},
							 {CENTERX+STEP,   CENTERY+STEP},
							 
							 {CENTERX,        CENTERY-STEP},
                             {CENTERX-STEP,   CENTERY-STEP},
                             {CENTERX+STEP,   CENTERY-STEP},
                             
                             {LEFT,           TOP},
                             {CENTERX-STEP,   TOP},
                             {CENTERX,        TOP},
                             {CENTERX+STEP,   TOP},
                             {RIGHT,          TOP},
                             
                             {RIGHT,          CENTERY+STEP},
                             {LEFT,           CENTERY+STEP},
                             
                             {LEFT,           CENTERY},
                             {RIGHT,          CENTERY},
                             
                             {RIGHT,           CENTERY-STEP},
                             {LEFT,          CENTERY-STEP},
                             
                             {LEFT,           BOT},
                             {CENTERX-STEP,   BOT},
                             {CENTERX,        BOT},
                             {CENTERX+STEP,   BOT},
                             {RIGHT,          BOT},
                            };
	
    public static void main(String[] args) {
        
        if (args.length == 1)
        {
        	FRAMES_TO_PATROL = Integer.parseInt(args[0]);
        }
    	try{
            File soundFile = new File("/home/user/Downloads/start.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            
            clip = AudioSystem.getClip(null);
            
            
            clip.open(ais);
            
            clip.setFramePosition(0);
            clip.start();
            Thread.sleep((clip.getMicrosecondLength() / 1000) +1);
            clip.stop();
            clip.close();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc)
        {
            exc.printStackTrace();
        } catch (InterruptedException exc) {
        }
        audioT = new AudioT(false, 2);
        audioT.start();
    

       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
       VideoCapture camera = new VideoCapture(0);
       Mat frame = new Mat();
		camera.set(38, 1.0);
       camera.read(frame); 
       CascadeClassifier faceCascade = new CascadeClassifier("face.xml");

       CVShow show = new CVShow(frame);
       show.setVisible(true);
       System.out.println("DETECT START");
       try{
    	   while(true){
    		   for(int i=0; i<patrolWay.length; i++){
				   AngleWriter.setAbsoluteX(patrolWay[i][0]);
				   AngleWriter.setAbsoluteY(patrolWay[i][1]);
				   
				   patrolWaitFrames = 0;
	    		   while(patrolWaitFrames != FRAMES_TO_PATROL){
	    			   if(!detect(camera,show,frame,faceCascade)){
	        			   patrolWaitFrames ++;
	        			   
	        		   } else {
	        			   patrolWaitFrames = 0;
	        			   i=0;
	        		   }
//	    			   System.out.println(patrolWaitFrames);
	    		   }
			   }
    	   }   
       } 
       finally {
    	   camera.release();
       }
    }
    
    private static boolean detect(VideoCapture camera,CVShow show,Mat frame,CascadeClassifier cascade){
        
		camera.read(frame);
		MatOfRect faces = new MatOfRect();
		Mat greenFrame = new Mat();
		Imgproc.cvtColor(frame, greenFrame, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(greenFrame, greenFrame);       	   
		cascade.detectMultiScale(greenFrame, faces, 1.2, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(30,30),new Size(greenFrame.width(), greenFrame.height()));
		Rect[] facesArray = faces.toArray();
		for (int i = 0; i < facesArray.length; i++){
			Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 255, 0, 255), 3);
		}
		show.setFrame(frame);   
        if (facesArray.length > 0) {   
           if(!audioT.isAlive())
           {
               audioT = new AudioT(true, 3);
               audioT.start();
           }
     	   int delX = (int)(0.053622*(facesArray[0].x + facesArray[0].width/2 - frame.width()/2));
     	   AngleWriter.setRelativeX(delX);
     	        
     	   int delY = -(int)(0.053622*(facesArray[0].y + facesArray[0].height/2 - frame.height()/2));
     	   AngleWriter.setRelativeY(delY);
     	   
     	   return true;
        }
        if(!audioT.isAlive())
        {
            audioT = new AudioT(false, 3);
            audioT.start();
        }
        return false;
    }
}
