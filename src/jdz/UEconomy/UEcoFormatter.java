
package jdz.UEconomy;

import jdz.bukkitUtils.misc.Pair;
import lombok.Getter;

public class UEcoFormatter {
	public static String charFormat(double value) {
		return charFormat(value, 6);
	}

	public static String engrFormat(double val, int sf) {
		if (val == 0)
			return "0";

		Pair<Double, Integer> valRadix = getValRadix(val);
		if (valRadix.getValue() < 3)
			return val + "";

		return String.format("%." + sf + "G e%d", valRadix.getKey(), valRadix.getValue());
	}

	public static String charFormat(double val, int sf) {
		if (val <= 0)
			return "0";

		sf = sf < 1 ? 1 : sf;

		Pair<Double, Integer> valRadix = getValRadix(val);
		if (valRadix.getValue() < 3)
			return val + "";

		CharNotation suffix = CharNotation.getByRadix(valRadix.getValue());
		if (suffix == CharNotation.NaN)
			return String.format("%." + sf + "G e%d", valRadix.getKey(), valRadix.getValue());
		return String.format("%." + sf + "G ", valRadix.getKey()) + suffix;
	}
	
	public static String makeWhole(String s) {
		return s.replaceFirst("\\.[0-9]+\\Z", "");
	}

	private static Pair<Double, Integer> getValRadix(double val) {
		if (val == 0)
			return new Pair<Double, Integer>(0D, 1);

		// If the value is negative, make it positive so the log10 works
		double posVal = Math.abs(val);
		double log10 = Math.log10(posVal);

		// Determine how many orders of 3 magnitudes the value is
		int radix = (int) Math.floor(log10 / 3) * 3;

		// Scale the value into the range 1<=val<1000
		val /= Math.pow(10, radix);
		return new Pair<Double, Integer>(val, radix);
	}

	public static double parse(String string) {
		if (string.length() < 1)
			return 0;

		string = string.replace("+", "");
		if (string.contains("e") | string.contains("E"))
			return Double.parseDouble(string);

		String suffix = string.replaceAll("[^a-zA-Z]+", "");
		String prefix = string.replace(suffix, "");

		if (suffix.equals(""))
			return Double.parseDouble(string);

		CharNotation notation = CharNotation.parse(suffix);
		return Double.parseDouble(prefix + notation.toEngr());
	}

	private static int nextRadix = 1;

	private static enum CharNotation {
		k, M, B, T, Qa, Qi, Sx, Sp, Oc, No, Dc, NaN;

		@Getter private final int radix = (nextRadix++) * 3;

		public static CharNotation getByRadix(int radix) {
			int index = (radix / 3) - 1;
			if (index < 0 || index >= values().length)
				return NaN;
			return values()[index];
		}

		public static CharNotation parse(String string) {
			for (CharNotation notation : CharNotation.values())
				if (notation.name().equalsIgnoreCase(string))
					return notation;
			return NaN;
		}

		public String toEngr() {
			return "e+" + radix;
		}
	}
}
