// File: src/MetroApp/Graph_M.java
package MetroApp;

import java.util.*;

public class Graph_M {

    private final Map<String, Map<String, Integer>> adjList = new HashMap<>();
    private final Map<String, Map<String, String>> lineMap = new HashMap<>();

    public Graph_M() {
        addEdge("Noida Sector 62", "Noida Sector 59", 5, "Blue");
        addEdge("Noida Sector 59", "Noida Sector 61", 5, "Blue");
        addEdge("Noida Sector 61", "Noida Sector 52", 5, "Blue");
        addEdge("Noida Sector 52", "Noida Sector 34", 5, "Blue");
        addEdge("Noida Sector 34", "Noida City Centre", 5, "Blue");
        addEdge("Noida City Centre", "Golf Course", 5, "Blue");
        addEdge("Golf Course", "Botanical Garden", 5, "Blue");
        addEdge("Botanical Garden", "Yamuna Bank", 5, "Blue");
        addEdge("Yamuna Bank", "Laxmi Nagar", 5, "Blue");
        addEdge("Laxmi Nagar", "Rajiv Chowk", 5, "Blue");

        addEdge("Samaypur Badli", "Rohini Sector 18", 5, "Yellow");
        addEdge("Rohini Sector 18", "Rohini Sector 16", 5, "Yellow");
        addEdge("Rohini Sector 16", "Rohini Sector 15", 5, "Yellow");
        addEdge("Rohini Sector 15", "Rohini West", 5, "Yellow");
        addEdge("Rohini West", "Pitampura", 5, "Yellow");
        addEdge("Pitampura", "Kashmere Gate", 5, "Yellow");
        addEdge("Kashmere Gate", "Rajiv Chowk", 5, "Yellow");

        addEdge("Rajiv Chowk", "Central Secretariat", 5, "Yellow");
        addEdge("Central Secretariat", "INA", 5, "Yellow");
        addEdge("INA", "Hauz Khas", 5, "Yellow");
        addEdge("Hauz Khas", "Malviya Nagar", 5, "Yellow");
        addEdge("Malviya Nagar", "Saket", 5, "Yellow");
        addEdge("Saket", "Qutub Minar", 5, "Yellow");

        addEdge("Noida City Centre", "Botanical Garden", 7, "Blue");
        addEdge("Golf Course", "Yamuna Bank", 10, "Blue");
        addEdge("Rohini Sector 16", "Pitampura", 6, "Yellow");
        addEdge("Rajiv Chowk", "Hauz Khas", 9, "Yellow");
        addEdge("Yamuna Bank", "Mandi House", 4, "Blue");
        addEdge("Mandi House", "Central Secretariat", 4, "Blue");
        addEdge("Kashmere Gate", "Mandi House", 6, "Blue");
        addEdge("Rohini West", "Netaji Subhash Place", 4, "Red");
        addEdge("Netaji Subhash Place", "Pitampura", 4, "Red");
        addEdge("INA", "AIIMS", 3, "Green");
        addEdge("AIIMS", "Green Park", 3, "Green");
        addEdge("Green Park", "Hauz Khas", 3, "Green");

        addEdge("Noida Sector 52", "Golf Course", 7, "Blue");
        addEdge("Noida Sector 34", "Botanical Garden", 6, "Blue");
        addEdge("Noida Sector 61", "Noida City Centre", 7, "Blue");
        addEdge("Netaji Subhash Place", "Mandi House", 8, "Red");
        addEdge("AIIMS", "Rajiv Chowk", 6, "Green");
    }

    public void addEdge(String u, String v, int cost, String line) {
        adjList.putIfAbsent(u, new HashMap<>());
        adjList.putIfAbsent(v, new HashMap<>());
        adjList.get(u).put(v, cost);
        adjList.get(v).put(u, cost);

        lineMap.putIfAbsent(u, new HashMap<>());
        lineMap.putIfAbsent(v, new HashMap<>());
        lineMap.get(u).put(v, line);
        lineMap.get(v).put(u, line);
    }

    public String getLine(String u, String v) {
        return lineMap.getOrDefault(u, Collections.emptyMap()).getOrDefault(v, "Unknown");
    }

    public Set<String> getStations() {
        return adjList.keySet();
    }

    public Set<String> getNeighbors(String station) {
        return adjList.getOrDefault(station, Collections.emptyMap()).keySet();
    }

    public int getDistance(String u, String v) {
        return adjList.getOrDefault(u, Collections.emptyMap()).getOrDefault(v, Integer.MAX_VALUE);
    }

    public int dijkstra(String src, String dest, boolean discount, StringBuilder out) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (String station : adjList.keySet()) {
            dist.put(station, Integer.MAX_VALUE);
        }
        dist.put(src, 0);
        pq.add(src);

        while (!pq.isEmpty()) {
            String curr = pq.poll();
            for (Map.Entry<String, Integer> neighbor : adjList.get(curr).entrySet()) {
                int newDist = dist.get(curr) + neighbor.getValue();
                if (newDist < dist.get(neighbor.getKey())) {
                    dist.put(neighbor.getKey(), newDist);
                    parent.put(neighbor.getKey(), curr);
                    pq.add(neighbor.getKey());
                }
            }
        }

        if (dist.get(dest) == Integer.MAX_VALUE) {
            return -1;
        }

        List<String> path = new ArrayList<>();
        String at = dest;
        while (at != null) {
            path.add(at);
            at = parent.get(at);
        }
        Collections.reverse(path);
        out.append("Best Route: ").append(String.join(" -> ", path)).append("\nDistance: ").append(dist.get(dest)).append(" units\n");
        return dist.get(dest);
    }

    public static class PathResult {

        public List<String> path;
        public int cost;

        public PathResult(List<String> path, int cost) {
            this.path = path;
            this.cost = cost;
        }
    }

    public List<PathResult> findAllPaths(String start, String end) {
        List<PathResult> results = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfs(start, end, new ArrayList<>(), 0, visited, results);
        results.sort(Comparator.comparingInt(r -> r.cost));
        return results;
    }

    private void dfs(String current, String end, List<String> path, int cost,
            Set<String> visited, List<PathResult> results) {
        path.add(current);
        visited.add(current);

        if (current.equals(end)) {
            results.add(new PathResult(new ArrayList<>(path), cost));
        } else {
            for (String neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    int edgeCost = getDistance(current, neighbor);
                    if (edgeCost != Integer.MAX_VALUE) {
                        dfs(neighbor, end, path, cost + edgeCost, visited, results);
                    }
                }
            }
        }

        path.remove(path.size() - 1);
        visited.remove(current);
    }

    public String getAllRoutesWithFare(String start, String end) {
        List<PathResult> paths = findAllPaths(start, end);
        StringBuilder sb = new StringBuilder();
        sb.append("All Possible Routes from ").append(start).append(" to ").append(end).append(":\n\n");
        for (PathResult pr : paths) {
            sb.append(String.join(" -> ", pr.path)).append(" | Fare: ₹").append(pr.cost).append("\n");
        }
        return sb.toString();
    }
}
