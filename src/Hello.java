package src;

public class Hello {
    public static void main(String[] args) throws Exception {
        TaskManager manager = new TaskManager();

        manager.load(); // load existing tasks first

        if (manager.getTasks().isEmpty()) {
            manager.add(new Task("Learn Gson"));
            manager.add(new Task("Persist tasks to file"));
            manager.save();
            System.out.println("First run: tasks created and saved");
        } else {
            System.out.println("Loaded tasks:");
            for (Task t : manager.getTasks()) {
                System.out.println(t);
            }
        }
    }
}
