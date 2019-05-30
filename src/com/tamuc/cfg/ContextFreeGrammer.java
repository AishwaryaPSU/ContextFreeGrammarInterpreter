package com.tamuc.cfg;

import org.leibnizcenter.cfg.algebra.semiring.dbl.LogSemiring;
import org.leibnizcenter.cfg.category.Category;
import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;
import org.leibnizcenter.cfg.category.terminal.Terminal;
import org.leibnizcenter.cfg.category.terminal.stringterminal.ExactStringTerminal;
import org.leibnizcenter.cfg.earleyparser.Parser;
import org.leibnizcenter.cfg.grammar.Grammar;
import org.leibnizcenter.cfg.token.Token;
import org.leibnizcenter.cfg.token.Tokens;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContextFreeGrammer {
    private final String startSymbol;
    private final String[] nonTerminalSymbols;
    private final String[] terminalSymbols;
    private final Map<String, List<String>> productionRules;
    private final Grammar grammar;


    public ContextFreeGrammer(String startSymbol, String[] nonTerminalSymbols, String[] terminalSymbols, Map<String, List<String>> productionRules) {
        this.startSymbol = startSymbol;
        this.nonTerminalSymbols = nonTerminalSymbols;
        this.terminalSymbols = terminalSymbols;
        this.productionRules = productionRules;
        this.grammar = buildGrammar();
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public String[] getNonTerminalSymbols() {
        return nonTerminalSymbols;
    }

    public String[] getTerminalSymbols() {
        return terminalSymbols;
    }

    public Map<String, List<String>> getProductionRules() {
        return productionRules;
    }


    public boolean validateString(String string) {
        try {
            //System.out.println(String.format("grammar rules %s, grammar %s", grammar.getAllRules(), grammar.toString()));
            Parser<String> parser = new Parser<>(grammar);
            //System.out.println(String.format("tokenized input string %s ", Tokens.tokenize(string, "")));
            double isValid = parser.recognize(Category.nonTerminal(this.startSymbol), Tokens.tokenize(string, ""));
            //System.out.println(String.format("isValid %s", isValid));
            if( isValid > 0.0) {
                char arr[] = string.toCharArray();
                String[] strArray = IntStream.range(0, arr.length).mapToObj(i -> arr[i]).map(a -> String.valueOf(a)).toArray(String[]::new);
                System.out.println(parser.getViterbiParseWithScore(Category.nonTerminal(this.startSymbol), Tokens.tokenize(strArray)));
            }
            return isValid > 0.0;
        } catch (Exception exception) {
            //System.out.println(String.format("error occurred while evaluating CFG for given string %s, Error %s", string, exception.getMessage()));
            //exception.printStackTrace();
        }
        return false;
    }

    private boolean containsSymbols(String string, String[] symbols) {
        return Arrays.stream(terminalSymbols).anyMatch(terminalSymbol -> string.contains(terminalSymbol));
    }


    private Grammar buildGrammar() {
        Grammar.Builder grammarBuilder = new Grammar.Builder("tamuc").setSemiring(LogSemiring.get());
        for (String nt : this.nonTerminalSymbols) {
            List<String> prules = this.productionRules.get(nt);
            if (prules == null) {
                return null;
            }

            for (String prule : prules) {
                char[] pruleArray = prule.toCharArray();
                Category[] terminalArray = new Category[pruleArray.length];
                int i = 0;
                for (char ch : pruleArray) {
                    String str = String.valueOf(ch);
                    boolean isStrNonTerminal = containsSymbols(str, nonTerminalSymbols);
                    if (isStrNonTerminal) {
                        terminalArray[i++] = new ExactStringTerminal(str);
                    } else {
                        terminalArray[i++] = Category.nonTerminal(str);
                    }
                }
                NonTerminal nnt = Category.nonTerminal(nt);
                grammarBuilder = grammarBuilder.addRule(nnt, terminalArray);
            }
        }

        return grammarBuilder.build();
    }

    @Override
    public String toString() {
        return grammar.toString();
    }
}

