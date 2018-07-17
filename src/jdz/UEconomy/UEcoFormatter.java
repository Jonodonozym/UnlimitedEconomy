
package jdz.UEconomy;

import lombok.Getter;

public class UEcoFormatter {
	public static String charFormat(double value) {
		return charFormat(value, 6, 3, false);
	}

	public static String charFormat(double value, int significantFigures) {
		return charFormat(value, significantFigures, 3, false);
	}

	public static String charFormat(double value, int significantFigures, boolean isWholeNumber) {
		return charFormat(value, significantFigures, 3, isWholeNumber);
	}

	/**
	 * Helper method to format doubles using character notation instead of
	 * scientific. Goes up to 100Dc (1E+35)
	 * 
	 * @param value
	 * @param minimumRadix Any numbers with a radix (1*10^radix) above this will be
	 *            in scientific notation
	 * @param significant figures The number of significant figures in the number.
	 *            Suggested is at least 4
	 * @return A string of the number
	 */
	public static String charFormat(double val, int significantFigures, int minRadix, boolean isWholeNumber) {
		if (val <= 0)
			return "0";

		significantFigures = significantFigures < 1 ? 1 : significantFigures;
		String s = String.format("%" + significantFigures + "." + (significantFigures - 1) + "e", val);
		String[] args = s.split("e\\+|e");
		if (args.length == 1)
			return String.format("%.2f", Double.parseDouble(s));
		int dp = 1;
		int radix = Integer.parseInt(args[1]);
		while (radix % 3 != 0) {
			radix--;
			dp++;
		}

		if (radix < minRadix) {
			if (!isWholeNumber || val % 1.0 != 0)
				return String.format("%.2f", val);
			return (int) val + "";
		}

		args[0] = args[0].replace(".", "");
		if (dp < significantFigures) {
			args[0] = args[0].substring(0, dp) + "." + args[0].substring(dp);
		}
		else {
			while (args[0].length() < dp) {
				args[0] = args[0] + "0";
			}
		}

		while (args[0].endsWith("0") || args[0].endsWith("."))
			args[0] = args[0].substring(0, args[0].length() - 1);

		CharNotation suffix = CharNotation.getByRadix(radix);
		if (suffix == CharNotation.NaN)
			return args[0] + "e+" + radix;
		return args[0] + suffix;
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
			System.out.println(radix);
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
