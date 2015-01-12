#!/bin/sh

#
# Changelog entries are stored in the git tags, in MediaWiki format
# (I didn't use Markdown, because git eats the '#' needed for headings)
#

#
# Parameters
#
cd /Users/jmason888/git/BootUnlocker
baseDir=/Users/jmason888/git/BootUnlocker
outputDir=build/docs/xda
outputFile=$outputDir/ChangeLog.xda.txt

cd $baseDir

(
  #
  # Extract git tags (in reverse order)
  # Get the text of the tags
  #
  git tag -l | sort -r | xargs -n 1 git tag -l -n100 | sed -e 's/^c0[^=]*===/\
===/;s/^ *//' 
  #

) | \
# Convert from MediaWiki to XDA BBcode
pandoc -f mediawiki -t ./scripts/XDAcode.lua >> $outputFile
