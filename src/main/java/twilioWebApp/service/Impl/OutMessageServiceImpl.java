package twilioWebApp.service.Impl;

import twilioWebApp.service.OutMessageService;
import twilioWebApp.model.OutboundMsg;
import java.util.List;
import twilioWebApp.dao.OutboundMsgDao;
import twilioWebApp.dao.Impl.OutboundMsgDaoImpl;

import org.hibernate.HibernateException;
import jakarta.validation.ConstraintViolationException;
public class OutMessageServiceImpl implements OutMessageService     {

    private OutboundMsgDao outboundMsgDao;

    public OutMessageServiceImpl() {
        this.outboundMsgDao = new OutboundMsgDaoImpl();
    }

    @Override
    public void delete(Integer id) throws HibernateException {
        try {
            outboundMsgDao.delete(id);
        } catch (HibernateException e) {
            throw new HibernateException("Error deleting outbound message", e);
        }
    }

    @Override
    public void save(OutboundMsg msg) throws HibernateException {
        try {
            outboundMsgDao.save(msg);
        } catch (ConstraintViolationException e) {
            if(e.getConstraintViolations().iterator().next().getMessage().contains("user_id")) {
                throw new HibernateException("User id is required");
            }else if(e.getConstraintViolations().iterator().next().getMessage().contains("id")) {
                throw new HibernateException("Message is already exist");
            }else{
                throw new HibernateException("Error saving outbound message", e);
            }
        }
    }

    @Override
    public List<OutboundMsg> findAll() throws HibernateException {
        try {
            return outboundMsgDao.findAll();
        } catch (HibernateException e) {
            throw new HibernateException("Error finding all outbound messages", e);
        }
    }

    @Override
    public OutboundMsg findById(Integer id) throws HibernateException {
        try {
            return outboundMsgDao.findById(id);
        } catch (HibernateException e) {
            throw new HibernateException("Error finding outbound message by id", e);
        }
    }

    @Override
    public List<OutboundMsg> findByUserId(Integer userId) throws HibernateException {
        try {
            return outboundMsgDao.findByUserId(userId);
        } catch (HibernateException e) {
            throw new HibernateException("Error finding outbound messages by user id", e);
        }
    }
    
    
}