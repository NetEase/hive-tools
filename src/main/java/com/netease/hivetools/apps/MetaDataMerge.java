package com.netease.hivetools.apps;

import com.netease.hivetools.mappers.MetaDataMapper;
import com.netease.hivetools.meta.Dbs;
import com.netease.hivetools.meta.Roles;
import com.netease.hivetools.meta.Types;
import com.netease.hivetools.meta.Version;
import com.netease.hivetools.service.MyBatisUtil;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

import java.util.*;

/**
 * Created by hzliuxun on 16/10/26.
 */
public class MetaDataMerge {
	private static final Logger logger = Logger.getLogger(MetaDataMerge.class.getName());

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");

		cliCommond(args);

//		MyBatisUtil.sourceName = "hive_haitao";
//		MyBatisUtil.destName = "hive_merge";

		logger.info("========================================================");
		logger.info("将元数据 " + MyBatisUtil.sourceName + " 合并到 " + MyBatisUtil.destName);
		logger.info("========================================================");

		MetaDataMapper sourceMetaData = new MetaDataMapper(MyBatisUtil.sourceName);
		MetaDataMapper destMetaData = new MetaDataMapper(MyBatisUtil.destName);

		List<String> tables = new ArrayList<String>();
		HashMap<String, Object> mapPlusId = new HashMap<String, Object>();
		HashMap<String, Object> pagingProc = new HashMap<String, Object>();

		// no constraint
		tables.add("DBS");
		tables.add("CDS");
		tables.add("COMPACTION_QUEUE");
		tables.add("COMPLETED_TXN_COMPONENTS");
		tables.add("MASTER_KEYS");
		tables.add("HIVE_LOCKS");
		tables.add("NEXT_COMPACTION_QUEUE_ID");
		tables.add("NEXT_LOCK_ID");
		tables.add("NEXT_TXN_ID");
		tables.add("NOTIFICATION_LOG");
		tables.add("NOTIFICATION_SEQUENCE");
		tables.add("NUCLEUS_TABLES");
		tables.add("PARTITION_EVENTS");
		// have constraint
		tables.add("ROLES");
		tables.add("ROLE_MAP");
		tables.add("SERDES");
		tables.add("SERDE_PARAMS");
		tables.add("SDS");
		tables.add("SD_PARAMS");
		tables.add("TBLS");
		tables.add("TBL_COL_PRIVS");
		tables.add("TAB_COL_STATS");
		tables.add("TBL_PRIVS");
		tables.add("TABLE_PARAMS");
		tables.add("PARTITIONS");
		tables.add("PARTITION_KEYS");
		tables.add("PARTITION_KEY_VALS");
		tables.add("PARTITION_PARAMS");
		tables.add("PART_COL_PRIVS");
		tables.add("PART_COL_STATS");
		tables.add("PART_PRIVS");
		tables.add("SKEWED_STRING_LIST");
		tables.add("SKEWED_COL_NAMES");
		tables.add("SKEWED_COL_VALUE_LOC_MAP");
		tables.add("SKEWED_STRING_LIST_VALUES");
		tables.add("SKEWED_VALUES");
		tables.add("SORT_COLS");
		tables.add("TXNS");
		tables.add("TXN_COMPONENTS");
		tables.add("TYPES");
		tables.add("TYPE_FIELDS");
		tables.add("BUCKETING_COLS");
		tables.add("FUNCS");
		tables.add("FUNC_RU");
		tables.add("IDXS");
		tables.add("INDEX_PARAMS");
		tables.add("COLUMNS_V2");
		tables.add("DATABASE_PARAMS");
		tables.add("DB_PRIVS");

		/* not merge
		tables.add("GLOBAL_PRIVS");
		tables.add("DELEGATION_TOKENS");
		tables.add("SEQUENCE_TABLE");
		*/

		pagingProc.put("PARTITIONS", "");
		pagingProc.put("PARTITION_PARAMS", "");
		pagingProc.put("PARTITION_KEY_VALS", "");
		pagingProc.put("SDS", "");
		pagingProc.put("SERDES", "");
		pagingProc.put("SERDE_PARAMS", "");

