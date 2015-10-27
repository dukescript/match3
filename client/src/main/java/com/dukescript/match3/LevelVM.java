/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dukescript.match3;

import com.dukescript.match3.js.DropBinding;
import com.dukescript.match3.js.ExplodeBinding;
import com.dukescript.match3.js.MoveableBinding;
import com.dukescript.match3.js.SwapBinding;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.java.html.json.ComputedProperty;
import net.java.html.json.Function;
import net.java.html.json.Model;
import net.java.html.json.ModelOperation;
import net.java.html.json.Models;
import net.java.html.json.OnPropertyChange;
import net.java.html.json.Property;

/**
 *
 * @author antonepple
 */
@Model(className = "Level", properties = {
    @Property(name = "columns", type = int.class),
    @Property(name = "rows", type = int.class),
    @Property(name = "tiles", type = Tile.class, array = true),
    @Property(name = "play", type = boolean.class),
    @Property(name = "deleted", type = int.class),
    @Property(name = "score", type = int.class),
    @Property(name = "moves", type = int.class),
    @Property(name = "possibleMoves", type = int.class),
    @Property(name = "goal", type = Goal.class)}, targetId = "")
class LevelVM {

    private static Tile EMPTY = new Tile(-1, "none", "0", false);
    private static int TILE_TYPES = 6;
    private static Random random = new Random(System.currentTimeMillis());
    private static int drop = 0;

    static void onPageLoad() throws Exception {
        Tile tile = new Tile();
        Models.toRaw(tile);
        ExplodeBinding.init();
        SwapBinding.init();
        DropBinding.init();
        MoveableBinding.init();
        Level level = new Level(8, 8, false, 0, 0, 5, 1, new Goal(1, 2, 3, 5, 6, 7, 0, 0, 0, 0, 0, 0));
        level.init();
        level.applyBindings();
    }

    @ModelOperation
    public static void init(Level level) {
        level.setScore(0);
        level.setPlay(false);
        level.setDeleted(0);
        level.setMoves(12);
        level.setGoal(new Goal(0, 1, 2, 8, 6, 4, 0, 0, 0, 0, 0, 0));

        for (int i = 0; i < level.getColumns() * level.getRows(); i++) {
            level.getTiles().add(i, new Tile(0, "none", "0", false));
        }
        for (int i = 0; i < level.getColumns() * level.getRows(); i++) {
            do {
                level.getTiles().set(i, new Tile(random.nextInt(TILE_TYPES), "none", "" + (i / level.getColumns() - 8), false));
            } while (isHorizontalMatch(level, i) || isVerticalMatch(level, i));
        }
        drop = 64;
        level.setPlay(true);
    }

    @Function
    public static void restart(Level level) {
        level.init();
    }

    @ComputedProperty
    public static boolean finished(int moves) {
        return moves <= 0;
    }

    @OnPropertyChange(value = "possibleMoves")
    public static void movesChanged(Level l) {
        if (l.getPossibleMoves() <= 0) {
            shuffle(l);
        }
    }

    @Function
    @ModelOperation
    public static void shuffle(Level level) {
        List<Tile> tiles = level.getTiles();
        Collections.shuffle(tiles);
        findMatches(level);
    }

    @Function
    public static void move(Level level, Tile data, String dir, int pos) {
        List<Tile> tiles = level.getTiles();
        int newPos = -1;
        if (dir.equals("up") && rowNumber(level, pos) > 0) {
            newPos = pos - 8;
        } else if (dir.equals("down") && rowNumber(level, pos) < 7) {
            newPos = pos + 8;
        } else if (dir.equals("left") && columnNumber(level, pos) > 0) {
            newPos = pos - 1;
        } else if (dir.equals("right") && columnNumber(level, pos) < 7) {
            newPos = pos + 1;
        }
        if (newPos == -1) {
            return;
        }
        Tile swap = tiles.get(newPos);
        if (dir.equals("up") && rowNumber(level, pos) > 0) {
            if (!checkMoveUp(level, pos, data.getType()) & !checkMoveDown(level, newPos, swap.getType())) {
                swap.setSwap("down");
                data.setSwap(dir);
                return;
            }
        } else if (dir.equals("down") && rowNumber(level, pos) < 7) {
            if (!checkMoveDown(level, pos, data.getType()) & !checkMoveUp(level, newPos, swap.getType())) {
                swap.setSwap("up");
                data.setSwap(dir);
                return;
            }
        } else if (dir.equals("left") && columnNumber(level, pos) > 0) {
            if (!checkMoveLeft(level, pos, data.getType()) & !checkMoveRight(level, newPos, swap.getType())) {
                swap.setSwap("right");
                data.setSwap(dir);
                return;
            }
        } else if (dir.equals("right") && columnNumber(level, pos) < 7) {
            if (!checkMoveRight(level, pos, data.getType()) & !checkMoveLeft(level, newPos, swap.getType())) {
                swap.setSwap("left");
                data.setSwap(dir);
                return;
            }
        }
        level.setPlay(false);
        level.setMoves(level.getMoves() - 1);
        tiles.set(pos, swap);
        tiles.set(newPos, data);
        findMatches(level);
        level.setPlay(true);
    }

