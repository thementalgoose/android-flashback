versionCodeString=`cat ./releases.json | grep versionCode | head -1`
versionCode=`echo $versionCodeString | sed 's/[^0-9]*//g'`
echo $versionCode