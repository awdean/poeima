package com.awdean.poeima.grammar.common;

import static org.petitparser.parser.primitive.CharacterParser.digit;
import static org.petitparser.parser.primitive.CharacterParser.of;

import java.math.BigDecimal;
import java.util.List;

import org.petitparser.parser.Parser;
import org.petitparser.utils.Functions;

import com.google.common.base.Joiner;
import com.google.common.collect.Range;

public class TerminalsGrammar {

    private TerminalsGrammar() {
    };

    public static Parser tFloat() {
        // tFloat = /\d+(\.\d+)?/ ;
        Parser parser = digit().plus(); // /\d+/
        parser = parser.seq(of('.').seq(digit().plus()).optional()); // /\d+(\.\d+)?/
        parser = parser.flatten().trim();
        return parser.map((String value) -> new BigDecimal(value));
    }

    public static Parser tLine() {
        // tString = /[^\n]+/ ;
        Parser parser = of('\n').neg().plus();
        return parser.flatten().map((String value) -> value.trim());
    }

    public static Parser tNumber() {
        // tNumber = /\d+/ ;
        Parser parser = digit().plus();
        parser = parser.flatten().trim();
        return parser.map((String value) -> Integer.parseInt(value));
    }

    public static Parser tRange() {
        // tRange = /\d+-\d+/ ;
        Parser parser = digit().plus().seq(of('-'), digit().plus());
        parser = parser.trim();
        parser = parser.map(Functions.withoutSeparators());
        return parser.map((List<List<Integer>> value) -> {
            int lhs = Integer.parseInt(Joiner.on("").join(value.get(0)));
            int rhs = Integer.parseInt(Joiner.on("").join(value.get(1)));
            return Range.closed(lhs, rhs);
        });
    }

}
