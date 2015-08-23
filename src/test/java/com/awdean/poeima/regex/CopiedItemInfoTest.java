package com.awdean.poeima.regex;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;

import com.awdean.poeima.data.ItemAttributes;
import com.awdean.poeima.data.ItemRarity;

public class CopiedItemInfoTest {

    static final String itemCopy = "Rarity: Magic\n" + "Seething Divine Life Flask of Staunching\n" + "--------\n"
            + "Quality: +20% (augmented)\n" + "Recovers 979 (augmented) Life over 7.00 Seconds\n"
            + "Consumes 15 of 45 Charges on use\n" + "Currently has 45 Charges\n" + "--------\n" + "Requirements:\n"
            + "Level: 60\n" + "--------\n" + "Item Level: 67\n" + "--------\n" + "42% test implicit value filler\n"
            + "--------\n" + "66% reduced Amount Recovered\n" + "Instant Recovery\n"
            + "Immunity to Bleeding during flask effect\n" + "Removes Bleeding on use\n" + "--------\n" + "Mirrored\n"
            + "--------\n" + "Corrupted\n" + "--------\n"
            + "Right click to drink. Can only hold charges while in belt. Refills as you kill monsters.\n";
    static final String itemCopyTrimHeader = "\n" + "--------\n" + "42% test implicit value filler\n" + "--------\n"
            + "66% reduced Amount Recovered\n" + "Instant Recovery\n" + "Immunity to Bleeding during flask effect\n"
            + "Removes Bleeding on use\n" + "--------\n" + "Mirrored\n" + "--------\n" + "Corrupted\n" + "--------\n"
            + "Right click to drink. Can only hold charges while in belt. Refills as you kill monsters.\n";
    static final String itemCopyTrimFooter = "Rarity: Magic\n" + "Seething Divine Life Flask of Staunching\n"
            + "--------\n" + "Quality: +20% (augmented)\n" + "Recovers 979 (augmented) Life over 7.00 Seconds\n"
            + "Consumes 15 of 45 Charges on use\n" + "Currently has 45 Charges\n" + "--------\n" + "Requirements:\n"
            + "Level: 60\n" + "--------\n" + "Item Level: 67\n" + "--------\n" + "42% test implicit value filler\n"
            + "--------\n" + "66% reduced Amount Recovered\n" + "Instant Recovery\n"
            + "Immunity to Bleeding during flask effect\n" + "Removes Bleeding on use";
    static final String itemCopyTrim = "\n" + "--------\n" + "42% test implicit value filler\n" + "--------\n"
            + "66% reduced Amount Recovered\n" + "Instant Recovery\n" + "Immunity to Bleeding during flask effect\n"
            + "Removes Bleeding on use";
    
    Properties properties;

    @Before
    public void loadProperties() throws IOException {
        properties = new Properties();
        // xxx.yyy.zzz.CopiedItemInfo
        String ciic = CopiedItemInfo.class.getName();
        if (-1 < ciic.lastIndexOf('.')) {
            // xxx.yyy.zzz.
            ciic = ciic.substring(0, ciic.lastIndexOf('.') + 1);
        }
        // xxx/yyy/zzz/
        ciic = ciic.replace('.', '/');
        
        String path = ciic + CopiedItemInfo.PROPERTIES;
        InputStream contents = this.getClass().getClassLoader().getResourceAsStream(path);
        properties.load(contents);
    }

    @Test
    public void testGetInstance() {
        assertThat(CopiedItemInfo.getInstance(), is(notNullValue()));
        assertThat(CopiedItemInfo.getInstance(), is(sameInstance(CopiedItemInfo.getInstance())));
    }

    @Test
    public void testCopiedItemInfoProperties() {
        CopiedItemInfo cii = new CopiedItemInfo(properties);
        
        Map<String, Function<CopiedItemInfo, String>> getters = new HashMap<>();
        getters.put("prefixItemLevel", CopiedItemInfo::getPrefixItemLevel);
        getters.put("prefixRarity", CopiedItemInfo::getPrefixRarity);
        getters.put("substringFlaskHelp", CopiedItemInfo::getSubstringFlaskHelp);
        getters.put("substringJewelHelp", CopiedItemInfo::getSubstringJewelHelp);
        getters.put("substringMapHelp", CopiedItemInfo::getSubstringMapHelp);
        getters.put("tagCorrupted", CopiedItemInfo::getTagCorrupted);
        getters.put("tagMirrored", CopiedItemInfo::getTagMirrored);
        getters.put("tagUnidentified", CopiedItemInfo::getTagUnidentified);
        getters.put("tokenRarityMagic", CopiedItemInfo::getTokenRarityMagic);
        getters.put("tokenRarityRare", CopiedItemInfo::getTokenRarityRare);
        
        assertThat(getters.entrySet(), hasSize(greaterThan(0)));
        for (Map.Entry<String, Function<CopiedItemInfo, String>> getter : getters.entrySet()) {
            String key = getter.getKey();
            assertThat(properties, hasKey(getter.getKey()));
            String value = properties.getProperty(key);
            assertThat(value, is(notNullValue()));
            assertThat(getter.getValue().apply(cii), is(value));
        }
    }

