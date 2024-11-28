package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Sequence<T> {

	public static List<Integer> intSequence(String input) {
		return sequence(input, Integer::parseInt);
	}

	public static List<Double> doubleSequence(String input) {
		return sequence(input, Double::parseDouble);
	}

	public static <T extends Enum<T>> List<T> enumSequence(String input, Class<T> enumType) {
		return sequence(input, s -> {
			return enumType.cast(Enum.valueOf(enumType,s));
		});
	}

	public static <T> List<T> sequence(String input, Function<String, T> converter) {
		String[] parts = input.replace(",", " ").split(" ");
		List<T> objects = new ArrayList<>();

		for (var part : parts) {
			objects.add(converter.apply(part));
		}

		return objects;
	}
	
	List<T> getList(List<T> t){
		return t;
	}

}
