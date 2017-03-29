package com.netease.hivetools.apps;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.security.PrivilegedExceptionAction;
import java.sql.*;

/**
 * Created by hzliuxun on 17/2/21.
 */
public class HiveTest {
  private static String authName = "hadoop.security.authentication";
  private static String authValue = "Kerberos";
  private static String krbUser = "hadoop/admin@HADOOP.HZ.NETEASE.COM";
  private static String krbPath = "/Users/apple/Downloads/hadoop.keytab";
  private static String defDbName = "liuxun1";
  private static String proxyUser = "hadoop";
  private static String rangerUser = "liuxun";

  private static String driverName = "org.apache.hive.jdbc.HiveDriver";

  public static void main(String[] args) throws Exception {
    hiveOperation();
  }

  private static void test() throws Exception {
    try {
      Class.forName(driverName);
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    }
    System.setProperty("java.security.auth.login.config", "gss-jaas.conf");
//    System.setProperty("sun.security.jgss.debug","true");
    System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
    System.setProperty("java.security.krb5.conf", "krb5.conf");

    Connection conn = DriverManager.getConnection(
        "jdbc:hive2://10.120.232.27:10000/" + defDbName +
            ";principal=hive/app-20.photo.163.org@HADOOP.HZ.NETEASE.COM;hive.server2.proxy.user=" + proxyUser +
            "#ranger.user.name=" + rangerUser);
    Statement statement = conn.createStatement();
    String sql = "show tables;";
    System.out.println("Running: " + sql);
    ResultSet res = statement.executeQuery(sql);
    while (res.next()) {
      System.out.println(String.valueOf(res.getInt(1)) + "\t"
          + res.getString(2));
    }
  }

  private static void hiveOperation() throws Exception {
    System.setProperty("java.security.krb5.conf", "/etc/krb5.conf");

    Configuration conf = new Configuration();
    conf.set(authName, authValue);
    UserGroupInformation.setConfiguration(conf);
    UserGroupInformation.loginUserFromKeytab("hadoop/admin@HADOOP.HZ.NETEASE.COM", "/Users/apple/Downloads/hadoop.keytab");
//    UserGroupInformation.loginUserFromKeytab("hadoop/admin@HADOOP.HZ.NETEASE.COM", "/home/hadoop/yarn/conf/hadoop.keytab");
//    UserGroupInformation.loginUserFromKeytab("hive/app-20.photo.163.org", "/home/hadoop/yarn/conf/hive.keytab");

    /*
    UserGroupInformation ugi = UserGroupInformation.createProxyUser(proxyUser, UserGroupInformation.getLoginUser());
    System.out.println("Current kerberos user : " + ugi);
    ugi.doAs(new PrivilegedExceptionAction<Void>() {

      public Void run() throws Exception {
      */
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection conn1 = DriverManager.getConnection(
            "jdbc:hive2://hadoop354.lt.163.org:10000/default;principal=hive/app-20.photo.163.org@HADOOP.HZ.NETEASE.COM");

        Connection conn = DriverManager.getConnection(
            "jdbc:hive2://hadoop354.lt.163.org:10000/" + defDbName +
                ";principal=hive/app-20.photo.163.org@HADOOP.HZ.NETEASE.COM;hive.server2.proxy.user=" + "hive" +
                "#ranger.user.name=" + rangerUser);
        Statement statement = conn.createStatement();
        String sql = "show tables";
        System.out.println("Running: " + sql);
        ResultSet res = statement.executeQuery(sql);
        System.out.println("Runned");
        while (res.next()) {
          System.out.println(String.valueOf(res.getMetaData()));
        }
    /*
        return null;
      }
    });
    */
  }
}
