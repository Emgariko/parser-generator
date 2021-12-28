package grammar.domain;

import java.util.ArrayList;
import java.util.List;

public class Rule {
    private final ArrayList<Element> els = new ArrayList<>();

    public static class Element {
        public final String name;
        public final String params;
        public final String code;

        public Element(String name, String params, String code) {
            this.name = name;
            this.params = params;
            this.code = code;
        }
    }

    public ArrayList<Element> getEls() {
        return els;
    }

    public void addElement(String name, String params, String code) {
        els.add(new Element(name, params, code));
    }
}
