package me.markoutte.sandbox.competitions.interviews;

import org.junit.jupiter.api.Test;

import static me.markoutte.sandbox.competitions.interviews.LongestMergedString.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LongestMergedStringTest {
    
    @Test
    public void testFindLongest() {
        assertEquals(0, findLongest(""));
        assertEquals(1, findLongest("1"));
        assertEquals(0, findLongest("0"));
        assertEquals(2, findLongest("101"));
        assertEquals(4, findLongest("11011"));
        assertEquals(2, findLongest("110011"));
        assertEquals(5, findLongest("111011011"));
        assertEquals(5, findLongest("110110111"));
        assertEquals(5, findLongest("110111011"));
        assertEquals(5, findLongest("11111"));
        assertEquals(50, findLongest("0111111111111111111111111111111111111111111111111110"));
        assertEquals(5, findLongest("001111100"));
    }

    @Test
    public void testFindLongest2ZerosCanBeDeleted() {
        assertEquals(0, findLongest("", 2));
        assertEquals(1, findLongest("1", 2));
        assertEquals(0, findLongest("0", 2));
        assertEquals(2, findLongest("101", 2));
        assertEquals(3, findLongest("10101", 2));
        assertEquals(2, findLongest("101001", 2));
        assertEquals(1, findLongest("1001001", 2));
        assertEquals(3, findLongest("1010101010101", 2));
        assertEquals(8, findLongest("1110110111", 2));
        assertEquals(5, findLongest("11100110111", 2));
        assertEquals(5, findLongest("11101100111", 2));
    }

    @Test
    public void testFindLongest3ZerosCanBeDeleted() {
        assertEquals(0, findLongest("", 3));
        assertEquals(1, findLongest("1", 3));
        assertEquals(0, findLongest("0", 3));
        assertEquals(2, findLongest("101", 3));
        assertEquals(3, findLongest("10101", 3));
        assertEquals(2, findLongest("101001", 3));
        assertEquals(1, findLongest("1001001", 3));
        assertEquals(4, findLongest("1010101010101", 3));
        assertEquals(8, findLongest("1110110111", 3));
        assertEquals(5, findLongest("11100110111", 3));
        assertEquals(5, findLongest("11101100111", 3));
    }
}