package draughts;

/**
 * Definiuje typ pola planszy.
 */
public enum Sort {

	/**
	 *Oznacza białe pole puste.
	 */
	blankWhite(0),

	/**
	 * Oznacza czarne pole puste.
	 */
	blankBlack(1),

	/**
	 * Oznacza pionek czarny.
	 */
	fullBlack(2),

	/**
	 * Oznacza pionek biały.
	 */
	fullWhite(3),

	/**
	 * Oznacza czarną damę.
	 */
	queenBlack(4),

	/**
	 * Oznacza białą damę.
	 */
	queenWhite(5);

	private int which;
	private static Sort[] sorts = { Sort.blankWhite, Sort.blankBlack, Sort.fullBlack, Sort.fullWhite,
			Sort.queenBlack, Sort.queenWhite };

	private Sort(int k) {
		which = k;
	}

	public int getWhich() {
		return which;
	}

	public static Sort getSort(int k) {
		return sorts[k];
	}
}
