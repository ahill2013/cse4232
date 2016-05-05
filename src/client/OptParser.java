/* ------------------------------------------------------------------------- */
/*   Copyright (C) 2016
                Author:  wnyffenegger2013@my.fit.edu
                Author:  ahill2013@my.fit.edu
                Florida Tech, Computer Science

       This program is free software; you can redistribute it and/or modify
       it under the terms of the GNU Affero General Public License as published by
       the Free Software Foundation; either the current version of the License, or
       (at your option) any later version.

      This program is distributed in the hope that it will be useful,
      but WITHOUT ANY WARRANTY; without even the implied warranty of
      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
      GNU General Public License for more details.

      You should have received a copy of the GNU Affero General Public License
      along with this program; if not, write to the Free Software
      Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.              */
/* ------------------------------------------------------------------------- */

package client;

import org.apache.commons.cli.*;

/**
 * Interprets options and command lines passed to it from the Handler or potentially from other
 * sources in the future.
 */
public class OptParser {

    private static Options opts;
    private static CommandLineParser parser;

    /**
     * Takes in the list of options and tries to build a command line.
     */
    public OptParser() {
        opts = new Options();
        opts.addOption(Option.builder("h").required(false).hasArg(false).longOpt("help").optionalArg(false).desc("displays this help menu").build());
        opts.addOption(Option.builder("t").required(false).hasArg(false).optionalArg(false).desc("use TCP (default)").build());
        opts.addOption(Option.builder("u").required(false).hasArg(false).optionalArg(false).desc("use UDP").build());
        opts.addOption(Option.builder("d").required(false).hasArg(true).longOpt("domain").argName("domain/host name").desc("specify the domain/host name").build());
        opts.addOption(Option.builder("r").required(false).hasArg(true).optionalArg(false).desc("register for an exam on startup").build());
        parser = OptParser.getDefaultParser();

    }

    public Options getOptions() {
        return opts;
    }

    public CommandLine getCMD(Options opts, String[] args) throws ParseException {
        return parser.parse(opts, args, true);
    }

    /**
     * Given an array of strings representing the tokens entered on the command line, return the
     * already created command line parser’s interpretation of those commands with the options
     * initialized at run time.
     *
     * @param args string array of tokens from command line
     * @return parser’s interpretation of the given commands
     * @throws ParseException
     */
    public CommandLine getCMD(final String[] args) throws ParseException {
        return parser.parse(opts, args, true);
    }

    /**
     * Returns a parser for use by whoever desires
     *
     * @return parser for interpreting command lines represented as string arrays of tokens
     */
    public static CommandLineParser getDefaultParser() {
        return new DefaultParser();
    }

    /**
     * Prints a help menu on the command line if the program is passed -h or --help
     */
    public static final void printHelpMenu() {
        String header = "Options:\n";
        String footer = "\nPlease report issues to ahill2013@my.fit.edu or wnyffenegger2013@my.fit.edu\n";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -cp bin/externals/*:bin client.Client <IP_address> <port>", header, opts, footer, true);
    }

}
