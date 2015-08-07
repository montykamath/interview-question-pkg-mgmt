package com.goodstart.programmingtest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 * PackageManager maintains a list of all packages (installed or not installed)
 * PackageManager can process a file line by line and handle these commands
 *   INSTALL, DEPEND, REMOVE, LIST, END
 * Each package maintains it's own dependencies and state
 * @author Monty Kamath
 */
public class PackageManager
{
	protected HashMap<String,Package> packages = new HashMap<String,Package>();

	/**
	 * Add a package to the map of all packages
	 * @param pkg - package to add
	 */
	public void addPackage(Package pkg)
	{
		this.packages.put(pkg.getName(), pkg);
	}
	
	/**
	 * Write a string to standard out
	 * @param s - the string to write
	 */
	public void echo(String s)
	{
		System.out.println(s);
	}
	
	/**
	 * Write a string to standard out with a 4 space indent 
	 * @param s - the string to write
	 */
	public void echoIndented(String s)
	{
		this.echo("    " + s);
	}
	
	
	/**
	 * Get a package with name.  If it doesn't exist, create it.
	 * @param name - name of package to get
	 * @return a package
	 */
	public Package getOrCreatePackage(String name)
	{
		Package pkg = this.getPackage(name);
		if(pkg == null)
		{
			pkg = new Package(name);
			this.addPackage(pkg);
		}
		return pkg;
	}
	
	/**
	 * Get a package with name
	 * @param name - name of package to get
	 * @return a package
	 */
	public Package getPackage(String name)
	{
		return this.packages.get(name);
	}
	
	/**
	 * Return all packages in a map of name -> package
	 * @return map of all packages
	 */
	public HashMap<String, Package> getPackages()
	{
		return packages;
	}
	
	/**
	 * Process the depend command
	 * @param parts - string array split on white space
	 */
	protected void processDependCommand(String[] parts)
	{
		String name = parts[1];
		Package pkg = this.getOrCreatePackage(name);
		// iterate the dependent packages (words 3 and beyond)
		// ex:   DEPEND TELNET TCPIP NETCARD
		// dependent packages are TCPIP and NETCARD
		for(int i = 2; i<parts.length; i++)
		{
			String dName = parts[i];
			Package dPkg = this.getOrCreatePackage(dName);
			pkg.addDependency(dPkg);
		}
	}

	/**
	 * Process the end command
	 * @param parts - string array split on white space
	 */
	protected void processEndCommand(String[] parts)
	{
		// do nothing
	}

	/**
	 * Process the file at path one line at a time
	 * where each line contains a command (INSTALL, DEPEND, REMOVE, LIST, END)
	 * @param path - file to process
	 * @throws IOException
	 */
	public void processFile(String path) throws IOException
	{
		File file = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		try
		{
			String line = null;
			// read until there are no more lines
			while ((line = reader.readLine()) != null)
			{
				this.processLine(line);
			}
		} finally
		{
			reader.close();
		}
	}

	/**
	 * Process the install command
	 * @param parts - string array split on white space
	 */
	protected void processInstallCommand(String[] parts)
	{
		String name = parts[1];
		Package pkg = this.getOrCreatePackage(name);
		pkg.setExplicit();

		if(pkg.isInstalled())
		{
			this.echoIndented(pkg.getName() + " is already installed");
			return;
		}

		pkg.install(this);
	}
	
	
	/**
	 * Process a single line in one of these forms:
	 *   DEPEND TELNET TCPIP NETCARD
	 *   INSTALL TELNET
	 *   REMOVE TELNET
	 *   END
	 *   LIST
	 * @param line
	 */
	protected void processLine(String line)
	{
		this.echo(line);
		String[] parts = line.split("\\s+");
		if(parts.length <= 0)
		{
			return; //ignore empty lines
		}
		String command = parts[0].toUpperCase();
		
		// If this list of commands grows past 5 then convert it to
		// a map of command objects instead of a big if..else statement
		if(command.equals("DEPEND"))
		{
			this.processDependCommand(parts);
		} 
		else if(command.equals("INSTALL"))
		{
			this.processInstallCommand(parts);
		} 
		else if(command.equals("REMOVE")) 
		{
			this.processRemoveCommand(parts);
		} 
		else if(command.equals("LIST")) 
		{
			this.processListCommand(parts);
		}
		else if(command.equals("END"))
		{
			this.processEndCommand(parts);
		}
		else
		{
			this.echoIndented("unknown command");
		}
	}
	
	/**
	 * Process the list command
	 * @param parts - string array split on white space
	 */
	protected void processListCommand(String[] parts)
	{		
		for(Package pkg : this.getPackages().values())
		{
			if(pkg.isInstalled())
			{
				this.echoIndented(pkg.getName());
			}
		}
	}
	
	/**
	 * Process the remove command
	 * @param parts - string array split on white space
	 */
	protected void processRemoveCommand(String[] parts)
	{		
		String name = parts[1];
		
		Package pkg = this.getPackage(name);
		if(pkg == null || !pkg.isInstalled())
		{
			this.echoIndented(name + " is not installed");
			return;
		}
		else
		{
			if(pkg.canBeRemoved(this, true))
			{
				pkg.remove(this);
			}
			else
			{
				this.echoIndented(name + " is still needed");
			}
		}
	}
	
	/**
	 * Remove a package from the map of packages
	 * @param pkg - package to remove
	 */
	public void removePackage(Package pkg)
	{
		this.packages.remove(pkg.getName());
	}
	
	/**
	 * Set the map of packages 
	 * @param packages - map of packages
	 */
	public void setPackages(HashMap<String, Package> packages)
	{
		this.packages = packages;
	}
}
