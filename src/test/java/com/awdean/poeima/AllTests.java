package com.awdean.poeima;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.awdean.poeima.data.ItemTypeTest;
import com.awdean.poeima.data.ItemRarityTest;
import com.awdean.poeima.data.ItemAttributesTest;
import com.awdean.poeima.grammar.clipboard.element.CommonGrammarTest;
import com.awdean.poeima.grammar.common.TerminalsGrammarTest;

@RunWith(Suite.class)
@SuiteClasses({
    // Systems interface tests
    ClipMonTest.class,
    // MVC tests 
    DisplayTest.class,
    // Data structure tests
    ItemAttributesTest.class,
    ItemRarityTest.class,
    ItemTypeTest.class,
    // Parser tests
    TerminalsGrammarTest.class,
    CommonGrammarTest.class
})
public class AllTests {

}
