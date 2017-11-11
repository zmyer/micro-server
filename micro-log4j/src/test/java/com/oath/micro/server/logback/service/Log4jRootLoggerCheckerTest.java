package com.oath.micro.server.logback.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.oath.micro.server.log4j.service.Log4jRootLoggerChecker;



public class Log4jRootLoggerCheckerTest {
	
	Log4jRootLoggerChecker checker;
	
	@Before
	public void setUp() {
		checker = new Log4jRootLoggerChecker(true, "INFO");
	}
	
	@Test
	public void testCheck() {
		checker.check();
		assertThat(Logger.getRootLogger().getLevel(), is(Level.INFO));
		
		checker.setCorrectLevelStr("DEBUG");
		checker.check();
		assertThat(Logger.getRootLogger().getLevel(), is(Level.DEBUG));
		
		checker.setActive(false);
		checker.setCorrectLevelStr("WARN");
		checker.check();
		assertThat(Logger.getRootLogger().getLevel(), is(Level.DEBUG));
	}

}
