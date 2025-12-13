package src;

public class Hello {
    public static void main(String[] args) {
        Task t = new Task("Learn Java step by step");
        System.out.println(t);
        t.markDone();
        System.out.println(t);
    }
}
