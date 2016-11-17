package com.netease.hivetools.mappers;

import com.netease.hivetools.mammut.PfHiveSite;
import com.netease.hivetools.service.MyBatisUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


public class MammutMapper {
  private static final Logger logger = Logger.getLogger(MammutMapper.class.getName());
  private String sourceName;

  public MammutMapper(String sourceName) {
    this.sourceName = sourceName;
  }

  public List<PfHiveSite> getPfHivesite() {
    logger.info("getPfHivesite >>>>>> ");
    List<PfHiveSite> list = null;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String statement = "com.netease.hivetools.mappers.MammutMapper.getPfHivesite";
      list = sqlSession.selectList(statement);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } finally {
      sqlSession.close();
    }


    for (PfHiveSite pfHiveSite : list) {
      Configuration conf = new Configuration(true);
      InputStream inputStream = new ByteArrayInputStream(pfHiveSite.xml.getBytes());
      conf.addResource(inputStream);

      pfHiveSite.connectionURL = conf.get("javax.jdo.option.ConnectionURL");
      pfHiveSite.connectionUserName = conf.get("javax.jdo.option.ConnectionUserName");
      pfHiveSite.connectionPassword = conf.get("javax.jdo.option.ConnectionPassword");

      logger.info(pfHiveSite.toString());
    }
    logger.info("getPfHivesite <<<<<<<< ");
    return list;
  }
}
