include ../Make.defines

PROGS =	daytimetcpcli-x64 daytimetcpcli1-x64 daytimetcpcli2-x64 daytimetcpcli3-x64 \
		daytimetcpsrv-x64 daytimetcpsrv1-x64 daytimetcpsrv2-x64 daytimetcpsrv3-x64 \
		daytimetcpcliv6-x64 daytimetcpsrvv6-x64 \
		byteorder-x64

all:	${PROGS}

daytimetcpcli-x64:	daytimetcpcli.o
		${CC} ${CFLAGS} -o $@ daytimetcpcli.o ${LIBS}

daytimetcpcli1-x64:	daytimetcpcli1.o
		${CC} ${CFLAGS} -o $@ daytimetcpcli1.o ${LIBS}

daytimetcpcli2-x64:	daytimetcpcli2.o
		${CC} ${CFLAGS} -o $@ daytimetcpcli2.o ${LIBS}

daytimetcpcli3-x64:	daytimetcpcli3.o
		${CC} ${CFLAGS} -o $@ daytimetcpcli3.o ${LIBS}

daytimetcpsrv-x64:	daytimetcpsrv.o
		${CC} ${CFLAGS} -o $@ daytimetcpsrv.o ${LIBS}

daytimetcpsrv1-x64:	daytimetcpsrv1.o
		${CC} ${CFLAGS} -o $@ daytimetcpsrv1.o ${LIBS}

daytimetcpsrv2-x64:	daytimetcpsrv2.o
		${CC} ${CFLAGS} -o $@ daytimetcpsrv2.o ${LIBS}

daytimetcpsrv3-x64:	daytimetcpsrv3.o
		${CC} ${CFLAGS} -o $@ daytimetcpsrv3.o ${LIBS}

daytimetcpcliv6-x64:	daytimetcpcliv6.o
		${CC} ${CFLAGS} -o $@ daytimetcpcliv6.o ${LIBS}

daytimetcpsrvv6-x64:	daytimetcpsrvv6.o
		${CC} ${CFLAGS} -o $@ daytimetcpsrvv6.o ${LIBS}

byteorder-x64:	byteorder.o
		${CC} ${CFLAGS} -o $@ byteorder.o ${LIBS}

clean:
		rm -f ${PROGS} ${CLEANFILES}
