public class try2
{
 	
	public static void main(String[] args) {
	    long startTime = System.currentTimeMillis();
	    try {
	    Thread.sleep(4000 );
	    }catch(Exception e) {
			    }
	    System.out.println(System.currentTimeMillis() - startTime);
	}
}