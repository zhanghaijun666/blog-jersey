#!/bin/bash

rm -rf /opt/Blog/Blog-1.0.jar
rm -rf /opt/Blog/www
if [[ ! -d "/opt/BlogCloudJersey" ]]; then
	cd /opt
	git clone git@github.com:zhanghaijun666/BlogCloudJersey.git
else
	cd /opt/BlogCloudJersey
	git stash
	git stash clear
	git pull
fi
cd /opt/BlogCloudJersey/BlogCloudServer
mvn clean package
if [[ ! -d "/opt/Blog" ]]; then
	mkdir -p /opt/Blog
fi
cd /opt/Blog
ln -s /opt/BlogCloudJersey/BlogCloudServer/target/BlogCloudServer-1.0-SNAPSHOT.jar /opt/Blog/Blog-1.0.jar
ln -s /opt/BlogCloudJersey/BlogCloudClient/www /opt/Blog/www
if [[ ! -d "/opt/Blog/config" ]]; then
	cp -r /opt/BlogCloudJersey/BlogCloudServer/config /opt/Blog/config
fi
if [[ ! -f "/opt/Blog/user.txt" ]]; then
	cp /opt/BlogCloudJersey/BlogCloudServer/user.txt /opt/Blog/user.txt
fi
if [[ ! -d "/opt/Blog/bin" ]]; then
	cp -r /opt/BlogCloudJersey/BlogCloudServer/bin /opt/Blog/bin
	chmod -R +x /opt/Blog/bin/*.sh
fi
if [[ ! -d "/usr/lib/systemd/system" ]]; then
	mkdir -p /usr/lib/systemd/system
fi
if [[ ! -f "/usr/lib/systemd/system/blog.service" ]]; then
	cp /opt/Blog/bin/blog.service /usr/lib/systemd/system/blog.service
fi

#CentOS7防火墙开启端口8888
#firewall-cmd --permanent --add-port=8888/tcp
#firewall-cmd --reload 

#查看进程占用
#lsof -i:8888

#重新加载systemctl
#systemctl daemon-reload

#查看service的状态
#systemctl   list-unit-files  | grep blog
#systemctl list-units --type=service 

#systemctl start blog
#systemctl restart blog
#systemctl stop blog