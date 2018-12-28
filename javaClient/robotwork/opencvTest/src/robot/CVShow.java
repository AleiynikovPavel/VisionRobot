package robot;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.opencv.core.Mat;


public class CVShow extends JFrame {

		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private JLabel pict;
		
	    public CVShow (Mat frame) {
	       setTitle("OpenCV");
	       setSize(frame.width(), frame.height());
	       JPanel p = new JPanel(); 
	       add(p); 
	       p.setLayout(new GridLayout(1,1)); 
	       pict = new JLabel("");
	       p.add(pict); 
	       setDefaultCloseOperation(EXIT_ON_CLOSE);
	    }
	    
	    public void setFrame(Mat frame) {
	    	pict.setIcon(new ImageIcon(matToBufferedImage(frame)));
	    }

		private BufferedImage matToBufferedImage(Mat frame) {
	        //Mat() to BufferedImage
	        int type = 0;
	        if (frame.channels() == 1) {
	            type = BufferedImage.TYPE_BYTE_GRAY;
	        } else if (frame.channels() == 3) {
	            type = BufferedImage.TYPE_3BYTE_BGR;
	        }
	        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
	        WritableRaster raster = image.getRaster();
	        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
	        byte[] data = dataBuffer.getData();
	        frame.get(0, 0, data);

	        return image;
	    }
}
