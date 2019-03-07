#! /bin/bash

if [[ -f "/opt/blog/Blog-1.0.jar" ]]; then
	cd /opt/blog
	OPTS="-Dsun.jnu.encoding=UTF-8 -Dfile.encoding=UTF-8"
	java -jar /opt/blog/Blog-1.0.jar com.server.ServerMain
fi