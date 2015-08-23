package com.awdean.poeima.regex;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class RegexUtilsTest {

    @Test
    public void testConstructorIsPrivate()
            throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        assertThat(RegexUtils.class.getDeclaredConstructors(), arrayWithSize(1));
        Constructor<RegexUtils> constructor = RegexUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThat(constructor.newInstance(), is(notNullValue()));
        constructor.setAccessible(false);
    }

    @Test
        public void testToLiteralPattern() {
            String input = ".+*?[^]$(){}=!<>|:-\\";
            assertTrue(input, input.matches(RegexUtils.toLiteralPattern(input)));
    
            for (int ii = 0; ii < 128 * 128; ii++) {
                int xx = ii;
                StringBuilder sb = new StringBuilder();
                for (int jj = 0; jj < 2; jj++) {
                    sb.append((char) (xx % 128));
                    xx /= 128;
                }
                input = sb.toString();
                assertTrue(input, input.matches(RegexUtils.toLiteralPattern(input)));
            }        
        }

    @Test(expected = NullPointerException.class)
        public void testToLiteralPatternNPE() {
            RegexUtils.toLiteralPattern(null);
        }
    
}
