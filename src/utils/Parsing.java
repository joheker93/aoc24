package utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Parsing {

	public static String flatten(String s) {
		return s.replace("\n", " ");
	}

	public static List<String> lines(String s) {
		return s.lines().toList();
	}

	public static List<String> words(String s, char separator){
		return Arrays.asList(s.replace(separator, ' ').split(" "));
	}
	public static List<String> words(String s) {
		return Arrays.asList(s.split(" "));
	}

	public static <T> Grid<T> asGrid(String input, Function<String, T> converter) {
		Grid<T> g = new Grid<>();
		for (var line : lines(input)) {
			var words = words(line).stream().map(converter::apply).toList();
			g.insertRow(words);
		}

		return g;
	}

	public static <T> Grid<T> asGrid(List<List<T>> l) {
		Grid<T> g = new Grid<T>();
		for (var row : l) {
			g.insertRow(row);
		}

		return g;
	}
}
