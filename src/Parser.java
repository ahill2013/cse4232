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

public class Parser {
    public enum CODES {START, EXIT, RETRIEVE, RECORD}

    private Options opts;
    private CommandLineParser parser;

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

    public CommandLine getCMD(String[] args) throws ParseException {
        return parser.parse(opts, args, true);
    }

    public static CommandLineParser getDefaultParser() {
        return new DefaultParser();
    }

}
