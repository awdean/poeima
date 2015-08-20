package com.awdean.data;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class ItemRarityTest {

    @Test
    public void testParseItemRarityDistinct() {
        List<ItemRarity> values = Arrays.asList(ItemRarity.values());
        Set<String> descriptions = values.stream()
                                         .map((ItemRarity itemRarity) -> itemRarity.toString())
                                         .collect(Collectors.toSet());
        assertThat(descriptions, iterableWithSize(values.size()));
    }
    
    @Test
    public void testParseItemRarityOnto() {
        for (ItemRarity itemRarity : ItemRarity.values()) {
            assertThat(ItemRarity.parseItemRarity(itemRarity.toString()), is(itemRarity));
        }
    }

}
