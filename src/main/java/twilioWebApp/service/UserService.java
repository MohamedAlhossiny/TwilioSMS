package twilioWebApp.service;

import java.util.List;
import twilioWebApp.model.User;
import org.hibernate.HibernateException;
public interface UserService {
    User authenticate(String username, String password) throws HibernateException;
    User register(User user) throws HibernateException;
    User findById(Integer id) throws HibernateException;
    List<User> findAll() throws HibernateException;
    void update(User user) throws HibernateException;
    void delete(Integer id) throws HibernateException;
}