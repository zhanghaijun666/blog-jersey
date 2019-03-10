#! /bin/bash

rm -rf /opt/blog/Blog-1.0.jar
rm -rf /opt/blog/www
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
if [[ ! -d "/opt/blog" ]]; then
	mkdir -p /opt/blog
fi
cd /opt/blog
ln -s /opt/BlogCloudJersey/BlogCloudServer/target/BlogCloudServer-1.0-SNAPSHOT.jar /opt/blog/Blog-1.0.jar
ln -s /opt/BlogCloudJersey/BlogCloudClient/www /opt/blog/www
if [[ ! -d "/opt/blog/config" ]]; then
	cp -r /opt/BlogCloudJersey/BlogCloudServer/config /opt/blog/config
fi
if [[ ! -f "/opt/blog/user.txt" ]]; then
	cp /opt/BlogCloudJersey/BlogCloudServer/user.txt /opt/blog/user.txt
fi
if [[ ! -d "/opt/blog/bin" ]]; then
	cp -r /opt/BlogCloudJersey/BlogCloudServer/bin /opt/blog/bin
	chmod -R +x /opt/blog/bin/*.sh
fi
if [[ ! -d "/usr/lib/systemd/system" ]]; then
	mkdir -p /usr/lib/systemd/system
fi
if [[ ! -f "/usr/lib/systemd/system/blog.service" ]]; then
	cp /opt/blog/bin/blog.service /usr/lib/systemd/system/blog.service
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