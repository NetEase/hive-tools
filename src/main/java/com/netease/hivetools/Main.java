package com.netease.hivetools;


import com.netease.hivetools.apps.DelMetaData;
import com.netease.hivetools.apps.Mammut;
import com.netease.hivetools.apps.MetaDataMerge;
import com.netease.hivetools.apps.SchemaToMetaBean;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Main {
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    PropertyConfigurator.configure("log4j.properties");

//    test(args);
//    cliCommond(args);
  }

  static private void cliCommond(String[] args) {
    Options opt = new Options();
    opt.addOption(OptionBuilder.withLongOpt("p")
        .withDescription("处理函数名称")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption("h", "help",  false, "打印命令行帮助");

    String formatstr = "hive-tools --p=[MetaDataMerge|SchemaToMetaBean|Mammut|DelMetaData] [-h/--help]";

    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new PosixParser();
    CommandLine cl = null;
    try {
      for (int i = 0; i < args.length; i ++)
        logger.debug("args[" +i +"] : " + args[i]);

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
    }  else if (procName.equalsIgnoreCase("DelMetaData")) {
      DelMetaData.main(args);
    } else {
      System.out.println("error --p arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }
  }

  static class TabInfo{
    public String tblId = "";
    public String tblName = "";
    public String tblType = "";
    public String tblLocation = "";
    public String partName = "";
    public String partLocation = "";

    public TabInfo() {}

    @Override
    public String toString() {
      return "tblId = " + tblId + ", tblName = " + tblName +", tblType = " + tblType +", tblLocation = " + tblLocation +", partName = " + partName +", partLocation = " + partLocation;
    }
  }

  @Test
  public static void test(String[] args)
  {
    String url = "jdbc:mysql://10.120.232.16:3306/haitao1201?useUnicode=true&characterEncoding=UTF-8";
    try
    {
      Class.forName("com.mysql.jdbc.Driver").newInstance();
      Connection c = DriverManager.getConnection(url, "haitao1201", "haitao1201");
      PreparedStatement p = c.prepareStatement("select TBLS.TBL_ID, TBLS.TBL_NAME, TBLS.TBL_TYPE, SDS.LOCATION from TBLS, SDS where TBLS.SD_ID = SDS.SD_ID;"); //  limit 300
      p.execute();
      ResultSet rs = p.getResultSet();
      ArrayList<TabInfo> tabInfos = new ArrayList<>();
      while (!rs.isLast())
      {
        if (!rs.next())
          break;

        TabInfo tabInfo = new TabInfo();
        tabInfo.tblId = rs.getString(1);
        tabInfo.tblName = rs.getString(2);
        tabInfo.tblType = rs.getString(3);
        tabInfo.tblLocation = rs.getString(4)==null?"":rs.getString(4);

        tabInfos.add(tabInfo);
      }
      rs.close();

      for (TabInfo tabInfo : tabInfos) {
        String sql = "select SDS.LOCATION, PARTITIONS.PART_NAME from SDS, PARTITIONS where PARTITIONS.SD_ID = SDS.SD_ID and TBL_ID = " + tabInfo.tblId + " limit 1";
        p = c.prepareStatement(sql);
        p.execute();
        rs = p.getResultSet();
        while (!rs.isLast())
        {
          if (!rs.next())
            break;
          tabInfo.partLocation = rs.getString(1)==null?"":rs.getString(1);
          tabInfo.partName = rs.getString(2)==null?"":rs.getString(2);
        }
        rs.close();
      }

      int count = 0, notsame = 0;
      for (TabInfo tabInfo : tabInfos) {
        count ++;
        boolean samePath = tabInfo.partLocation.startsWith(tabInfo.tblLocation);
        if (samePath) {
//          System.out.println("Y " + tabInfo.toString());
        } else if(!samePath && !tabInfo.partLocation.isEmpty()) {
          notsame ++;
          System.out.println("N " + tabInfo.toString());
        }
      }
      System.out.println("总数: " + count + ", 不相同的: " + notsame);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

}
