#!/bin/sh
#You can put definitions for commonly used functions inside your .profile so that they'll be available whenever you log in and you can use them at command prompt.

#Alternatively, you can group the definitions in a file, say test.sh, and then execute the file in the current shell by typing $. test.sh This has the effect of causing any functions defined inside test.sh to be read in and defined to the current shell 
# Calling one function from another
number_one () {
   echo "This is the first function speaking..."
   number_two
}

number_two () {
   echo "This is now the second function speaking..."
}

# Calling function one.
number_one

# remove function definition from shell
$unset .f function_name
