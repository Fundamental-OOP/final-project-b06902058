JFLAGS = -cp
JC = javac
JVM= java 
.SUFFIXES: .java .class


.java.class:
	$(JC) $(JFLAGS) . -sourcepath src -d out/ src/*.java


CLASSES = src/Main.java 

MAIN = src/Main

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(JFLAGS) out/ Main


