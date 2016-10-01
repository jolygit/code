CXX=gcc -lrt -g -Wall -std=gnu++0x
tmp-x86_64 : tmp.o
	 $(CXX) -o tmp-x86_64 tmp.o -I.
tmp.o : tmp.cpp
	 $(CXX) -c tmp.cpp -I.
