# NOTE: though parentheses and braces are functionally equivalent, braces will be used exclusively
#     for directories, and parentheses everything else for document standardization

JC = javac

SRCDIR = src/
JDBC = externals/sqlite-jdbc-3.8.11.2.jar
CLI = externals/commons-cli-1.3.1.jar
MKDIR = mkdir -p
CLSDIR = bin/
EXT = externals/
CLIENTDIR = client/
SERVERDIR = server/
CLSS = $(addprefix ${CLSDIR}${SERVERDIR},Parser.class BackEnd.class LogicEngine.class TCPHandler.class \
        TCPThreadedServer.class UDPHandler.class Handler.class)

JDBC_DEST = ${CLSDIR}${JDBC}
CLI_DEST = ${CLSDIR}${CLI}

.PHONY: all clean

all: ${CLSDIR} $(CLSS) | ${CLSDIR}

${CLSDIR}${SERVERDIR}TCPHandler.class: ${CLSDIR} $(addprefix ${SRCDIR}${SERVERDIR},TCPHandler.java LogicEngine.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${SRCDIR} ${SRCDIR}${SERVERDIR}TCPHandler.java

${CLSDIR}${SERVERDIR}TCPThreadedServer.class: ${CLSDIR} $(addprefix ${SRCDIR}${SERVERDIR},TCPThreadedServer.java TCPHandler.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${SRCDIR} ${SRCDIR}${SERVERDIR}TCPThreadedServer.java

${CLSDIR}${SERVERDIR}UDPHandler.class: ${CLSDIR} $(addprefix ${SRCDIR}${SERVERDIR},UDPHandler.java LogicEngine.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${SRCDIR} ${SRCDIR}${SERVERDIR}UDPHandler.java

${CLSDIR}${SERVERDIR}Handler.class: ${CLSDIR} $(addprefix ${SRCDIR}${SERVERDIR},Handler.java Parser.java BackEnd.java TCPThreadedServer.java UDPHandler.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLI_DEST}:${SRCDIR} ${SRCDIR}${SERVERDIR}Handler.java

${CLSDIR}${SERVERDIR}LogicEngine.class: ${CLSDIR} $(addprefix ${SRCDIR}${SERVERDIR},LogicEngine.java BackEnd.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${SRCDIR} ${SRCDIR}${SERVERDIR}LogicEngine.java

${CLSDIR}${SERVERDIR}Parser.class: ${CLSDIR} ${SRCDIR}${SERVERDIR}Parser.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLI_DEST} ${SRCDIR}${SERVERDIR}Parser.java

${CLSDIR}${SERVERDIR}BackEnd.class: ${CLSDIR} ${SRCDIR}${SERVERDIR}BackEnd.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${JDBC_DEST} ${SRCDIR}${SERVERDIR}BackEnd.java

${CLSDIR}:
	${MKDIR} ${CLSDIR}${EXT}
	${MKDIR} ${CLSDIR}${CLIENTDIR}
	${MKDIR} ${CLSDIR}${SERVERDIR}
	cp -r ${JDBC} ${CLSDIR}${EXT}
	cp -r ${CLI} ${CLSDIR}${EXT}

clean:
	${RM}r ${CLSDIR} *.db
