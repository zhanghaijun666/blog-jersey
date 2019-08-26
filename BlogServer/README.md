#### 技术
[Jersey](https://jersey.github.io/)
 , [simpleframework](https://github.com/ngallagher/simpleframework)
 , [liquibase](http://www.liquibase.org/)
 , [activejdbc](http://javalite.io/record_selection)
 , [protobuf](https://developers.google.com/protocol-buffers/)
 , [Fail2ban](https://www.jianshu.com/p/4fdec5794d08)

#### 数据库支持
[mysql](https://www.mysql.com/) , [H2](http://www.h2database.com/html/main.html)

#### 许可证
[License](https://blog.csdn.net/lee272616/article/details/55057311)

#### Jersey 创建后台项目
`mvn archetype:generate -DarchetypeArtifactId=jersey-quickstart-grizzly2 -DarchetypeGroupId=org.glassfish.jersey.archetypes -DinteractiveMode=false -DgroupId=com -DartifactId=BlogServer -Dpackage=com.server -DarchetypeVersion=2.27`

#### 备用
[jersey 文件上传-使用两种不同的方式](https://blog.csdn.net/wk313753744/article/details/46235895)
[git文件存储](https://git-scm.com/book/zh/v2/Git-%E5%86%85%E9%83%A8%E5%8E%9F%E7%90%86-Git-%E5%AF%B9%E8%B1%A1)

#### 项目部署
获取动态IP: `dhclinet`
重启网络：`service network restart`

vi /etc/sysconfig/network-scripts/ifcfg-enp0s3
```markdown
TYPE=Ethernet
PROXY_METHOD=none
BROWSER_ONLY=no
BOOTPROTO=static
DEFROUTE=yes
IPV4_FAILURE_FATAL=no
IPV6INIT=yes
IPV6_AUTOCONF=yes
IPV6_DEFROUTE=yes
IPV6_FAILURE_FATAL=no
IPV6_ADDR_GEN_MODE=stable-privacy
NAME=enp0s3
UUID=857fa866-31e0-4d62-8b1b-cf54c854bfab
DEVICE=enp0s3
ONBOOT=yes
IPADDR=192.168.1.5
NETMASK=255.255.255.0
GATEWAY=192.168.1.1
```


