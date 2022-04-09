package me.markoutte.sandbox.competitions.interviews;

import junit.framework.TestCase;
import org.junit.Assert;

import static me.markoutte.sandbox.competitions.interviews.LongestMergedString.*;

public class LongestMergedStringTest extends TestCase {
    public void testFindLongest() {
        Assert.assertEquals(0, findLongest(""));
        Assert.assertEquals(1, findLongest("1"));
        Assert.assertEquals(0, findLongest("0"));
        Assert.assertEquals(2, findLongest("101"));
        Assert.assertEquals(4, findLongest("11011"));
        Assert.assertEquals(2, findLongest("110011"));
        Assert.assertEquals(5, findLongest("111011011"));
        Assert.assertEquals(5, findLongest("110110111"));
        Assert.assertEquals(5, findLongest("110111011"));
        Assert.assertEquals(5, findLongest("11111"));
        Assert.assertEquals(50, findLongest("0111111111111111111111111111111111111111111111111110"));
        Assert.assertEquals(5, findLongest("001111100"));
    }

    public void testFindLongest2ZerosCanBeDeleted() {
        Assert.assertEquals(0, findLongest("", 2));
        Assert.assertEquals(1, findLongest("1", 2));
        Assert.assertEquals(0, findLongest("0", 2));
        Assert.assertEquals(2, findLongest("101", 2));
        Assert.assertEquals(3, findLongest("10101", 2));
        Assert.assertEquals(2, findLongest("101001", 2));
        Assert.assertEquals(1, findLongest("1001001", 2));
        Assert.assertEquals(3, findLongest("1010101010101", 2));
        Assert.assertEquals(8, findLongest("1110110111", 2));
        Assert.assertEquals(5, findLongest("11100110111", 2));
        Assert.assertEquals(5, findLongest("11101100111", 2));
    }

    public void testFindLongest3ZerosCanBeDeleted() {
        Assert.assertEquals(0, findLongest("", 3));
        Assert.assertEquals(1, findLongest("1", 3));
        Assert.assertEquals(0, findLongest("0", 3));
        Assert.assertEquals(2, findLongest("101", 3));
        Assert.assertEquals(3, findLongest("10101", 3));
        Assert.assertEquals(2, findLongest("101001", 3));
        Assert.assertEquals(1, findLongest("1001001", 3));
        Assert.assertEquals(4, findLongest("1010101010101", 3));
        Assert.assertEquals(8, findLongest("1110110111", 3));
        Assert.assertEquals(5, findLongest("11100110111", 3));
        Assert.assertEquals(5, findLongest("11101100111", 3));
    }
}