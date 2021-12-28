package grammar.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Grammar {
    private String name;
    public final Map<String, Terminal> terminalsMap = new HashMap<>();
    private final ArrayList<Terminal> terminals = new ArrayList<>();
    public final Map<String, Nonterminal> nontermsMap = new HashMap<>();
    private final ArrayList<Nonterminal> nonterms = new ArrayList<>();
    private String startName;

    public Grammar() {}

    public void setName(String name) {
        this.name = name;
    }

    public void addTerminal(String name, String rule) {
        Terminal term = new Terminal(name, rule);
        terminals.add(term);
        terminalsMap.put(name, term);
    }

    public void addNonterminal(Nonterminal nonterm) {
        nonterms.add(nonterm);
        nontermsMap.put(nonterm.getName(), nonterm);
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

    public ArrayList<Nonterminal> getNonterms() {
        return nonterms;
    }

    public String getStartName() {
        return startName;
    }

    public void printFirst() {
        System.out.println("FIRST:\n");
        for (Nonterminal nonterm : getNonterms()) {
            System.out.println(nonterm.first.stream().map((Function<Terminal, Object>) Terminal::getName).collect(Collectors.toList()));
        }
    }

    public void printFollow() {
        System.out.println("FOLLOW:\n");
        for (Nonterminal nonterm : getNonterms()) {
            System.out.println(nonterm.follow.stream().map((Function<Terminal, Object>) Terminal::getName).collect(Collectors.toList()));
        }
    }
}
