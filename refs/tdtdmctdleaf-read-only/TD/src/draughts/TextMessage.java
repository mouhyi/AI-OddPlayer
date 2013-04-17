package draughts;


/**
 * Definiuje wiadomość tekstową przesyłaną pomiędzy graczami.
 */
public class TextMessage implements java.io.Serializable {

    private String message;
    private Author mAuthor;

    /**
     * Tworzy wiadomość tekstową o podanej treści i autorze.
     * @param message Treść wiadomości.
     * @param mAuthor Typ autora wiadomości.
     */
    public TextMessage(String message, Author mAuthor) {
        this.message = message;
        this.mAuthor = mAuthor;
    }

    /**
     *Zwraca treść wiadomości.
     * @return Treść wiadomości.
     */
    public String getMessage() {
        return message;
    }

    /**
     *Ustawia treść wiadomości.
     * @param message Treść wiadomości.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Zwraca typ autora wiadomości.
     * @return Typ autora wiadomości.
     */
    public Author getMAuthor() {
        return mAuthor;
    }

    /**
     * Ustawia typ autora wiadomości.
     * @param mAuthor Typ autora wiadomości.
     */
    public void setMAuthor(Author mAuthor) {
        this.mAuthor = mAuthor;
    }
}
