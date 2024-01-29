package diver;

import game.*;
import graph.ShortestPaths;

import java.util.*;


/** This is the place for your implementation of the {@code SewerDiver}.
 */
public class McDiver implements SewerDiver {

    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        // TODO : Look for the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, scram.
        Set<Long> visited;
        visited = new HashSet<Long>();
        helper(visited, state);
        return;

    }

    /**
     * Performs Depth First Search on Collection of NodeStatus to check if Node is visited
     */
    private boolean helper(Set<Long> visited, SeekState state) {
        long currentId = state.currentLocation();
        int currentDist = state.distanceToRing();
        if (currentDist == 0) {
            return true;
        }
        visited.add(state.currentLocation());

        for (NodeStatus n : state.neighbors()) {
            long neighborId = n.getId();
            if (!visited.contains(neighborId)) {
                state.moveTo(neighborId);
                if (n.getDistanceToRing() - currentDist == 0) {
                    state.moveTo(currentId);
//                  state.neighbors().remove(currentId);
                }
                if(helper(visited, state)){
                    return true;
                }
                state.moveTo(currentId);
            }
        }
        return false;
    }


    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
//        while (!state.currentNode().equals(state.exit()) && state.stepsToGo() > 0) {
//            boolean exitReached = shortestExit(state);
//
//            if (exitReached){
//                return;
//            }
//            return;
//        }
        Set<Node> visited;
        visited = new HashSet<Node>();
        shortestExit(state, visited);
        return;
    }

    /**
     * Helper function that Sets a Maze of all the nodes from the state of the
     * ScramState, initializes a ShortestPath obj to calculate the best path to the
     * exit Node
     */
    public boolean shortestExit(ScramState state, Set<Node> visited) {
//        Set current Node
        Node currentNode = state.currentNode();
//        Set exit Node
        Node exitNode = state.exit();
//        Set Collection of Nodes from state.allNodes
        Set<Node> setNodes = (Set<Node>) state.allNodes();
//        Initialize Maze of Nodes
        Maze maze = new Maze(setNodes);
//        Initialize ShortestPath object
        ShortestPaths<Node, Edge> path = new ShortestPaths<>(maze);
//        Call singleSourceDistances to initialize the bestEdges and distances Maps and calculate best Paths
        path.singleSourceDistances(currentNode);
//        Find bestPath(List<Edge>)/smallest Weighted Edges using ShortestPath object func bestPath()
        List<Edge> shortPath = path.bestPath(exitNode);
//        Calculate current distance to the exit from the current position of McDiver
        double currentDistance = path.getDistance(exitNode);
        if (currentDistance == 0) {
            return true;
        }
//      Add the current Node to the visited Set
        visited.add(currentNode);
//        Loop through list of edges and for every edge find the nextNode using Maze.dest func
//        while stepToGo is less than the count of the edges (List<Edge>) moveTo the nextNode
//        Return
//        Loop through the Best Path List of Edges (shortPath)
        for (Edge e : shortPath) {
//          Retrieve the Next Node from the current Node
            Node nextNode = e.destination();
//          Check to see if the currentSteps minus weight is <= or >= 0
            if (state.stepsToGo() - e.length() > 0) {
//          Check to see if source node of edge is the currentNode and if the destination Node (nextNode)
//          is in the visited Set
                if (e.source().equals(currentNode) && !visited.contains(nextNode)) {
//          Move to nextNode in List<Edges> list
                    state.moveTo(nextNode);
                }
//          Recursion step
                if (shortestExit(state, visited)){
                    return true;
                }
//          Move the next to the next node after the recursion step [not sure if this is correct but will debug]
                state.moveTo(nextNode);
//          Update the currentNode to be the nextNode
                currentNode = nextNode;
            }
        }
        return false;
    }
}
