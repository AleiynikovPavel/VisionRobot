package robot;

public class Timer {

	private static int wait = 0;
	private static int counter = 0;
	
	public static boolean isWait(int framesWait){
		wait = framesWait;
		if(counter % wait == 0){
			counter = 0;
			return false;
		} else {
			counter++;
			return true;
		}
	}

}
