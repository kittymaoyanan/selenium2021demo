Dragontesting web_selenium
====================

初始环境搭建
> 运行环境要求JDK1.8以上。

> 浏览器及对应的Driver。

> Maven环境。

> Allure运行环境安装。

> ffmpeg环境安装。（非必须）

###Ps:环境搭建可参考以下地址:[Web 自动化测试执行指南](http://help.dragontesting.cn/yihao/1.0/doc/%E9%BE%99%E6%B5%8B1%E5%8F%B7%E8%BF%90%E8%A1%8C%E6%B5%8B%E8%AF%95%E6%8C%87%E5%8D%97-Selenium.pdf)

## 目前使用maven+testng+selenium作为基础，二次封装常用API，简化代码，采用allure产生报告。


## 使用相关
在龙测自动化平台上完成自动化案例的编写后，在测试代码处点击下载测试项目，在平台上维护的流程信息与手机配置信息均可自动生成在项目工程中，环境配置正确无误的情况下可选用一键执行文件，即可开始自动化测试之旅。</br>
一键执行文件使用说明：window系统下，在项目的根目录下，双击run.bat文件即可完成一键启动。linux/MAC系统下，命令行进入项目的根目录下，使用sh run.sh 触发一键启动。ps:若项目类型为Android和ios，运行一键启动前需要先连接手机及appium。


## 目录结构

初始的目录结构如下：

~~~
longceforweb
├─allure-results                APP运行结果的报告存放目录
|-pom.xml                       项目依赖包及配置管理文件
├─testNg.xml                    待执行脚本管理文件
│-run.bat                       windows环境下一键执行文件
│-runwithvideo.bat              待视频一键运行报告
│-run.sh                        linux/mac 环境下一键执行文件
│-runwithvideo.sh               linux/mac 环境下待视频一键执行文件
│-resources
│   ├─log4j.properties          日志信息配置表单
│   └─config.properties         系统配置表单
├─src                           框架系统目录
│  ├─main                       空目录
│  │  └─java              
│  │    ├─com.dt.web.DtTestCase 常用API文件 
│  │    ├─core
│  │    │   └─BaseTest          基础测试类
│  │    └─Lsterner
│  │        └─TestListener      监听文件
│  └─test                       框架类库目录
│     └─java                    框架类库目录  
│       ├─fields                元素管理文件夹
│       │   └─Elements          项目元素管理问题
│       └─testcase              脚本文件
├─log4j                         运行日志信息     
├─README.md                     README 文件
~~~

 

## 其他说明
交流QQ群号：230125864
## 版权信息

苏州龙测智能科技有限公司