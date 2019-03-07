#! /bin/bash

rm -rf /opt/blog
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
ln -s /opt/BlogCloudJersey/BlogCloudServer/target/BlogCloudServer-1.0-SNAPSHOT.jar /opt/blog/Blog-1.0.jar
ln -s /opt/BlogCloudJersey/BlogCloudClient/www /opt/blog/www
ln -s /opt/BlogCloudJersey/BlogCloudServer/config /opt/blog/config
ln -s /opt/BlogCloudJersey/BlogCloudServer/user.txt /opt/blog/user.txt
ln -s /opt/BlogCloudJersey/BlogCloudServer/bin /opt/blog/bin
chmod -R +x /opt/blog/bin
if [[ ! -f "/usr/lib/systemd/system/blog.service" ]]; then
	cp /opt/blog/bin/blog.service /usr/lib/systemd/system/blog.service
fi

