#! /bin/bash

if [[ -f "/opt/Blog/Blog-1.0.jar" ]]; then
	cd /opt/Blog
	OPTS="-Dsun.jnu.encoding=UTF-8 -Dfile.encoding=UTF-8"
	$JAVA_HOME/bin/java -jar /opt/Blog/Blog-1.0.jar com.server.ServerMain > /opt/blog-service.log
	#java -cp lib/*:/opt/Blog/Blog-1.0.jar com.server.ServerMain initdb "$@"
fi