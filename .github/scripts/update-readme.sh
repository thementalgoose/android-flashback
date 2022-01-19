#!/bin/bash
# Usage: sh update-readme.sh 8.1.123
regex="([0-9]{1,3}\.[0-9]{1,3}\.[0-9]*)"
replacement="$1"
sedQuery="s/$regex/$replacement/g"
sed -i "" -E "$sedQuery" ./README.md