package netease.bigdata.hivetools;

import netease.bigdata.hivetools.service.DbsService;
import netease.bigdata.hivetools.service.PartitionService;

import java.io.File;

public class MainTest {
  public static void main(String[] args) {
    System.out.println(new File(".").getAbsolutePath());

    DbsService dbsService = new DbsService();
    int i = 0;

//    PartitionService partitionService  = new PartitionService();
//    Partition p = new Partition("/tmp", 1, "/tmp1:||:tmp2");
//    partitionService.insertPartition(p);
//    Partition po = partitionService.getPartitionByDir("/tmp");
//    System.out.println(po.getDir());
//    partitionService.deletePartition("/tmp");
  }
}
