include ../Make.defines
PROGS = clientprocessorobj  soundProcessorobj
all:	${PROGS}
databaseobj:
	$(CC) database.cpp -c -o ../obj/database.o -I../include -I ../mongo-c-driver/src/mongoc/ -I ../mongo-c-driver/src/libbson/src/bson/ -g  -D_REENTRANT -Wall  
serverprocessorobj:
	$(CC) serverProcessor.cpp -c -o ../obj/serverProcessor.o -I../include -I ../mongo-c-driver/src/mongoc/ -I ../mongo-c-driver/src/libbson/src/bson/ -g  -D_REENTRANT -Wall  

clientprocessorobj:
	$(CC) clientProcessor.cpp -c -o ../obj/clientProcessor.o -I /opt/local/include -I../include  -g  -D_REENTRANT -Wall  ../libunp.a -lpthread

soundProcessorobj:
	$(CC)  soundProcessor.cpp -c -o ../obj/soundProcessor.o -I../include `pkg-config --cflags --libs alsa`
