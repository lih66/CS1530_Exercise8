import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Pi {

    public static long valuesInside(long n){
        long inside = 0;
        double x, y;
        for (long i = 0; i < n; i ++){
            // get random double
            x = ThreadLocalRandom.current().nextDouble(0.000, 1.000);
            y = ThreadLocalRandom.current().nextDouble(0.000, 1.000);
            
            // check if the pint lies "inside"
            // x^2 + y^2 < 1
            if ((Math.pow(x, 2) + Math.pow(y, 2)) <1)
                inside++;
        }
        return inside;
    }
    
    public static void main(String[] args) {
	
		long numThreads = 0;			// number of threads
		long numIteration = 0;			// total number of iterations
		AtomicLong inside = new AtomicLong(0);	// total number of values "inside" the circle
	    double pi = 0;				// calculate value of pi

		// get number of threads
	    try{
	        numThreads = Long.parseLong(args[0]);
	        if (numThreads < 1)
	                throw new Exception();
	    } catch (Exception ex){
	        System.out.println("Expected positive int as argument!");
	        System.exit(1);
	    }

	    // get number of iterations
	    try{
	        numIteration = Long.parseLong(args[1]);
	        if (numIteration < 1)
	                throw new Exception();
	    } catch (Exception ex){
	        System.out.println("Expected positive int as argument!");
	        System.exit(1);
	    }

	    // calculate iterationPerThread
	    final long iterationPerThread = numIteration / numThreads;		

	    // create threads
	    Thread[] ts = new Thread[(int)numThreads];

	    for (int j = 0; j < (int)numThreads; j++){
	        final int fj = j;
	        long finside = 0;
	        ts[fj] = new Thread(() ->
	            {
	                inside.addAndGet(valuesInside(iterationPerThread));
	            });
	    }    

		try {
		    for (Thread t : ts)	
		    	t.start();
		    for (Thread t : ts)
		    	t.join();
		} catch (InterruptedException iex) {}

		// Print out values
		System.out.println("Total \t = " + numIteration);
		System.out.println("Inside \t = " + inside.get());
		double ratio = (double)inside.get() / numIteration;
		System.out.println("Ratio \t = " + ratio);
		System.out.println("Pi \t = " + ratio * 4.00);
    }	
}