		boolean conflict = checkConflict();
		checkHdfsCluster();

		Scanner sc = new Scanner(System.in);
		String useInput = "";
		while (!useInput.equals("Y")) {
			System.out.print("将元数据 " + MyBatisUtil.sourceName + " 合并到 " + MyBatisUtil.destName + " 操作请输入[Y/n] : ");

			useInput = sc.nextLine();
			if (useInput.equalsIgnoreCase("n")) {
				System.exit(1);
			}
		}

		boolean mergeSuccess = true;
		outer_loop:
		for (String tabName :tables) {
			logger.info("将 " + MyBatisUtil.sourceName + "." + tabName + " 合并到 " + MyBatisUtil.destName + "." + tabName);
			int destMaxDestId = destMetaData.getTableMaxId(tabName);
			int sourceMaxDestId = sourceMetaData.getTableMaxId(tabName);

			logger.info(">>> (开始分页处理) [数据源:" + MyBatisUtil.destName +"].[表:" + tabName + "] 中 maxId = " + sourceMaxDestId + ".");

			mapPlusId.put("sourceName", MyBatisUtil.sourceName);
			mapPlusId.put("destName", MyBatisUtil.destName);
			mapPlusId.put(tabName, destMaxDestId);

			if (pagingProc.containsKey(tabName)) {
				HashMap<String, Object> mapPagindId = new HashMap<String, Object>();
				int totalCount = 0;
				int paging = (sourceMaxDestId/10000 + 1);
				logger.error("paging = " + paging);
				for (int index = 0; index <= paging; index ++) {
					int startId = index * 10000;
					int endId = (index + 1) * 10000;
					mapPagindId.put("startId", startId);
					mapPagindId.put("endId", endId);

					logger.info("(分页处理"+index+") 从[数据源:" + MyBatisUtil.sourceName +"].[表:" + tabName + "] 中, 获取startId>= " + startId + ", endId < " + endId + " 的数据.");
					List<Object> listRecords = (List)sourceMetaData.getPagingTableRecords(tabName, mapPagindId);
					logger.info("(分页处理"+index+") 从[数据源:" + MyBatisUtil.sourceName +"].[表:" + tabName + "] 中, 获取到 " + listRecords.size() + " 条记录.");
					int numResult = destMetaData.batchInsert(tabName, listRecords, mapPlusId);
					if (numResult < 0) {
						logger.error("跳出分页处理循环");
						mergeSuccess = false;
						break outer_loop; // 跳出分页处理循环
					}
					totalCount += numResult;
				}
				logger.info("<<< (结束分页处理) 累计插入到[数据源:" + MyBatisUtil.destName +"].[表:" + tabName + "] 中 " + totalCount + " 条记录.");
			} else {
				List<Object> listRecords = (List)sourceMetaData.getTableRecords(tabName, null);
				logger.info("从[数据源:" + MyBatisUtil.sourceName +"].[表:" + tabName + "] 中, 获取到 " + listRecords.size() + " 条记录.");
				int numResult = destMetaData.batchInsert(tabName, listRecords, mapPlusId);
				if (numResult < 0) {
					mergeSuccess = false;
					break;
				}
				logger.info("插入到[数据源:" + MyBatisUtil.destName +"].[表:" + tabName + "] 中 " + numResult + " 条记录.");
			}
		}

		if (true == mergeSuccess) {
			logger.info("合并 "+ MyBatisUtil.sourceName + " 到 " + MyBatisUtil.destName + " 成功! [√]");
			logger.info("[数据源:" + MyBatisUtil.destName + "].[表:DBS]中数据库名称全部进行了自动调整,请根据需要手工修改.");
			logger.info("SELECT * FROM DBS WHERE NAME LIKE '" + MyBatisUtil.sourceName + "%'");
			logger.info("UPDATE DBS SET NAME = ? WHERE DB_ID = ?");
			logger.info("============= " + MyBatisUtil.sourceName + " 表ID跳号信息 =============");
			logger.info(mapPlusId);
			logger.info("========================================================");
		} else {
			logger.error("合并 " + MyBatisUtil.sourceName + " 到 " + MyBatisUtil.destName + " 失败! [×]");
			logger.error("============= " + MyBatisUtil.sourceName + " 表ID跳号信息 =============");
			logger.error(mapPlusId);
			logger.error("========================================================");
		}

