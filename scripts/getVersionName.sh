versionNameString=`cat ./releases.json | grep versionName | tail -1`
versionName=`echo $versionNameString | sed 's/[^0-9.]*//g'`
echo $versionName