package generator;

import grammar.domain.Grammar;
import grammar.domain.Nonterminal;
import grammar.domain.Rule;
import grammar.domain.Terminal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ParserGenerator {
    private final Grammar g;
    private final String EPS_TOKEN_REGEX = "\"EPS\"";
    private Terminal EPS_TERM = null;
    public static Terminal END = new Terminal("END", "");

    // :TODO: if there is no EPS term in user grammar then it should be mocked.
    public ParserGenerator(Grammar g) {
        this.g = g;
        findEps();
    }

    private void findEps() {
        List<Terminal> terms = g.getTerminals();
        for (Terminal term : terms) {
            if (checkEps(term)) {
                EPS_TERM = term;
                break;
            }
        }
    }

    private boolean checkEps(String s) {
        return checkEps(g.terminalsMap.get(s));
    }

    private boolean checkEps(Terminal term) {
        return term.getRegex().equals(EPS_TOKEN_REGEX);
    }

    public void calc() {
        calcFirst();
        calcFollow();
    }

    private void calcFirst() {
        boolean changes = true;
        while (changes) {
            changes = false;
            for (Nonterminal nonterm : g.getNonterms()) {
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
        String name = g.getStartName();
        Nonterminal nonterm_ = g.nontermsMap.get(name); // :TODO: check != null
        nonterm_.follow.add(END);

        boolean changes = true;
        while (changes) {
            changes = false;
            for (Nonterminal nontermA : g.getNonterms()) {
                for (Rule rule : nontermA.getRules()) {
                    ArrayList<Rule.Element> els = rule.getEls();
                    int ruleSz = els.size();
                    for (int i = 0; i < ruleSz; i++) {
                        Rule.Element el = els.get(i);
                        if (g.nontermsMap.containsKey(el.name)) {
                            Nonterminal nontermB = g.nontermsMap.get(el.name);
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

    // :TODO: There are should be the only one EPS terminal.
    // :TODO: if two different terminals have equal regex and different name ?
    private HashSet<Terminal> calcRuleFirst(Rule rule, int ind) {
        HashSet<Terminal> res = new HashSet<>();
        ArrayList<Rule.Element> els = rule.getEls();

        Rule.Element el = els.get(ind);
        if (g.terminalsMap.containsKey(el.name)) {
            Terminal term = g.terminalsMap.get(el.name);
            if (term == EPS_TERM) { // regex is EPS
                res.add(EPS_TERM);
            } else {
                res.add(term);
            }
        } else {
            Nonterminal nonterm = g.nontermsMap.get(el.name);
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

}
