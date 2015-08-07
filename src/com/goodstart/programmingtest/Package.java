package com.goodstart.programmingtest;

import java.util.ArrayList;

/**
 * Package represents an installable component.
 * Each package has a list of dependent packages (can be empty)
 * Explicit packages were installed by the user
 * Implicit packages were installed because they are required by an explicitly installed package.
 * @author Monty Kamath
 */
public class Package
{
	protected String name;
	protected boolean isExplicit = false;
	protected boolean isInstalled = false;
	protected ArrayList<Package> dependencies = new ArrayList<Package>();

	/**
	 * Constructor
	 * @param aName - name of package
	 */
	public Package(String aName)
	{
		this.name = aName;
	}

	/**
	 * Add a dependent package
	 * @param pkg - dependent package
	 */
	public void addDependency(Package pkg)
	{
		this.dependencies.add(pkg);
	}

	/**
	 * Inquire if I can be removed from the package manager
	 * @param pm - package manager
	 * @param explicitRemoval - is this an explicit removal from the user or an implied removal of some dependency
	 * @return - true if the package can be remove and false otherwise
	 */
	public boolean canBeRemoved(PackageManager pm, boolean explicitRemoval)
	{
		// If this is an implicit removal (meaning the user didn't request it specifically 
		//    but instead requested to remove some other package that depends on me)
		// And I was explicitly installed (meaning the user did request it)
		// Then I cannot be removed
		if(!explicitRemoval && this.isExplicit())
		{
			return false;
		}
		
		for(Package pkg : pm.getPackages().values())
		{
			if(pkg.isInstalled() && pkg.dependsOnPackage(this))
			{
				// I cannot be removed if any installed package depends on me
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Set isExplicit to false
	 */
	public void clearExplicit()
	{
		//set explicit to false
		this.isExplicit = false;
	}
	
	/**
	 * Set isInstalled to false
	 */
	public void clearInstalled()
	{
		this.isInstalled = false;
	}

	/**
	 * Inquire if I depend on some other package
	 * @param pkg - package to see if I depend upon
	 * @return - true if i depend on pkg; false otherwise
	 */
	public boolean dependsOnPackage(Package pkg)
	{
		for(Package d : this.getDependencies())
		{
			if(d == pkg || d.dependsOnPackage(pkg))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Accessor
	 * @return dependencies
	 */
	public ArrayList<Package> getDependencies()
	{
		return dependencies;
	}
	
	/**
	 * Accessor
	 * @return name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Install me and all of my dependencies (recursive)
	 * @param pm - package manger
	 */
	public void install(PackageManager pm)
	{
		if(this.isInstalled())
		{
			return; // do nothing already installed
		}

		pm.echoIndented("Installing " + this.getName());
		this.setInstalled();
		pm.addPackage(this);
		
		// install my dependencies (and their dependencies recursive)
		for(Package d : this.getDependencies())
		{
			d.install(pm);
		}
		
	}
	
	/**
	 * Accessor
	 * @return isExplicit
	 */
	public boolean isExplicit()
	{
		return isExplicit;
	}
	
	/**
	 * Accessor
	 * @return isInstalled
	 */
	public boolean isInstalled()
	{
		return isInstalled;
	}
	
	/**
	 * Remove me and all of my dependencies (recursive) as long
	 * as no other installed package depends on them
	 * @param pm - package manger
	 */
	public void remove(PackageManager pm)
	{
		pm.echoIndented("Removing "+ this.getName());
		// Don't actually remove me from the package manager
		// That way we will still know me and all of my dependencies in the future
		// pm.removePackage(this);
		
		// I'm being removed so mark me as not installed and not explicitly installed
		this.clearInstalled();
		this.clearExplicit();
		
		// try to remove my dependencies (and their dependencies recursive) if they can be removed
		for(Package d : this.getDependencies())
		{
			if(d.canBeRemoved(pm, false))
			{
				d.remove(pm);
			}
		}
	}
	
	/**
	 * Remove the package from my list of dependencies
	 * @param pkg - package to remove
	 */
	public void removeDependency(Package pkg)
	{
		this.dependencies.remove(pkg);
	}
	
	/**
	 * Accessor
	 * @param dependencies - dependencies to set
	 */
	public void setDependencies(ArrayList<Package> dependencies)
	{
		this.dependencies = dependencies;
	}

	/**
	 * Set isExplicit to true
	 */
	public void setExplicit()
	{
		this.isExplicit = true;
	}

	/**
	 * Set isInstalled to true
	 */
	public void setInstalled()
	{
		this.isInstalled = true;
	}
	
	/**
	 * Accessor
	 * @param name - name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

}
