package grammar.domain;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
    private String name;
    private final List<Terminal> terminals = new ArrayList<>();
    private String startName;

    public Grammar() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTerminal(String name, String rule) {
        terminals.add(new Terminal(name, rule));
    }

    public void setStartName(String name) {
        startName = name;
    }
}
