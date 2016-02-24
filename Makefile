JC = javac

SRCDIR = src/
JDBC = externals/sqlite-jdbc-3.8.11.2.jar
CLI = externals/commons-cli-1.3.1.jar
MKDIR = mkdir -p
PRODUCT = bin/
EXT = externals/

JDBC_DEST = ${PRODUCT}${JDBC}
CLI_DEST = ${PRODUCT}${CLI}

all: folder parser backend logic handler

handler:
	$(JC) -d ${PRODUCT} -cp ${PRODUCT}${CLI}:${PRODUCT} ${SRCDIR}Handler.java

logic:
	$(JC) -d ${PRODUCT} -cp ${PRODUCT} ${SRCDIR}LogicEngine.java

parser:
	${JC} -d ${PRODUCT} -cp ${CLI_DEST} ${SRCDIR}Parser.java

backend:
	${JC} -d ${PRODUCT} -cp ${JDBC_DEST} ${SRCDIR}BackEnd.java

folder:
	${MKDIR} ${PRODUCT}
	${MKDIR} ${PRODUCT}${EXT}
	cp -r ${JDBC} ${PRODUCT}${EXT}
	cp -r ${CLI} ${PRODUCT}${EXT}


clean:
	rm -r bin/*.class
	rm -r bin
