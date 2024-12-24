
package days.day17;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import day.Day;
import day.Part;
import utils.Parsing;
import utils.Sequence;

public class Day17 implements Day {

    long regA;
    long regB;
    long regC;
    int ptr;
    int[] sequence;
    List<Integer> output = new ArrayList<>();

    @Override
    public void solveA(String input) {
        Program p = parse(input, Program.class, Part.A);

        regA = p.regA;
        regB = p.regB;
        regC = p.regC;
        ptr = p.pointer;

        sequence = p.sequence;
        runProgram(p.regA);

        System.out.println(output);
    }

    private long getCombo() {
        long value = -1;
        int combo = sequence[ptr];
        if (combo < 4) {
            value = combo;
        } else if (combo == 4) {
            value = regA;
        } else if (combo == 5) {
            value = regB;
        } else if (combo == 6) {
            value = regA;
        } else {
            System.out.println("Invalid combo operand" + combo);
        }
        return value;
    }

    private void adv() {
        long combo = getCombo();
        regA = regA / (int) Math.pow(2, combo);
        ptr++;
    }

    private void bxl() {
        long literal = sequence[ptr];
        regB = xor(regB, literal);
        ptr++;
    }

    private void bst() {
        regB = getCombo() % 8;
        ptr++;
    }

    private void jnz() {
        if (regA == 0) {
            return;
        }
        ptr = sequence[ptr];
    }

    private void bxc() {
        regB = xor(regB, regC);
        ptr++;
    }

    private void out() {
        output.add((int) (getCombo() % 8));
        ptr++;
    }

    private void bdv() {
        regB = regA / (long) Math.pow(2, getCombo());
        ptr++;
    }

    private void cdv() {
        regC = regA / (long) Math.pow(2, getCombo());
        ptr++;
    }

    private long xor(long a, long b) {
        return a ^ b;
    }

    private void runProgram(long l) {
        regA = l;
        regB = 0;
        regC = 0;
        ptr = 0;
        output.clear();

        while (ptr < sequence.length - 1) {
            int opcode = sequence[ptr++];
            if (opcode == 0) {
                adv();
            } else if (opcode == 1) {
                bxl();
            } else if (opcode == 2) {
                bst();
            } else if (opcode == 3) {
                jnz();
            } else if (opcode == 4) {
                bxc();
            } else if (opcode == 5) {
                out();
            } else if (opcode == 6) {
                bdv();
            } else if (opcode == 7) {
                cdv();
            }
        }
    }

    @Override
    public void solveB(String input) {

        Queue<Long> inits = new LinkedList<>();

        for (long i = 1; i < 8; i++) {
            runProgram(i);
            if (valid()) {
                inits.add(i);
            }
        }

        List<Long> possibleSols = new ArrayList<>();
        while (!inits.isEmpty()) {
            long aPre = inits.poll();
            for (long i = 1; i < 8; i++) {
                long test = aPre * 8 + i;
                runProgram(test);
                if (output.size() == 16 && valid()) {
                    possibleSols.add(test);
                }
                if (output.size() > 16) {
                    continue;
                }

                if (valid()) {
                    inits.add(test);
                }
            }
        }

        System.out.println(Collections.min(possibleSols));
    }

    private boolean valid() {
        if (output.size() == 0) {
            return false;

        }
        int backIndex = sequence.length - 1;
        for (int i = output.size() - 1; i >= 0; i--) {
            if (!output.get(i).equals(sequence[backIndex--])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object parseA(String input) {
        return parseB(input);
    }

    @Override
    public Object parseB(String input) {
        Pattern p = Pattern.compile(
                "Register A: (?<regA>\\d+)\\nRegister B: (?<regB>\\d+)\\nRegister C: (?<regC>\\d+)\\n\\nProgram: (?<seq>.*)");
        Matcher m = p.matcher(input);

        long regA = -1;
        long regB = -1;
        long regC = -1;
        int pointer = 0;
        int[] sequence = null;
        while (m.find()) {
            regA = Parsing.stol(m.group("regA"));
            regB = Parsing.stol(m.group("regB"));
            regC = Parsing.stol(m.group("regC"));
            var seq = Sequence.sequence(m.group("seq"), ",", Parsing::stoi);
            sequence = new int[seq.size()];
            for (int i = 0; i < seq.size(); i++) {
                sequence[i] = seq.get(i);
            }

        }

        return new Program(sequence, regA, regB, regC, pointer);
    }

    private record Program(int[] sequence, long regA, long regB, long regC, int pointer) {
    };

}