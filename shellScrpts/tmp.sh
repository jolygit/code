#!/usr/bin/bash
filename="$1"
while read -r l
do
    name=$l
    echo "Name read from file - $name"
done < "$filename"
