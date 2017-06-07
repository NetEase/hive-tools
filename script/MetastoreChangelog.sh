export ZK_HOST=hadoop944.hz.163.org
export ZK_PATH=/hive-metastore-changelog/hive-cluster3
export FILTE_DATABASE=beauty_dw
export FILTE_TABLE=ods_beauty
#export DEBUG='-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000'

/home/hadoop/java-current/jre/bin/java ${DEBUG} -cp "./hive-tools-current.jar"  com.netease.hivetools.apps.MetastoreChangelog --z=${ZK_HOST} --c=${ZK_PATH} --d=${FILTE_DATABASE} --t=${FILTE_TABLE}
