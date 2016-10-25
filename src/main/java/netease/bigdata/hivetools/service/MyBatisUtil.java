package netease.bigdata.hivetools.service;

import java.io.*;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil {
  private static SqlSessionFactory soucrcFactory = null;
  private static SqlSessionFactory destFactory = null;

  public enum SOURCE_OR_DEST {
    SOURCE_DB, DEST_DB
  }

  private MyBatisUtil() {
  }

  /*
  static {
    Reader reader = null;
    try {
      reader = Resources.getResourceAsReader("mybatis-config.xml");
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }
    Properties props = new Properties();
    File file = new File("hive-tools.properties");
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      props.load(fis);
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


    props.setProperty("jdbc.driverClassName", "com.mysql.jdbc.Driver");
    props.setProperty("jdbc.url", "jdbc:mysql://10.120.232.16:3306/hive_music?useUnicode=true&characterEncoding=UTF-8");
    props.setProperty("jdbc.username", "hive_music");
    props.setProperty("jdbc.password", "hive_music");
    soucrcFactory = new SqlSessionFactoryBuilder().build(reader, props);
  }
  */

  public static void initSqlSessionFactory(String sourceName, SOURCE_OR_DEST source_or_dest) {
    Reader reader = null;
    try {
      reader = Resources.getResourceAsReader("mybatis-config.xml");
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    /* TEST
    File directory = new File("");//设定为当前文件夹
    try{
      System.out.println(directory.getCanonicalPath());//获取标准的路径
      System.out.println(directory.getAbsolutePath());//获取绝对路径
    }catch(Exception e){}*/

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

    if (source_or_dest == SOURCE_OR_DEST.SOURCE_DB) {
      soucrcFactory = new SqlSessionFactoryBuilder().build(reader, props);
    } else {
      destFactory = new SqlSessionFactoryBuilder().build(reader, props);
    }
  }

  public static SqlSessionFactory getSqlSessionFactory(SOURCE_OR_DEST source_or_dest) {
    return (source_or_dest == SOURCE_OR_DEST.SOURCE_DB)?soucrcFactory:destFactory;
  }
}