#! /bin/bash

if [[ ! -d "/opt/BlogCloudJersey" ]]; then
	cd /opt
	git clone git@github.com:zhanghaijun666/BlogCloudJersey.git
else
	cd /opt/BlogCloudJersey
	git pull
fi
cd /opt/BlogCloudJersey/BlogCloudServer
mvn clean package
if [[ ! -d "/opt/blog" ]]; then
	mkdir /opt/blog
fi
cd /opt/blog
rm -rf ./*
cp /opt/BlogCloudJersey/BlogCloudServer/target/BlogCloudServer-1.0-SNAPSHOT.jar /opt/blog/Blog-1.0.jar
cp -r /opt/BlogCloudJersey/BlogCloudClient/www /opt/blog/www
cp -r /opt/BlogCloudJersey/BlogCloudServer/config /opt/blog/config
cp -r /opt/BlogCloudJersey/BlogCloudServer/user.txt /opt/blog/user.txt
cp -r /opt/BlogCloudJersey/BlogCloudServer/bin /opt/blog/bin
chmod -R +x /opt/blog/bin

