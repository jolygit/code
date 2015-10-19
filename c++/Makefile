CXX=g++ -I /usr/local/boost_1_47_0 -g -Wall -std=gnu++0x
test-x86_64 : test.o my.o
	$(CXX) -o test-x86_64 test.o my.o -I.
test.o : test.cpp my.h
	$(CXX) -c test.cpp -I.
addon.o : my.cpp my.h
	$(CXX) -c my.cpp -I.
