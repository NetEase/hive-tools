package com.netease.hivetools.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class MyBatisUtil {

  private static final Logger logger = Logger.getLogger(MyBatisUtil.class.getName());

  private static SqlSessionFactory soucrcFactory = null;
  private static SqlSessionFactory destFactory = null;
  private static SqlSessionFactory onlineFactory = null;
  public static String sourceName;
  public static String destName;
  public static String onlneName;

  private static void initSqlSessionFactory(String sourceName) {
    Reader reader = null;
    try {
      reader = Resources.getResourceAsReader("mybatis-config.xml");
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    Properties allProps = new Properties();
    File file = new File("hive-tools.properties");
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      allProps.load(fis);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (IOException e) {
        }
      }
    }

    Properties props = new Properties();
    props.setProperty("jdbc.driverClassName", allProps.getProperty(sourceName+".jdbc.driverClassName"));
    props.setProperty("jdbc.url", allProps.getProperty(sourceName+".jdbc.url"));
    props.setProperty("jdbc.username", allProps.getProperty(sourceName+".jdbc.username"));
    props.setProperty("jdbc.password", allProps.getProperty(sourceName+".jdbc.password"));

    if (sourceName.equals(MyBatisUtil.sourceName)) {
      soucrcFactory = new SqlSessionFactoryBuilder().build(reader, props);
    } else if (sourceName.equals(MyBatisUtil.destName)) {
      destFactory = new SqlSessionFactoryBuilder().build(reader, props);
    } else if (sourceName.equals(MyBatisUtil.onlneName)) {
      onlineFactory = new SqlSessionFactoryBuilder().build(reader, props);
    } else {
      logger.error("not found source : " + sourceName);
    }
  }

  public static SqlSessionFactory getSqlSessionFactory(String sourceName) {
    if (sourceName.equals(MyBatisUtil.sourceName)) {
      if (null == soucrcFactory)
        initSqlSessionFactory(sourceName);

      return soucrcFactory;
    } else if (sourceName.equals(MyBatisUtil.destName)) {
      if (null == destFactory)
        initSqlSessionFactory(sourceName);

      return destFactory;
    } else if (sourceName.equals(MyBatisUtil.onlneName)) {
      if (null == onlineFactory)
        initSqlSessionFactory(sourceName);

      return onlineFactory;
    } else {
      logger.error("not found source : " + sourceName);
      return null;
    }
  }
}