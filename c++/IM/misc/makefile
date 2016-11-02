PROGS =	databaseobj serverprocessorobj clientprocessorobj
all:	${PROGS}
databaseobj:
	g++ -g database.cpp -c -o ../obj/database.o -I../include -I ../mongo-c-driver/src/mongoc/ -I ../mongo-c-driver/src/libbson/src/bson/ -g  -D_REENTRANT -Wall  ../libunp.a -lpthread -L ../mongo-c-driver/ -lmongoc-1.0 -L ../mongo-c-driver/src/libbson/ -lbson-1.0
serverprocessorobj:
	g++ -g serverProcessor.cpp -c -o ../obj/serverProcessor.o -I../include -I ../mongo-c-driver/src/mongoc/ -I ../mongo-c-driver/src/libbson/src/bson/ -g  -D_REENTRANT -Wall  ../libunp.a -lpthread -L ../mongo-c-driver/ -lmongoc-1.0 -L ../mongo-c-driver/src/libbson/ -lbson-1.0

clientprocessorobj:
	g++ -g clientProcessor.cpp -c -o ../obj/clientProcessor.o -I../include -I ../mongo-c-driver/src/mongoc/ -I ../mongo-c-driver/src/libbson/src/bson/ -g  -D_REENTRANT -Wall  ../libunp.a -lpthread -L ../mongo-c-driver/ -lmongoc-1.0 -L ../mongo-c-driver/src/libbson/ -lbson-1.0


