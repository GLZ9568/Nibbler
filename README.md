## Nibbler - Datastax Opscenter Diagnostic Files Analyzer

Nibbler is a GUI tool to analyze the Datastax opscenter diagnostic files. It makes the troubleshooting tasks much easier and helps you quickly narrow down the issue by providing the accurate and integrated diag analysis reports. 


## Requirements

1. Java 1.8+  <br> 
2. Supported platforms: MacOS, Linux, Windows

## Getting started

1. Download the executable [Nibbler.jar](https://github.com/GLZ9568/Nibbler/raw/master/out/artifacts/Nibbler_jar/Nibbler.jar) file and double click it to run or from command line run `java -jar Nibbler.jar` <br> <br>
2. Click the **Open Diag Directory** button to select the uncompressed Opscenter diag tar ball path. Please note that the path should be the root path of the Opscenter diag path.E.g `/Users/username/Downloads/Mycluster-diagnostics-2018_01_10_03_04_49_UTC` <br> <br>
3. Press the **Start Analyzing** button to start parsing and analyzing the Opscenter diags. <br> <br>
4. When analyzing is done, it will pop a window which tells the analysis is done and it will save analysis report files to the same path of the executable file. <br> <br>
5. You can browse the analysis reports from each Collapse Tab. <br> <br>
   - `Cluster Configuration Summary`. This tab displays the information for the whole cluster configuration and breaks down the configuration node by node. It will also do the health check: 1. if the node is using the supported OS version 2. if there are jdk or dse versions mismatch per DC 3. if data and commitlog directories are the same disk device 4. if NTP service is down 
   - `Node Status`. This tab displays more comprehensive information than nodetool status. It integrates the information from nodetool status, dsetool ring, and nodetool info which provides the node status, uptime, health, workload type.
   - `Node Info`. This tab mainly provides the information from nodetool info for each node per DC. The order of the output for each node is sorted by the ratio of used_heap/max_heap per DC so that you can easily identify which node uses the most heap in a DC which indicates that node could have the heap pressure. It also integrates the hostname with the node ip address which will be convenient if customer only provides the information based on hostname. It will also do the health check if the nodes are configured with different heap size per DC. 
   - `Table Statistics`. This tab provides key metrics for table statistics. It will check the large partitions (> 100mb), tombstones, max cells per slice(for hot tables), local read/write latency together with proxyhistopgram which will reflect the latency from server and client side.
   - `Thread Pool Statistics`. This tab provides the statistics for thread pool on each node. It displays the non-zero dropped messages and non-zero values for all Active, Pending, Blocked, All time blocked metrics for each thread pool. It will also integrate the read repair statistics from netstats which can tell how inconsistent the cluster looks like.
   - `Node Resource Usage Info`. This tab provides the statistics for resource usage per node. The output is sorted by total cpu usage per node. It provides the comprehensive information of cpu, physical memory, heap, offheap memory for each node. 
   - `Node Configuration Files Info`. This tab provides the key configuration parameters from cassandra.yaml and dse.yaml. It will also provide the health check if seed list configuration is consistent across the cluster.
   <br><br>
6. In some cases, the Opscenter diags may be incomplete or file contents are corrupted. The tool can handle most of the incomplete diags and corrupted file contents. However, it can still be broken in some cases. You can refer to the nibbler.log which is under the same directory of the executable file to investigate the issue.


## Future work

Will add the function for system.log analysis. 

## Contacts 

If you want to contribute to this tool, please contact mike.zhang@datastax.com and chun.gao@datastax.com.
   
