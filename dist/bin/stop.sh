#!/bin/sh
# 需要指定停止的模式是test，还是prod，默认是test，如果不指定的话默认取test
echo Try to close the app
ACTION_MODE=$1
if [ "$ACTION_MODE" = "" ]
then
	ACTION_MODE=test
fi
echo STOPPING APPLICATION ACTION_MODE:[$ACTION_MODE]
pid=`ps -ef | grep bcloud-server.jar | grep $ACTION_MODE | grep -v grep | awk '{print $2}'`
# 判断进程是否再运行 在运行则终止
if [ -n "$pid" ]
then
   kill -9 $pid
   echo Application stop success
else
   echo Application already stop
fi
