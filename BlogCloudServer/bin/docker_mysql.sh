#!/bin/bash

if [[ ! -z $(docker ps | grep blog_mysql) ]]; then
	echo "docker_mysql started"
elif [[ -z $(docker ps -a | grep blog_mysql) ]]; then
	echo "docker_mysql run"
	docker run --name blog_mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=1q2w3e -d mysql
else
	echo "docker_mysql starting"
	docker start blog_mysql
fi

#进入容器
#docker exec -it blog_mysql bash
