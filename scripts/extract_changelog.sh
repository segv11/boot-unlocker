#!/bin/sh

(

#
# Extract git tags (in reverse order)
# Get the text of the tags
#
git tag -l | sort -r | xargs -n 1 git tag -l -n100 | sed -e 's/^c0[^=]*===/\
===/;s/^ *//' 
#

# Convert from mediawiki (needed because git drops #'s) to goole wiki
# TODO: need googlecodewiki pandoc writer
) | pandoc -f mediawiki -t dokuwiki | sed -e "
s|//|_|g
s/====/===/g
s/[A-Z][a-z][a-z]*[A-Z][a-zA-Z]*/!&/g
"

