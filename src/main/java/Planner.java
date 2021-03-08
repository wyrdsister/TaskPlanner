import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Planner {
    private MultiValuedMap<Integer, Integer> taskMap;

    public Planner() {}

    public List<List<Integer>> planTasks(String filename) throws Exception {
        readFileAndFillTaskMap(filename);

        Set<Integer> keys = taskMap.keySet();
        Set<Integer> values = new LinkedHashSet<>(taskMap.values());

        List<Integer> noIncomingNodes = new LinkedList<>();
        for (Integer key : keys) {
            if (values.add(key))
                noIncomingNodes.add(key);
        }

        return sortTaskMap(noIncomingNodes);
    }

    private List<List<Integer>> sortTaskMap(List<Integer> noIncomingNodes) throws Exception {
        List<List<Integer>> sortedTasks = new LinkedList<>();
        sortedTasks.add(noIncomingNodes);

        Set<Integer> allValues = null;
        List<Integer> allLinkedValues = new LinkedList<>();

        while (!taskMap.isEmpty()) {
            if (noIncomingNodes.isEmpty())
                throw new Exception("Graph has cycle.");

            for (Integer value : noIncomingNodes) {
                Collection<Integer> linkedList = taskMap.remove(value);
                if (linkedList == null || linkedList.isEmpty())
                    continue;

                allLinkedValues.addAll(linkedList);
            }

            List<Integer> newNoIncomingNodes = new LinkedList<>();
            allValues = new LinkedHashSet<>(taskMap.values());
            for (Integer linked : allLinkedValues){
                if (allValues.add(linked))
                    newNoIncomingNodes.add(linked);
            }
            allValues.clear();
            allLinkedValues.clear();

            noIncomingNodes = newNoIncomingNodes;
            if (!noIncomingNodes.isEmpty()) sortedTasks.add(noIncomingNodes);
        }

        return sortedTasks;
    }

    private void readFileAndFillTaskMap(String filename) {
        this.taskMap = new HashSetValuedHashMap<>(1000, 1);

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] numbers = line.split(" ");
                int value1 = Integer.parseInt(numbers[0]);
                int value2 = Integer.parseInt(numbers[1]);

                taskMap.put(value1, value2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
