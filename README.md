# Nibbler: DataStax OpsCenter Diagnostic Files Analyzer

Nibbler is a user-friendly, graphical tool designed to streamline the analysis of DataStax OpsCenter diagnostic files. This powerful tool simplifies troubleshooting tasks and generates accurate, integrated diagnostic reports to help you quickly pinpoint potential issues.

## Requirements

- Java 1.8 or higher
- Compatible platforms: MacOS, Linux, Windows

## Getting Started

1. Download the executable Nibbler.jar file and run it by double-clicking or executing `java -jar Nibbler.jar` from the command line.
2. Click the "Open Diag Directory" button and select the root path of the uncompressed OpsCenter diagnostic tarball (e.g., /Users/username/Downloads/Mycluster-diagnostics-2018_01_10_03_04_49_UTC).
3. Press the "Start Analyzing" button to initiate the parsing and analysis of OpsCenter diagnostic data.
4. Upon completion, a window will appear, indicating the analysis is finished, and report files will be saved in the same directory as the executable file.

## Features

Nibbler offers a comprehensive analysis of your cluster data through various collapsible tabs:

- **Cluster Configuration Summary Tab**: Displays overall cluster configuration and node-specific information, along with health checks for OS version, JDK/DSE version consistency, data and commitlog directory checks, and NTP service status.
- **Node Status Tab**: Offers a detailed view of node status, uptime, health, and workload type by integrating data from nodetool status, dsetool ring, and nodetool info.
- **Node Info Tab**: Provides nodetool info data, sorted by used_heap/max_heap ratio, allowing easy identification of nodes with potential heap pressure. Includes health checks for heap size consistency and integrates hostname with node IP addresses.
- **Table Statistics Tab**: Showcases key table metrics, including large partitions, tombstones, max cells per slice (for hot tables), and local read/write latency with proxy histogram data.
- **Thread Pool Statistics Tab**: Displays thread pool statistics for each node, including non-zero dropped messages and non-zero values for active, pending, blocked, and all-time blocked metrics.
- **Node Resource Usage Info Tab**: Offers a detailed view of resource usage per node, sorted by total CPU usage, and includes information on CPU, physical memory, heap, and off-heap memory.
- **Node Configuration Files Info Tab**: Presents key configuration parameters from cassandra.yaml and dse.yaml, along with a health check for seed list consistency across the cluster.

> **Note**: Nibbler can handle most incomplete diagnostics and corrupted file contents; however, some cases may still cause issues. For troubleshooting, refer to the nibbler.log file located in the same directory as the executable file.

## Contributions

If you are interested in contributing to Nibbler's development, please reach out to [mike.zhang@datastax.com](mailto:mike.zhang@datastax.com) and [chun.gao@datastax.com](mailto:chun.gao@datastax.com).
