package draughts;

/**
 * Definuje gracza.
 */
public class Player {

    private String name;
    private Author mAuthor;

    /**
     * Tworzy gracza o pustym imieniu.
     */
    public Player() {
        name = "";
    }

    /**
     * Tworzy gracza o podanym imieniu i typie.
     * @param name Imię gracza.
     * @param mAuthor Typ gracza.
     */
    public Player(String name, Author mAuthor) {
        this.name = name;
        this.mAuthor = mAuthor;
    }

    /**
     * Zwraca imię gracza.
     * @return Imię gracza.
     */
    public String getName() {
        return name;
    }

    /**
     * Ustawia imię gracza.
     * @param name Imię gracza
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Zwraca typ gracza.
     * @return Typ gracza.
     */
    public Author getMAuthor() {
        return mAuthor;
    }

    /**
     * Ustawia typ gracza.
     * @param mAuthor Typ gracza.
     */
    public void setMAuthor(Author mAuthor) {
        this.mAuthor = mAuthor;
    }
}
