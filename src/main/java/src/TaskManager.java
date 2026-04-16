package src;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private SessionFactory factory;

    public TaskManager() {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Task.class)
                .buildSessionFactory();
    }

    // CREATE
    public void add(Task task) {
        Session session = factory.getCurrentSession();

        try {
            session.beginTransaction();

            session.persist(task);

            session.getTransaction().commit();
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
}