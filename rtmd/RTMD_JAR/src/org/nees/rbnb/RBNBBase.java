/* RBNBBase.java
 * ************************************************************************
 * Created on May 7, 2004
 *
 * A base class for all NEES RBNB widgets
 *
 */
package org.nees.rbnb;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;

/**
 * A base for RBNB Widgets. Includes base parameters and constents. For actual usage
 * this class must be extended. Specifically: the arguments must be precessed by
 * calling the methods of the supper class; usage, and other methods
 * need to be overwriten or extended.
 * 
 * @author Terry E Weymouth
 * @version $Revision$
 * 
 */
public abstract class RBNBBase
{

	private static final String SERVER_NAME = "localhost";
	private static final String SERVER_PORT = "3333";
	private String serverName = SERVER_NAME;
	private String serverPort = SERVER_PORT;
	private String server = serverName + ":" + serverPort;
   
	private String optionNotes = null;
	
	protected boolean parseArgs(String[] args) throws IllegalArgumentException
	{
		try {
			CommandLine cmd = (new PosixParser()).parse(setOptions(), args);
			return setArgs(cmd);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException("Argument Exception: " + e);
		}
	}
	
	
	/**
	 * Process the parsed command line; will usuall call setBaseArgs
	 * 
	 * @param cmd (org.apache.commons.cli.CommandLine) -- the parsed command line
	 * @return true if the command line processed sucessfully
	 * 
	 * @see #setBaseArgs
	 */
	protected abstract boolean setArgs(CommandLine cmd);
	
	protected boolean setBaseArgs(CommandLine cmd)
	{	
		if (cmd.hasOption('h'))
		{
			printUsage();
			return false;
		}
        if (cmd.hasOption("v"))
        {
            System.err.println(getCVSVersionString());
            return false;
        }
		if (cmd.hasOption('s')) {
			String a=cmd.getOptionValue('s');
			if (a!=null) setServerName(a);
		}
		if (cmd.hasOption('p')) {
			String a=cmd.getOptionValue('p');
			if (a!=null) setServerPort(a);
		}
		return true;
	}
	
	/**
	 * @param name
	 */
	public void setServerName(String name) {
		serverName = name;
	}

	/**
	 * @param port
	 */
	public void setServerPort(String port) {
		serverPort = port;
	}

	public String getServer()
	{
		server = serverName + ":" + serverPort;
		return server;
	}

	protected void printUsage() {
		HelpFormatter f = new HelpFormatter();
		f.printHelp(this.getClass().getName(),setOptions());
		if (optionNotes != null)
		{
			System.out.println("Note: " + optionNotes);
		}
	}

	/**
	 * Set the Options object for command line parsing; will usually call setBaseOptions
	 * 
	 * @return org.apache.commons.cli.Options
	 * @see #setBaseOptions
	 */
	protected abstract Options setOptions();
	
	protected Options setBaseOptions(Options opt)
	{
		opt.addOption("h",false,"Print help");
		opt.addOption("s",true,"Server Hostname *" + SERVER_NAME);
		opt.addOption("p",true,"Server Port Number *" + SERVER_PORT);
        opt.addOption("v",false,"Print Version information");
		return opt;
	}
	
	protected void setNotes(String n)
	{
		optionNotes = n;
	}

    protected abstract String getCVSVersionString();

}


