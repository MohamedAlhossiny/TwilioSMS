package twilioWebApp.dao.Impl;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.List;
import twilioWebApp.util.HibernateUtil;
import twilioWebApp.dao.TwilioAccountDao;
import twilioWebApp.model.TwilioAccount;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import jakarta.validation.*;


public class TwilioAccountDaoImpl implements TwilioAccountDao {
    private SessionFactory sessionFactory;

    public TwilioAccountDaoImpl() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void save(TwilioAccount twilio) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.persist(twilio);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User ID does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("sid")) {
                throw new HibernateException("SID is already in use");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("token")) {
                throw new HibernateException("Token is already in use");
            }else{
                throw new HibernateException("Error saving twilio account", e);
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void update(TwilioAccount twilio) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            session.merge(twilio);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("sid")) {
                throw new HibernateException("SID is already in use");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("token")) {
                throw new HibernateException("Token is already in use");
            }else{
                throw new HibernateException("Error updating twilio account", e);
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Integer id) throws HibernateException {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        
        try {
            transaction = session.beginTransaction();
            TwilioAccount account = session.get(TwilioAccount.class, id);
            if (account != null) {
                session.remove(account);
            }
            transaction.commit();
        } catch (ConstraintViolationException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Twilio account does not exist");
            }else{
                throw new HibernateException("Error deleting twilio account", e);
            }
        } finally {
            session.close();
        }
    }

    @Override
    public TwilioAccount findById(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {      
            return session.get(TwilioAccount.class, id);
        }catch (Exception e) {
            throw new HibernateException("Error finding twilio account by id", e);
        }
    }

    @Override
    public List<TwilioAccount> findAll() throws HibernateException {
        try (Session session = sessionFactory.openSession()) {      
            return session.createSelectionQuery("from TwilioAccount", TwilioAccount.class).list();
        }catch (Exception e) {
            throw new HibernateException("Error finding all twilio accounts", e);
        }
    }

    @Override
    public TwilioAccount findByUserId(Integer userId) throws HibernateException { 
        try (Session session = sessionFactory.openSession()) {      
            return session.createSelectionQuery("from TwilioAccount where user_id = :userId", TwilioAccount.class)
            .setParameter("userId", userId).uniqueResult();
        }catch (Exception e) {
            throw new HibernateException("Error finding twilio account by user id", e);
        }
    }
}
