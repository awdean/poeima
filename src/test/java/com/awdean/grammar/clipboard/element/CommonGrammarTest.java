package com.awdean.grammar.clipboard.element;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import com.awdean.data.ItemAttributes;

public class CommonGrammarTest {

    @Test
    public void testTAugmented() {
        Parser parser = CommonGrammar.tAugmented();
        String input = CommonGrammar.AUGMENTED;
        assertParseExactInvariants(parser, input);
        assertValueInvariants(parser, input, input);
    }

    @Test
    public void testTSectionBreak() {
        Parser parser = CommonGrammar.tSectionBreak();
        String input = CommonGrammar.SECTION_BREAK;
        assertParseExactInvariants(parser, input);
        assertValueInvariants(parser, input, input);
    }

    @Test
    public void testTUnmet() {
        Parser parser = CommonGrammar.tUnmet();
        String input = CommonGrammar.UNMET;
        assertParseExactInvariants(parser, input);
        assertValueInvariants(parser, input, input);
    }

    @Test
    public void testTCorrupted() {
        Parser parser = CommonGrammar.tCorrupted();
        String input = CommonGrammar.CORRUPTED;
        assertParseExactInvariants(parser, input);
        
        ItemAttributes output = new ItemAttributes();
        output.corrupted = true;
        
        assertValueInvariants(parser, input, output);
    }

    @Test
    public void testTMirrored() {
        Parser parser = CommonGrammar.tMirrored();
        String input = CommonGrammar.MIRRORED;
        assertParseExactInvariants(parser, input);
        
        ItemAttributes output = new ItemAttributes();
        output.mirrored = true;
        
        assertValueInvariants(parser, input, output);
    }

    @Test
    public void testTUnidentified() {
        Parser parser = CommonGrammar.tUnidentified();
        String input = CommonGrammar.UNIDENTIFIED;
        assertParseExactInvariants(parser, input);
        
        ItemAttributes output = new ItemAttributes();
        output.unidentified = true;
        
        assertValueInvariants(parser, input, output);
    }
    
    @Test
    public void testTItemLine() {
        Parser parser = CommonGrammar.tItemLine();
        
        assertFalse(parser.end().accept(CommonGrammar.AUGMENTED));
        assertFalse(parser.end().accept(CommonGrammar.CORRUPTED));
        assertFalse(parser.end().accept(CommonGrammar.MIRRORED));
        assertFalse(parser.end().accept(CommonGrammar.SECTION_BREAK));
        assertFalse(parser.end().accept(CommonGrammar.UNIDENTIFIED));
        assertFalse(parser.end().accept(CommonGrammar.UNMET));
        assertFalse(parser.end().accept("TEST\nTEST"));
        
        String input = "TEST";
        String output = "TEST";

        assertValueInvariants(parser, input, output);
    }

    
    @Test
    public void testItemImplicitParse() {
        Parser parser = CommonGrammar.itemImplicit().end();

        assertFalse(parser.accept("+10% to all Elemental Resistances"));
        assertFalse(parser.accept("TEST\n+10% to all Elemental Resistances"));
        
        // It's possible we'd want to exclude other defined item sections here.
        
        assertTrue(parser.accept(CommonGrammar.SECTION_BREAK + "\n" + "+10% to all Elemental Resistances"));
    }

    @Test
    public void testItemImplicitValue() {
        Parser parser = CommonGrammar.itemImplicit();
        String input = CommonGrammar.SECTION_BREAK + "\n" + "+10% to all Elemental Resistances";
        
        ItemAttributes output = new ItemAttributes();
        output.implicit = "+10% to all Elemental Resistances";
        
        assertValueInvariants(parser, input, output);
    }

    @Test
    public void testItemLevelParse() {
        Parser parser = CommonGrammar.itemLevel().end();
        
        String rejected = CommonGrammar.SECTION_BREAK + "\n" + "+10% to all Elemental Resistances";
        assertFalse(parser.accept(rejected));
        
        String accepted = CommonGrammar.SECTION_BREAK + "\n" + "Item Level: 80";
        assertTrue(parser.accept(accepted));
    }

