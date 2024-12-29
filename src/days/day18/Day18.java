package days.day18;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import day.Day;
import day.Part;
import utils.Grid;
import utils.Parsing;
import utils.Point;
import utils.enums.Direction;

public class Day18 implements Day{

    @Override
    public void solveA(String input) {
        var memory = parse(input, CorruptedMemory.class, Part.A);
        var grid = new Grid<>(memory.memory());
        Point start = new Point(0,0);
        Point end = new Point(grid.width() - 1, grid.height() - 1);
        
        for(int i = 0; i < 1024; i++){
            grid.setValue(memory.points().get(i), "#");
        }

        Node node = sp(start, end, grid);

        List<Point> path = backtrack(node);
        System.out.println(path.size() - 1);

    }

    private List<Point> backtrack(Node node){
        if(node.parent == null){
            return new ArrayList<>(List.of(node.point));
        }

        List<Point> path = new ArrayList<>();
        path.add(node.point);
        path.addAll(backtrack(node.parent));
    
        return path;
        
    }

    @Override
    public void solveB(String input) {
        var memory = parse(input, CorruptedMemory.class, Part.A);
        var grid = new Grid<String>(memory.memory());
        Point start = new Point(0,0);
        Point end = new Point(grid.width()  - 1, grid.height() - 1);
        Node node = sp(start, end, grid);
        int index = 0;
        Set<Point> currentPath = new HashSet<>(backtrack(node));
        
        while(node != null){
            Point pos = memory.points().get(index);
            grid.setValue(pos, "#");
            if(currentPath.contains(pos)){
                node = sp(start,end,grid);
                currentPath = node == null ? currentPath : new HashSet<>(backtrack(node));
            }
            index++;
        }

        System.out.println(memory.points().get(index - 1));
    }

    private Node sp(Point start, Point end, Grid<String> memory){
        Queue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(n -> n.cost));
        Set<Point> visited = new HashSet<>();

        queue.add(new Node(0, start, null));

        while(!queue.isEmpty()){
            Node current = queue.poll();

            if(current.point.equals(end)){
                return current;
            }

            if(visited.contains(current.point)){
                continue;
            }

            visited.add(current.point);

            for(var neighbour : memory.neighbourIndexes(current.point, Direction.NON_DIAGONAL)){
                if(memory.at(neighbour).equals("#")){
                    continue;
                }

                queue.add(new Node(current.cost + 1, neighbour, current));
            }
        }

        return null;
    }

    private class Node{
        int cost;
        Point point;
        Node parent;

        private Node(int c, Point cur, Node par){
            cost = c;
            point = cur;
            parent = par;
        }
    }

    @Override
    public Object parseA(String input) {
        List<String> lines = Parsing.lines(input);
        List<Point> points = new ArrayList<>();
        for(String line : lines){
            var coords = line.split(",");
            int x = Parsing.stoi(coords[0]);
            int y = Parsing.stoi(coords[1]);
            points.add(new Point(x,y));
        }

        int xmax = Collections.max(points, Comparator.comparingInt(p -> p.getX())).getX();
        int ymax = Collections.max(points, Comparator.comparingInt(p -> p.getY())).getY();
        
        Grid<String> g = Grid.createGrid(xmax,ymax, ".");

        return new CorruptedMemory(g,points);
    }

    @Override
    public Object parseB(String input) {
        return parseA(input);
    }

    private record CorruptedMemory(Grid<String> memory, List<Point> points){};



}
