grammar Grammar;

// grammar to parse a user-defined grammar

@header {
import grammar.domain.*;
}

gram[Grammar g] @init{$g = new Grammar();}:
    header[g] terminals[g] start_state[g] nonterminals[g];

header[Grammar g]:
    '@grammar' NAME {$g.setName($NAME.text);} ';';

terminals[Grammar g] :
    '@terminals'
    (terminal_rule[g])*;


// : TODO skip-rule?
terminal_rule[Grammar g]:
    NAME ':' REGEXP ';' {
        $g.addTerminal($NAME.text, $REGEXP.text);
    };

start_state[Grammar g]:
    '@start' ':' NAME {$g.setStartName($NAME.text);} ';';

nonterminals[Grammar g]:
    '@rules' (nonterminal {$g.addNonterminal($nonterminal.nonterm);})+;

nonterminal returns [Nonterminal nonterm]
@init {
    $nonterm = new Nonterminal();
}:
    NAME {$nonterm.setName($NAME.text);}
    ('[' nonterminal_params[$nonterm] ']')?
    ('returns' '[' nonterminal_returns[$nonterm] ']')?
    ':' r1=nonterm_rule {$nonterm.addRule($r1.r);}
    ('|' ((r2=nonterm_rule {$nonterm.addRule($r2.r);}) |
     ({$nonterm.addRule(null);})) // :TODO: don't forget this case
     )* ';';

nonterminal_params[Nonterminal nonterm]:
    type=NAME name=NAME {$nonterm.addParam($type.text, $name.text);}
    (',' type1=NAME name1=NAME {$nonterm.addParam($type1.text, $name1.text);})*;

nonterminal_returns[Nonterminal nonterm]:
    type=NAME name=NAME {$nonterm.addRet($type.text, $name.text);}
        (',' type1=NAME name1=NAME {$nonterm.addRet($type1.text, $name1.text);})*;

nonterm_rule returns [Rule r] locals [String params, String code]
@init {
    $r = new Rule();
    $params = "";
    $code = "";
}:
    (NAME ('[' rule_params {$params = $rule_params.s;}']')? (rule_code {$code = $rule_code.s;})? {
        $r.addElement($NAME.text, $params, $code);
        $params = "";
        $code = "";
    })+;

// :TODO: which parameters might be passed?
rule_params returns[String s] @init{ $s = new String(""); }:
    n1=NAME {$s += $n1.text;} (',' n2=NAME {$s += ", " + $n2.text;})*;

rule_code returns[String s] locals[StringBuilder res]:
// :TODO: may be use '#' '#' insted of '{' '}'
    SOURCE_CODE {
        $res = new StringBuilder($SOURCE_CODE.text);
        $res.deleteCharAt($res.length() - 1);
        $res.deleteCharAt(0);
        $s = $res.toString();
    };

SKIP_WHITESPACES: [ \n\r\t]+ -> skip;
INT: [0-9]+;
NAME: [a-zA-Z][a-zA-Z0-9_]*;

REGEXP: '"' (~('"'))* '"';
SOURCE_CODE: '#' (~('#'))+ '#';