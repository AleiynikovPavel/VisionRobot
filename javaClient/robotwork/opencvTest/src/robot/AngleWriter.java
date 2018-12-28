package robot;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class AngleWriter {
	static int serverPort = 9090;
    static String address = "127.0.0.1";
    
    static int currentX = 90;
    static int currentY = 90;
    
    public static void init(){
    	setAbsoluteX(90);
    	setAbsoluteY(90);
    	currentX = 90;
    	currentY = 90;
    }
    
    public static void init(int x, int y){
    	setAbsoluteX(x);
    	setAbsoluteY(y);
    	currentX = x;
    	currentY = y;
    }

    public static void setAbsoluteX(int x) {
		setAngel(1, x);
		currentX = x;
	}
    
    public static void setAbsoluteY(int y) {
		setAngel(2, y);
		currentY = y;
	}
    
    public static void setRelativeX(int x){
    	if((x != 0)&&(x+currentX < 180)&&(x+currentX>0)){
    		currentX +=x;
    		setAbsoluteX(currentX);
    	}
    }
    
    public static void setRelativeY(int y){
    	if((y != 0)&&(y+currentY < 180)&&(y+currentY>0)){
    		currentY +=y;
    		setAbsoluteY(currentY);
    	}
    }
    
    
    private static void setAngel(int servoNum, int angle) {
		try {
            InetAddress ipAddress = InetAddress.getByName(address);
            Socket socket = new Socket(ipAddress, serverPort);
            OutputStream sout = socket.getOutputStream();
            InputStream sin = socket.getInputStream();
            DataOutputStream out = new DataOutputStream(sout);
            DataInputStream in = new DataInputStream(sin);
			out.writeChars("r"+servoNum+angle);
            out.flush();
            out.close();
            sout.close();
            in.close();
            sin.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
