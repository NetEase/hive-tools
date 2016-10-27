package com.netease.hivetools;

import com.netease.hivetools.mappers.MetaDataMapper;
import com.netease.hivetools.meta.Dbs;
import com.netease.hivetools.meta.Roles;
import com.netease.hivetools.meta.Types;
import com.netease.hivetools.meta.Version;
import com.netease.hivetools.service.MyBatisUtil;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by hzliuxun on 16/10/26.
 */
public class MetaDataMerge {
	private static final Logger logger = Logger.getLogger(MetaDataMerge.class.getName());

	private static boolean checkConflict = false;

	public static void main(String[] args) {
		cliCommond(args);

//		MyBatisUtil.sourceName = "hive_haitao";
//		MyBatisUtil.destName = "hive_merge";

		MetaDataMapper sourceMetaData = new MetaDataMapper(MyBatisUtil.sourceName);
		MetaDataMapper destMetaData = new MetaDataMapper(MyBatisUtil.destName);

		List<String> tables = new ArrayList<String>();
		HashMap<String, Object> mapPlusId = new HashMap<String, Object>();

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

		checkConflict();
		if (checkConflict)
			return;

		for (String tabName :tables) {
			logger.info("merge " + MyBatisUtil.sourceName + "." + tabName + " ==> " + MyBatisUtil.destName + "." + tabName);
			int maxDestId = destMetaData.getTableMaxId(tabName);

			mapPlusId.put("sourceName", MyBatisUtil.sourceName);
			mapPlusId.put("destName", MyBatisUtil.destName);
			mapPlusId.put(tabName, maxDestId);
			List<Object> listRecords = (List)sourceMetaData.getTableRecords(tabName);
			int numResult = destMetaData.batchInsert(tabName, listRecords, mapPlusId);
			if (numResult < 0) {
				logger.error("merge " + MyBatisUtil.sourceName + " ==> " + MyBatisUtil.destName + " faild!");
				logger.error("============= " + MyBatisUtil.sourceName + " plus id infomation =============");
				logger.error(mapPlusId);
				logger.error("========================================================");
				System.exit(1);
			}
		}

		logger.info("merge "+ MyBatisUtil.sourceName + " ==> " + MyBatisUtil.destName + " success!");
		logger.info("============= " + MyBatisUtil.sourceName + " plus id infomation =============");
		logger.info(mapPlusId);
		logger.info("========================================================");
	}

	static private void cliCommond(String[] args) {
		Options opt = new Options();
		opt.addOption("c", "check", false, "检查二个元数据库是否存在数据冲突");
		opt.addOption("h", "help",  false, "打印命令行帮助");
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

		String formatstr = "MetaDataMerga --s=<arg> --d=<arg> [-c/--check][-h/--help]";

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
			return;
		}
		if (cl.hasOption("c")) {
			checkConflict = true;
		}
		if( cl.hasOption("s") ) {
			MyBatisUtil.sourceName = cl.getOptionValue("s");
			System.out.println("迁出的元数据库 ==> " + cl.getOptionValue("s"));
		} else {
			System.out.println("missing --s arg");
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp(formatstr, "", opt, "");
			return;
		}

		if( cl.hasOption("d") ) {
			MyBatisUtil.destName = cl.getOptionValue("d");
			System.out.println("迁入的元数据库 ==> " + cl.getOptionValue("d"));
		} else {
			System.out.println("missing --d arg");
			HelpFormatter hf = new HelpFormatter();
			hf.printHelp(formatstr, "", opt, "");
			return;
		}
	}

	static private void checkConflict() {
		MetaDataMapper sourceMetaData = new MetaDataMapper(MyBatisUtil.sourceName);
		MetaDataMapper destMetaData = new MetaDataMapper(MyBatisUtil.destName);

		boolean conflict = false;

		List<String> tables = new ArrayList<String>();
		tables.add("VERSION");
		tables.add("DBS");
		tables.add("TYPES");

		/* not check Conflict
		tables.add("ROLES");
		*/
		logger.info("===============================================================");
		for (String tabName :tables) {
			logger.info(" Check [" + MyBatisUtil.destName + "." + tabName + "] UniqueKey  ");

			List<Object> listRecords = (List) sourceMetaData.getTableRecords(tabName);
			List<Object> listUniqueKey = destMetaData.checkUniqueKey(tabName, listRecords);
			if (listUniqueKey.size() > 0) {
				conflict = true;
				logger.error(" Check [" + MyBatisUtil.sourceName + "." + tabName + "] UniqueKey Conflict");
				for(Object object : listUniqueKey){
					if (object instanceof Dbs) {
						logger.error(((Dbs) object).getName());
					} else if (object instanceof Version) {
						logger.error("hive meta version " + ((Version) object).getSchemaVersion() + " Conflict!");
					} else if (object instanceof Roles) {
						logger.error(((Roles) object).getRoleName());
					} else if (object instanceof Types) {
						logger.error(((Types) object).getTypeName());
					} else {
						logger.error(object);
					}
				}
				logger.error("-----------------------------------------------------");
			}
		}

		if (conflict == true) {
			System.exit(1);
		} else {
			logger.info(" [" + MyBatisUtil.destName + "] 没有数据冲突");
			logger.info("===============================================================");
		}
	}
}
