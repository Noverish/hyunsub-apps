PID=`ps -ef | grep java | grep hyunsub-auth | grep version | awk '{print $2}'`
if [ ! -z $PID ]; then
  echo "* kill $PID"
  kill -9 $PID
fi

# // TODO version은 기존 auth와 grep 에서 구분하기 위해서 넣었음. 마이그레이션이 완료되면 제거해야 함
nohup java -Dspring.profiles.active=prod -Dversion=2 -jar hyunsub-auth.jar > nohup.out 2>&1 < /dev/null &
echo "* new $!"