    private static boolean findMatches(Level level) {
        List<Tile> tiles = level.getTiles();
        HashMap<Integer, List<Integer>> horizontalHits = findHorizontalHits(tiles, level);
        HashMap<Integer, List<Integer>> verticalHits = findVerticalHits(tiles, level);
        HashSet<Integer> common = new HashSet<>(horizontalHits.keySet());
        common.retainAll(verticalHits.keySet());
        for (Integer integer : common) {
            // check which powerup
        }
        HashSet<Integer> uniqueHorizontal = new HashSet<>(horizontalHits.keySet());
        uniqueHorizontal.removeAll(verticalHits.keySet());
        Set<Integer> toDelete = new HashSet<Integer>();
        toDelete.addAll(verticalHits.keySet());
        toDelete.addAll(horizontalHits.keySet());
        level.setScore(level.getScore() + (toDelete.size() * 50));
        for (int index : toDelete) {
            tiles.get(index).setDrop("none");
            tiles.get(index).setExplode(true);
            level.getGoal().delete(tiles.get(index).getType());
        }
        level.setDeleted(toDelete.size());
        return !toDelete.isEmpty();
    }

    @Function
    public static void remove(Level l) {
        l.setDeleted(l.getDeleted() - 1);
        if (l.getDeleted() == 0) {
            fillUpTiles(l, l.getTiles());
        }
    }

    @Function
    public static void dropped(Level l, Tile data) {
        drop--;
        if (drop == 0) {
            if (!findMatches(l)) {
                findPossibleMoves(l);
            }
        }
    }

    public static void findPossibleMoves(Level l) {
        List<Tile> tiles = l.getTiles();
        int moves = 0;
        for (int i = 0; i < tiles.size(); i++) {
            Tile check = tiles.get(i);
            if (checkMoveDown(l, i, check.getType())) {
                moves++;
            }
            if (checkMoveUp(l, i, check.getType())) {
                moves++;
            }
            if (checkMoveLeft(l, i, check.getType())) {
                moves++;
            }
            if (checkMoveRight(l, i, check.getType())) {
                moves++;
            }
        }
    }

    private static void fillUpTiles(Level level, List<Tile> tiles) {
        for (int col = 0; col < level.getColumns(); col++) {
            for (int row = level.getRows() - 1; row >= 0; row--) {
                int index = row * level.getColumns() + col;
                Tile current = tiles.get(index);
                if (current.getType() == -1) {
                    int above = index - level.getColumns();
                    while (above >= 0) {
                        Tile test = tiles.get(above);
                        if (test.getType() == -1) {
                            above = above - level.getColumns();
                        } else {
                            drop++;
                            test.setDrop("" + rowNumber(level, above));
                            tiles.set(index, test);
                            tiles.set(above, EMPTY);
                            break;
                        }
                    }
                    if (tiles.get(index).getType() == -1) {
                        // everything above is empty
                        int toFill = index;
                        while (toFill >= 0) {
                            drop++;
                            final Tile tile = new Tile(random.nextInt(TILE_TYPES), "none", "" + ((toFill / level.getColumns()) - level.getRows()), false);
                            tiles.set(toFill, tile);
                            toFill = toFill - level.getColumns();
                        }
                    }
                }
            }
        }
    }

    private static HashMap<Integer, List<Integer>> findVerticalHits(List<Tile> tiles, Level level) {
        HashMap<Integer, List<Integer>> hits = new HashMap<>();
        int type = tiles.get(0).getType();
        int clusterLength = 1;
        for (int i = 1; i < tiles.size(); i++) {
            int index = (i % level.getColumns()) * level.getColumns() + ((i / level.getRows()));
            int lastIndex = ((i - 1) % level.getColumns()) * level.getColumns() + (((i - 1) / level.getRows()));
            Tile current = tiles.get(index);
            if (columnNumber(level, index) != columnNumber(level, lastIndex) // next col
                    || current.getType() != type) {
                // no match
                if (clusterLength >= 3) {
                    ArrayList<Integer> cluster = new ArrayList<>();
                    for (int j = 1; j <= clusterLength; j++) {
                        int calcIndex = ((i - j) % level.getColumns()) * level.getColumns() + (((i - j) / level.getRows()));
                        cluster.add(calcIndex);
                        hits.put(calcIndex, cluster);
                    }
                }
                type = current.getType();
                clusterLength = 1;
            } else {
                clusterLength++;
                if (i == ((level.getColumns() * level.getRows()) - 1) && clusterLength >= 3) {
                    ArrayList<Integer> cluster = new ArrayList<>();
                    for (int j = 1; j <= clusterLength; j++) {
                        int calcIndex = ((i - j) % level.getColumns()) * level.getColumns() + (((i - j) / level.getRows()));
                        cluster.add(calcIndex);
                        hits.put(calcIndex, cluster);
                    }
                }
            }
        }
        return hits;
    }

