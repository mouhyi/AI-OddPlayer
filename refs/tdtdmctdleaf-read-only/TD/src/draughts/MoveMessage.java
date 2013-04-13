package draughts;


import java.io.Serializable;

/**
 * Definiuje wiadomość z ruchem gracza.
 */
public class MoveMessage implements Serializable {

    private CheckerModel first;
    private CheckerModel second;
    private Author mAuthor;
    private boolean endsTurn;

    /**
     * Tworzy wiadomość ruchu gracza <code>mAuthor</code> z pola <code>first</code> na <code>second</code> i informacją o zakończeniu sekwencji ruchów.
     * @param first Pole, z którego ruszył się gracz.
     * @param second Pole, na które ruszył się gracz.
     * @param mAuthor Typ gracza, który wykonuje ruch.
     * @param endsTurn Informacja o zakończeniu sekwencji ruchów.
     */
    public MoveMessage(CheckerModel first, CheckerModel second, Author mAuthor, boolean endsTurn) {
        this.first = first;
        this.second = second;
        this.mAuthor = mAuthor;
        this.endsTurn = endsTurn;
    }

    /**
     * Zwraca pole, z którego ruszał się gracz.
     * @return Pole, z którego ruszał się gracz.
     */
    public CheckerModel getFirst() {
        return first;
    }

    /**
     * Ustawia pole,z którego ruszał się gracz.
     * @param first Pole,z którego ruszał się gracz.
     */
    public void setFirst(CheckerModel first) {
        this.first = first;
    }

    /**
     * Zwraca pole, na które ruszył się gracz.
     * @return Pole, na które ruszył się gracz.
     */
    public CheckerModel getSecond() {
        return second;
    }

    /**
     * Ustawia pole, na które ruszył się gracz.
     * @param second Pole, na które ruszył się gracz.
     */
    public void setSecond(CheckerModel second) {
        this.second = second;
    }

    /**
     * Zwraca gracza, który wykonał ruch.
     * @return Gracz, który wykonał ruch.
     */
    public Author getMAuthor() {
        return mAuthor;
    }

    /**
     * Ustawia gracza, który wykonał ruch.
     * @param mAuthor Gracz, który wykonał ruch.
     */
    public void setMAuthor(Author mAuthor) {
        this.mAuthor = mAuthor;
    }

    /**
     * Zwraca informację o zakończeniu sekwencji ruchów.
     * @return Informacja o zakończeniu sekwencji ruchów.
     */
    public boolean isEndsTurn() {
        return endsTurn;
    }

    /**
     * Ustawia informację o zakończeniu sekwencji ruchów.
     * @param endsTurn Informacja o zakończeniu sekwencji ruchów.
     */
    public void setEndsTurn(boolean endsTurn) {
        this.endsTurn = endsTurn;
    }
}
