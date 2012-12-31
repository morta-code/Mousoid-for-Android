package hu.morta.android.mousoid;

public class GesturePattern {
	
	public static void load(int key){
		instance = new GesturePattern(key);
	}
	
	public static void clear(){
		instance = null;
	}
	
	
	public static int getKey(float[] values) {
		values0 = values1.clone();
		values1 = values2.clone();
		values2 = values.clone();
		if(instance == null)
			return 0;
		if(values2[1] > -1.5 || values1[1] > -5)
			return 0;
		if(values0[0] < -1 &&
				values0[1] < -2 &&
				values1[0] > 4)
			return instance.Key;
		
		return 0;
	}

	private static GesturePattern instance = null;
	
	private int Key;
	private static float[] values0 = {0,0,0};
	private static float[] values1 = {0,0,0};
	private static float[] values2 = {0,0,0};
	
	private GesturePattern(int key) {
		Key = key;
	}

}
