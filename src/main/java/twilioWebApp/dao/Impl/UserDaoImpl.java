package twilioWebApp.dao.Impl;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.List;
import twilioWebApp.util.HibernateUtil;
import twilioWebApp.dao.UserDao;
import twilioWebApp.model.User;
import org.hibernate.HibernateException;
import jakarta.validation.*;


public class UserDaoImpl implements UserDao {
    private SessionFactory sessionFactory;
    public UserDaoImpl() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }
    @Override
    public void save(User user) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("username")) {
                throw new HibernateException("Username already exists or must be not null");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("passwd")) {
                throw new HibernateException("Password must be not null");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("role")) {
                throw new HibernateException("Role must be admin or customer");
            }else{
                throw new HibernateException("Error saving user", e);
            }
        }
    }   

    @Override
    public void update(User user) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("username")) {
                throw new HibernateException("Username already exists or must be not null");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("passwd")) {
                throw new HibernateException("Password must be not null");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("role")) {
                throw new HibernateException("Role must be admin or customer");
            }else{
                throw new HibernateException("Error updating user", e);
            }

        }   
    }

    @Override
    public void delete(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.remove(user);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("User ID does not exist");
            }else{
                throw new HibernateException("Error deleting user", e);
            }
        }
    }   

    @Override
    public User findById(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.getTransaction().commit();
            return user;
        }catch(Exception e){
            throw new HibernateException("Error finding user by id", e);
        }
    }      

    @Override
    public List<User> findAll() throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = session.createSelectionQuery("from User", User.class).list();
            session.getTransaction().commit();
            return users;
        } catch (Exception e) {
            throw new HibernateException("Error finding all users", e);
        }
    }  

    @Override
    public User findByUsername(String username) throws HibernateException {
       try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.createSelectionQuery("from User where username = :username", User.class).setParameter("username", username).uniqueResult();
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            throw new HibernateException("Error finding user by username", e);
        }
    }   
}
