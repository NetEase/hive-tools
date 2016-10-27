package com.netease.hivetools.mappers;

import com.netease.hivetools.SchemaToMetaBean;
import com.netease.hivetools.service.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;


import java.util.*;


public class MetaDataMapper {
  private static final Logger logger = Logger.getLogger(MetaDataMapper.class.getName());
  private String sourceName;

  public MetaDataMapper(String sourceName) {
    this.sourceName = sourceName;
  }

  // table => Table
  private String formatTableName(String tabName) {
    String firstChar = tabName.substring(0, 1);
    String tmp = tabName.substring(1, tabName.length()).toLowerCase();
    tabName = firstChar.toUpperCase() + tmp;

    return tabName;
  }

  public List<Object> getTableRecords(String tabName) {
    List<Object> list = null;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      tabName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.get" + tabName + "Records";
      list = sqlSession.selectList(statement);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } finally {
      sqlSession.close();
    }
    return list;
  }

  public int getTableMaxId(String tabName) {
    int maxId = 0;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      tabName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.get" + tabName+ "MaxId";
      maxId = sqlSession.selectOne(statement);
      logger.info("getTableMaxId" + tabName + " maxId = " + maxId);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } finally {
      sqlSession.close();
    }
    return maxId;
  }

  public int getTableMinId(String tabName) {
    int minId = 0;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      tabName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.get" +tabName+ "MinId";
      minId = sqlSession.selectOne(statement);
      logger.info("getTableMinId" + tabName + " minId = " + minId);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } finally {
      sqlSession.close();
    }
    return minId;
  }

  public int batchInsert(String tabName, List<Object> list, HashMap<String, Object> mapPlusId) {
    int numInsert = 0;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String tableName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      logger.info("batchInsert" + tabName + " plusId = " + mapPlusId.get(tabName));

      if (tabName.equals("COMPACTION_QUEUE")) {
        int nnn = 0;
      }

      List<List<Object>> splitList = MetaDataMapper.getSplitList(list, 100);
      int index = 0;
      for (List<Object> subList : splitList) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("mapPlusId", mapPlusId);
        params.put("list",  subList);

        String statement = "com.netease.hivetools.mappers.MetaDataMapper.batchInsert" + tableName;
        numInsert = sqlSession.insert(statement, params);
        int progress = ((index++ + 1)*100 / splitList.size());
        logger.info("batch insert table " + tabName + " record progress [" + progress +"%]");
      }
      logger.info("batch insert table " + tabName + " record [" + list.size()+"]");
      sqlSession.commit();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      sqlSession.rollback();
      numInsert = -1;
    } finally {
      sqlSession.close();
    }

    return numInsert;
  }

  public List<Object> checkUniqueKey(String tabName, List<Object> list) {
    List<Object> listUniqueKey = new ArrayList<Object>();
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String tableName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      logger.info("checkUniqueKey" + tabName);

      List<List<Object>> splitList = MetaDataMapper.getSplitList(list, 100);
      int index = 0;
      for (List<Object> subList : splitList) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("list",  subList);

        String statement = "com.netease.hivetools.mappers.MetaDataMapper.checkUniqueKey" + tableName;
        List<Object> listTmp = sqlSession.selectList(statement, params);
        if (listTmp != null) {
          listUniqueKey.addAll(listTmp);
        }
        int progress = ((index++ + 1)*100 / splitList.size());
        logger.info("check Unique Key table " + tabName + " progress [" + progress +"%]");
      }
      logger.info("batch Unique Key table " + tabName + " record [" + list.size()+"]");
      sqlSession.commit();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      sqlSession.rollback();
    } finally {
      sqlSession.close();
    }

    return listUniqueKey;
  }

  public static <T> List<List<T>> getSplitList(List<T> list , int size)
  {
    List<List<T>> returnList = new ArrayList<List<T>>();
    int listSize = list.size();
    int num = listSize%size==0?listSize/size:(listSize/size+1);
    int start = 0;
    int end = 0;
    for(int i = 1; i <= num; i ++) {
      start = (i-1)*size;
      end = i*size>listSize?listSize:i*size;
      returnList.add(list.subList(start, end));
    }
    return returnList;
  }
}
