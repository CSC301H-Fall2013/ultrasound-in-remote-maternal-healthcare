package csc301.ultrasound.global;

/**
 * Contains math related utility methods.
 */
public class MathUtil
{
	/**
	 * Clamp x to within a specified range.
	 *
	 * @param x The value to clamp.
	 * @param min The minimum value of the range.
	 * @param max The maximum value of the range.
	 * @return The clamped value.
	 */
	public static double clamp(double x, double min, double max)
	{
		if (x > max)
			return max;
		else if (x < min)
			return min;
		else
			return x;
	}
	
	/**
	 * Map x from range(A, B) to range(C, D).
	 *
	 * @param x The value to map to the new range.
	 * @param in_min The minimum value of the input range.
	 * @param in_max The maximum value of the input range.
	 * @param out_min The minimum value of the output range.
	 * @param out_max The maximum value of the output range.
	 * @return The remapped x.
	 */
	public static double map(double x, double in_min, double in_max, double out_min, double out_max)
	{
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
}
