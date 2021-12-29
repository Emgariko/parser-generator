package grammar.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Nonterminal {
    private String name;
    private ArrayList<Rule> rules = new ArrayList<>();
    private ArrayList<Param> params = new ArrayList<>();
    private ArrayList<Param> rets = new ArrayList();
    public final Set<Terminal> first = new HashSet<>();
    public final Set<Terminal> follow = new HashSet<>();

    public String getName() {
        return name;
    }

    public static class Param {
        public final String type;
        public final String name;

        public Param(String type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void addParam(String type, String name) {
        params.add(new Param(type, name));
    }

    public void addRet(String type, String name) {
        rets.add(new Param(type, name));
    }

    public ArrayList<Param> getRets() {
        return rets;
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public ArrayList<Param> getParams() {
        return params;
    }
}
