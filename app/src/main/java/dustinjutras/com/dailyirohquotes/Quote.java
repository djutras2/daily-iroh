package dustinjutras.com.dailyirohquotes;


/**
 * Updated by Dustin Jutras on 1/20/2018.
 */

public class Quote {

    String quote, character;

    public Quote(String quote, String character) {
        this.quote = quote;
        this.character = character;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getCharacter() {
        return character.substring(0,1).toUpperCase() + character.substring(1).toLowerCase();
    }

    public void setCharacter(String character) {
        this.character = character;
    }
}
