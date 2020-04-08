#!/bin/sh
# 常量定义
export BIN_PATH=$(cd `dirname $0`;pwd)
echo BIN_PATH:[$BIN_PATH]
cd $BIN_PATH
cd ..
export CONTEXT_PATH=`pwd`
echo CONTEXT_PATH:[$CONTEXT_PATH]
export LOG_PATH=$CONTEXT_PATH/logs
echo LOG_PATH:[$LOG_PATH]
# 需要指定启动的模式是test，还是prod，默认是test，如果不指定的话
ACTION_MODE=$1
if [ "$ACTION_MODE" = "" ]
then
	ACTION_MODE=test
fi
# 是否持续输出日志
SHOW_LOG=$2
if [ "$SHOW_LOG" = "" ]
then
	SHOW_LOG=no
else
  SHOW_LOG=yes
fi
# 判断log文件夹是否存在 不存在则创建
if [ ! -d $LOG_PATH ]; then
  mkdir -p $LOG_PATH
fi
# 删除历史的server.log文件
rm -f $LOG_PATH/server.log
# 停止正在运行的进程
sh $BIN_PATH/stop.sh $ACTION_MODE
# 后台启动应用 并输出控制台日志
echo STARTING APPLICATION ACTION_MODE:[$ACTION_MODE]
# 防止由于缓存问题导致的java指令读取不到
source /etc/profile
java -version
nohup java -jar bcloud-server.jar --spring.profiles.active=$ACTION_MODE >> $LOG_PATH/server-console.log 2>&1 &
# 帮助信息
if [ "$SHOW_LOG" = "yes" ]
then
  echo [if you want stop app, can run] sh $BIN_PATH/stop.sh $ACTION_MODE
	tail -f $LOG_PATH/server.log
else
  echo [if you want stop app, can run] sh $BIN_PATH/stop.sh $ACTION_MODE
  echo [if you want search logs, can run] tail -f $LOG_PATH/server.log
fi
