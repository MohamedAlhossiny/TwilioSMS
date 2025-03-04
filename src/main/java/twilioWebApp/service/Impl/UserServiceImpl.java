package twilioWebApp.service.Impl;

import java.util.List;
import twilioWebApp.service.UserService;
import twilioWebApp.dao.UserDao;
import twilioWebApp.dao.Impl.UserDaoImpl;
import twilioWebApp.model.User;
import org.hibernate.HibernateException;
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }


    @Override
    public User authenticate(String username, String password) throws HibernateException{
        User user = userDao.findByUsername(username);
        if (user == null || !user.getPasswd().equals(password)) {
            return null;
        }
        return user;
    }

    @Override
    public User register(User user) throws HibernateException{
        userDao.save(user);
        return user;
    }

    @Override
    public List<User> findAll() throws HibernateException{
        return userDao.findAll();
    }

    @Override
    public User findById(Integer id) throws HibernateException{
        return userDao.findById(id);
    }

    @Override
    public void update(User user) throws HibernateException{
        userDao.update(user);
    }   

    @Override
    public void delete(Integer id) throws HibernateException{
        userDao.delete(id);
    }
}
