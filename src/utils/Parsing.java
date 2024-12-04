package utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Parsing {

	public static int stoi(String s) {
		return Integer.valueOf(s);
	}

	public static double stod(String s) {
		return Double.valueOf(s);
	}

	public static <T extends Enum<T>> T asEnum(Class<T> enumType, String s) {
		return enumType.cast(Enum.valueOf(enumType, s));
	}

	public static String flatten(String s) {
		return s.replace("\n", " ");
	}

	public static List<String> lines(String s) {
		return s.lines().toList();
	}

	public static List<String> words(String s, String separator) {
		if (separator.isEmpty()) {
			List<String> chars = new ArrayList<String>();
			for (char c : s.toCharArray()) {
				chars.add(String.valueOf(c));
			}

			return chars;
		}

		return Arrays.asList(s.replace(separator, " ").split("\\s+"));
	}

	public static List<String> words(String s) {
		return Arrays.asList(s.split("\\s+"));
	}

	public static <T> Grid<T> asGrid(String input, String separator, Function<String, T> converter) {
		Grid<T> g = new Grid<>();
		for (var line : lines(input)) {
			var words = words(line, separator).stream().map(converter::apply).toList();
			g.insertRow(words);
		}

		return g;
	}

	public static <T> Grid<T> asGrid(String input, Function<String, T> converter) {
		return asGrid(input, " ", converter);
	}

	public static <T> Grid<T> asGrid(List<List<T>> l) {
		Grid<T> g = new Grid<T>();
		for (var row : l) {
			g.insertRow(row);
		}

		return g;
	}

}
