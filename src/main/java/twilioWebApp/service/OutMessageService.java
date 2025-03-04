package twilioWebApp.service;

import twilioWebApp.model.OutboundMsg;
import java.util.List;
import org.hibernate.HibernateException;

public interface OutMessageService {
    public void delete(Integer id) throws HibernateException;
    public void save(OutboundMsg msg) throws HibernateException;
    public List<OutboundMsg> findAll() throws HibernateException;
    public OutboundMsg findById(Integer id) throws HibernateException;
    public List<OutboundMsg> findByUserId(Integer userId) throws HibernateException;
}
