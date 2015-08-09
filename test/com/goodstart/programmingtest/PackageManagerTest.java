package com.goodstart.programmingtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * PackageManagerTest is a test case class that tests the functionality
 * of the PackageManger and Package classes.
 * @author Monty Kamath
 */
public class PackageManagerTest
{
	protected TestEnabledPackageManager pm;
	
	/**
	 * Assert that TELNET and it's dependencies are installed properly
	 */
	protected void assertTelnetAndDependenciesInstalled()
	{
		assertTrue(pm.getPackage("TELNET").isInstalled());
		assertTrue(pm.getPackage("TELNET").isExplicit());
		assertTrue(pm.getPackage("TCPIP").isInstalled());
		assertFalse(pm.getPackage("TCPIP").isExplicit());
		assertTrue(pm.getPackage("NETCARD").isInstalled());
		assertFalse(pm.getPackage("NETCARD").isExplicit());
		assertEquals(2, pm.getPackage("TELNET").getDependencies().size());
	}

	/**
	 * Assert that TELNET and it's dependencies are not installed
	 */
	protected void assertTelnetAndDependenciesNotInstalled()
	{
		assertFalse(pm.getPackage("TELNET").isInstalled());
		assertFalse(pm.getPackage("TELNET").isExplicit());
		assertFalse(pm.getPackage("TCPIP").isInstalled());
		assertFalse(pm.getPackage("TCPIP").isExplicit());
		assertFalse(pm.getPackage("NETCARD").isInstalled());
		assertFalse(pm.getPackage("NETCARD").isExplicit());
		assertEquals(2, pm.getPackage("TELNET").getDependencies().size());
	}
	
