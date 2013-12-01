package csc301.ultrasound.global;

/**
 * Contains general utility methods.
 */
public class Util
{
	/**
	 * Convert an integer to a boolean.
	 *
	 * @param v The value to convert.
	 * @return true, if successful. false otherwise.
	 */
	public static boolean intToBool(int v)
	{
		if (v == 0)
			return false;
		else
			return true;
	}
}
