package com.awdean.data;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.rmi.AlreadyBoundException;
import java.util.Arrays;
import java.util.function.BiFunction;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Range;

public class ItemAttributesTest {

    static final ItemAttributes DEFAULT = new ItemAttributes();
    static final ItemAttributes COMBINED = new ItemAttributes();
    static final ItemAttributes LHS = new ItemAttributes();
    static final ItemAttributes RHS = new ItemAttributes();

    ItemAttributes lhs;
    ItemAttributes rhs;

    @Before
    public void initialize() {
        lhs = LHS;
        rhs = RHS;
    }

    @Test
    public void testExclusive() {
        try {
            ItemAttributes.exclusive(null);
        } catch (AlreadyBoundException e) {
            throw new AssertionError("Unexpected exception: java.rmi.AlreadyBoundException");
        }
    }

    @Test(expected = AlreadyBoundException.class)
    public void testExclusiveEx() throws AlreadyBoundException {
        ItemAttributes.exclusive("test");
    }

    @Test
    public void testJoin() throws AlreadyBoundException {
        ItemAttributes combined = ItemAttributes.join(lhs, rhs);
        assertThat(combined, is(COMBINED));
    }

    @Test(expected = AlreadyBoundException.class)
    public void testJoinEx() throws AlreadyBoundException {
        ItemAttributes.join(lhs, lhs);
        ItemAttributes.join(rhs, rhs);
    }

    @Test
    public void testJoinCoreInPlace() {
        assertJoinInPlaceInvariants(lhs, rhs, ItemAttributes::joinCoreInPlace);
    }

    @Test
    public void testJoinInherentInPlace() {
        assertJoinInPlaceInvariants(lhs, rhs, ItemAttributes::joinInherentsInPlace);
    }

    @Test
    public void testJoinRequirementsInPlace() {
        assertJoinInPlaceInvariants(lhs, rhs, ItemAttributes::joinRequirementsInPlace);
    }

    @Test
    public void testJoinSocketsInPlace() {
        assertJoinInPlaceInvariants(lhs, rhs, ItemAttributes::joinSocketsInPlace);
    }

    @Test
    public void testJoinLevelInPlace() {
        assertJoinInPlaceInvariants(lhs, rhs, ItemAttributes::joinLevelInPlace);
    }

    @Test
    public void testJoinPropertiesInPlace() {
        assertJoinInPlaceInvariants(lhs, rhs, ItemAttributes::joinPropertiesInPlace);
    }

    @Test
    public void testJoinTagsInplace() {
        assertJoinInPlaceInvariants(lhs, rhs, ItemAttributes::joinTagsInPlace);
    }

    private void assertJoinInPlaceInvariants(ItemAttributes lhs, ItemAttributes rhs,
            BiFunction<ItemAttributes, ItemAttributes, String> joiner) {
        ItemAttributes combined = new ItemAttributes();
        assertThat(joiner.apply(combined, lhs), is(nullValue()));
        assertThat(joiner.apply(combined, rhs), is(nullValue()));

        ItemAttributes target = new ItemAttributes();
        assertThat(joiner.apply(target, COMBINED), is(nullValue()));

        assertThat(combined, is(target));
        assertThat(combined, is(not(DEFAULT)));
        
        assertThat(joiner.apply(combined, combined), is(notNullValue()));
    }

    static {
        // Core attributes
        COMBINED.rarity = ItemRarity.NORMAL;
        LHS.rarity = ItemRarity.NORMAL;
        COMBINED.name = "Test Name";
        RHS.name = "Test Name";
        COMBINED.base = "Test Base";
        LHS.base = "Test Base";

        // Common attributes
        COMBINED.type = ItemType.AMULET;
        RHS.type = ItemType.AMULET;
        COMBINED.quality = 20;
        LHS.quality = 20;
        // Armour attributes
        COMBINED.armour = 111;
        RHS.armour = 111;
        COMBINED.evasionRating = 222;
        LHS.evasionRating = 222;
        COMBINED.energyShield = 333;
        RHS.energyShield = 333;
        COMBINED.block = 4;
        LHS.block = 4;
        // Weapon attributes
        COMBINED.physical = Range.closed(2, 3);
        RHS.physical = Range.closed(2, 3);
        COMBINED.elemental = Arrays.asList(Range.closed(5, 7), Range.closed(11, 13), Range.closed(17, 19));
        LHS.elemental = Arrays.asList(Range.closed(5, 7), Range.closed(11, 13), Range.closed(17, 19));
        COMBINED.chaos = Range.closed(23, 29);
        RHS.chaos = Range.closed(23, 29);
        COMBINED.critical = BigDecimal.ONE;
        LHS.critical = BigDecimal.ONE;
        COMBINED.attacks = BigDecimal.TEN;
        RHS.attacks = BigDecimal.TEN;

        // Item requirements
        COMBINED.levelRequired = 9;
        LHS.levelRequired = 9;
        COMBINED.strengthRequired = 18;
        RHS.strengthRequired = 18;
        COMBINED.dexterityRequired = 27;
        LHS.dexterityRequired = 27;
        COMBINED.intelligenceRequired = 36;
        RHS.intelligenceRequired = 36;

        // Item sockets
        COMBINED.sockets = "R-R-G-G-B-B";
        LHS.sockets = "R-R-G-G-B-B";

        // Item level
        COMBINED.level = 42;
        RHS.level = 42;

        // Item properties
        COMBINED.implicit = "Test Implicit";
        LHS.implicit = "Test Implicit";
        COMBINED.explicits = Arrays.asList("Test Explicit One", "Test Explicit Two");
        RHS.explicits = Arrays.asList("Test Explicit One", "Test Explicit Two");

        // Item tags
        COMBINED.corrupted = true;
        LHS.corrupted = true;
        COMBINED.mirrored = true;
        RHS.mirrored = true;
        COMBINED.unidentified = true;
        LHS.unidentified = true;
    }

}
