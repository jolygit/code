include ../Make.defines
IDIR =../include
ODIR=../obj
_DEPS = unp.h
DEPS = $(patsubst %,$(IDIR)/%,$(_DEPS))

_OBJ = server.o client.o
OBJ = $(patsubst %,$(ODIR)/%,$(_OBJ))
PROGS =	server-x64 client-x64 test-x64

all:	${PROGS}

$(ODIR)/%.o: %.cpp $(DEPS) 
	$(CC) -c -o $@ $< $(CFLAGS) 

server-x64: $(OBJ)
	$(CC) -o $@ $(ODIR)/server.o $(ODIR)/database.o $(ODIR)/processor.o $(CFLAGS)  ${LIBS}
client-x64: $(OBJ)
	$(CC) -o $@ $(ODIR)/client.o $(CFLAGS)  ${LIBS}
test-x64:
	gcc -g test.c -o test-x64 $(CFLAGS)  ${LIBS}
.PHONY: clean

clean:
	rm -f $(ODIR)/*.o *~ core $(INCDIR)/*~ ${PROGS}
