package src;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import src.Category;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private SessionFactory factory;

    public TaskManager() {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Task.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Category.class)
                .addAnnotatedClass(Comment.class)
                .addAnnotatedClass(Priority.class)
                .buildSessionFactory();

    }

    // CREATE
    public void add(Task task) {

        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            System.out.println("TRYING TO ADD TASK: " + task.getTitle());

            // USER
            User user = session.get(User.class, 1);
            if (user == null) {
                user = new User("Default User");
                session.persist(user);
            }
            task.setUser(user);

            // PRIORITY
            if (task.getPriority() != null) {

                String level = task.getPriority().getLevel();

                Priority p = session.createQuery(
                        "from Priority where level = :level", Priority.class)
                        .setParameter("level", level)
                        .uniqueResult();

                if (p == null) {
                    p = new Priority(level);
                    session.persist(p);
                }

                task.setPriority(p);
            }

            session.persist(task);

            session.getTransaction().commit();

            System.out.println("TASK ADDED OK");

        } catch (Exception e) {
            System.out.println("ERROR WHILE ADDING:");
            e.printStackTrace(); // THIS is what we need
        } finally {
            session.close();
        }
    }

    // READ
    public ArrayList<Task> getTasks() {

        Session session = factory.getCurrentSession();
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            session.beginTransaction();

            List<Task> result = session
                    .createQuery("from Task", Task.class)
                    .getResultList();

            tasks.addAll(result);

            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return tasks;
    }

    // UPDATE
    public void update(Task task) {
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            session.merge(task);

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    // DELETE
    public void remove(Task task) {
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            Task t = session.get(Task.class, task.getId());
            if (t != null) {
                session.remove(t);
            }

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }

    public void addCommentToTask(int taskId, String text) {

        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            Task task = session.get(Task.class, taskId);

            if (task != null) {
                Comment comment = new Comment(text);
                task.addComment(comment);

                session.persist(task);
            }

            session.getTransaction().commit();
        } finally {
            session.close();
        }
    }
}