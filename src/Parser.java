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

import org.apache.commons.cli.*;

import java.util.LinkedList;

/**
 * Interprets options and command lines passed to it from the Handler or potentially from other
 * sources in the future.
 */
public class Parser {
    public enum CODES {START, EXIT, RETRIEVE, RECORD}

    private Options opts;
    private CommandLineParser parser;

    /**
     * Takes in the list of options and tries to build a command line.
     *
     * @param options — list of string arrays where each array is an option
     * @throws IllegalArgumentException if improper number of arguments were given
     */
    public Parser(LinkedList<String[]> options) throws IllegalArgumentException {
        opts = new Options();
        for (String[] opt: options) {
            switch (opt.length) {
                case 1:
                    throw new IllegalArgumentException("Usage: only one argument");
                case 2:
                    opts.addOption(opt[0], opt[1]);
                    break;
                case 3:
                    opts.addOption(opt[0], Boolean.parseBoolean(opt[1]), opt[2]);
                    break;
                case 4:
                    opts.addOption(opt[0], opt[1], Boolean.parseBoolean(opt[2]), opt[3]);
                    break;
                default:
                    throw new IllegalArgumentException("Usage: no arguments");
            }

        }

        parser = Parser.getDefaultParser();

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

}