		/*
		String useInput2 = "";
		while (!useInput2.equals("Y")) {
			if (true == mergeSuccess) {
				System.out.print("合并 "+ MyBatisUtil.sourceName + " 到 " + MyBatisUtil.destName + " 成功[√], 是否回滚本次合并操作[Y/n] : ");
			} else {
				System.out.print("合并 "+ MyBatisUtil.sourceName + " 到 " + MyBatisUtil.destName + " 失败!![×] 必须回滚本次合并操作[Y/n] : ");
			}
			useInput2 = sc.nextLine();
			if (useInput2.equalsIgnoreCase("n")) {
				System.exit(1);
			} else if (useInput2.equals("Y")) {
				rollback(tables, mapPlusId);
			}
		}*/
	}

	static private void cliCommond(String[] args) {
		Options opt = new Options();
		opt.addOption("c", "check", false, "检查二个元数据库是否存在数据冲突");
		opt.addOption("h", "help",  false, "打印命令行帮助");
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

		String formatstr = "MetaDataMerga --s=<arg> --d=<arg> [-h/--help]";

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
		if( cl.hasOption("s") ) {
			MyBatisUtil.sourceName = cl.getOptionValue("s");
		} else {
			System.out.println("missing --s arg");
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp(formatstr, "", opt, "");
			System.exit(1);
		}

		if( cl.hasOption("d") ) {
			MyBatisUtil.destName = cl.getOptionValue("d");
		} else {
			System.out.println("missing --d arg");
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp(formatstr, "", opt, "");
			System.exit(1);
		}
	}

	static private void rollback(List<String> tables, HashMap<String, Object> mapPlusId) {
		MetaDataMapper destMetaData = new MetaDataMapper(MyBatisUtil.destName);

		for (int n = tables.size()-1; n >= 0; n --) {
			String tableName = tables.get(n);
			if (false == mapPlusId.containsKey(tableName)) {
				logger.error("回滚元数据表 " + tableName + " 参数错误! [×]");
			}
			int numRollback = destMetaData.rollback(tableName, mapPlusId);
			if (numRollback < 0) {
				logger.error("回滚元数据表 " + tableName + " 失败! [×]");
				System.exit(1);
			}
		}
		logger.error("回滚元数据表成功! [√]");
		logger.info("========================================================");
	}

	static private boolean checkConflict() {
		MetaDataMapper sourceMetaData = new MetaDataMapper(MyBatisUtil.sourceName);
		MetaDataMapper destMetaData = new MetaDataMapper(MyBatisUtil.destName);

		boolean conflict = false;

		List<String> dbs = new ArrayList<String>();
		dbs.add("VERSION");
		dbs.add("DBS");
		dbs.add("TYPES");

		/* not check Conflict
		tables.add("ROLES");
		*/

		for (String dbName :dbs) {
			logger.info(">>> 检查 [数据源:" + MyBatisUtil.sourceName + "].[数据库:" + dbName + "] 和 [数据源:" + MyBatisUtil.destName + "].[数据库:" + dbName + "] 数据是否存在冲突?");

			List<Object> listRecords = (List) sourceMetaData.getTableRecords(dbName, null);
			if (dbName.equalsIgnoreCase("DBS")) {
				// 生成新的数据库名称
				for(Object object : listRecords){
					String newDbName = MyBatisUtil.sourceName + "_" +  ((Dbs) object).getName();
					((Dbs) object).setName(newDbName);
				}
			}
			List<Object> listUniqueKey = destMetaData.checkUniqueKey(dbName, listRecords);
			if (listUniqueKey.size() > 0) {
				conflict = true;
				for(Object object : listUniqueKey){
					if (object instanceof Dbs) {
						logger.error("数据库名 " + ((Dbs) object).getName() + " 在2个数据源中同时存在! 存在数据冲突!!! [×]");
					} else if (object instanceof Version) {
						logger.error("2个数据源的元数据版本号 " + ((Version) object).getSchemaVersion() + " 不一致! [×]");
					} else if (object instanceof Roles) {
						logger.error(((Roles) object).getRoleName() + " 冲突!!! [×]");
					} else if (object instanceof Types) {
						logger.error(((Types) object).getTypeName() + " 冲突!!! [×]");
					} else {
						logger.error(object + " 冲突!!! [×]");
					}
				}
			} else {
				logger.info("<<< [数据源:" + MyBatisUtil.sourceName + "].[数据库:" + dbName + "] 和 [数据源:" + MyBatisUtil.destName + "].[数据库:" + dbName + "] 不存在冲突. [√]");
			}
		}

		if (conflict == true) {
			logger.error("检查完毕 [数据源:" + MyBatisUtil.sourceName + "] 和 [数据源:" + MyBatisUtil.destName + "] 存在数据冲突! 程序退出!! [×]");
			System.exit(1);
		} else {
			logger.info("检查完毕 [数据源:" + MyBatisUtil.sourceName + "] 和 [数据源:" + MyBatisUtil.destName + "] 没有数据冲突. [√]");
		}

		return conflict;
	}

	static private void checkHdfsCluster() {
		MetaDataMapper sourceMetaData = new MetaDataMapper(MyBatisUtil.sourceName);
		MetaDataMapper destMetaData = new MetaDataMapper(MyBatisUtil.destName);

		logger.info(">>> 检查 [数据源:" + MyBatisUtil.sourceName+"] HDFS ClusterID 与 [数据源:"+MyBatisUtil.destName+"] 是否相同?");

		List<Object> sourceDbs = (List) sourceMetaData.getTableRecords("DBS", null);
		List<Object> destDbs = (List) destMetaData.getTableRecords("DBS", null);

		boolean differentCluster = false;

		HashMap<String, String> sourceHdfsCluster = new HashMap<String, String>();
		for(Object object : sourceDbs) {
			String dbLocationUri = ((Dbs)object).getDbLocationUri();
			dbLocationUri = dbLocationUri.substring("hdfs://".length(), dbLocationUri.length());

			String[] splits = dbLocationUri.split("/");
			if (splits.length > 0) {
				String hdfsCluster = "hdfs://"+splits[0];
				sourceHdfsCluster.put(hdfsCluster, hdfsCluster);
			} else {
				logger.error(MyBatisUtil.sourceName + " DBS.DbLocationUri : " + ((Dbs)object).getDbLocationUri() + " 错误!");
				System.exit(1);
			}
		}

		for(Object object : destDbs) {
			String dbLocationUri = ((Dbs)object).getDbLocationUri();
			dbLocationUri = dbLocationUri.substring("hdfs://".length(), dbLocationUri.length());

			String[] splits = dbLocationUri.split("/");
			if (splits.length > 0) {
				String hdfsCluster = "hdfs://"+splits[0];
				if(!sourceHdfsCluster.containsKey(hdfsCluster)) {
					StringBuilder sbSourceHdfs = new StringBuilder();
					for(String cluster : sourceHdfsCluster.values()) {
						sbSourceHdfs.append(cluster + ", ");
					}

					logger.error("[数据源:" + MyBatisUtil.sourceName + "].[ClusterId:" + sbSourceHdfs.toString() + "] 不等于 [数据源:"
							+ MyBatisUtil.destName + "].[ClusterId:" + hdfsCluster + "] [×]");
					differentCluster = true;
				}
			} else {
				logger.error("[数据源:" + MyBatisUtil.destName + "] DBS.DbLocationUri : " + ((Dbs)object).getDbLocationUri() + " 错误! [×]");
				System.exit(1);
			}
		}

		if (differentCluster == true) {
			logger.error("<<< [数据源:" + MyBatisUtil.sourceName+"] HDFS ClusterID 与 [数据源:"+MyBatisUtil.destName+"] 不同! 程序退出!! [×]");
			System.exit(1);
		} else {
			logger.info("<<< [数据源:" + MyBatisUtil.sourceName+"] HDFS ClusterID 与 [数据源:"+MyBatisUtil.destName+"] 相同. [√]");
		}
	}
}
