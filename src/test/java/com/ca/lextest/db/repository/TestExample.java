package com.ca.lextest.db.repository;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.ca.lextest.junit.categories.*;

@Category({Smoke.class, Db.class, Fast.class})
public class TestExample {
	  @BeforeClass
	  public void setUpClass() throws Exception {
	    // run once before any of the tests
	  }
	 
	  @Before
	  public void setUp() throws Exception {
	    // run before each test
	  }
	 
	  @Test
	  public void testGetter() throws Exception {
	    // call the hasFancyProps() getter
		  fail();
	  }
	 
	  @Test
	  public void testMethod() throws Exception {
	    // call the myFancyMethod() method
		  fail();
	  }
	 
	  @After
	  public void tearDown() throws Exception {
	    // run after each test
	  }
	 
	  @AfterClass
	  public void tearDownClass() throws Exception {
	    // run once after all tests
	  }
}