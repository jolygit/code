PROGS = clientprocessorobj
all:	${PROGS}
databaseobj:
	g++ -g database.cpp -c -o ../obj/database.o -I../include -I ../mongo-c-driver/src/mongoc/ -I ../mongo-c-driver/src/libbson/src/bson/ -g  -D_REENTRANT -Wall  
serverprocessorobj:
	g++ -g serverProcessor.cpp -c -o ../obj/serverProcessor.o -I../include -I ../mongo-c-driver/src/mongoc/ -I ../mongo-c-driver/src/libbson/src/bson/ -g  -D_REENTRANT -Wall  

clientprocessorobj:
	g++ -g clientProcessor.cpp -c -o ../obj/clientProcessor.o -I /opt/local/include -I../include  -g  -D_REENTRANT -Wall  ../libunp.a -lpthread



