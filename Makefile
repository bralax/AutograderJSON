all: Jsonify.class

%.class: %.java
	javac -classpath json-simple-1.1.jar:. $<
run:
	java -classpath json-simple-1.1.jar:. Jsonify $(json)
clean:
	rm AutograderMain.java *.class
