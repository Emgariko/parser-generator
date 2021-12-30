package grammar.domain;

import generator.ParserGenerator;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Grammar {
    private String name;
    public final Map<String, Terminal> terminalsMap = new HashMap<>();
    private final ArrayList<Terminal> terminals = new ArrayList<>();
    public final Map<String, Nonterminal> nontermsMap = new HashMap<>();
    private final ArrayList<Nonterminal> nonterms = new ArrayList<>();
    private String startName;
    public Terminal EPS_TERM = null;
    public static Terminal END = new Terminal("END", "");

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

    private static boolean checkEps(Terminal term) {
        return term.getRegex().equals(ParserGenerator.EPS_TOKEN_REGEX);
    }


    public void findEps() {
        List<Terminal> terms = getTerminals();
        for (Terminal term : terms) {
            if (checkEps(term)) {
                EPS_TERM = term;
                break;
            }
        }
    }

    public void calc() {
        findEps();
        calcFirst();
        calcFollow();
    }

    private void calcFirst() {
        boolean changes = true;
        while (changes) {
            changes = false;
            for (Nonterminal nonterm : getNonterms()) {
                for (Rule rule : nonterm.getRules()) {
                    int sz = nonterm.first.size();
                    nonterm.first.addAll(calcRuleFirst(rule, 0));
                    if (sz != nonterm.first.size()) {
                        changes = true;
                    }
                }
            }
        }
    }

    private void calcFollow() {
        String name = getStartName();
        Nonterminal nonterm_ = nontermsMap.get(name); // :TODO: check != null
        nonterm_.follow.add(END);

        boolean changes = true;
        while (changes) {
            changes = false;
            for (Nonterminal nontermA : getNonterms()) {
                for (Rule rule : nontermA.getRules()) {
                    ArrayList<Rule.Element> els = rule.getEls();
                    int ruleSz = els.size();
                    for (int i = 0; i < ruleSz; i++) {
                        Rule.Element el = els.get(i);
                        if (nontermsMap.containsKey(el.name)) {
                            Nonterminal nontermB = nontermsMap.get(el.name);
                            int prevSz = nontermB.follow.size();

                            if (i != ruleSz - 1) {
                                HashSet<Terminal> restFirst = calcRuleFirst(rule, i + 1);
                                if (restFirst.contains(EPS_TERM)) {
                                    restFirst.remove(EPS_TERM);
                                    restFirst.addAll(nontermA.follow);
                                }
                                nontermB.follow.addAll(restFirst);
                            } else {
                                nontermB.follow.addAll(nontermA.follow);
                            }

                            if (prevSz != nontermB.follow.size()) {
                                changes = true;
                            }
                        }
                    }
                }
            }
        }
    }

    // :TODO: There should be the only one EPS terminal.
    public HashSet<Terminal> calcRuleFirst(Rule rule, int ind) {
        HashSet<Terminal> res = new HashSet<>();
        ArrayList<Rule.Element> els = rule.getEls();

        Rule.Element el = els.get(ind);
        if (terminalsMap.containsKey(el.name)) {
            Terminal term = terminalsMap.get(el.name);
            if (term == EPS_TERM) { // regex is EPS
                res.add(EPS_TERM);
            } else {
                res.add(term);
            }
        } else {
            Nonterminal nonterm = nontermsMap.get(el.name);
            Set<Terminal> nontermFirst = nonterm.first;
            res.addAll(nontermFirst);
            if (res.contains(EPS_TERM)) {
                res.remove(EPS_TERM);
                if (ind + 1 < els.size()) {
                    res.addAll(calcRuleFirst(rule, ind + 1));
                } else {
                    res.add(EPS_TERM);
                }
            }
        }
        return res;
    }

    public HashSet<Terminal> first1(Nonterminal nontermA, Rule rule, int ind) {
        HashSet<Terminal> first = calcRuleFirst(rule, ind);
        if (first.contains(EPS_TERM)) {
            first.remove(EPS_TERM);
            first.addAll(nontermA.follow);
        }
        return first;
    }
}
