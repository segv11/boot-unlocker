#!/bin/sh

(

# Get the commit logs from the current point back to the last tag
# Look for leading asterisks to mark changelog lines
recentTag=`git describe`
newStuff=`git log '--pretty=tformat:%H %cd:%n%B' $recentTag..HEAD | grep '^ *\*' | sed -e 's/^ *//'`

echo "=== Current Version: ==="
if test -z "$newStuff"; then
    echo "* No changes"
else
    echo "$newStuff"
fi
echo ""

# Convert from mediawiki (needed because git drops #'s) to markdown
) #| pandoc -f mediawiki -t markdown

