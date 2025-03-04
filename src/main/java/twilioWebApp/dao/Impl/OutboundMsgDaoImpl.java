package twilioWebApp.dao.Impl;

import twilioWebApp.dao.OutboundMsgDao;
import twilioWebApp.model.OutboundMsg;
import java.util.List;
import java.util.Date;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import twilioWebApp.util.HibernateUtil;
import org.hibernate.HibernateException;
import jakarta.validation.*;
public class OutboundMsgDaoImpl implements OutboundMsgDao{
    private final SessionFactory sessionFactory;

    public OutboundMsgDaoImpl() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public List<OutboundMsg> findByFromNumber(String fromNumber) throws HibernateException{
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OutboundMsg> outboundMsgs = session.createSelectionQuery("from OutboundMsg where from_num = :fromNumber", OutboundMsg.class)
                .setParameter("fromNumber", fromNumber)
                .list();
            session.getTransaction().commit();
            return outboundMsgs;
        }catch (Exception e) {
            throw new HibernateException("Error finding outbound messages by from number", e);
        }
    }

    @Override
        public List<OutboundMsg> findByToNumber(String toNumber) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OutboundMsg> outboundMsgs = session.createSelectionQuery("from OutboundMsg where to_num = :toNumber", OutboundMsg.class)
                .setParameter("toNumber", toNumber)
                .list();
            session.getTransaction().commit();
            return outboundMsgs;
        }catch (Exception e) {
            throw new HibernateException("Error finding outbound messages by to number", e);
        }
    }

    @Override   
    public List<OutboundMsg> findByDate(Date date1, Date date2) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OutboundMsg> outboundMsgs = session.createQuery("from OutboundMsg where msg_date between :date1 and :date2", OutboundMsg.class)
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .list();
            session.getTransaction().commit();
            return outboundMsgs;
        }catch (Exception e) {
            throw new HibernateException("Error finding outbound messages by date", e);
        }
    }   

    @Override
    public List<OutboundMsg> findByBody(String body) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OutboundMsg> outboundMsgs = session.createSelectionQuery("from OutboundMsg where body like :body", OutboundMsg.class)
                .setParameter("body", "%" + body + "%")
                .list();
            session.getTransaction().commit();
            return outboundMsgs;
        }catch (Exception e) {
            throw new HibernateException("Error finding outbound messages by body", e);
        }
    }   

    @Override
    public List<OutboundMsg> findByUserId(Integer userId) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OutboundMsg> outboundMsgs = session.createSelectionQuery("from OutboundMsg where user_id = :userId", OutboundMsg.class)
                .setParameter("userId", userId)
                .list();
            session.getTransaction().commit();
            return outboundMsgs;
        }catch (Exception e) {
            throw new HibernateException("Error finding outbound messages by user id", e);
        }
    }   

    @Override
    public void save(OutboundMsg outboundMsg) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(outboundMsg);
            session.getTransaction().commit();
        }catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User ID does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Outbound message ID already exists");
            }else{
                throw new HibernateException("Error saving outbound message", e);
            }
        }
    }   

    @Override
    public void update(OutboundMsg outboundMsg) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(outboundMsg);
            session.getTransaction().commit();
        }catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User ID does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Outbound message ID does not exist");
            }else{
                throw new HibernateException("Error updating outbound message", e);
            }
        }
    }   

    @Override
    public void delete(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OutboundMsg msg = session.get(OutboundMsg.class, id);
            session.remove(msg);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Outbound message ID does not exist");
            }else{
                throw new HibernateException("Error deleting outbound message", e);
            }
        }
    }   

    @Override
    public OutboundMsg findById(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            OutboundMsg msg = session.get(OutboundMsg.class, id);
            session.getTransaction().commit();
            return msg;
            }catch (Exception e) {
            throw new HibernateException("Error finding outbound message by id", e);
        }
    }      

    @Override
    public List<OutboundMsg> findAll() throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<OutboundMsg> outboundMsgs = session.createSelectionQuery("from OutboundMsg", OutboundMsg.class).list();
            session.getTransaction().commit();
            return outboundMsgs;
        }catch (Exception e) {
            throw new HibernateException("Error finding all outbound messages", e);
        }
    }      
}
