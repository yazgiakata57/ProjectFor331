package ub.cse.algo;

import java.util.*;

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
    public ArrayList<Client> orderOfDijkstras(ArrayList<Client> arrayOfClients){
        ArrayList<Client> ret_val= new ArrayList<>();
        ret_val.sort(new possibleMaxProfit());
        return ret_val;
    }
    public ArrayList<Integer> pathOfTheNode(Graph graph, Integer from, Integer to,HashSet<Integer> removedNodes){
        return new ArrayList<>();
    }

    // bandwidthTrack maps each node to a hashmap that maps the time to the population of that node at that time
    public void updateGraph(HashMap<Integer, HashMap<Integer, Integer>> bandwidthTrack, Graph graph,ArrayList<Integer> pathOfTheNode,HashSet<Integer> removedNodes ){
        for (int i=0;i<pathOfTheNode.size();i++){
            Integer time=i;
            Integer node= pathOfTheNode.get(time);
            Integer curValOfNode=bandwidthTrack.get(node).get(time);
            Integer updatedValOfNode= curValOfNode+1;
            bandwidthTrack.get(node).put(time,updatedValOfNode);

            //find the client object of the node
            //DUMMY CODE BELOW
            Client a= clients.get(0);

            if (updatedValOfNode==a.beta){
                removedNodes.add(a.id);
            }
        }
    }
    public HashMap< Integer, ArrayList<Integer>> GENERAL(){
        ArrayList<Client> client= orderOfDijkstras(clients);
        Integer from=0;
        HashSet<Integer> removedNodes= new HashSet<>();
        HashMap<Integer,HashMap<Integer, Integer>> bandwidthTrack= new HashMap<>();
        HashMap< Integer, ArrayList<Integer>> ret_val= new HashMap<>();

        for (Client c:client){
            Integer to= c.id;

            ArrayList<Integer> path=pathOfTheNode(graph,from,to,removedNodes);
            ret_val.put(c.id,path);

            updateGraph(bandwidthTrack,graph,path,removedNodes);
        }
        return ret_val;
    }
    public SolutionObject outputPaths() {
        SolutionObject sol = new SolutionObject();
        /* TODO: Your solution goes here */
        sol.paths=GENERAL();
        return sol;
    }










    public class Comparator<T> {
        public int compare(T a, T b){
            return -1;
        }
    }
    public class possibleMaxProfit extends Comparator<Client> implements java.util.Comparator<Client> {

        @Override
        public int compare(Client o1, Client o2) {
            if(o1.alpha*o1.payment>o2.alpha*o2.payment){
                return 1;
            }else if(o1.alpha*o1.payment==o2.alpha*o2.payment){
                return 0;
            }
            return -1;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }

        @Override
        public java.util.Comparator<Client> reversed() {
            return java.util.Comparator.super.reversed();
        }
    }
}
