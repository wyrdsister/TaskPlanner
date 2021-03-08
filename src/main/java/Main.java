import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Write filename...");
        String filename = scanner.nextLine();

        Planner planner = new Planner();
        try {
            planner.planTasks(filename).forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
