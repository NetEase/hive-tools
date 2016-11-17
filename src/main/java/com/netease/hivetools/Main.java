package com.netease.hivetools;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.netease.hivetools.apps.Mammut;
import com.netease.hivetools.apps.MetaDataMerge;
import com.netease.hivetools.apps.SchemaToMetaBean;
import org.apache.commons.cli.*;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;

public class Main {
  public static void main(String[] args) {
    System.exit(1);
    PropertyConfigurator.configure("log4j.properties");
    cliCommond(args);
  }

  static private void cliCommond(String[] args) {
    Options opt = new Options();
    opt.addOption(OptionBuilder.withLongOpt("p")
        .withDescription("处理函数名称")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("s")
        .withDescription("迁出的元数据库")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("d")
        .withDescription("迁入的元数据库")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption("h", "help",  false, "打印命令行帮助");

    String formatstr = "hive-tools --p=[MetaDataMerge|SchemaToMetaBean|Mammut] --s=<source-name> --d=<dest-name> [-h/--help]";

    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new PosixParser();
    CommandLine cl = null;
    try {
      cl = parser.parse(opt, args);
    } catch (ParseException e) {
      formatter.printHelp(formatstr, opt);
    }
    if (cl.hasOption("h")) {
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }

    if (false == cl.hasOption("p")) {
      System.out.println("missing --t arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }

    String procName = cl.getOptionValue("p");
    if (procName.equalsIgnoreCase("MetaDataMerge")) {
      MetaDataMerge.main(args);
    } else if (procName.equalsIgnoreCase("SchemaToMetaBean")) {
      SchemaToMetaBean.main(args);
    } else if (procName.equalsIgnoreCase("Mammut")) {
      Mammut.main(args);
    } else {
      System.out.println("error --p arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }
  }

}
