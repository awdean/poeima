package com.awdean;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.awdean.data.ItemAttributesTest;
import com.awdean.grammar.common.TerminalsGrammarTest;

@RunWith(Suite.class)
@SuiteClasses({ ClipMonTest.class, DisplayTest.class, ItemAttributesTest.class, TerminalsGrammarTest.class })
public class AllTests {

}
