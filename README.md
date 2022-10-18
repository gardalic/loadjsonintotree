# City Exercise

## Description

The solution takes a JSON file as a source for the city data, parses it and constructs a tree out of the available data. The hierarchy is constucted using a column list that can be changed, and the file can be passed to it from the command line.

## Classes - short description

1. `LoadCities`
    * main class that runs the loading in which the root node, column list and the JSON file is defined
2. `LoadCitiesFromFile`
    * interface for the file loading class
3. `LoadCitiesFromJson`
    * parses the JSON file, constructs the list of nodes according to the column list provided, and then calls the addition method of the location service
4. `LocationService`
    * service class that is in charge of constructing the hierarchy
    * `addDistinctNodes` checks if the city (the last element of the passed list) is already added, and calls the function to process the node list if not
    * `addNodesFromList` is a recursive function that checks if each node in the list is valid, and constructs it from the bottom up if it is. It *does not* add nodes unless all nodes in the list are valid, and it doesn't add nodes multiple times, so each node is logged once
5. `Location`
    * basic class representing a node in the hierarchy
