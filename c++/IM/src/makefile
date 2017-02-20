include ../Make.defines
IDIR =../include
ODIR=../obj
_DEPS = unp.h
DEPS = $(patsubst %,$(IDIR)/%,$(_DEPS))

_OBJ = client.o 
OBJ = $(patsubst %,$(ODIR)/%,$(_OBJ))
PROGS =	client-x64 

all:	${PROGS}

$(ODIR)/%.o: %.cpp $(DEPS) 
	$(CC) -c -o $@ $< $(CFLAGS) 

server-x64: $(OBJ)
	$(CC) -o $@ $(ODIR)/server.o $(ODIR)/database.o $(ODIR)/serverProcessor.o $(CFLAGS) $(SFLAGS)  ${LIBS} ${SLIBS} 
client-x64: $(OBJ)
	$(CC) -o $@ $(ODIR)/client.o $(ODIR)/clientProcessor.o $(ODIR)/soundProcessor.o $(CFLAGS)  ${LIBS} 
test-x64:
	gcc -g test.c -o test-x64 $(CFLAGS)  ${LIBS}
soundTest-x64:
	g++ -g soundTest.cpp $(ODIR)/soundProcessor.o -o $@ -I ../include `pkg-config --cflags --libs alsa` 
.PHONY: clean

clean:
	rm -f $(ODIR)/*.o *~ core $(INCDIR)/*~ ${PROGS}
