package twilioWebApp.dao;

import twilioWebApp.model.User;
import org.hibernate.HibernateException;
public interface UserDao extends GenericDao<User> {
    User findByUsername(String username) throws HibernateException;
}
