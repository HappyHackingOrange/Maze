# Maze
Java implement of generating random maze using recursive backtracking algorithm then solving a maze, with a GUI. 

Click the video link here for the demostration of the generation and solving a maze in two ways (breadth-first search then depth-firest search).

[![Maze generation and solver](https://img.youtube.com/vi/ObG_pypiixs/0.jpg)](https://youtu.be/ObG_pypiixs)

# Compiling and executing instructions:

1. Unzip Stowbunenko_Vincent_prog4.zip in a folder.

2. You should be in the folder containing src and bin folders. 

3. Change permission so that the java files can be written to the bin folder after compiling:

	chmod -R 755 bin

4. Type the following command to compile:

	javac -d bin src/dataStructures/*.java src/project/*.java src/*.java

5. Go to the bin folder and type the following command to run the application:

	cd bin
	java Driver

6. To read a maze from a file, type the following command if the filename is maze.txt:

	java Driver maze.txt
