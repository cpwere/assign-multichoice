# assign-multichoice
assigment task

Instructions on how to run the java application for the A* search algorithm path-finding. 
===========================================================================================

Note: This program was created using Eclipse IDE , this project was purely done as an assigment task. 

1. The project name is "routefinder" - this is the root context folder containing all the source and related files (txt, .properties, etc)

2. The project is mainly based on reading an input .txt file that has content representing a terrain layout (tiles)

3. The algorithm works out the best-path from the Start tile '@' to the destination tile 'X' and prints a route using '#' along the path

4. Tile value per tile type is stored in the .properties file

5. The result file 'large_map.txt' will be generated and saved in the root context folder of the project. 

6. Running the program: 

  - Locate the run the java class RouteFinderApp.java , run the main method 
  - result file 'large_map.txt' will be generated 
  - output can also be verified by uncommenting the line (below) in 'RouteFinderApp.java'  file of code to also print to System.out  
 
	public static void main(String[] args) {
		.
		.
		.	
		//TODO: to printout to System.out un-comment line below
		//finder.printTerrainLayout();
		
		
7. Authors: Siphiwe Madi
