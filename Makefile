# NOTE: though parentheses and braces are functionally equivalent, braces will be used exclusively
#     for directories, and parentheses everything else for document standardization

JC = javac

SRCDIR = src/
JDBC = externals/sqlite-jdbc-3.8.11.2.jar
CLI = externals/commons-cli-1.3.1.jar
MKDIR = mkdir -p
CLSDIR = bin/
EXT = externals/
CLSS = $(addprefix ${CLSDIR},Parser.class BackEnd.class LogicEngine.class TCPHandler.class \
        TCPThreadedServer.class UDPHandler.class Handler.class)
SRCS = $(addprefix ${SRCDIR},Parser.java BackEnd.java LogicEngine.java TCPHandler.java \
        TCPThreadedServer.java UDPHandler.java Handler.java)

JDBC_DEST = ${CLSDIR}${JDBC}
CLI_DEST = ${CLSDIR}${CLI}

.PHONY: all clean

# TODO: make is compiling everything twice before excluding them if no change was made
all: ${CLSDIR} $(CLSS) | ${CLSDIR}

${CLSDIR}TCPHandler.class: ${CLSDIR} $(addprefix ${SRCDIR},TCPHandler.java LogicEngine.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLSDIR} ${SRCDIR}TCPHandler.java

${CLSDIR}TCPThreadedServer.class: ${CLSDIR} $(addprefix ${SRCDIR},TCPThreadedServer.java TCPHandler.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLSDIR} ${SRCDIR}TCPThreadedServer.java

${CLSDIR}UDPHandler.class: ${CLSDIR} $(addprefix ${SRCDIR},UDPHandler.java LogicEngine.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLSDIR} ${SRCDIR}UDPHandler.java

${CLSDIR}Handler.class: ${CLSDIR} $(addprefix ${SRCDIR},Handler.java Parser.java BackEnd.java TCPThreadedServer.java UDPHandler.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLI_DEST}:${CLSDIR} ${SRCDIR}Handler.java

${CLSDIR}LogicEngine.class: ${CLSDIR} $(addprefix ${SRCDIR},LogicEngine.java BackEnd.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLSDIR} ${SRCDIR}LogicEngine.java

${CLSDIR}Parser.class: ${CLSDIR} ${SRCDIR}Parser.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLI_DEST} ${SRCDIR}Parser.java

${CLSDIR}BackEnd.class: ${CLSDIR} ${SRCDIR}BackEnd.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${JDBC_DEST} ${SRCDIR}BackEnd.java

${CLSDIR}:
	${MKDIR} ${CLSDIR}${EXT}
	cp -r ${JDBC} ${CLSDIR}${EXT}
	cp -r ${CLI} ${CLSDIR}${EXT}

clean:
	rm -rf ${CLSDIR}
