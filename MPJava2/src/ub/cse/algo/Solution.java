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
    public boolean reachedQuota(HashMap<Integer, HashMap<Integer,Integer>> nodePopulation,Integer neighbor, int time){
        HashMap<Integer,Integer> timetable= nodePopulation.get(neighbor);
        Integer populationAtGivenTime= timetable.get(time);
        if (populationAtGivenTime>=bandwidths.get(neighbor)){
            return true;
        }
        return false;
    }
    //Dijkstras
    public ArrayList<Integer> pathOfTheNode(Graph graph, Integer from, Integer to,HashMap<Integer, Integer> removedNodes, HashMap<Integer, HashMap<Integer,Integer>> nodePopulation, HashMap<Integer,ArrayList<Integer>>paths){
        //from is client id and finding the node with the exact client id is rough
        HashSet<Integer> explored = new HashSet<>();
        HashMap<Integer, Integer> path = new HashMap<>();

        node2Comp comp = new node2Comp();
        PriorityQueue<Node2> todo = new PriorityQueue<>((java.util.Comparator) comp);
        int bwdith = bandwidths.get(from);
        todo.add(new Node2(from, bwdith,from));
        int time=0;
        while (!todo.isEmpty()){
            Node2 node = todo.poll();
            Integer currentClient = node.clientID;
            if (!explored.contains(currentClient)){
                //SET PART
                explored.add(currentClient);
                path.put(currentClient, node.prev);
                time++;

                //NOW ADD TO THE PRIORITY QUEUE
                ArrayList<Integer> neighbors = graph.get(currentClient);
                // QUESTION: DO YOU HAVE TO CONSIDER EACH NEIGHBOR
                for (Integer neighbor: neighbors){
                    int nextBWidth = bandwidths.get(neighbor) + node.totalBandwidth;
                    // IMPORTANTE!!!!! :    IF NEIGHBOR HAS NOT REACHED ITS CAPACITY AT THE GIVEN TIME ADD IT TO THE QUEUE... OTHERWISE, DONT BOTHER.
                    if (!reachedQuota(nodePopulation,neighbor,time)){
                        Node2 newNode = new Node2(neighbor, nextBWidth, currentClient);
                        todo.add(newNode);
                    }
                }

            }

        }

        if (path.containsKey(to)){
            ArrayList<Integer> p = new ArrayList<>();
            int cur = to;
            while (cur != graph.contentProvider){
                p.add(0,cur);
                int prev = path.get(cur);
                cur = prev;
                if (paths.containsKey(cur)){
                    p.addAll(0,paths.get(cur));
                    return p;
                }
            }
            return p;
        }
        return new ArrayList<>();
    }

    // bandwidthTrack maps each node to a hashmap that maps the time to the population of that node at that time
    public void updatePopulation(HashMap<Integer, HashMap<Integer, Integer>> nodePopulation, ArrayList<Integer> pathOfTheNode ){

        for (int i=0; i< pathOfTheNode.size();i++){
            Integer currentNode=pathOfTheNode.get(i);
            int time=i;
            Integer currentPopulationOfNode=nodePopulation.get(currentNode).getOrDefault(time,0);
            nodePopulation.get(currentNode).put(time, currentPopulationOfNode+1);
        }
    }
    public HashMap< Integer, ArrayList<Integer>> GENERAL(){
        ArrayList<Client> client= orderOfDijkstras(clients);
        Integer from= graph.contentProvider;
        HashSet<Integer> removedNodes= new HashSet<>();
        HashMap<Integer,HashMap<Integer, Integer>> nodePopulation= new HashMap<>();
        HashMap< Integer, ArrayList<Integer>> ret_val= new HashMap<>();

        for (Client c:client){
            Integer to=c.id;

            ArrayList<Integer> path=pathOfTheNode(graph,from,to,null,null);
            ret_val.put(c.id,path);

            updatePopulation(nodePopulation,path);
        }
        return ret_val;
    }
    public SolutionObject outputPaths() {
        SolutionObject sol = new SolutionObject();
        /* TODO: Your solution goes here */
        sol.paths=GENERAL();
        return sol;
    }








//COMPARATORS

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
    }
    public static class Node2 {
        public int clientID;
        public int totalBandwidth;
        public int prev;
        public Node2(int clientID, int bwidth, int prev){
            this.clientID = clientID;
            this.totalBandwidth = bwidth;
            this.prev = prev;
        }
    }

    public class node2Comp extends Comparator<Node2> {
        @Override
        public int compare (Node2 n1, Node2 n2){
            return Integer.compare(n2.totalBandwidth, n1.totalBandwidth);
        }
    }

//    public class PQComparator extends Comparator<Client> implements java.util.Comparator<Client>{
//        @Override
//        public int compare (Client c1, Client c2){
//            Integer c1_bandwidth=bandwidths.get(c1.id); // takes O(1) time gets val from ArrayList
//            Integer c2_bandwidth=bandwidths.get(c2.id); // takes O(1) time gets val from ArrayList
//            if(c1_bandwidth>c2_bandwidth){
//                return 1;
//            }else if(c1_bandwidth==c2_bandwidth){
//                return 0;
//            }
//            return -1;
//        }
//    }

}


