IPTH=/usr/scramnet/inc
LIBDIR=-L/usr/scramnet/lib -L/usr/scramnet/diag -L/usr/scramnet/hwd
LIBS=-lplus -lwin -lhw -lhwd -ltst
JAVA_HOME=/usr/local/java

object:
        gcc -c -fPIC -I$(IPTH) -I$(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux/ ScramNetIO.c $(LIBDIR)
        gcc -shared -I$(IPTH) -I$(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux/ ScramNetIO.o -o scramnetio.so $(LIBDIR) $(LIBS)

clean:
        rm -rf *~