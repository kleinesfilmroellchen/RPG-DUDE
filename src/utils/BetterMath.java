package utils;

// <>|

/**
 * A class for all needed math functions that aren't supplied with the default
 * {@code Math} class.
 * @author kleinesfilmroellchen
 * @see java.lang.Math Default math class
 * @since 0.0.0005
 * @version 0.0.0008
 */
public class BetterMath {

	private static BetterRandom generator = new BetterRandom();

	/**
	 * Clamps a value between a minimum and maximum.
	 * @param v The value to be clamped
	 * @param max Maximum
	 * @param min Minimum
	 * @return The clamped value
	 */
	public static int clamp(int v, int max, int min) {
		if (v < min) return min;
		if (v > max) return max;
		return v;
	}

	/**
	 * Clamps a value between a minimum and maximum. (same as other clamp, but for
	 * long)
	 * @param v The value to be clamped
	 * @param max Maximum
	 * @param min Minimum
	 * @return The clamped value
	 */
	public static long clamp(long v, long max, long min) {
		if (v < min) return min;
		if (v > max) return max;
		return v;
	}

	/**
	 * Squares the number.
	 */
	public static float sq(float f) {
		return f * f;
	}

	/**
	 * Squares the number.
	 */
	public static double sq(double f) {
		return f * f;
	}

	/**
	 * Returns the factorial of the given number.
	 */
	public static int fact(int n) {
		return n == 0 ? 1 : fact(n - 1) * n;
	}

	/**
	 * Returns the factorial of the given number.
	 */
	public static long fact(long n) {
		return n == 0 ? 1 : fact(n - 1) * n;
	}

	/**
	 * Produces a pseudorandom number between the two limits. Does not guarantee
	 * useful results when the upper limit is lower than the lower limit.
	 * @param lower The lower limit of the random numbers returned, inclusive.
	 * @param upper The upper limit of the random numbers returned, inclusive.
	 */
	public static double random(double lower, double upper) {
		return lower + (generator.nextDouble() * (upper - lower));
	}

	/**
	 * Produces a pseudorandom number between the two limits. Does not guarantee
	 * useful results when the upper limit is lower than the lower limit.
	 * @param lower The lower limit of the random numbers returned, inclusive.
	 * @param upper The upper limit of the random numbers returned, inclusive.
	 */
	public static float random(float lower, float upper) {
		return lower + (generator.nextFloat() * (upper - lower));
	}

	/**
	 * Picks a random element from the given array. Any element (any type and even
	 * {@code null} values) returned by the array's iterator can be returned. <br>
	 * <br>
	 * Note that this uses the {@code BetterMath.random(float, float)} method to
	 * pick indices.
	 * @param array The array to pick from.
	 * @return A random element from the given array.
	 * @see utils.BetterMath#random(float, float)
	 */
	public static <T> T random(T[] array) {
		return array[Math.round(random(0f, array.length - 1f))];
	}
}
