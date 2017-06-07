export SOURCE=exchange_db
export DEL_DB=default,nisp_nhids,real,azkaban_autotest_db
export DEL_TBL=

~/java-current/jre/bin/java -cp "./hive-tools-current.jar" com.netease.hivetools.apps.DelMetaData --s=$SOURCE --d=$DEL_DB --t=$DEL_TBL