#!/bin/bash

rm -rf /opt/Blog/Blog-1.0.jar
rm -rf /opt/Blog/www
if [[ ! -d "/opt/BlogJersey" ]]; then
	cd /opt
	git clone git@github.com:zhanghaijun666/BlogJersey.git
else
	cd /opt/BlogJersey
	git stash
	git stash clear
	git pull
fi
cd /opt/BlogJersey/BlogServer
mvn clean package
if [[ ! -d "/opt/Blog" ]]; then
	mkdir -p /opt/Blog
fi
cd /opt/Blog
ln -s /opt/BlogJersey/BlogServer/target/BlogServer-1.0-SNAPSHOT.jar /opt/Blog/Blog-1.0.jar
ln -s /opt/BlogJersey/BlogClient/www /opt/Blog/www
if [[ ! -d "/opt/Blog/config" ]]; then
	cp -r /opt/BlogJersey/BlogServer/config /opt/Blog/config
fi
if [[ ! -f "/opt/Blog/user.txt" ]]; then
	cp /opt/BlogJersey/BlogServer/user.txt /opt/Blog/user.txt
fi
if [[ ! -d "/opt/Blog/bin" ]]; then
	cp -r /opt/BlogJersey/BlogServer/bin /opt/Blog/bin
	chmod -R +x /opt/Blog/bin/*.sh
fi
if [[ ! -d "/usr/lib/systemd/system" ]]; then
	mkdir -p /usr/lib/systemd/system
fi
if [[ ! -f "/usr/lib/systemd/system/blog.service" ]]; then
	cp /opt/Blog/bin/blog.service /usr/lib/systemd/system/blog.service
	systemctl daemon-reload
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


#pkill sendmail 关闭系统发送邮件
#pkill prelink 关闭系统自检测

#修改bash提示符
#export PS1="[\u@`env| grep SSH_CONNECTION | awk '{print $(NF-1)}'` \W]\$ "