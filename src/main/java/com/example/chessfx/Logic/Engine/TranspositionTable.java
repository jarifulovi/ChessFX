package com.example.chessfx.Logic.Engine;

import java.util.HashMap;
import java.util.Map;


public class TranspositionTable {
    private final Map<Long, TTEntry> table = new HashMap<>();

    public TTEntry get(Long hash, int depth) {
        TTEntry entry = table.get(hash);
        if (entry != null && entry.getDepth() >= depth) {
            return entry;
        }
        return null;
    }


    public void put(Long hash, int value, int depth, NodeType nodeType) {
        table.put(hash, new TTEntry(value, depth, nodeType));
    }

    public enum NodeType {
        EXACT, ALPHA, BETA
    }

    public record TTEntry(int value, int depth, TranspositionTable.NodeType nodeType) {
        public int getValue() {
            return value;
        }

        public int getDepth() {
            return depth;
        }

        public TranspositionTable.NodeType getNodeType() {
            return nodeType;
        }
    }
}