    @Test
    public void testItemLevelValue() {
        Parser parser = CommonGrammar.itemLevel();
        String input = CommonGrammar.SECTION_BREAK + "\n" + "Item Level: 80";
        
        ItemAttributes output = new ItemAttributes();
        output.level = 80;
        
        assertValueInvariants(parser, input, output);
    }

    @Test
    public void testItemSocketsParse() {
        Parser parser = CommonGrammar.itemSockets().end();
        
        String rejected = CommonGrammar.SECTION_BREAK + "\n" + "+10% to all Elemental Resistances";
        assertFalse(parser.accept(rejected));
        
        String accepted = CommonGrammar.SECTION_BREAK + "\n" + "Sockets: B-R R-G-R G ";
        assertTrue(parser.accept(accepted));
    }

    @Test
    public void testItemSocketsValue() {
        Parser parser = CommonGrammar.itemSockets();
        String input = CommonGrammar.SECTION_BREAK + "\n" + "Sockets: B-R R-G-R G ";
        
        ItemAttributes output = new ItemAttributes();
        output.sockets = "B-R R-G-R G";
        
        assertValueInvariants(parser, input, output);
    }

    @Test
    public void testItemRequirementsParse() {
        Parser parser = CommonGrammar.itemRequirements().end();
        
        String rejected = CommonGrammar.SECTION_BREAK + "\n" + "+10% to all Elemental Resistances";
        assertFalse(parser.accept(rejected));
        
        String accepted = CommonGrammar.SECTION_BREAK + "\nRequirements: \n" + "Level: 80";
        assertTrue(parser.accept(accepted));
        accepted += "\n" + "Str: 111 (augmented)";
        assertTrue(parser.accept(accepted));
        accepted += "\n" + "Dexterity: 222 (augmented) (unmet)";
        assertTrue(parser.accept(accepted));
        accepted += "\n" + "Int: 333 (unmet)";
        assertTrue(parser.accept(accepted));
    }

    @Test
    public void testItemRequirementsValue() {
        Parser parser = CommonGrammar.itemRequirements();
        String input = CommonGrammar.SECTION_BREAK + "\n" +
                       "Requirements: \n" +
                       "Level: 80\n" +
                       "Str: 111 (augmented)\n" +
                       "Dexterity: 222 (augmented) (unmet)\n" +
                       "Int: 333 (unmet)\n";
        
        ItemAttributes output = new ItemAttributes();
        output.levelRequired = 80;
        output.strengthRequired = 111;
        output.dexterityRequired = 222;
        output.intelligenceRequired = 333;
        
        assertValueInvariants(parser, input, output);
    }

    @Test
    public void testLevelRequirementParse() {
        Parser parser = CommonGrammar.levelRequirement().end();
        
        String rejected = "+10% to all Elemental Resistances";
        assertFalse(parser.accept(rejected));
        
        String accepted = "Level: 80";
        assertTrue(parser.accept(accepted));
    }

    @Test
    public void testLevelRequirementValue() {
        Parser parser = CommonGrammar.levelRequirement();
        String input = "Level: 80";
        
        ItemAttributes output = new ItemAttributes();
        output.levelRequired = 80;
        
        assertValueInvariants(parser, input, output);
        assertValueInvariants(parser, input + " (augmented)", output);
        assertValueInvariants(parser, input + " (unmet)", output);
        assertValueInvariants(parser, input + " (augmented) (unmet)", output);
    }

    @Test
    public void testStrRequirementParse() {
        Parser parser = CommonGrammar.strRequirement().end();
        
        String rejected = "+10% to all Elemental Resistances";
        assertFalse(parser.accept(rejected));
        
        String accepted = "Str: 111";
        assertTrue(parser.accept(accepted));
        accepted = "Strength: 111";
        assertTrue(parser.accept(accepted));
    }

