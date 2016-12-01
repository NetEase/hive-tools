package com.netease.hivetools;


import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.netease.hivetools.apps.Mammut;
import com.netease.hivetools.apps.MetaDataMerge;
import com.netease.hivetools.apps.SchemaToMetaBean;
import com.netease.hivetools.mappers.MetaDataMapper;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

public class Main {
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    PropertyConfigurator.configure("log4j.properties");

//    test(args);
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

  public static void test(String[] args)
  {
    System.out.println("test string=" + "walmart öbama 👽💔");
    String url = "jdbc:mysql://10.120.232.16:3306/haitao1201?useUnicode=true&characterEncoding=UTF-8";
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      Connection c = DriverManager.getConnection(url, "haitao1201", "haitao1201");
      PreparedStatement p = c.prepareStatement("select * from PARTITION_PARAMS where PART_ID>= 130000 and PART_ID < 140000;");
      p.execute();
      ResultSet rs = p.getResultSet();
      while (!rs.isLast())
      {
        rs.next();
        String retrieved = rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3);
        logger.info("retrieved=\"" + retrieved + "\"");

      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}
