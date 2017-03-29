package com.netease.hivetools.mappers;

import com.netease.hivetools.apps.SchemaToMetaBean;
import com.netease.hivetools.meta.Dbs;
import com.netease.hivetools.meta.SerdeParams;
import com.netease.hivetools.meta.Tbls;
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

  public List<Object> getDbsRecords(String tabName) {
    List<Object> list = null;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.getTabsRecords";
      list = sqlSession.selectList(statement);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } finally {
      sqlSession.close();
    }
    return list;
  }

  public List<Object> getTableRecords(String tabName, Map<String, Object> params) {
    List<Object> list = null;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      tabName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.get" + tabName + "Records";

      if (null == params) {
        // init
        params = new HashMap<String,Object>();
        params.put("database_name", "%");
        params.put("db_id", "%");
      }

      list = sqlSession.selectList(statement, params);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
    } finally {
      sqlSession.close();
    }
    return list;
  }

  public List<Object> getPagingTableRecords(String tabName, HashMap<String, Object> mapPagindId) {
    List<Object> list = null;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      tabName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.getPaging" + tabName + "Records";
      Map<String, Object> params = new HashMap<String,Object>();
      params.put("mapPagindId", mapPagindId);
      list = sqlSession.selectList(statement, params);
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

  public boolean deleteTable(Tbls tbls) {
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.deleteTbls";
      int delCount = sqlSession.delete(statement, tbls);
      sqlSession.commit();
      logger.info("--- deleteTable[" + tbls.getTblName() + "]--- delete count = " + delCount);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      return false;
    } finally {
      sqlSession.close();
    }
    return true;
  }

  public boolean deleteDatabase(Dbs dbs) {
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String statement = "com.netease.hivetools.mappers.MetaDataMapper.deleteDbs";
      int delCount = sqlSession.delete(statement, dbs);
      sqlSession.commit();
      logger.info("=== deleteDatabase[" + dbs.getName() + "]=== delete count = " + delCount);
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      return false;
    } finally {
      sqlSession.close();
    }
    return true;
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
    List<Object> tmpSubList = null;
    try {
      String tableName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      logger.info("批量插入表 " + tabName + " plusId = " + mapPlusId.get(tabName));

      if (tabName.equals("COMPACTION_QUEUE")) {
        int nnn = 0;
      }

      List<List<Object>> splitList = MetaDataMapper.getSplitList(list, 100);
      int index = 0;
      for (List<Object> subList : splitList) {
        Map<String, Object> params = new HashMap<String,Object>();
        params.put("mapPlusId", mapPlusId);
        params.put("list",  subList);

        tmpSubList = subList;

        String statement = "com.netease.hivetools.mappers.MetaDataMapper.batchInsert" + tableName;
        numInsert += sqlSession.insert(statement, params);
        int progress = ((index++ + 1)*100 / splitList.size());
        logger.info("批量插入表 " + tabName + " 处理进度 [" + progress +"%]");
        sqlSession.commit();
      }
      logger.info("批量插入表 " + tabName + " 记录总数 [" + list.size()+"]");
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());

      for (Object object : tmpSubList) {
        if (object.getClass() == SerdeParams.class) {
          SerdeParams serdeParams = (SerdeParams)object;
          logger.error("SerdeId=" + serdeParams.getSerdeId() + ", ParamKey=" + serdeParams.getParamKey() + ", ParamValue=" + serdeParams.getParamValue());
        }
      }

      sqlSession.rollback();
      numInsert = -1;
    } finally {
      sqlSession.close();
    }

    return numInsert;
  }

  public int rollback(String tabName, HashMap<String, Object> mapPlusId) {
    int numRollback = 0;
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String tableName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      logger.info("回滚元数据表 " + tabName + " plusId = " + mapPlusId.get(tabName));

      String statement = "com.netease.hivetools.mappers.MetaDataMapper.rollback" + tableName;
      Map<String, Object> params = new HashMap<String,Object>();
      params.put("mapPlusId", mapPlusId);
      numRollback = sqlSession.delete(statement, params);
      logger.info("回滚元数据表 " + tabName + " 记录总数 [" + numRollback+"]");
      sqlSession.commit();
    } catch (Exception e) {
      e.printStackTrace();
      logger.error(e.getMessage());
      sqlSession.rollback();
      numRollback = -1;
    } finally {
      sqlSession.close();
    }

    return numRollback;
  }

  public List<Object> checkUniqueKey(String tabName, List<Object> list) {
    List<Object> listUniqueKey = new ArrayList<Object>();
    SqlSession sqlSession = MyBatisUtil.getSqlSessionFactory(this.sourceName).openSession();
    try {
      String tableName = SchemaToMetaBean.formatTableColumnName(tabName, true);
      logger.info("检查唯一键冲突 " + tabName);

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
        logger.info("检查唯一键, 表 " + tabName + " 进度 [" + progress +"%]");
      }
      logger.info("检查唯一键, 表 " + tabName + " 记录数 [" + list.size()+"]");
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
