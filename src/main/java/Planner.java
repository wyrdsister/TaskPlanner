import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Planner {
    private TIntObjectMap<TIntList> taskMap;

    public Planner() {
        this.taskMap = new TIntObjectHashMap<>();
    }

    public List<TIntSet> planTasks(String filename) throws Exception {
        readFileAndFillTaskMap(filename);

        TIntSet noIncomingNodes = new TIntHashSet(taskMap.keySet());
        for (TIntList linkedNodes : taskMap.valueCollection()) {
            noIncomingNodes.removeAll(linkedNodes);
        }

        return sortTaskMap(noIncomingNodes);
    }

    private List<TIntSet> sortTaskMap(TIntSet noIncomingNodes) throws Exception {
        List<TIntSet> sortedTasks = new LinkedList<>();
        sortedTasks.add(noIncomingNodes);

        TIntList currentLevelNoIncomingNodes = new TIntLinkedList();

        while (!taskMap.isEmpty()) {
            if (noIncomingNodes.isEmpty())
                throw new Exception("Graph has cycle.");

            TIntIterator iterator = noIncomingNodes.iterator();
            while (iterator.hasNext()) {
                int removedNode = iterator.next();
                TIntCollection linkedNodes = taskMap.remove(removedNode);
                if (linkedNodes == null || linkedNodes.isEmpty())
                    continue;

                currentLevelNoIncomingNodes.addAll(linkedNodes);
            }

            TIntSet newNoIncomingNodes = new TIntHashSet(currentLevelNoIncomingNodes);
            for (TIntList remainingLinkedNodes : taskMap.valueCollection()) {
                newNoIncomingNodes.removeAll(remainingLinkedNodes);
            }
            currentLevelNoIncomingNodes.clear();

            noIncomingNodes = newNoIncomingNodes;
            if (!noIncomingNodes.isEmpty())
                sortedTasks.add(noIncomingNodes);
        }

        return sortedTasks;
    }

    private void readFileAndFillTaskMap(String filename) throws IOException {
        try (FileInputStream stream = new FileInputStream(filename);
             Scanner scanner = new Scanner(stream)) {
            while (scanner.hasNext()) {
                int value1 = scanner.nextInt();
                int value2 = scanner.nextInt();

                if (!taskMap.containsKey(value1)) {
                    TIntList linkedList = new TIntArrayList();
                    linkedList.add(value2);
                    taskMap.put(value1, linkedList);
                } else {
                    taskMap.get(value1).add(value2);
                }
            }
        }
    }
}
