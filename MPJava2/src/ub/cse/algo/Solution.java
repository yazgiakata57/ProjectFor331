package ub.cse.algo;

import java.util.*;

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
    public ArrayList<Client> orderOfDijkstras(){
        clients.sort(new possibleMaxProfit());
        return clients;
    }

    //GETTING NULL POINTER EXCEPTION HERE THINK ABOUT THIS STUFF
    //WHERE DO WE BUILD THE NODEPOPULATION?
    public boolean reachedQuota(HashMap<Integer, HashMap<Integer,Integer>> nodePopulation,Integer neighbor, Integer time){
        HashMap<Integer,Integer> timetable= nodePopulation.get(neighbor);
        Integer populationAtGivenTime= timetable.get(time);
        return populationAtGivenTime >= bandwidths.get(neighbor);
    }


    //Dijkstras
    public ArrayList<Integer> pathOfTheNode(Integer to, HashMap<Integer, HashMap<Integer,Integer>> nodePopulation, HashMap<Integer,ArrayList<Integer>> paths){
        HashSet<Integer> explored = new HashSet<>();
        HashMap<Integer, Integer> path = new HashMap<>();

        node2Comp comp = new node2Comp();
        PriorityQueue<Node2> todo = new PriorityQueue<>((java.util.Comparator) comp);
        int from= graph.contentProvider;

        int bwdith = bandwidths.get(from);
        todo.add(new Node2(from, bwdith, from, 0));

        while (!todo.isEmpty()){
            Node2 node = todo.poll();
            Integer currentClient = node.clientID;

            if (!explored.contains(currentClient)){
                explored.add(currentClient);
                path.put(currentClient, node.prev);
                //where do i update the time?
                //NOW ADD TO THE PRIORITY QUEUE
                ArrayList<Integer> neighbors = graph.get(currentClient);
                int new_time= node.time+1;
                for (Integer neighbor: neighbors){
                    int nextBWidth = bandwidths.get(neighbor) + node.totalBandwidth;

                    // IMPORTANTE!!!!! :    IF NEIGHBOR HAS NOT REACHED ITS CAPACITY AT THE GIVEN TIME ADD IT TO THE QUEUE... OTHERWISE, DONT BOTHER.
                    //
                    //
                    ///
                    ////

                    if (!reachedQuota(nodePopulation,neighbor,new_time)){
                        Node2 newNode = new Node2(neighbor, nextBWidth, currentClient, new_time);
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
    public HashMap<Integer, HashMap<Integer, Integer>> updatePopulation(HashMap<Integer, HashMap<Integer, Integer>> nodePopulation, ArrayList<Integer> pathOfTheNode ){
        for (int i=0; i< pathOfTheNode.size();i++){
            Integer currentNode=pathOfTheNode.get(i);
            int time=i;
            Integer currentPopulationOfCurrentNode=nodePopulation.get(currentNode).getOrDefault(time,0);
            nodePopulation.get(currentNode).put(time, currentPopulationOfCurrentNode+1);

            if (i!=0){
                int prev_time=i-1;
                Integer prevNode=pathOfTheNode.get(prev_time);
                Integer prevPopulationOfPrevNode= nodePopulation.get(prevNode).getOrDefault(prev_time,0);
                nodePopulation.get(prevNode).put(time, prevPopulationOfPrevNode-1);
            }
        }
        return nodePopulation;
    }
    public HashMap< Integer, ArrayList<Integer>> GENERAL(){
        ArrayList<Client> client= orderOfDijkstras();
        HashMap<Integer,HashMap<Integer, Integer>> nodePopulation= new HashMap<>();
        HashMap< Integer, ArrayList<Integer>> ret_val= new HashMap<>();

        for (Client c:client){
            Integer to=c.id;

            ArrayList<Integer> path=pathOfTheNode(to,nodePopulation,ret_val);
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
    public static class possibleMaxProfit implements Comparator<Client>{

        @Override
        public int compare(Client o1, Client o2) {
            return Float.compare(o2.alpha * o2.payment, o1.alpha * o1.payment);
        }
    }
    public static class Node2 {
        public Integer clientID;
        public Integer totalBandwidth;
        public Integer prev;
        public Integer time;
        public Node2(int clientID, int bwidth, int prev, int time){
            this.clientID = clientID;
            this.totalBandwidth = bwidth;
            this.prev = prev;
            this.time=time;
        }
    }
    public class node2Comp implements Comparator<Node2> {
        @Override
        public int compare (Node2 n1, Node2 n2){
            return Integer.compare(n2.totalBandwidth, n1.totalBandwidth);
        }
    }
}


