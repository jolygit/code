#!/bin/bash
for i in $(cut -f 1,3 -d: /etc/passwd) ; do
    array[${i#*:}]=${i%:*}
done
echo "User ID $1 is ${array[$1]}."
echo "${#array[@]} users in the system"
echo $$
