package grammar.domain;

import java.util.ArrayList;
import java.util.List;

public class Grammar {
    private String name;
    private final ArrayList<Terminal> terminals = new ArrayList<>();
    private final ArrayList<Nonterminal> nonterms = new ArrayList<>();
    private String startName;

    public Grammar() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public void addTerminal(String name, String rule) {
        terminals.add(new Terminal(name, rule));
    }

    public void addNonterminal(Nonterminal nonterm) {
        nonterms.add(nonterm);
    }

    public void setStartName(String name) {
        startName = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Terminal> getTerminals() {
        return terminals;
    }
}
