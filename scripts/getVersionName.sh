versionNameString=`cat ./releases.json | grep versionName | tail -1`
versionName=`echo "${versionNameString//[^0-9.]/}"`
echo $versionName