package ub.cse.algo;

import java.util.ArrayList;
import java.util.HashMap;

import static ub.cse.algo.Traversals.bfs;
import static ub.cse.algo.Traversals.bfsPaths;

public class Solution {

    private Info info;
    private Graph graph;
    private ArrayList<Client> clients;
    private ArrayList<Integer> bandwidths;

    /**
     * Basic Constructor
     *
     * @param info: data parsed from input file
     */
    public Solution(Info info) {
        this.info = info;
        this.graph = info.graph;
        this.clients = info.clients;
        this.bandwidths = info.bandwidths;
    }

    /**
     * Method that returns the calculated 
     * SolutionObject as found by your algorithm
     *
     * @return SolutionObject containing the paths, priorities and bandwidths
     */
    public SolutionObject outputPaths() {
        SolutionObject sol = new SolutionObject();
        /* TODO: Your solution goes here */
        HashMap< Integer, ArrayList<Integer>> bfsPaths = bfsPaths(graph, clients);
        // in the for loop check if D(P'c)>alpha_c*d(c) where:
        // D(P'c) is the path we return
        // alpha_c is patience of client
        // d(c) is the length of the shortest path

        // POTENTIAL_HOLE_IN_THINKING (PHIT): What if there is another path that would meet these constraints but is not the shortest path?

        for (Client c: clients){
            int length = bfsPaths.get(c).size();
            if (length>c.alpha*length){
                bfsPaths.remove(c);
            }
        }
        sol.paths=bfsPaths;
        return sol;
    }
}
