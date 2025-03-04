package twilioWebApp.service;

import twilioWebApp.model.TwilioAccount;
import twilioWebApp.model.OutboundMsg;
import java.util.List;
import org.hibernate.HibernateException;
public interface TwilioService {
    TwilioAccount findByUserId(Integer userId) throws HibernateException;
    void sendSms(OutboundMsg msg) throws HibernateException;
    void save(TwilioAccount account) throws HibernateException;
    void update(TwilioAccount account) throws HibernateException;
    void delete(Integer id) throws HibernateException;
    TwilioAccount findById(Integer id) throws HibernateException;
    List<TwilioAccount> findAll() throws HibernateException;
}
