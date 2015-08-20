package com.awdean.data;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class ItemTypeTest {

    @Test
    public void testParseItemTypeDistinct() {
        List<ItemType> values = Arrays.asList(ItemType.values());
        Set<String> descriptions = values.stream()
                                         .map((ItemType itemType) -> itemType.toString())
                                         .collect(Collectors.toSet());
        assertThat(descriptions, iterableWithSize(values.size()));
    }
    
    @Test
    public void testParseItemTypeOnto() {
        for (ItemType itemType : ItemType.values()) {
            assertThat(ItemType.parseItemType(itemType.toString()), is(itemType));
        }
    }
    
}
