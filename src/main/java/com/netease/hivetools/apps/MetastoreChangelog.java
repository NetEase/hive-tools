package com.netease.hivetools.apps;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.ranger.binding.metastore.thrift.TUpdateMetadataRequest;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TMemoryBuffer;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzliuxun on 17/4/1.
 */

class zkListener implements ConnectionStateListener {
  public static final Log LOGGER = LogFactory.getLog(MetastoreChangelog.class);

  @Override
  public void stateChanged(CuratorFramework client, ConnectionState state) {
    switch (state) {
      case LOST:
        LOGGER.error("DistributedLock lost session with zookeeper");
        break;
      case CONNECTED:
        LOGGER.warn("DistributedLock connected with zookeeper");
        break;
      case RECONNECTED:
        LOGGER.warn("DistributedLock reconnected with zookeeper");
        break;
    }
  }
}

public class MetastoreChangelog {
  private static final Logger LOGGER = Logger.getLogger(MetastoreChangelog.class.getName());

  protected static CuratorFramework zkClient;
  private static zkListener listener = null;
  private static String zkHost = "";
  private static String zkPath = "";
  private static String filte_database = "";
  private static String filte_table = "";
  private final static String MAX_ID_FILE_NAME = "/maxid";
  private final static String LOCK_RELATIVE_PATH = "/lock";

  public static void main(String[] args) {
    PropertyConfigurator.configure("log4j.properties");

    cliCommond(args);

    try {
      setUpZooKeeperAuth();
      getSingletonClient();

      listZNodeData();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void setUpZooKeeperAuth() throws IOException {
    /*
    if (UserGroupInformation.isSecurityEnabled()) {
      String principal = hiveConf.getVar(HiveConf.ConfVars.METASTORE_KERBEROS_PRINCIPAL);
      if (StringUtils.isEmpty(principal)) {
        throw new IOException("Hive Metastore Kerberos principal is empty");
      }
      String keyTabFile = hiveConf.getVar(HiveConf.ConfVars.METASTORE_KERBEROS_KEYTAB_FILE);
      if (StringUtils.isEmpty(keyTabFile)) {
        throw new IOException("Hive Metastore Kerberos keytab is empty");
      }
      // Install the JAAS Configuration for the runtime
      Utils.setZookeeperClientKerberosJaasConfig(principal, keyTabFile);
    }
    */
  }

  private final static ACLProvider zooKeeperAclProvider = new ACLProvider() {
    List<ACL> nodeAcls = new ArrayList<ACL>();

    @Override
    public List<ACL> getDefaultAcl() {
      if (UserGroupInformation.isSecurityEnabled()) {
        // Read all to the world
        nodeAcls.addAll(ZooDefs.Ids.READ_ACL_UNSAFE);
        // Create/Delete/Write/Admin to the authenticated user
        nodeAcls.add(new ACL(ZooDefs.Perms.ALL, ZooDefs.Ids.AUTH_IDS));
      } else {
        // ACLs for znodes on a non-kerberized cluster
        // Create/Read/Delete/Write/Admin to the world
        nodeAcls.addAll(ZooDefs.Ids.OPEN_ACL_UNSAFE);
      }
      return nodeAcls;
    }

    @Override
    public List<ACL> getAclForPath(String path) {
      return getDefaultAcl();
    }
  };

  private static void getSingletonClient() throws Exception {
    if (zkClient == null) {
      synchronized (MetastoreChangelog.class) {
        if (zkClient == null) {
          zkClient =
              CuratorFrameworkFactory
                  .builder()
                  .connectString(zkHost)
                  .aclProvider(zooKeeperAclProvider)
                  .retryPolicy(
                      new RetryNTimes(3, 3000))
                  .build();
          listener = new zkListener();
          zkClient.getConnectionStateListenable().addListener(listener);
          zkClient.start();
        }
      }
    }
  }

  private static void listZNodeData() {
    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("==> writeZNodeData()");
    }

    try {
      GetChildrenBuilder childrenBuilder = zkClient.getChildren();
      List<String> children = childrenBuilder.forPath(zkPath);

      int index = 0;
      for (String child : children) {
        child = "/" + child;
        if (child.equalsIgnoreCase(LOCK_RELATIVE_PATH)
            || child.equalsIgnoreCase(MAX_ID_FILE_NAME)) {
          // do not delete maxid and lock file
          continue;
        }
        String childPath = zkPath + child;
        Stat stat = zkClient.checkExists().forPath(childPath);
        if (null != stat ) {
          byte[] bytes = zkClient.getData().forPath(childPath);
          TUpdateMetadataRequest tUpdateMetadataRequest = new TUpdateMetadataRequest();
          TMemoryBuffer tmb = new TMemoryBuffer(8);
          tmb.write(bytes);
          TProtocol tp = new org.apache.thrift.protocol.TJSONProtocol(tmb);
          tUpdateMetadataRequest.read(tp);

          if (tUpdateMetadataRequest.getDeltas() == null) {
            continue;
          } else {
            for (int i = 0; i < tUpdateMetadataRequest.getDeltas().size(); i++) {
              String dbName = tUpdateMetadataRequest.getDeltas().get(i).getDatabase();
              String tabName = tUpdateMetadataRequest.getDeltas().get(i).getTable();

              if (!filte_database.isEmpty() && !dbName.contains(filte_database)) {
                continue;
              }
              if (!filte_table.isEmpty() && !tabName.contains(filte_table)) {
                continue;
              }
              LOGGER.debug(" --- " + childPath + " --- ");
              LOGGER.debug(tUpdateMetadataRequest.toString());
//            LOGGER.debug(tUpdateMetadataRequest.getDeltas().get(i).toString());
              break;
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }

    if(LOGGER.isDebugEnabled()) {
      LOGGER.debug("<== writeZNodeData()");
    }
  }

  static private void cliCommond(String[] args) {
    Options opt = new Options();
    opt.addOption("h", "help",  false, "打印命令行帮助");
    opt.addOption(OptionBuilder.withLongOpt("z")
        .withDescription("zookeeper服务器地址")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("c")
        .withDescription("zookeeper 中 MetastoreChangelog 路径")
        .withValueSeparator('=')
        .hasArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("d")
        .withDescription("database 名称")
        .withValueSeparator('=')
        .hasOptionalArg()
        .create());
    opt.addOption(OptionBuilder.withLongOpt("t")
        .withDescription("table 名称")
        .withValueSeparator('=')
        .hasOptionalArg()
        .create());

    String formatstr = "MetastoreChangelog --z=<arg> --c=<arg> --d=<arg> --t=<arg> [-h/--help]";

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
    if( cl.hasOption("z") ) {
      zkHost = cl.getOptionValue("z");
    } else {
      System.out.println("missing --z arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }
    if( cl.hasOption("c") ) {
      zkPath = cl.getOptionValue("c");
    } else {
      System.out.println("missing --c arg");
      HelpFormatter hf = new HelpFormatter();
      hf.printHelp(formatstr, "", opt, "");
      System.exit(1);
    }
    if( cl.hasOption("d") ) {
      filte_database = cl.getOptionValue("d");
    }

    if( cl.hasOption("t") ) {
      filte_table = cl.getOptionValue("t");
    }
  }
}
