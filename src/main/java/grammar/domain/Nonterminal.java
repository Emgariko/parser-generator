package grammar.domain;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class Nonterminal {
    private String name;
    private List<Rule> rules = new ArrayList<>();
    private List<Param> params = new ArrayList<>();
    private List<Param> rets = new ArrayList();

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
}
