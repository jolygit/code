#!/bin/bash
 
echo -e "Hi, please type the word: \c "
read  w
echo "The word you entered is: $w"
echo -e "Can you please enter two words? "
read w1 w2
echo "Here is your input: \"$w1\" \"$w2\""
echo -e "How do you feel about bash scripting? "
# read command now stores a reply into the default build-in variable $REPLY
read
echo "You said $REPLY, I'm glad to hear that! "
echo -e "What are your favorite colours ? "
# -a makes read command to read into an array
read -a colours
echo "My favorite colours are also ${colours[0]}, ${colours[1]} and ${colours[2]}:-)" 

