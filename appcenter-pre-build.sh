GOOGLE_JSON_FILE=$APPCENTER_SOURCE_DIRECTORY/app/src/live/google-services.json

echo "Updating Google Json"
echo "$GOOGLE_JSON" > $GOOGLE_JSON_FILE
echo "Verifying JSON file contents"
cat $GOOGLE_JSON_FILE