	/**
	 * Return the expected results from the list comand
	 * @return - expected output
	 */
	protected String getExpectedListResults()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DEPEND   TELNET TCPIP NETCARD\n");  
		sb.append("INSTALL TELNET\n");
		sb.append("    Installing TELNET\n");
		sb.append("    Installing TCPIP\n");
		sb.append("    Installing NETCARD\n");
		sb.append("LIST\n");
		sb.append("    TCPIP\n");
		sb.append("    NETCARD\n");
		sb.append("    TELNET\n");
		return sb.toString();
	}
	
	/**
	 * Return the expected results from the file of input commands
	 * @return - expected output
	 */
	protected String getExpectedResults()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DEPEND   TELNET TCPIP NETCARD\n");  
		sb.append("DEPEND TCPIP NETCARD\n");  
		sb.append("DEPEND DNS TCPIP NETCARD\n");  
		sb.append("DEPEND  BROWSER   TCPIP  HTML\n");  
		sb.append("INSTALL NETCARD\n");  
		sb.append("    Installing NETCARD\n");  
		sb.append("INSTALL TELNET\n");  
		sb.append("    Installing TELNET\n");  
		sb.append("    Installing TCPIP\n");  
		sb.append("INSTALL foo\n");  
		sb.append("    Installing foo\n");  
		sb.append("REMOVE NETCARD\n"); 
		sb.append("    NETCARD is still needed\n"); 
		sb.append("INSTALL BROWSER\n"); 
		sb.append("    Installing BROWSER\n"); 
		sb.append("    Installing HTML\n"); 
		sb.append("INSTALL DNS\n"); 
		sb.append("    Installing DNS\n"); 
		sb.append("LIST\n"); 
		sb.append("    TCPIP\n"); 
		sb.append("    HTML\n"); 
		sb.append("    NETCARD\n"); 
		sb.append("    BROWSER\n"); 
		sb.append("    TELNET\n"); 
		sb.append("    foo\n"); 
		sb.append("    DNS\n"); 
		sb.append("REMOVE TELNET\n"); 
		sb.append("    Removing TELNET\n"); 
		sb.append("REMOVE NETCARD\n"); 
		sb.append("    NETCARD is still needed\n"); 
		sb.append("REMOVE DNS\n"); 
		sb.append("    Removing DNS\n"); 
		sb.append("REMOVE NETCARD\n"); 
		sb.append("    NETCARD is still needed\n"); 
		sb.append("INSTALL NETCARD\n"); 
		sb.append("    NETCARD is already installed\n"); 
		sb.append("REMOVE TCPIP\n"); 
		sb.append("    TCPIP is still needed\n"); 
		sb.append("REMOVE BROWSER\n"); 
		sb.append("    Removing BROWSER\n"); 
		sb.append("    Removing TCPIP\n"); 
		sb.append("    Removing HTML\n"); 
		sb.append("REMOVE TCPIP\n"); 
		sb.append("    TCPIP is not installed\n"); 
		sb.append("LIST\n"); 
		sb.append("    NETCARD\n"); 
		sb.append("    foo\n"); 
		sb.append("END\n"); 
		return sb.toString();
	}

	/**
	 * Get the input file as a collection of lines
	 * @return - list of lines
	 */
	protected ArrayList<String> getInputFileAsLines()
	{
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("DEPEND   TELNET TCPIP NETCARD");
		lines.add("DEPEND TCPIP NETCARD");
		lines.add("DEPEND DNS TCPIP NETCARD");
		lines.add("DEPEND  BROWSER   TCPIP  HTML");
		lines.add("INSTALL NETCARD");
		lines.add("INSTALL TELNET");
		lines.add("INSTALL foo");
		lines.add("REMOVE NETCARD");
		lines.add("INSTALL BROWSER");
		lines.add("INSTALL DNS");
		lines.add("LIST");
		lines.add("REMOVE TELNET");
		lines.add("REMOVE NETCARD");
		lines.add("REMOVE DNS");
		lines.add("REMOVE NETCARD");
		lines.add("INSTALL NETCARD");
		lines.add("REMOVE TCPIP");
		lines.add("REMOVE BROWSER");
		lines.add("REMOVE TCPIP");
		lines.add("LIST");
		lines.add("END");
		return lines;
	}
	
	
	/**
	 * Set up the test case
	 */
	@Before
	public void setUp()
	{
		pm = new TestEnabledPackageManager();
	}
	
	/**
	 * Test the depend command
	 */
	@Test
	public void testDepend()
	{
		pm.processLine("DEPEND   TELNET TCPIP NETCARD");
		this.assertTelnetAndDependenciesNotInstalled();
	}


	/**
	 * Test the input file of commands
	 * @throws IOException
	 */
	@Test
	public void testFileInput() throws IOException
	{
		// Instead of using a file as input, use a collection of lines
		// That way the test case doesn't depend on external resources
		// You can always uncomment this line to test with an external file
		//pm.processFile("c:\\problema.in.txt");
		
		for(String line : this.getInputFileAsLines())
		{
			pm.processLine(line);
		}
		String real = pm.getOutput();
		String expected = this.getExpectedResults();
		assertTrue(real.equals(expected));
	}

	/**
	 * Test the install command
	 */
	@Test
	public void testInstall()
	{
		pm.processLine("INSTALL foo");
		assertFalse(null == pm.getPackage("foo"));
		assertEquals(0, pm.getPackage("foo").getDependencies().size());
	}
	
	/**
	 * Test the install command where there are dependencies
	 */
	@Test
	public void testInstallWithDependencies()
	{
		pm.processLine("DEPEND   TELNET TCPIP NETCARD");
		pm.processLine("INSTALL TELNET");
		assertTelnetAndDependenciesInstalled();
	}
	
	/**
	 * Test the list command
	 */
	@Test
	public void testList()
	{
		pm.processLine("DEPEND   TELNET TCPIP NETCARD");
		pm.processLine("INSTALL TELNET");
		pm.processLine("LIST");

		String real = pm.getOutput();
		String expected = this.getExpectedListResults();
		assertTrue(real.equals(expected));
	}

	/**
	 * Test the remove command when there are dependencies
	 */
	@Test
	public void testRemoveWithDependencies()
	{
		pm.processLine("DEPEND   TELNET TCPIP NETCARD");
		pm.processLine("INSTALL TELNET");
		this.assertTelnetAndDependenciesInstalled();
		
		pm.processLine("REMOVE TELNET");
		this.assertTelnetAndDependenciesNotInstalled();
		assertTrue(pm.getPackages().size() == 3);
	}
	
	/**
	 * Test the remove command when you try to remove a package that cannot be removed
	 */
	@Test
	public void testRemoveWithDependenciesFails()
	{
		pm.processLine("DEPEND   TELNET TCPIP NETCARD");
		pm.processLine("INSTALL TELNET");
		this.assertTelnetAndDependenciesInstalled();
		
		pm.processLine("REMOVE TCPIP");
		this.assertTelnetAndDependenciesInstalled();
		assertTrue(pm.getPackages().size() == 3);
	}
	
}
