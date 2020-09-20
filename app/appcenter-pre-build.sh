#!/usr/bin/env bash

GOOGLE_JSON_FILE=$APPCENTER_SOURCE_DIRECTORY/app/src/live/google-services.json

echo "Updating Google Json"
touch $GOOGLE_JSON_FILE
echo "$GOOGLE_JSON" > $GOOGLE_JSON_FILE
echo "Verifying JSON file contents"
cat $GOOGLE_JSON_FILE