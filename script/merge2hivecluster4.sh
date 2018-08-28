export SOURCE_NAME=exchange_db
export DEST_NAME=online_cluster4

/home/hadoop/java-current/jre/bin/java -cp "./hive-tools-current.jar" com.netease.hivetools.apps.MetaDataMerge --s=$SOURCE_NAME --d=$DEST_NAME