    // Initialization should fail if given a properties object with a non-string value
    @Test
    public void testCopiedItemInfoPropertiesEx0() {
        properties.put("TEST_KEY", String.class);
        Exception ex = null;
        try {
            new CopiedItemInfo(properties);
        } catch (RuntimeException rtex) {
            ex = rtex;
        }
        assertThat(ex, is(notNullValue()));
        assertThat(ex.getCause(), is(notNullValue()));
        assertThat(ex.getCause(), is(instanceOf(InvalidPropertiesFormatException.class)));
    }
    
    // Initialization should fail if given a properties object missing expected keys
    @Test
    public void testCopiedItemInfoPropertiesEx1() {
        properties.remove("prefixItemLevel");
        Exception ex = null;
        try {
            new CopiedItemInfo(properties);
        } catch (RuntimeException rtex) {
            ex = rtex;
        }
        assertThat(ex, is(notNullValue()));
        assertThat(ex.getCause(), is(notNullValue()));
        assertThat(ex.getCause(), is(instanceOf(InvalidPropertiesFormatException.class)));
        assertThat(ex.getCause().getMessage(), containsString("Excess"));
        assertThat(ex.getCause().getMessage(), not(containsString("Missing")));
    }
    
    // Initialization should fail if given a properties object missing expected keys
    @Test
    public void testCopiedItemInfoPropertiesEx2() {
        properties.put("TEST_KEY", "TEST_VALUE");
        Exception ex = null;
        try {
            new CopiedItemInfo(properties);
        } catch (RuntimeException rtex) {
            ex = rtex;
        }
        assertThat(ex, is(notNullValue()));
        assertThat(ex.getCause(), is(notNullValue()));
        assertThat(ex.getCause(), is(instanceOf(InvalidPropertiesFormatException.class)));
        assertThat(ex.getCause().getMessage(), containsString("Missing"));
        assertThat(ex.getCause().getMessage(), not(containsString("Excess")));
    }
    
    @Test
    public void testToString() {
        assertThat(CopiedItemInfo.getInstance().toString(), is(notNullValue()));
    }

    @Test
    public void testParseHeader() {
        ItemAttributes item = new ItemAttributes();
        item.rarity = ItemRarity.MAGIC;
        item.name = "Seething Divine Life Flask of Staunching";
        item.level = 67;
        
        ItemAttributes parsed = new ItemAttributes();
        assertThat(parsed, is(not(item)));
        String after = CopiedItemInfo.getInstance().parseHeader(parsed, itemCopy);
        assertThat(after, is(notNullValue()));
        assertThat(after, is(not(itemCopy)));
        assertThat(after, is(itemCopyTrimHeader));
        assertThat(parsed, is(item));
        
        parsed = new ItemAttributes();
        assertThat(parsed, is(not(item)));
        after = CopiedItemInfo.getInstance().parseHeader(parsed, itemCopyTrimFooter);
        assertThat(after, is(notNullValue()));
        assertThat(after, is(not(itemCopyTrimFooter)));
        assertThat(after, is(itemCopyTrim));
        assertThat(parsed, is(item));
    }
    
    @Test
    public void testParseFooter() {
        ItemAttributes item = new ItemAttributes();
        item.corrupted = true;
        item.mirrored = true;
        
        ItemAttributes parsed = new ItemAttributes();
        assertThat(parsed, is(not(item)));
        String after = CopiedItemInfo.getInstance().parseFooter(parsed, itemCopy);
        assertThat(after, is(notNullValue()));
        assertThat(after, is(not(itemCopy)));
        assertThat(after, is(itemCopyTrimFooter));
        assertThat(parsed, is(item));
        
        parsed = new ItemAttributes();
        assertThat(parsed, is(not(item)));
        after = CopiedItemInfo.getInstance().parseFooter(parsed, itemCopyTrimHeader);
        assertThat(after, is(notNullValue()));
        assertThat(after, is(not(itemCopyTrimHeader)));
        assertThat(after, is(itemCopyTrim));
        assertThat(parsed, is(item));
    }

    @Test
    public void testParse() {
        ItemAttributes item = new ItemAttributes();
        item.rarity = ItemRarity.MAGIC;
        item.name = "Seething Divine Life Flask of Staunching";
        item.level = 67;
        item.implicit = "42% test implicit value filler";
        item.explicits = Arrays.asList("66% reduced Amount Recovered", "Instant Recovery", "Immunity to Bleeding during flask effect",
            "Removes Bleeding on use");
        
        ItemAttributes parsed = CopiedItemInfo.getInstance().parse(itemCopyTrimFooter);
        assertThat(parsed, is(item));
        
        item.corrupted = true;
        item.mirrored = true;
        
        assertThat(parsed, is(not(item)));
        parsed = CopiedItemInfo.getInstance().parse(itemCopy);
        assertThat(parsed, is(item));
    }

}