    private static HashMap<Integer, List<Integer>> findHorizontalHits(List<Tile> tiles, Level level) {
        HashMap<Integer, List<Integer>> hits = new HashMap<>();
        // collect all horizontal matches
        int type = tiles.get(0).getType();
        int clusterLength = 1;
        for (int i = 1; i < tiles.size(); i++) {
            Tile current = tiles.get(i);
            if (rowNumber(level, i) != rowNumber(level, i - 1) // next row
                    || current.getType() != type) {
                // no match
                if (clusterLength >= 3) {
                    ArrayList<Integer> cluster = new ArrayList<>();
                    for (int j = 1; j <= clusterLength; j++) {
                        cluster.add(i - j);
                        hits.put(i - j, cluster);
                    }
                }
                type = current.getType();
                clusterLength = 1;
            } else {
                clusterLength++;
            }
        }
        return hits;
    }

    public static int rowNumber(Level level, int index) {
        return index / level.getColumns();
    }

    public static int columnNumber(Level level, int index) {
        return index % level.getRows();
    }

    public static boolean isHorizontalMatch(Level level, int i) {
        List<Tile> tiles = level.getTiles();
        return columnNumber(level, i) >= 2 && tiles.get(i).getType() == tiles.get(i - 1).getType() && tiles.get(i).getType() == tiles.get(i - 2).getType();
    }

    public static boolean isVerticalMatch(Level level, int i) {
        List<Tile> tiles = level.getTiles();
        return rowNumber(level, i) >= 2 && tiles.get(i).getType() == tiles.get(i - level.getColumns()).getType() && tiles.get(i).getType() == tiles.get(i - 2 * level.getColumns()).getType();
    }

    public static boolean checkMoveUp(Level level, int position, int type) {
        if (rowNumber(level, position) > 0) {
            int newPos = position - level.getColumns();
            return checkLeft(level, newPos, type) || checkUp(level, newPos, type) || checkRight(level, newPos, type) || checkLeftRight(level, newPos, type);
        }
        return false;
    }

    public static boolean checkMoveDown(Level level, int position, int type) {
        if (rowNumber(level, position) < level.getRows() - 1) {
            int newPos = position + level.getColumns();
            return checkLeft(level, newPos, type) || checkRight(level, newPos, type) || checkDown(level, newPos, type) || checkLeftRight(level, newPos, type);
        }
        return false;
    }

    public static boolean checkMoveRight(Level level, int position, int type) {
        if (columnNumber(level, position) <= level.getColumns() - 2) {
            int newPos = position + 1;
            return checkRight(level, newPos, type) || checkUp(level, newPos, type) || checkDown(level, newPos, type) || checkUpDown(level, newPos, type);
        }
        return false;
    }

    public static boolean checkMoveLeft(Level level, int position, int type) {
        if (columnNumber(level, position) > 0) {
            int newPos = position - 1;
            return checkLeft(level, newPos, type) || checkUp(level, newPos, type) || checkDown(level, newPos, type) || checkUpDown(level, newPos, type);
        }
        return false;
    }

    public static boolean checkLeftRight(Level l, int newPos, int type) {
        if (columnNumber(l, newPos) >= 1 && columnNumber(l, newPos) < (l.getColumns() - 1) && type == l.getTiles().get(newPos - 1).getType() && type == l.getTiles().get(newPos + 1).getType()) {
            return true;
        }
        return false;
    }

    public static boolean checkLeft(Level l, int newPos, int type) {
        if (columnNumber(l, newPos) >= 2 && type == l.getTiles().get(newPos - 1).getType() && type == l.getTiles().get(newPos - 2).getType()) {
            return true;
        }
        return false;
    }

    public static boolean checkRight(Level l, int newPos, int type) {
        if (columnNumber(l, newPos) < (l.getColumns() - 2) && type == l.getTiles().get(newPos + 1).getType() && type == l.getTiles().get(newPos + 2).getType()) {
            return true;
        }
        return false;
    }

    public static boolean checkUpDown(Level l, int newPos, int type) {
        if (newPos + l.getColumns() >= l.getTiles().size() || newPos - l.getColumns() < 0) {
            return false;
        }
        if (rowNumber(l, newPos) >= 1 && rowNumber(l, newPos) <= l.getRows() - 1 && type == l.getTiles().get(newPos - l.getColumns()).getType() && type == l.getTiles().get(newPos + l.getColumns()).getType()) {
            return true;
        }
        return false;
    }

    public static boolean checkUp(Level l, int newPos, int type) {
        if (rowNumber(l, newPos) >= 2 && type == l.getTiles().get(newPos - l.getColumns()).getType() && type == l.getTiles().get(newPos - 2 * l.getColumns()).getType()) {
            return true;
        }
        return false;
    }

    public static boolean checkDown(Level l, int newPos, int type) {
        if (rowNumber(l, newPos) < l.getRows() - 2 && type == l.getTiles().get(newPos + l.getColumns()).getType() && type == l.getTiles().get(newPos + 2 * l.getColumns()).getType()) {
            return true;
        }
        return false;
    }

}
