include ../Make.defines
IDIR =../include
ODIR=../obj
_DEPS = unp.h
DEPS = $(patsubst %,$(IDIR)/%,$(_DEPS))

_OBJ = server.o 
OBJ = $(patsubst %,$(ODIR)/%,$(_OBJ))
PROGS =	server-x64 

all:	${PROGS}

$(ODIR)/%.o: %.cpp $(DEPS) 
	$(CC) -c -o $@ $< $(CFLAGS) 

server-x64: $(OBJ)
	$(CC) -o $@ $^ $(CFLAGS)  ${LIBS}

.PHONY: clean

clean:
	rm -f $(ODIR)/*.o *~ core $(INCDIR)/*~ ${PROGS}
