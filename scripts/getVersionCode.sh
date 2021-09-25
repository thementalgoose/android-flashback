content=`cat ../releases.json`
versionCodeString=`echo $content | grep versionCode | tail -1`
versionCode=`echo "${${versionCodeString}//[^0-9]/}"`
echo $versionCode