package utils;

import java.util.Random;

/**
 * A very good random number generator.
 * Uses the Mersenne Twister algorithm to get very good pseudo-random numbers.
 * See <a href="http://en.wikipedia.org/wiki/Mersenne_Twister">this Wikipedia article<a/> for more infos.
 * Parts of the C code there was ported to Java and used here.
 * @author kleinesfilmroellchen
 * @since 0.0.0004
 * @version 0.0.0008
 *
 */
public class BetterRandom extends Random {
	/**
	 * Needed for some strange reason
	 */
	private static final long serialVersionUID = -8791807566136137568L;
	/**
	 * Minimum amount of iterations to "warm up" the generator 
	 */
	private static final long WARMUP_ITERATIONS = 800000;
	/**
	 * Stack size for precalculated random numbers
	 */
	private static final int N = 624;
	/**
	 * Half of stack size
	 */
	private static final int M = 397;
	
	/**
	 * Main variable - contains current stack of precalculated random numbers
	 */
	private int[] y;
	private int idx = N+1;
	
	/**
	 * Initializes the random number generator with ~800.000 cycles.
	 * It has to be a different amount each time for some reason.
	 */
	public BetterRandom() {
		long iterations = WARMUP_ITERATIONS + System.currentTimeMillis() + (long)(Math.rint(Math.random() * 20));
		//Keeps iterations in reasonable area, but not clamp them to some fixed number (PRNG would become predictable)
		iterations = BetterMath.clamp(iterations, (long)(WARMUP_ITERATIONS * new Random().nextFloat()*2+3), WARMUP_ITERATIONS);
		
		for (int i = 0; i < iterations; i++) {
			this.nextInt();
		}
	}
	
	/**
	 * Updates the array completely and therefore calls the random number generator 624 times.
	 * The complex operations are there to avoid time-intense modulo operations.
	 */
	private void arrayUpdate() {
		int[] A = { 0, 0x9908B0DF };
		int i = 0;
		
		for (; i < N-M; i++)
			y[i] = y[i+(M  )] ^ (((y[i  ] & 0x80000000) | (y[i+1] & 0x7FFFFFFF)) >> 1) ^ A[y[i+1] & 1];
		for (; i < N-1; i++)
			y[i] = y[i+(M-N)] ^ (((y[i  ] & 0x80000000) | (y[i+1] & 0x7FFFFFFF)) >> 1) ^ A[y[i+1] & 1];
		y[N-1] = y[M-1]	      ^ (((y[N-1] & 0x80000000) | (y[0  ] & 0x7FFFFFFF)) >> 1) ^ A[y[0  ] & 1];
	}
	
	/**
	 * Simple pseudo-random numbers for starters to initialize.
	 * @return An array of 624 random numbers.
	 */
	private static int[] initialize() {
		int[] p = new int[N];
		int mult = 1812433253;
		int seed = 5489;
		
		for (int i = 0; i < N; i++) {
			p[i] = seed;
			//new number (this is like the simplest PRNG in the world)
			seed = mult * (seed ^ (seed >> 30)) + (i+1);
		}
		
		return p;
	}
	
	/**
	 * Generates the next pseudorandom number while updating the array, if needed.
	 * In the following the original documentation is present:<br><br>
	 * {@inheritDoc}
	 */
	@Override
	public int next(int bits) {
		int e;
		
		//new array has to be calculated or the whole one initialized
		if (idx >= N) {
			if (idx > N)
				y = initialize();
			arrayUpdate();
			idx = 0;
		}
		
		e  = y[idx++];
		e ^= (e >> 11);
		e ^= (e <<  7) & 0x9D2C5680;
		e ^= (e << 15) & 0xEFC60000;
		e ^= (e >> 18);
		
		//on default returns 32 bits - change to traditional next function
		return e >>> (31 - bits);
	}
	
	/* Because next() follows all conventions of the Random class,
	 * no further function overriding is necessary.
	 */
}
