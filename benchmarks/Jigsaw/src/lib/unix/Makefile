
###############################################################################################
# 
# Makefile for libUnix.so and Unix*.class for Unix-specific 
# jigsaw features
#
###
#
# written by gisburn (Roland.Mainz@informatik.med.uni-giessen.de)
# created       4.2.1999
# last modified 4.2.1999
#
### 
# test configuration:
# uname -a      == SunOS puck 5.7 Generic sun4m sparc SUNW,SPARCstation-5
# java -version == Solaris VM (build Solaris_JDK_1.2_01_dev06_fcsV, native threads, sunwjit)
# gcc 2.8.1
# GNU Make version 3.77
# GNU bash, version 2.02.1(1)-release
# and so on...
#


###############################################################################################
# user paths
JNIINCLUDE=/usr/local/java1.2/include
JNIINC=-I ${JNIINCLUDE} -I ${JNIINCLUDE}/linux

## think twice before editing the stuff below...


###############################################################################################
# CC (tested with gcc 2.8.1)
CC     = gcc
CFLAGS = ${JNIINC} -fpic -O2 -Wall

###############################################################################################
# JAVA stuff
JAVAC      = javac
JAVACFLAGS = -deprecation
JAVAH      = javah
JAVAHFLAGS = -jni
###############################################################################################
# All required targets
all: ../../classes/org/w3c/util/Unix.class ../../classes/org/w3c/util/UnixException.class libUnix.so 

###############################################################################################
# single project targets...	
org_w3c_util_Unix.h: ../../classes/org/w3c/util/Unix.class
	${JAVAH} ${JAVAHFLAGS} org.w3c.util.Unix

libUnix.o: org_w3c_util_Unix.h libUnix.c
	${CC} ${CFLAGS}	-c libUnix.c 
	
libUnix.so: libUnix.o
	ld -r -o libUnix.so libUnix.o
	
../../classes/org/w3c/util/Unix.class:../../classes/org/w3c/util/Unix.java
	(cd ../../classes/org/w3c/util/ && ${JAVAC} ${JAVACFLAGS} Unix.java)
	
# this is normally build by Unix.class -> Unix.java, but...	
UnixException.class: UnixException.java
	${JAVAC} ${JAVACFLAGS} UnixException.java			

###############################################################################################
# misc. development targets	
clean: 
	-rm org_w3c_util_Unix.h libUnix.so libUnix.o	

###############################################################################################		
# done.
		
