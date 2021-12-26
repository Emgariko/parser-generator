package grammar.domain;

public class Terminal {
    private final String name;
    private final String regex;

    public Terminal(String name, String regex) {
        this.name = name;
        this.regex = regex;
    }
}
