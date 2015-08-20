package com.awdean;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.awdean.data.ItemAttributesTest;
import com.awdean.data.ItemRarityTest;
import com.awdean.data.ItemTypeTest;
import com.awdean.grammar.common.TerminalsGrammarTest;

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
    TerminalsGrammarTest.class 
})
public class AllTests {

}