    @Test
    public void testStrRequirementValue() {
        Parser parser = CommonGrammar.strRequirement();
        String input = "Str: 111";
        
        ItemAttributes output = new ItemAttributes();
        output.strengthRequired = 111;
        
        assertValueInvariants(parser, input, output);
        assertValueInvariants(parser, input + " (augmented)", output);
        assertValueInvariants(parser, input + " (unmet)", output);
        assertValueInvariants(parser, input + " (augmented) (unmet)", output);
        
        input = "Strength: 111";
        
        assertValueInvariants(parser, input, output);
        assertValueInvariants(parser, input + " (augmented)", output);
        assertValueInvariants(parser, input + " (unmet)", output);
        assertValueInvariants(parser, input + " (augmented) (unmet)", output);
    }

    @Test
    public void testDexRequirementParse() {
        Parser parser = CommonGrammar.dexRequirement().end();
        
        String rejected = "+10% to all Elemental Resistances";
        assertFalse(parser.accept(rejected));
        
        String accepted = "Dex: 111";
        assertTrue(parser.accept(accepted));
        accepted = "Dexterity: 111";
        assertTrue(parser.accept(accepted));
    }

    @Test
    public void testDexRequirementValue() {
        Parser parser = CommonGrammar.dexRequirement();
        String input = "Dex: 111";
        
        ItemAttributes output = new ItemAttributes();
        output.dexterityRequired = 111;
        
        assertValueInvariants(parser, input, output);
        assertValueInvariants(parser, input + " (augmented)", output);
        assertValueInvariants(parser, input + " (unmet)", output);
        assertValueInvariants(parser, input + " (augmented) (unmet)", output);
        
        input = "Dexterity: 111";
        
        assertValueInvariants(parser, input, output);
        assertValueInvariants(parser, input + " (augmented)", output);
        assertValueInvariants(parser, input + " (unmet)", output);
        assertValueInvariants(parser, input + " (augmented) (unmet)", output);
    }

    @Test
    public void testIntRequirementParse() {
        Parser parser = CommonGrammar.intRequirement().end();
        
        String rejected = "+10% to all Elemental Resistances";
        assertFalse(parser.accept(rejected));
        
        String accepted = "Int: 111";
        assertTrue(parser.accept(accepted));
        accepted = "Intelligence: 111";
        assertTrue(parser.accept(accepted));
    }

    @Test
    public void testIntRequirementValue() {
        Parser parser = CommonGrammar.intRequirement();
        String input = "Int: 111";
        
        ItemAttributes output = new ItemAttributes();
        output.intelligenceRequired = 111;
        
        assertValueInvariants(parser, input, output);
        assertValueInvariants(parser, input + " (augmented)", output);
        assertValueInvariants(parser, input + " (unmet)", output);
        assertValueInvariants(parser, input + " (augmented) (unmet)", output);
        
        input = "Intelligence: 111";
        
        assertValueInvariants(parser, input, output);
        assertValueInvariants(parser, input + " (augmented)", output);
        assertValueInvariants(parser, input + " (unmet)", output);
        assertValueInvariants(parser, input + " (augmented) (unmet)", output);
    }

    private void assertParseExactInvariants(Parser stringParser, String accepted) {
        String rejected = "The quick brown fox jumped over the lazy dog.";
        if (rejected.equals(accepted)) {
            rejected = "Pack my box with five dozen liquor jugs.";
        }
        
        assertFalse(stringParser.end().accept(rejected));
        assertTrue(stringParser.end().accept(accepted));
        
        Result result = stringParser.parse(accepted);
        assertTrue(result.isSuccess());
        
        result = stringParser.parse("\t" + accepted + " \n");
        assertTrue(result.isSuccess());
    }

    private <T> void assertValueInvariants(Parser parser, String input, T output) {
        Result result = parser.parse(input);
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(equalTo(output)));
        
        result = parser.parse("\t" + input + " \n");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(equalTo(output)));
    }
    
}
