# NOTE: though parentheses and braces are functionally equivalent, braces will be used exclusively
#     for directories, and parentheses everything else for document standardization

JC = javac

SRCDIR = src/
JDBC = externals/sqlite-jdbc-3.8.11.2.jar
CLI = externals/commons-cli-1.3.1.jar
CORE = externals/httpcore-4.2.5.jar
MKDIR = mkdir -p
CLSDIR = bin/
EXT = externals/
CLIENTDIR = client/
SERVERDIR = server/
ASN1ODIR = asn1objects/
ASN1DIR = net/ddp2p/ASN1/
DATATYPEDIR = datatypes/
CLSS = $(addprefix ${CLSDIR}${SERVERDIR},Parser.class BackEnd.class TCPHandler.class \
        TCPThreadedServer.class UDPHandler.class Handler.class ServerDecoder.class)

JDBC_DEST = ${CLSDIR}${JDBC}
CLI_DEST = ${CLSDIR}${CLI}
CORE_DEST = ${CLSDIR}${CORE}

.PHONY: all asn1 client datatypes asn1objects clean

all: ${CLSDIR} asn1 datatypes asn1objects $(CLSS) client | ${CLSDIR}

${CLSDIR}${SERVERDIR}TCPHandler.class: ${CLSDIR} asn1 $(addprefix ${SRCDIR}${SERVERDIR},TCPHandler.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CORE_DEST}:${SRCDIR} ${SRCDIR}${SERVERDIR}TCPHandler.java

${CLSDIR}${SERVERDIR}TCPThreadedServer.class: ${CLSDIR} asn1 $(addprefix ${SRCDIR}${SERVERDIR},TCPThreadedServer.java TCPHandler.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CORE_DEST}:${SRCDIR} ${SRCDIR}${SERVERDIR}TCPThreadedServer.java

${CLSDIR}${SERVERDIR}UDPHandler.class: ${CLSDIR} asn1 $(addprefix ${SRCDIR}${SERVERDIR},UDPHandler.java ServerDecoder.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CORE_DEST}:${SRCDIR} ${SRCDIR}${SERVERDIR}UDPHandler.java

${CLSDIR}${SERVERDIR}Handler.class: ${CLSDIR} asn1 $(addprefix ${SRCDIR}${SERVERDIR},Handler.java Parser.java BackEnd.java TCPThreadedServer.java UDPHandler.java) | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CORE_DEST}:${CLI_DEST}:${SRCDIR} ${SRCDIR}${SERVERDIR}Handler.java

${CLSDIR}${SERVERDIR}Parser.class: ${CLSDIR} ${SRCDIR}${SERVERDIR}Parser.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CLI_DEST} ${SRCDIR}${SERVERDIR}Parser.java

${CLSDIR}${SERVERDIR}BackEnd.class: ${CLSDIR} ${SRCDIR}${SERVERDIR}BackEnd.java | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${JDBC_DEST} ${SRCDIR}${SERVERDIR}BackEnd.java

${CLSDIR}${SERVERDIR}ServerDecoder.class: ${CLSDIR} asn1objects datatypes | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CORE_DEST}:${SRCDIR} ${SRCDIR}${SERVERDIR}ServerDecoder.java

asn1: ${CLSDIR} | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${SRCDIR} ${SRCDIR}${ASN1DIR}ASN1_Util.java ${SRCDIR}${ASN1DIR}ASN1DecoderFail.java \
		${SRCDIR}${ASN1DIR}ASNLenRuntimeException.java ${SRCDIR}${ASN1DIR}ASNObj.java \
		${SRCDIR}${ASN1DIR}ASNObjArrayable.java ${SRCDIR}${ASN1DIR}Decoder.java ${SRCDIR}${ASN1DIR}Encoder.java

asn1objects: ${CLSDIR} asn1 | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${SRCDIR} ${SRCDIR}${ASN1ODIR}ASN1GetProject.java \
		${SRCDIR}${ASN1ODIR}ASN1GetProjects.java ${SRCDIR}${ASN1ODIR}ASN1GetProjectsUnabridged.java \
		${SRCDIR}${ASN1ODIR}ASN1Project.java ${SRCDIR}${ASN1ODIR}ASN1ProjectOK.java \
		${SRCDIR}${ASN1ODIR}ASN1Projects.java ${SRCDIR}${ASN1ODIR}ASN1ProjectsAnswer.java \
		${SRCDIR}${ASN1ODIR}ASN1Take.java ${SRCDIR}${ASN1ODIR}ASN1Task.java

datatypes: ${CLSDIR} | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${SRCDIR} ${SRCDIR}${DATATYPEDIR}GetProject.java ${SRCDIR}${DATATYPEDIR}Project.java \
		${SRCDIR}${DATATYPEDIR}ProjectOK.java ${SRCDIR}${DATATYPEDIR}Projects.java \
		${SRCDIR}${DATATYPEDIR}ProjectsAnswer.java ${SRCDIR}${DATATYPEDIR}Take.java \
		${SRCDIR}${DATATYPEDIR}Task.java

client: ${CLSDIR} asn1objects datatypes | ${CLSDIR}
	$(JC) -d ${CLSDIR} -cp ${CORE_DEST}:${SRCDIR} ${SRCDIR}${CLIENTDIR}Client.java ${SRCDIR}${CLIENTDIR}ClientParser.java

${CLSDIR}:
	${MKDIR} ${CLSDIR}${EXT}
	${MKDIR} ${CLSDIR}${CLIENTDIR}
	${MKDIR} ${CLSDIR}${SERVERDIR}
	cp -r ${JDBC} ${CLSDIR}${EXT}
	cp -r ${CLI} ${CLSDIR}${EXT}
	cp -r ${CORE} ${CLSDIR}${EXT}

clean:
	${RM}r ${CLSDIR} *.db
