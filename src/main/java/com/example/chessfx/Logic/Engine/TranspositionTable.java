package com.example.chessfx.Logic.Engine;

import java.util.HashMap;
import java.util.Map;


public class TranspositionTable {
    public final Map<Long, Integer> table = new HashMap<>();

    public Integer get(Long hash) {
        return table.get(hash);
    }

    public void put(Long hash, int value) {
        table.put(hash, value);
    }
}
