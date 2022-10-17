package kr.or.mrhi;

public enum Regex {
	ID(1), NAME(2), EVALUATION(3), ORDER(4), MAX_MIN(5), PRINT(6), SEARCH(7);

	private final int value;

	private Regex(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}