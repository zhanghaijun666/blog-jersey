#! /bin/bash

echo "export JAVA_HOME=/usr/local/tools/java_jdk" >> /etc/profile
echo "export PATH=\$PATH:\$JAVA_HOME/bin" >> /etc/profile
source /etc/profile

if [[ -f "/opt/Blog/Blog-1.0.jar" ]]; then
	cd /opt/Blog
	OPTS="-Dsun.jnu.encoding=UTF-8 -Dfile.encoding=UTF-8"
	java -jar /opt/Blog/Blog-1.0.jar com.server.ServerMain > /opt/blog-service.log
	#java -cp lib/*:/opt/Blog/Blog-1.0.jar com.server.ServerMain initdb "$@"
fi