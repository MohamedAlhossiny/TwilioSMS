package twilioWebApp.service.Impl;

import twilioWebApp.service.TwilioService;
import twilioWebApp.model.TwilioAccount;
import twilioWebApp.dao.TwilioAccountDao;
import twilioWebApp.dao.Impl.TwilioAccountDaoImpl;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.List;
import twilioWebApp.model.OutboundMsg;
import org.hibernate.HibernateException;

public class TwilioServiceImpl implements TwilioService {
    private TwilioAccountDao twilioAccountDao;

    public TwilioServiceImpl() {
        this.twilioAccountDao = new TwilioAccountDaoImpl();
    }

    @Override
    public TwilioAccount findByUserId(Integer userId) throws HibernateException{
        return twilioAccountDao.findByUserId(userId);
    }

    @Override
    public void sendSms(OutboundMsg message) throws HibernateException{
        TwilioAccount account = findByUserId(message.getUser_id());
        Twilio.init(account.getSid(), account.getToken());
        Message msg = Message.creator(
            new PhoneNumber(message.getTo_num()),
            new PhoneNumber(message.getFrom_num()),
            message.getBody()
        ).create();
        if (msg.getStatus() == Message.Status.FAILED) {
            throw new RuntimeException("Failed to send SMS: " + msg.getErrorMessage());
        }
    }
    
    @Override
    public void save(TwilioAccount account) throws HibernateException{
        twilioAccountDao.save(account);

    }
    
    @Override
    public void update(TwilioAccount account) throws HibernateException{
        twilioAccountDao.update(account);
    }

    @Override
    public void delete(Integer id) throws HibernateException{
        twilioAccountDao.delete(id);
    }

    @Override
    public TwilioAccount findById(Integer id) throws HibernateException{
        return twilioAccountDao.findById(id);
    }

    @Override
    public List<TwilioAccount> findAll() throws HibernateException{
        return twilioAccountDao.findAll();
    }
}
