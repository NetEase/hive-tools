package com.netease.hivetools.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.PrivilegedExceptionAction;
import java.util.Scanner;

import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Logger;

/**
 * Created by hzliuxun on 17/2/6.
 */
public class HdfsTest {
  private static final Logger logger = Logger.getLogger(HdfsTest.class.getName());

  public static String _proxyUser = "";
  public static String _filePath = "/";
  public static String _hadoop_cluster = "hadoop";
  public static String _command = "";
  public static Configuration _hadoop_conf = new Configuration();

  public static void main(String[] argv) throws Exception {
    cliCommond(argv);

    Configuration conf = new Configuration();
    HdfsTest hdfsClient = new HdfsTest();

    hdfsClient.proxyTest();
  }


  static private void cliCommond(String[] args) {
    Options opt = new Options();
    opt.addOption("h", "help",  false, "打印命令行帮助");
    opt.addOption(OptionBuilder.withLongOpt("p")
        .withDescription("处理函数名称")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("p")
        .withDescription("被代理用户名")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("c")
        .withDescription("命令")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("a")
        .withDescription("HDFS路径")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("b")
        .withDescription("线上集群:hadoop | DS测试集群:ds")
        .withValueSeparator('=')
        .hasArg()
        .create());

    String formatstr = "HdfsTest --p=<arg> --c=<arg> --a=<arg> --b=<arg> [-h/--help]";

    HelpFormatter formatter = new HelpFormatter();
    CommandLineParser parser = new PosixParser();
    CommandLine cl = null;
    try {
      // 处理Options和参数
      cl = parser.parse(opt, args);
    } catch (ParseException e) {
      formatter.printHelp(formatstr, opt); // 如果发生异常，则打印出帮助信息
    }
    // 如果包含有-h或--help，则打印出帮助信息
    if (cl.hasOption("h")) {
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }
    if( cl.hasOption("p") ) {
      _proxyUser = cl.getOptionValue("p");
    } else {
      System.out.println("missing --p arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }

    if( cl.hasOption("c") ) {
      _command = cl.getOptionValue("c");
    } else {
      System.out.println("missing --c arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }

    if( cl.hasOption("a") ) {
      _filePath = cl.getOptionValue("a");
    } else {
      System.out.println("missing --a arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }

    if( cl.hasOption("b") ) {
      _hadoop_cluster = cl.getOptionValue("b");
    } else {
      System.out.println("missing --b arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }

    logger.debug("被代理用户 : " + _proxyUser + ", hadoop集群 : " + _hadoop_cluster + ", 运行HDFS命令: " + _command + ", HDFS路径: " + _filePath);
  }

  private void kerberosTest(FileSystem fs, String path) throws IOException {
    FileStatus[] fsStatus = fs.listStatus(new Path("/"));
    for (int i = 0; i < fsStatus.length; i++) {
      System.out.println(fsStatus[i].getPath().toString());
    }
  }

  public void proxyTest() throws Exception {
    UserGroupInformation ugi = null;

    if (_hadoop_cluster.equalsIgnoreCase("hadoop")) {
      _hadoop_conf.addResource("hadoop357.lt.163.org/core-site.xml");
      _hadoop_conf.addResource("hadoop357.lt.163.org/hdfs-site.xml");
      UserGroupInformation.setConfiguration(_hadoop_conf);
      UserGroupInformation.loginUserFromKeytab("hadoop/admin@HADOOP.HZ.NETEASE.COM", "/home/hadoop/yarn/conf/hadoop.keytab");

      ugi = UserGroupInformation.createProxyUser(_proxyUser, UserGroupInformation.getLoginUser());
    } else {
      _hadoop_conf.addResource("classb-ds-bigdata11.server.163.org/core-site.xml");
      _hadoop_conf.addResource("classb-ds-bigdata11.server.163.org/hdfs-site.xml");
      UserGroupInformation.setConfiguration(_hadoop_conf);
      UserGroupInformation.loginUserFromKeytab("hive/classb-ds-bigdata4.server.163.org@IF.HZ.NETEASE.COM", "/home/hadoop/yarn/conf/hive.keytab");

      ugi = UserGroupInformation.createProxyUser(_proxyUser, UserGroupInformation.getLoginUser());
    }

    System.out.println("  >>> Login User is: " + UserGroupInformation.getLoginUser().toString());
    System.out.println("  >>> Proxy user is: " + ugi.getUserName());
    System.out.println("  >>> current User is: " + UserGroupInformation.getCurrentUser().toString());
    System.out.println("  >>> The credential is: " + ugi.getCredentials().toString());

    ugi.doAs(new PrivilegedExceptionAction() {
      public Void run() throws Exception {
        System.out.println(">>> doAs current User is: " + UserGroupInformation.getCurrentUser().toString());

//      _hadoop_conf.set("hadoop.security.authentication", "Kerberos");
//      _hadoop_conf.addResource("hadoop357.lt.163.org/core-site.xml");
//      _hadoop_conf.addResource("hadoop357.lt.163.org/hdfs-site.xml");
        FileSystem dfs = FileSystem.get(_hadoop_conf);
        if (HdfsTest._command.equals("read")) {
          HdfsTest.this.read(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("write")) {
          HdfsTest.this.write(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("append")) {
          HdfsTest.this.append(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("replicate")) {
          HdfsTest.this.setReplication(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("status")) {
          HdfsTest.this.getStatus(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("delete")) {
          HdfsTest.this.delete(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("ls")) {
          HdfsTest.this.ls(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("kerberos")) {
          HdfsTest.this.kerberosTest(dfs, HdfsTest._filePath);
        } else if (HdfsTest._command.equals("mkdir")) {
          HdfsTest.this.mkdir(dfs, HdfsTest._filePath);
        }
        return null;
      }
    });
  }

  private void read(FileSystem fs, String path) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.read()");
    InputStream in = null;
    try {
      if (fs.exists(new Path(path)) != true) {
        System.out.println("/t>>>" + path + " is not exists!"); return;
      }
      in = fs.open(new Path(path));
      IOUtils.copy(in, System.out);
    } finally {
      IOUtils.closeQuietly(in);
    }
    System.out.println("  <<<Entering HelloHDFS.read()");
  }

  private void mkdir(FileSystem dfs, String filePath) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.mkdir()");

    Path path = new Path(filePath);
    dfs.mkdirs(path);

    System.out.println("  <<<Entering HelloHDFS.mkdir()");
  }

  private void write(FileSystem dfs, String filePath) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.create()");

    Path path = new Path(filePath);
    FSDataOutputStream out = dfs.create(path);
    try {
      String words = "123456";
      out.writeBytes(words);
      out.write(words.getBytes("UTF-8"));

      out.close();
    } finally {
      out.close();
    }
    System.out.println("  <<<Entering HelloHDFS.create()");
  }

  private void append(FileSystem fs, String filePath) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.append()");
    OutputStreamWriter out = null;
    try {
      out = new OutputStreamWriter(fs.append(new Path(filePath)));
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      String line = br.readLine();
      while (line != null)  {
        line = br.readLine();
        if (line.equals("endfile")) {
          break;
        }
        IOUtils.write(line, out);
      }
    } finally {
      out.close();
    }
    System.out.println("  <<<Exiting HelloHDFS.append()");
  }

  private void getStatus(FileSystem fs, String filePath) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.getStatus()");
    FileStatus[] fileStatusList = fs.listStatus(new Path(filePath));
    for (FileStatus fileStatus : fileStatusList) {
      System.out.println(fileStatus);
    }
    System.out.println("  <<<Entering HelloHDFS.getStatus()");
  }

  private void delete(FileSystem fs, String filePath) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.delete()");
    Path path = new Path(filePath);
    if (fs.exists(path)) {
      fs.delete(path, true);
    }
    System.out.println("  <<<Exiting HelloHDFS.delete()");
  }

  private void setReplication(FileSystem dfs, String filePath) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.setReplicationFactor()");
    Scanner reader = new Scanner(System.in);

    System.out.println("Enter replication factor:");
    short replicationFactor = reader.nextShort();
    dfs.setReplication(new Path(filePath), replicationFactor);
    System.out.println("  <<<Entering HelloHDFS.setReplicationFactor()");
  }

  private void ls(FileSystem dfs, String filePattern) throws IOException {
    System.out.println("  >>>Entering HelloHDFS.search()");

    FileStatus[] fsStatus = dfs.listStatus(new Path(filePattern));
    for (int i = 0; i < fsStatus.length; i++) {
      System.out.println(fsStatus[i].getPath().toString());
    }
    System.out.println("  <<<Entering HelloHDFS.search()");
  }
}
