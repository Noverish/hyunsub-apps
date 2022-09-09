PID=`ps -ef | grep java | grep hyunsub-auth | awk '{print $2}'`
if [ ! -z $PID ]; then
  echo "* kill $PID"
  kill -9 $PID
fi

nohup java -Dspring.profiles.active=prod -jar hyunsub-auth.jar > nohup.out 2>&1 < /dev/null &
echo "* new $!"
