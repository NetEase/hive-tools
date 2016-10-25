package netease.bigdata.hivetools.service;


import netease.bigdata.hivetools.mappers.DbsMapper;
import netease.bigdata.hivetools.meta.Dbs;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class DbsService {

  public DbsService() {
    // try open session
    MyBatisUtil.initSqlSessionFactory("hive_music", MyBatisUtil.SOURCE_OR_DEST.SOURCE_DB);
    MyBatisUtil.initSqlSessionFactory("hive_edu", MyBatisUtil.SOURCE_OR_DEST.DEST_DB);
    List<Dbs> dbsList = getDbs(MyBatisUtil.SOURCE_OR_DEST.DEST_DB);
    int i = 0;
  }

  public List<Dbs> getDbs(MyBatisUtil.SOURCE_OR_DEST source_or_dest) {
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(source_or_dest).openSession();
    try {
      DbsMapper dbsMapper = sqlSession.getMapper(DbsMapper.class);
      return dbsMapper.getDbs();
    } finally {
      sqlSession.close();
    }
  }
}