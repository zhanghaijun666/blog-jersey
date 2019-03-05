#! /bin/bash

if [[ ! -d "/opt/tools/" ]]; then
	mkdir -f /opt/tools/
fi
cd /opt/tools/
if [[ ! -d "/usr/local/maven" ]]; then
	rm -rf apache-maven-3.6.0-bin.tar.gz 
	rm -rf /opt/tools/maven
	wget http://mirror.bit.edu.cn/apache/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz
	tar -zxvf apache-maven-3.6.0-bin.tar.gz
	rm -f apache-maven-3.6.0-bin.tar.gz
	mv apache-maven-3.6.0 maven
	ln -s /opt/tools/maven /usr/local/maven
	echo "export MAVEN_HOME=/usr/local/maven" >> /etc/profile
	echo "export PATH=\$PATH:\$MAVEN_HOME/bin" >> /etc/profile
	source /etc/profile
fi
if [[ ! -d "/usr/local/node" ]]; then
	wget https://nodejs.org/dist/v10.15.2/node-v10.15.2-linux-x64.tar.gz
	tar -zxvf node-v10.15.2-linux-x64.tar.gz
	rm -rf node-v10.15.2-linux-x64.tar.gz
	mv node-v10.15.2-linux-x64 node
	ln -s /opt/tools/node /usr/local/node
	echo "export NODE_HOME=/usr/local/node" >> /etc/profile
	echo "export PATH=\$PATH:\$NODE_HOME/bin" >> /etc/profile
	source /etc/profile
fi




