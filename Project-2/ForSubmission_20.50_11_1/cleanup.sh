#!/bin/bash


# Change this to your netid
netid=kxs168430

#
# Root directory of your project
PROJDIR=$HOME/proj2

#
# This assumes your config file is named "config.txt"
# and is located in your project directory
#
CONFIG=$PROJDIR/code/config.txt

#
# Directory your java classes are in
#
BINDIR=$PROJDIR/code

#
# Your main project class
#
PROG=Node

n=1

cat $CONFIG | sed -e "s/#.*//" | sed -e "/^\s*$/d" | grep dc
(
    read i
    echo $i
    while read line 
    do
        host=$( echo $line | awk '{ print $2 }' )

        echo $host
        ssh $netid@$host killall -9 java &
	ssh $netid@$host killall -9 sshd &
        sleep 1

        n=$(( n + 1 ))
    done
   
)


echo "Cleanup complete"
