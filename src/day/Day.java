package day;

public interface Day {

	void solveA(String input);

	void solveB(String input);

	Object parseA(String input);

	Object parseB(String input);

	default <T, K> T parse(String input, Class<T> type, Part part) {
		if (part == Part.A) {
			return type.cast(parseA(input));
		}

		if (part == Part.B) {
			return type.cast(parseB(input));
		}

		System.out.println("Part " + part + ", has no valid parser!");
		return null;
	}

}
