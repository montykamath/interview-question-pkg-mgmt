package com.goodstart.programmingtest;

/**
 * TestEnabledPackageManager captures the output of PackageManager
 * and makes it available for test cases.
 * @author Monty Kamath
 */
public class TestEnabledPackageManager extends PackageManager
{
	protected StringBuilder output = new StringBuilder();
	
	/**
	 * Write a string to standard out
	 * @param s - the string to write
	 */
	public void echo(String s)
	{
		super.echo(s);
		
		this.output.append(s + "\n");
	}
	
	/**
	 * Get the contents written to standard out
	 * @return string contents
	 */
	public String getOutput()
	{
		return output.toString();
	}
	
}
