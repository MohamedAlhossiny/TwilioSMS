package twilioWebApp.dao.Impl;

import twilioWebApp.dao.VerificationCodeDao;
import twilioWebApp.model.VerificationCode;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import twilioWebApp.util.HibernateUtil;
import org.hibernate.HibernateException;
import jakarta.validation.ConstraintViolationException;
public class VerificationCodeDaoImpl implements VerificationCodeDao {
    private SessionFactory sessionFactory;
    
    public VerificationCodeDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void save(VerificationCode code) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(code);
            session.getTransaction().commit();
        }catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User ID does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Verification code already exists");
            }else{
                throw new HibernateException("Error saving verification code", e);
            }
        }
    }   

    @Override
    public void update(VerificationCode code) throws HibernateException   {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(code);
            transaction.commit();
        }catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User ID does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Verification code does not exist");
            }else{
                throw new HibernateException("Error updating verification code", e);
            }
        }
    }

    @Override
    public void delete(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            VerificationCode code = session.get(VerificationCode.class, id);
            session.remove(code);
            transaction.commit();
        }catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Verification code does not exist");
            }else{
                throw new HibernateException("Error deleting verification code", e);
            }
        }
    }   

    @Override
    public void deleteAllCodesByUser(Integer userId) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("delete from VerificationCode where user_id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
            transaction.commit();
        }catch (ConstraintViolationException e) {
            throw new HibernateException("Error deleting all verification codes", e);
        }
    }

    @Override
    public void deleteAllCodes() throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createMutationQuery("delete from VerificationCode").executeUpdate();
            transaction.commit();
        }catch (Exception e) {
            throw new HibernateException("Error deleting all verification codes", e);
        }
    }

    @Override
    public VerificationCode findById(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            return session.get(VerificationCode.class, id);
        }catch (Exception e) {
            throw new HibernateException("Error finding verification code by id", e);
        }
    }

    @Override
    public List<VerificationCode> findByUserId(Integer userId) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            return session.createSelectionQuery("from VerificationCode where user_id = :user_id order by created_at desc", VerificationCode.class)
                .setParameter("user_id", userId)
                .list();
        }catch (Exception e) {
            throw new HibernateException("Error finding verification code by user id", e);
        }
    }

    @Override
    public List<VerificationCode> findAll() throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            return session.createSelectionQuery("from VerificationCode", VerificationCode.class).list(); 
        }catch (Exception e) {
            throw new HibernateException("Error finding all verification codes", e);
        }
    }
}


