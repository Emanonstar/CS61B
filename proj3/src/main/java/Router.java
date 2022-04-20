import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Objects;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    static long st;
    static long dest;
    static Map<Long, Double> bestKnownDistanceFromSoureTo;
    static Map<Long, Long> edgeTo;
    static GraphDB graph;
    static Set<Long> marked;
    static PriorityQueue<Long> fringe;
    static Map<Long, Double> h;
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        st = g.closest(stlon, stlat);
        dest = g.closest(destlon, destlat);
        bestKnownDistanceFromSoureTo = new HashMap<>();
        edgeTo = new HashMap<>();
        graph = g;
        marked = new HashSet<>();
        for (long v : g.vertices()) {
            bestKnownDistanceFromSoureTo.put(v, Double.MAX_VALUE);
        }
        h = new HashMap<>();

        bestKnownDistanceFromSoureTo.put(st, 0.0);
        edgeTo.put(st, Long.MAX_VALUE);
        fringe = new PriorityQueue<>(new NodeComparator());
        fringe.add(st);

        while (!fringe.isEmpty()) {
            long v = fringe.poll();
            marked.add(v);

            if (v == dest) {
                break;
            }

            for (long w : g.adjacent(v)) {
                if (marked.contains(w)) {
                    continue;
                }
                relax(v, w);
            }
        }

        List<Long> path = new ArrayList<>();
        long index = dest;
        while (index != st) {
            path.add(0, index);
            index = edgeTo.get(index);
        }
        path.add(0, st);
        return path;
    }

    /** Relax edge between Node v and Node w. */
    private static void relax(long v, long w) {
        double dst = bestKnownDistanceFromSoureTo.get(v) + graph.distance(v, w);
        if (dst < bestKnownDistanceFromSoureTo.get(w)) {
            bestKnownDistanceFromSoureTo.put(w, dst);
            edgeTo.put(w, v);
            fringe.add(w);
//            if (fringe.contains(w)) {
//                fringe.remove(w);
//                fringe.add(w);
//            } else {
//                fringe.add(w);
//            }
        }
    }

    /** Calculate priority in PriorityQueue of Node. */
    private static class NodeComparator implements Comparator<Long> {
        @Override
        public int compare(Long v, Long w) {
            double d =  bestKnownDistanceFromSoureTo.get(v) + h(v)
                    - bestKnownDistanceFromSoureTo.get(w) - h(w);
            if (d < 0) {
                return -1;
            } else if (d > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private static double h(long v) {
        double hV;
        if (h.containsKey(v)) {
            hV = h.get(v);
        } else {
            hV = graph.distance(v, dest);
        }
        return hV;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> drt = new ArrayList<>();
        String way = g.getWay(route.get(0), route.get(1));

        NavigationDirection nd = new NavigationDirection();
        nd.direction = NavigationDirection.START;
        nd.way = way;
        nd.distance = g.distance(route.get(0), route.get(1));
//        if (!way.equals("")) {
//            nd.way = way;
//        }

        for (int i = 1, l = route.size(); i < l - 1; i++) {
            long v = route.get(i);
            long w = route.get(i + 1);
            String newWay = g.getWay(v, w);

            if (newWay.equals(way)) {
                nd.distance += g.distance(v, w);
                continue;
            }

            drt.add(nd);
            way = newWay;
            nd = new NavigationDirection();
            nd.way = way;
//            if (!way.equals("")) {
//                nd.way = way;
//            }
            nd.direction = direction(route.get(i - 1), v, w, g);
            nd.distance += g.distance(v, w);
        }
        drt.add(nd);
        return drt;
    }

    private static int direction(long v, long w, long x, GraphDB g) {
        int direction;
        double angle1 = g.bearing(w, x);
        double angle2 =  g.bearing(v, w);
        double angle = angle1 - angle2;
        if (angle > 180) {
            angle -= 360;
        } else if (angle < -180) {
            angle += 360;
        }
        if (angle >= -15 && angle <= 15) {
            direction = NavigationDirection.STRAIGHT;
        } else if (angle < 0 && angle >= -30) {
            direction = NavigationDirection.SLIGHT_LEFT;
        } else if (angle > 0 && angle <= 30) {
            direction = NavigationDirection.SLIGHT_RIGHT;
        } else if (angle < 0 && angle >= -100) {
            direction = NavigationDirection.LEFT;
        } else if (angle > 0 && angle <= 100) {
            direction = NavigationDirection.RIGHT;
        } else if (angle < 0) {
            direction = NavigationDirection.SHARP_LEFT;
        } else {
            direction = NavigationDirection.SHARP_RIGHT;
        }
        return direction;
    }

    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }

}
