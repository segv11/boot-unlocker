#!/bin/sh
#
# push updates to the documentation wiki
# do this AFTER a commit and push to the main repo
#
# https://github.com/apenwarr/git-subtree/blob/master/git-subtree.txt
#
git subtree push --prefix app/assets/wiki/ https://code.google.com/p/boot-unlocker-gnex.wiki/ master 
