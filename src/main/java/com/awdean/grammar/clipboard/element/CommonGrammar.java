package com.awdean.grammar.clipboard.element;

import static com.awdean.grammar.common.TerminalsGrammar.tLine;
import static com.awdean.grammar.common.TerminalsGrammar.tNumber;
import static org.petitparser.parser.primitive.StringParser.of;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.petitparser.parser.Parser;
import org.petitparser.utils.Functions;

import com.awdean.data.ItemAttributes;

public class CommonGrammar {

    public static final String AUGMENTED = "(augmented)";
    public static final String CORRUPTED = "Corrupted";
    public static final String MIRRORED = "Mirrored";
    public static final String SECTION_BREAK = "--------";
    public static final String UNIDENTIFIED = "Unidentified";
    public static final String UNMET = "(unmet)";

    public static Parser tAugmented() {
        // tAugmented = "(augmented)" ;
        return of(AUGMENTED).flatten().trim();
    }

    public static Parser tSectionBreak() {
        // tSectionBreak = "--------" ;
        return of(SECTION_BREAK).flatten().trim();
    }

    public static Parser tUnmet() {
        // tUnment = "(unmet)" ;
        return of(UNMET).flatten().trim();
    }

    public static Parser tCorrupted() {
        // tCorrupted = "Corrupted" ;
        Parser parser = of(CORRUPTED).flatten().trim();
        return parser.map(new Function<String, ItemAttributes>() {

            @Override
            public ItemAttributes apply(String t) {
                ItemAttributes item = new ItemAttributes();
                item.corrupted = true;
                return item;
            }

        });
    }

    public static Parser tMirrored() {
        // tMirrored = "Mirrored" ;
        Parser parser = of(MIRRORED).flatten().trim();
        return parser.map(new Function<String, ItemAttributes>() {

            @Override
            public ItemAttributes apply(String t) {
                ItemAttributes item = new ItemAttributes();
                item.mirrored = true;
                return item;
            }

        });
    }

    public static Parser tUnidentified() {
        // tUnidentified = "Mirrored" ;
        Parser parser = of(UNIDENTIFIED).flatten().trim();
        return parser.map(new Function<String, ItemAttributes>() {

            @Override
            public ItemAttributes apply(String t) {
                ItemAttributes item = new ItemAttributes();
                item.unidentified = true;
                return item;
            }

        });
    }

    public static Parser tItemLine() {
        Parser parser = tAugmented().not();
        parser = parser.seq(tCorrupted().not());
        parser = parser.seq(tMirrored().not());
        parser = parser.seq(tSectionBreak().not());
        parser = parser.seq(tUnidentified().not());
        parser = parser.seq(tUnmet().not());
        parser = parser.seq(tLine());
        return parser.map(Functions.lastOfList());
    }

    public static Parser itemImplicit() {
        Parser parser = tSectionBreak().seq(tItemLine());
        parser = parser.map(Functions.lastOfList());
        return parser.map(new Function<String, ItemAttributes>() {

            @Override
            public ItemAttributes apply(String t) {
                ItemAttributes item = new ItemAttributes();
                item.implicit = t;
                return item;
            }

        });
    }

    public static Parser itemLevel() {
        Parser parser = tSectionBreak().seq(of("Item Level:").trim(), tNumber());
        parser = parser.map(Functions.lastOfList());
        return parser.map(new Function<Integer, ItemAttributes>() {

            @Override
            public ItemAttributes apply(Integer t) {
                ItemAttributes item = new ItemAttributes();
                item.level = t;
                return item;
            }

        });
    }

    public static Parser itemSockets() {
        Parser parser = tSectionBreak().seq(of("Sockets:").trim(), tItemLine());
        parser = parser.map(Functions.lastOfList());
        return parser.map(new Function<String, ItemAttributes>() {

            @Override
            public ItemAttributes apply(String t) {
                ItemAttributes item = new ItemAttributes();
                item.sockets = t;
                return item;
            }

        });
    }

    public static Parser itemRequirements() {
        Parser parser = tSectionBreak().seq(of("Requirements:").trim());
        parser = parser.seq(levelRequirement().or(strRequirement(), dexRequirement(), intRequirement()).plus());
        parser = parser.map(FLATTEN_AND_JOIN);
        return parser;
    }

    public static Parser levelRequirement() {
        Parser parser = of("Level:").trim().seq(tNumber(), tAugmented().optional(), tUnmet().optional());
        parser = parser.map(Functions.nthOfList(1));
        return parser.map(new Function<Integer, ItemAttributes>() {

            @Override
            public ItemAttributes apply(Integer t) {
                ItemAttributes item = new ItemAttributes();
                item.levelRequired = t;
                return item;
            }

        });
    }

    public static Parser strRequirement() {
        Parser parser = of("Str:").or(of("Strength:")).trim().seq(tNumber(), tAugmented().optional(),
                tUnmet().optional());
        parser = parser.map(Functions.nthOfList(1));
        return parser.map(new Function<Integer, ItemAttributes>() {

            @Override
            public ItemAttributes apply(Integer t) {
                ItemAttributes item = new ItemAttributes();
                item.strengthRequired = t;
                return item;
            }

        });
    }

    public static Parser dexRequirement() {
        Parser parser = of("Dex:").or(of("Dexterity:")).trim().seq(tNumber(), tAugmented().optional(),
                tUnmet().optional());
        parser = parser.map(Functions.nthOfList(1));
        return parser.map(new Function<Integer, ItemAttributes>() {

            @Override
            public ItemAttributes apply(Integer t) {
                ItemAttributes item = new ItemAttributes();
                item.dexterityRequired = t;
                return item;
            }

        });
    }

    public static Parser intRequirement() {
        Parser parser = of("Int:").or(of("Intelligence:")).trim().seq(tNumber(), tAugmented().optional(),
                tUnmet().optional());
        parser = parser.map(Functions.nthOfList(1));
        return parser.map(new Function<Integer, ItemAttributes>() {

            @Override
            public ItemAttributes apply(Integer t) {
                ItemAttributes item = new ItemAttributes();
                item.intelligenceRequired = t;
                return item;
            }

        });
    }

    public static final Function<Object, Object> FLATTEN_AND_JOIN = new Function<Object, Object>() {

        @Override
        @SuppressWarnings("unchecked")
        public Object apply(Object t) {
            if (null == t) {
                return null;
            }

            if (t instanceof List) {
                return flatten((List<Object>) t).stream().reduce(null, (a, b) -> ItemAttributes.join(a, b));
            } else if (t.getClass().isArray()) {
                return flatten(Arrays.asList(t)).stream().reduce(null, (a, b) -> ItemAttributes.join(a, b));
            } else {
                return t;
            }
        }
    };

    @SuppressWarnings("unchecked")
    public static List<Object> flatten(List<Object> list) {
        List<Object> accum = new ArrayList<>();
        for (Object item : list) {
            if (item instanceof List) {
                accum.addAll(flatten((List<Object>) item));
            } else if (item.getClass().isArray()) {
                accum.addAll(flatten(Arrays.asList(item)));
            } else {
                accum.add(item);
            }
        }
        return accum;
    }
}
