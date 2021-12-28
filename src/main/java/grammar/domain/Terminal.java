package grammar.domain;

import java.util.Objects;

public class Terminal {
    private final String name;
    private final String regex;

    public Terminal(String name, String regex) {
        this.name = name;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Terminal terminal = (Terminal) o;
        return Objects.equals(regex, terminal.regex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regex);
    }
}
