package com.awdean.grammar.common;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import org.junit.Test;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class TerminalsGrammarTest {

    @Test
    public void testConstructorIsPrivate() throws IllegalAccessException,
                                                  InstantiationException,
                                                  InvocationTargetException,
                                                  NoSuchMethodException {
        assertThat(TerminalsGrammar.class.getDeclaredConstructors(), arrayWithSize(1));
        Constructor<TerminalsGrammar> constructor = TerminalsGrammar.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThat(constructor.newInstance(), is(notNullValue()));
        constructor.setAccessible(false);
    }
    
    @Test
    public void testTFloatParse() {
        Parser parser = TerminalsGrammar.tFloat().end();
        
        assertFalse(parser.accept(""));
        assertFalse(parser.accept("forty two"));
        
        assertFalse(parser.accept("-42"));
        assertFalse(parser.accept("(42)"));
        
        assertFalse(parser.accept("-4.20"));
        assertFalse(parser.accept("(4.20)"));
        
        assertFalse(parser.accept("42."));
        assertFalse(parser.accept(".42"));

        assertFalse(parser.accept("4 .2"));
        assertFalse(parser.accept("4. 2"));
        
        assertTrue(parser.accept("4.20"));
        assertTrue(parser.accept("0.42"));
        
        assertTrue(parser.accept("42"));
    }
    
    @Test
    public void testTFloatValue() {
        Parser parser = TerminalsGrammar.tFloat();
        
        Result result = parser.parse("42");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(BigDecimal.valueOf(42)));
        
        result = parser.parse("4.20");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(BigDecimal.valueOf(420, 2)));
        
        result = parser.parse("\t0.42 \n");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(BigDecimal.valueOf(42, 2)));
    }

    @Test
    public void testTLineParse() {
        Parser parser = TerminalsGrammar.tLine().end();
        
        assertFalse(parser.accept("TEST\nTEST"));
        
        assertTrue(parser.accept("TEST"));
    }

    @Test
    public void testTLineValue() {
        Parser parser = TerminalsGrammar.tLine();
        
        Result result = parser.parse("TEST");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is("TEST"));
        
        result = parser.parse("\tThe quick brown fox jumped over the lazy dog. \n");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is("The quick brown fox jumped over the lazy dog."));
    }

    @Test
    public void testTNumberParse() {
        Parser parser = TerminalsGrammar.tNumber().end();
        
        assertFalse(parser.accept(""));
        assertFalse(parser.accept("forty two"));
        
        assertFalse(parser.accept("-42"));
        assertFalse(parser.accept("(42)"));
        
        assertFalse(parser.accept("-4.20"));
        assertFalse(parser.accept("(4.20)"));
        
        assertFalse(parser.accept("42."));
        assertFalse(parser.accept(".42"));

        assertFalse(parser.accept("4 .2"));
        assertFalse(parser.accept("4. 2"));
        
        assertFalse(parser.accept("4.20"));
        assertFalse(parser.accept("0.42"));

        assertTrue(parser.accept("42"));
    }

    @Test
    public void testTNumberValue() {
        Parser parser = TerminalsGrammar.tNumber();
        
        Result result = parser.parse("42");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(Integer.valueOf(42)));
        
        result = parser.parse("\t42 \n");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(Integer.valueOf(42)));
    }

    @Test
    public void testTRangeParse() {
        Parser parser = TerminalsGrammar.tRange().end();
        
        assertFalse(parser.accept(""));
        assertFalse(parser.accept("forty two"));
        
        assertFalse(parser.accept("forty-two"));
        
        assertFalse(parser.accept("-42-42"));
        
        assertFalse(parser.accept("4.0-20.0"));
        
        assertFalse(parser.accept("4 - 20"));
        
        assertTrue(parser.accept("4-20"));
    }

    @Test
    public void testTRangeValue() {
        Parser parser = TerminalsGrammar.tRange();
        
        Result result = parser.parse("4-20");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(ContiguousSet.create(Range.closed(4, 20), DiscreteDomain.integers())));
        
        result = parser.parse("\t4-20 \n");
        assertTrue(result.isSuccess());
        assertThat(result.get(), is(ContiguousSet.create(Range.closed(4, 20), DiscreteDomain.integers())));
    }

}
