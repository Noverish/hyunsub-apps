PID=`ps -ef | grep java | grep hyunsub-video | awk '{print $2}'`
if [ ! -z $PID ]; then
  echo "* kill $PID"
  kill -9 $PID
fi

nohup java -Dspring.profiles.active=prod -jar hyunsub-video.jar > nohup.out 2>&1 < /dev/null &
echo "* new $!"
