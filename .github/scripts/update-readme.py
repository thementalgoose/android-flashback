import sys
import re

if len(sys.argv) != 2:
    print("Argument for version number not specified. Please call via. python update-readme.py <version>")
    exit(1)

filename = "README.md"

print("Updating README with '" + sys.argv[1] + "'")

with open(filename, 'r+') as f:
    text = f.read()
    text = re.sub('([0-9]{1,3}\.[0-9]{1,3}\.[0-9]*)', sys.argv[1], text)
    f.seek(0)
    f.write(text)
    f.truncate()

print("README updated")