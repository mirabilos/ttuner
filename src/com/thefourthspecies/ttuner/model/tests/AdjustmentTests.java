package com.thefourthspecies.ttuner.model.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.thefourthspecies.ttuner.model.data.Adjustment;
import com.thefourthspecies.ttuner.model.exceptions.ParseException;


public class AdjustmentTests {

	final static private double syn = 81/80.0;
	final static private double pyth = 531441/524288.0;

	double DELTA = 0.0000000001;	
	
	@Test
	public void goodTests() throws ParseException {
		Adjustment a = new Adjustment("+1/6P");
		double a1 = Math.pow(pyth, 1/6.0);
		assertEquals(a1, a.toDouble(), DELTA);
		
		a = new Adjustment("-1/5P");
		a1 = Math.pow(pyth, -1/5.0);
		assertEquals(a1, a.toDouble(), DELTA);
		
		a = new Adjustment("-1/4S");
		a1 = Math.pow(syn, -1/4.0);
		assertEquals(a1, a.toDouble(), DELTA);
		
		a = new Adjustment("+234/033321S");
		a1 = Math.pow(syn, 234/33321.0);
		assertEquals(a1, a.toDouble(), DELTA);
		
	}
	
	@Test
	public void testGetSymbol() throws ParseException {
		Adjustment a = new Adjustment("-1/6P");
		assertEquals("-1/6P", a.getSymbol());
	}
	
	@Test (expected = ParseException.class)
	public void testNoComma() throws ParseException {
		Adjustment a = new Adjustment("+1/6");
		fail("SHOULDN'T EXIST!: " + a);
	}
	
	@Test (expected = ParseException.class)
	public void testBadFormat() throws ParseException {
		Adjustment a = new Adjustment("+23s/32.3P");
		fail("SHOULDN'T EXIST!: " + a);
	}
}
