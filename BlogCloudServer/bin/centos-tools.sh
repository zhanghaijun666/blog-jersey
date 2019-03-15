#!/bin/bash
# zhanghaijun

wget --help &>/dev/null
if [[ $? = 127 ]]; then
	yum -y install wget
fi
if [[ ! -f "/usr/local/tools" ]]; then
	mkdir -p /usr/local/tools
fi
cd /usr/local/tools
java -version &>/dev/null
if [[ $? = 127 ]]; then
	wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" https://download.oracle.com/otn-pub/java/jdk/8u201-b09/42970487e3af4f5aa5bca3f542482c60/jdk-8u201-linux-x64.tar.gz
	tar -zxvf jdk-8u201-linux-x64.tar.gz
	rm -f jdk-8u201-linux-x64.tar.gz
	mv jdk1.8.0_201 java_jdk
	echo "export JAVA_HOME=/usr/local/tools/java_jdk" >> /etc/profile
	echo "export PATH=\$PATH:\$JAVA_HOME/bin" >> /etc/profile
	source /etc/profile
fi
cd /usr/local/tools
mvn -version &>/dev/null
if [[ $? = 127 ]]; then
	wget http://mirror.bit.edu.cn/apache/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.tar.gz
	tar -zxvf apache-maven-3.6.0-bin.tar.gz
	rm -f apache-maven-3.6.0-bin.tar.gz
	mv apache-maven-3.6.0 maven
	echo "export MAVEN_HOME=/usr/local/tools/maven" >> /etc/profile
	echo "export PATH=\$PATH:\$MAVEN_HOME/bin" >> /etc/profile
	source /etc/profile
fi
cd /usr/local/tools
node -v &>/dev/null
if [[ $? = 127 ]]; then
	wget https://nodejs.org/dist/v10.15.2/node-v10.15.2-linux-x64.tar.gz
	tar -zxvf node-v10.15.2-linux-x64.tar.gz
	rm -rf node-v10.15.2-linux-x64.tar.gz
	mv node-v10.15.2-linux-x64 node
	echo "export NODE_HOME=/usr/local/tools/node" >> /etc/profile
	echo "export PATH=\$PATH:\$NODE_HOME/bin" >> /etc/profile
	source /etc/profile
fi
cd /usr/local/tools
protoc --version &>/dev/null
if [[ $? = 127 ]]; then
	yum -y install gcc automake autoconf libtool make
	yum -y install gcc gcc-c++
	wget https://github.com/protocolbuffers/protobuf/archive/v3.6.1.tar.gz
	tar -zxvf v3.6.1.tar.gz
	rm -rf v3.6.1.tar.gz
	mv protobuf-3.6.1 protobuf
	cd protobuf
	bash autogen.sh
	bash configure
	make
	make install
fi
cd /usr/local/tools
git --help &>/dev/null
if [[ $? = 127 ]]; then
	yum -y install git
	git config --global user.name "zhanghaijun666"
	git config --global user.email "zhanghaijun_java@163.com"
fi