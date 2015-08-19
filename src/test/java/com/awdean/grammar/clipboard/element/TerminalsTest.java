package com.awdean.grammar.clipboard.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;
import org.petitparser.context.Result;
import org.petitparser.parser.Parser;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class TerminalsTest {

	@Test
	public void testTFloatParse() {
		Parser parser = Terminals.tFloat().end();
		
		assertTrue(parser.parse("").isFailure());
		assertTrue(parser.parse("forty two").isFailure());
		
		assertTrue(parser.parse("-42").isFailure());
		assertTrue(parser.parse("(42)").isFailure());
		
		assertTrue(parser.parse("-4.20").isFailure());
		assertTrue(parser.parse("(4.20)").isFailure());
		
		assertTrue(parser.parse("42.").isFailure());
		assertTrue(parser.parse(".42").isFailure());

		assertTrue(parser.parse("4 .2").isFailure());
		assertTrue(parser.parse("4. 2").isFailure());
	}
	
	@Test
	public void testTFloatValue() {
		Parser parser = Terminals.tFloat();
		
		Result result = parser.parse("42");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), BigDecimal.valueOf(42));
		
		result = parser.parse("4.20");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), BigDecimal.valueOf(420, 2));
		
		result = parser.parse("\t0.42 \n");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), BigDecimal.valueOf(42, 2));
	}

	@Test
	public void testTNumberParse() {
		Parser parser = Terminals.tNumber().end();
		
		assertTrue(parser.parse("").isFailure());
		assertTrue(parser.parse("forty two").isFailure());
		
		assertTrue(parser.parse("-42").isFailure());
		assertTrue(parser.parse("(42)").isFailure());
		
		assertTrue(parser.parse("-4.20").isFailure());
		assertTrue(parser.parse("(4.20)").isFailure());
		
		assertTrue(parser.parse("42.").isFailure());
		assertTrue(parser.parse(".42").isFailure());
		
		assertTrue(parser.parse("42.").isFailure());
		assertTrue(parser.parse(".42").isFailure());

		assertTrue(parser.parse("4 .2").isFailure());
		assertTrue(parser.parse("4. 2").isFailure());

		assertTrue(parser.parse("4.20").isFailure());
		assertTrue(parser.parse("0.42").isFailure());
	}

	@Test
	public void testTNumberValue() {
		Parser parser = Terminals.tNumber();
		
		Result result = parser.parse("42");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), Integer.valueOf(42));
		
		result = parser.parse("\t42 \n");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), Integer.valueOf(42));
	}

	@Test
	public void testTRangeParse() {
		Parser parser = Terminals.tRange().end();
		
		assertTrue(parser.parse("").isFailure());
		assertTrue(parser.parse("FORTY TWO").isFailure());
		assertTrue(parser.parse("FORTY-TWO").isFailure());
		
		assertTrue(parser.parse("-42-42").isFailure());
		
		assertTrue(parser.parse("4.0-20.0").isFailure());
		
		assertTrue(parser.parse("4-20").isSuccess());
	}

	@Test
	public void testTRangeValue() {
		Parser parser = Terminals.tRange();
		
		Result result = parser.parse("4-20");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), ContiguousSet.create(Range.closed(4, 20), DiscreteDomain.integers()));
		
		result = parser.parse("\t4-20 \n");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), ContiguousSet.create(Range.closed(4, 20), DiscreteDomain.integers()));
	}

	@Test
	public void testTStringParse() {
		Parser parser = Terminals.tString().end();
		
		assertTrue(parser.parse(Terminals.SECTION_BREAK).isFailure());
		
		assertTrue(parser.parse("TEST\nTEST").isFailure());
	}

	@Test
	public void testTStringValue() {
		Parser parser = Terminals.tString();
		
		Result result = parser.parse("TEST");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), "TEST");
		
		result = parser.parse("\tThe quick brown fox jumped over the lazy dog. \n");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), "The quick brown fox jumped over the lazy dog.");
	}

	@Test
	public void testTSectionBreakParse() {
		Parser parser = Terminals.tSectionBreak().end();

		assertTrue(parser.parse("TEST").isFailure());
		assertTrue(parser.parse(Terminals.SECTION_BREAK).isSuccess());
	}

	@Test
	public void testTSectionBreakValue() {
		Parser parser = Terminals.tSectionBreak();
		
		Result result = parser.parse(Terminals.SECTION_BREAK);
		assertTrue(result.isSuccess());
		assertEquals(result.get(), Terminals.SECTION_BREAK);

		result = parser.parse("\t" + Terminals.SECTION_BREAK + " \n");
		assertTrue(result.isSuccess());
		assertEquals(result.get(), Terminals.SECTION_BREAK);
	}

}
