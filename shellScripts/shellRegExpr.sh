#!/bin/bash
filename=$1
#outfile=${filename#/*/} # deletes the shortest match from the beginning of the string
#outfile=${filename##/*/} # as above but longest match
#outfile=${filename%.*} # shortest from the end of the string 
#outfile=${filename%%.*} # longest from the end
outfile=${filename//:/'\n'} # substitute new line to :
#echo -e $outfile # -e will move to new lines rather then print them
#echo ${#filename} # lenth of the variable filename in character strings
curdir=$(grep  '#!' ./shellRegExpr.sh) #command substitution, i.e left is asigned the return of the command on the right
echo -e $curdir
#sendmail=$(mail $(who | cut -d' ' -f1))
#echo sendmail
