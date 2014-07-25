package com.example.picspot.misc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	
	private Pattern pattern;
	private Matcher matcher;
	private String str;
	
	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

	public Validator(String pat){
		this.pattern = Pattern.compile(pat);
	}

	  /**
	   * Validate username with regular expression
	   * @param username username for validation
	   * @return true valid username, false invalid username
	   */
	public boolean isValid(final String str){
		matcher = pattern.matcher(str);
		return matcher.matches();

	}
	
}
