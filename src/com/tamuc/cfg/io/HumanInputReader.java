package com.tamuc.cfg.io;

import com.tamuc.cfg.ContextFreeGrammer;

import java.util.*;
import java.util.stream.Collectors;

public class HumanInputReader {
    private Scanner scanner;

    public HumanInputReader() {
        this.scanner = new Scanner(System.in);
    }

    public ContextFreeGrammer readCFGFromSTDIN() {
        System.out.println("Enter the start symbol");
        System.out.println(">");
        String startSymbol = scanner.nextLine();
        System.out.println("Enter the non terminal symbols [comma separated values]");
        System.out.println(">");
        String nonTermialSymbolsString = scanner.nextLine();
        String[] nonTerminalSymbols = Arrays.stream(nonTermialSymbolsString.split(",")).map(s -> s.trim()).toArray(String[]::new);
        System.out.println("Enter the terminal symbols [comma separated values]");
        System.out.println(">");
        String termialSymbolsString = scanner.nextLine();
        String[] terminalSymbols = Arrays.stream(termialSymbolsString.split(",")).map(s -> s.trim()).toArray(String[]::new);
        System.out.println("Enter the production rules [comma separated values]");
        System.out.println(">");
        String productionRulesString = scanner.nextLine();
        //System.out.println(String.format("productionRulesString %s", productionRulesString));
        String[] productionRules = Arrays.stream(productionRulesString.split(",")).map(s -> s.trim()).toArray(String[]::new);
        //System.out.println(String.format("rules array %s", Arrays.toString(productionRules)));
        Map<String, List<String>> productionRulesMap = Arrays.stream(productionRules)
                .map(productionRule -> {
                    String[] rules = Arrays.stream(productionRule.split("->")).map(s -> s.trim()).toArray(String[]::new);
                    //System.out.println(String.format("rhs lhs production rule %s",Arrays.toString(rules)));
                    return rules;
                })
                .collect(Collectors.toMap(rule -> rule[0], rule -> Arrays.asList(rule[1]), (a,b) -> {
                    //System.out.println(String.format("duplicate key found, merging..., a %s , b %s", a, b));
                     List<String> mergedList = new ArrayList<>();
                     mergedList.addAll(a);
                     mergedList.addAll(b);
                    //System.out.println(String.format("mergedList %s", mergedList));
                     return mergedList;
                }));

        return  new ContextFreeGrammer(startSymbol,nonTerminalSymbols, terminalSymbols, productionRulesMap);
    }

    public String readString() {
        String inputString = scanner.next();
        return inputString;
    }

    public boolean readBoolean() {
        boolean booleanInput = scanner.nextBoolean();
        return booleanInput;
    }

    public void close() {
        scanner.close();
    }
}
