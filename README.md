## Nibbler - Datastax Opscenter Diagnostic Files Analyzer

Nibbler is a tool to analyze the Datastax opscenter diagnostic files. It makes the troubleshoot tasks much easier and helps you quickly narrow down the issue by providing the accurate and integrated diag analysis reports. 


## Requirements

1.Java 1.8+

2.Supported platforms: MacOS, Linux, Windows

## Getting started

1.Download the executable [Nibbler.jar](https://github.com/GLZ9568/Nibbler/raw/master/out/artifacts/Nibbler_jar2/Nibbler.jar) file and double click it to run


2.Click the **Open Diag Directory** button to select the uncompressed Opscenter diag tar ball path. Please note that the path should be the root path of the Opscenter diag path.E.g `/Users/username/Downloads/Mycluster-diagnostics-2018_01_10_03_04_49_UTC`


3.Press the **Start Analyzing** button to start parsing and analyzing the Opscenter diags.


4.When analyzing is done, it will pop a window which tells the analysis is done and it will save analysis report files to the same path of the executable file.


5.You can browse the analysis reports from each Collapse Tab.


6.In some cases, the Opscenter diags may be incomplete or file contents are corrupted. The tool can handle most of the incomplete diags and corrupted file contents. However, it can still be broken in some cases. You can refer to the Nibbler.log which is under the same directory of the executable file to investigate the issue.


