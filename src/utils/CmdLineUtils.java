package utils;

import java.io.PrintStream;

public class CmdLineUtils {

	public final static String	BAR_FILL	= "#";
	public final static String	BAR_CAPS	= "|";

	/**
	 * Creates a percentage bar visualisation for progress etc.
	 * @param size the width of the percent bar in characters, not including the
	 * delimiter lines.
	 * @param percent the percentage to be represented. should be between 0 and 1.
	 * @return a string representation of the percentage bar.
	 */
	public static String createPercentBar(int size, float percent) {
		String ret = BAR_CAPS;
		int toFill = (int) (size * percent);
		for (; toFill > 0; --toFill, --size)
			ret += BAR_FILL;
		while (--size > 0)
			ret += " ";

		return ret + BAR_CAPS + " " + String.format("%,3.1f", percent * 100f);
	}

	/**
	 * Deletes characters equal to the percent bar, then reprints the percent bar
	 * onto the print stream.
	 * @param out the printstream to be used for this operation.
	 * @param size the width of the percent bar in characters, not including the
	 * delimiter lines.
	 * @param percent the percentage to be represented. should be between 0 and 1.
	 * @see CmdLineUtils#createPercentBar(int, float)
	 */
	public static void redoPercentBar(PrintStream out, int size, float percent) {
		String bar = createPercentBar(size, percent);
		// print backspaces - does not work in eclipse (14 year unfixed bug)
		for (int i = 0; i < bar.length(); ++i)
			out.print("\b");

		out.print(bar);
	}
}
