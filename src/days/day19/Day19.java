package days.day19;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import day.Day;
import day.Part;
import utils.Parsing;
import utils.Sequence;

public class Day19 implements Day {

    @Override
    public void solveA(String input) {
        Towels towels = parse(input, Towels.class, Part.A);
    
        List<String> required = towels.required();
        List<String> available = towels.available();

        int sum = 0;
        var impossibles = new HashMap<String,Long>();
        for (var towel : required) {
            long nr = npos(towel, available, impossibles);
            if (nr > 0) {
                sum++;
            } 
        }

        System.out.println(sum);
    }

    @Override
    public void solveB(String input) {
        Towels towels = parse(input, Towels.class, Part.A);
        List<String> required = towels.required();
        List<String> available = towels.available();

        long sum = 0;
        Map<String,Long> map = new HashMap<>();
        for(var towel : required){
            long p = npos(towel, available, map);
            sum+=p;
        }

        System.out.println(sum);
    }

    private long npos(String towel, List<String> available, Map<String,Long> possibles) {

        if(possibles.containsKey(towel)){
            return possibles.get(towel);
        }

        long sum = 0;
        
        if (towel.isEmpty() || towel.isBlank()) {
            return 1;
        }
        
        for (var avail : available) {
            if (towel.startsWith(avail)) {
                var p = npos(towel.substring(avail.length()), available, possibles);
                sum+=p;
                possibles.put(towel.substring(avail.length()), p);
            }
        }

        return sum;
    }

    @Override
    public Object parseA(String input) {
        var lines = Parsing.lines(input);
        List<String> available = Sequence.sequence(lines.get(0), ", ", s -> s);
        lines.remove(0);
        lines.remove(0);
        return new Towels(available, lines);
    }

    @Override
    public Object parseB(String input) {
        return parseA(input);
    }

    private record Towels(List<String> available, List<String> required) {
    };

}