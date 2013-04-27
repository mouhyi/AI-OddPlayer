public class trry {
 
	
	static int toRet;
	
	
	public static void main(String[] args) {
	    int z = getMove();
	    System.out.println(z);
	}
	
	public static int getMove() {
		toRet = 0;
		Thread t = new Thread () {
			public void run() {
			    while(true) toRet = toRet++ / 5; //Change this with your code
			}
		};
		t.start();		
		try {
			long start = System.currentTimeMillis();
			Thread.sleep(1900); //this makes getMove sleep
			System.out.println(System.currentTimeMillis() - start);
		}catch (Exception e) {
			t.stop();
		}
		t.stop();
		return toRet;
	}
}
