package twilioWebApp.dao;

import java.util.List;
import org.hibernate.HibernateException;
public interface GenericDao<T> {
    void save(T entity) throws HibernateException;
    void update(T entity) throws HibernateException;
    void delete(Integer id) throws HibernateException;
    T findById(Integer id) throws HibernateException;
    List<T> findAll() throws HibernateException;
}
