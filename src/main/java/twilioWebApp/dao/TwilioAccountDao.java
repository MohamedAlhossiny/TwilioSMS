package twilioWebApp.dao;

import twilioWebApp.model.TwilioAccount;
import org.hibernate.HibernateException;

public interface TwilioAccountDao extends GenericDao<TwilioAccount> {
    TwilioAccount findByUserId(Integer userId) throws HibernateException;
}
