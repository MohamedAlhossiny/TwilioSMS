package twilioWebApp.dao.Impl;

import twilioWebApp.dao.InboundMsgDao;
import twilioWebApp.model.InboundMsg;
import java.util.List;
import java.util.Date;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import jakarta.validation.*;

public class InboundMsgDaoImpl implements InboundMsgDao {
    private final SessionFactory sessionFactory;

    public InboundMsgDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<InboundMsg> findByFromNumber(String fromNumber) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<InboundMsg> inboundMsgs = session.createSelectionQuery("from InboundMsg where from_num = :fromNumber", InboundMsg.class)
                .setParameter("fromNumber", fromNumber)
                .list();
            session.getTransaction().commit();
            return inboundMsgs;
        } catch (ConstraintViolationException e) {
            throw new HibernateException("Error finding inbound messages by from number", e);
        }
    }

    @Override
    public List<InboundMsg> findByToNumber(String toNumber) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<InboundMsg> inboundMsgs = session.createSelectionQuery("from InboundMsg where to_num = :toNumber", InboundMsg.class)
                .setParameter("toNumber", toNumber)
                .list();
            session.getTransaction().commit();
            return inboundMsgs;
        } catch (Exception e) {
            throw new HibernateException("Error finding inbound messages by to number", e);
        }
    }


    @Override
    public List<InboundMsg> findByDate(Date date1, Date date2) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<InboundMsg> inboundMsgs = session.createSelectionQuery("from InboundMsg where created_at between :date1 and :date2", InboundMsg.class)
                .setParameter("date1", date1)
                .setParameter("date2", date2)
                .list();
            session.getTransaction().commit();
            return inboundMsgs;
        } catch (Exception e) {
            throw new HibernateException("Error finding inbound messages by date", e);
        }
    }

    @Override
    public List<InboundMsg> findByBody(String body) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<InboundMsg> inboundMsgs = session.createSelectionQuery("from InboundMsg where body like :body", InboundMsg.class)
                .setParameter("body", "%" + body + "%")
                .list();
            session.getTransaction().commit();
            return inboundMsgs;
        } catch (Exception e) {
            throw new HibernateException("Error finding inbound messages by body", e);
        }
    }

    @Override
    public List<InboundMsg> findByUserId(Integer userId) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<InboundMsg> inboundMsgs = session.createSelectionQuery("from InboundMsg where user_id = :userId", InboundMsg.class)
                .setParameter("userId", userId)
                .list();
            session.getTransaction().commit();
            return inboundMsgs;
        } catch (Exception e) {
            throw new HibernateException("Error finding inbound messages by user id", e);
        }
    }

    @Override
    public void save(InboundMsg inboundMsg) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(inboundMsg);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User ID does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Inbound message ID already exists");
            }else{
                throw new HibernateException("Error saving inbound message", e);
            }
        }
    }

    @Override
    public void update(InboundMsg inboundMsg) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(inboundMsg);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User ID does not exist");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Inbound message ID does not exist");
            }else{
                throw new HibernateException("Error updating inbound message", e);
            }
        }
    }   

    @Override
    public void delete(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            InboundMsg msg = session.get(InboundMsg.class, id);
            session.remove(msg);
            session.getTransaction().commit();
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Inbound message ID does not exist");
            }else{
                throw new HibernateException("Error deleting inbound message", e);
            }
        }
    }

    @Override
    public InboundMsg findById(Integer id) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            InboundMsg msg = session.get(InboundMsg.class, id);
            session.getTransaction().commit();
            return msg;
        } catch (Exception e) {
            throw new HibernateException("Error finding inbound message by id", e);
        }
    }   

    @Override
    public List<InboundMsg> findAll() throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<InboundMsg> inboundMsgs = session.createSelectionQuery("from InboundMsg", InboundMsg.class).list();
            session.getTransaction().commit();
            return inboundMsgs;
        } catch (Exception e) {
            throw new HibernateException("Error finding all inbound messages", e);
        }
    }
}
    

