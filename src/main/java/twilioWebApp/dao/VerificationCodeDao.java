package twilioWebApp.dao;

import twilioWebApp.model.VerificationCode;
import java.util.List;
import org.hibernate.HibernateException;

public interface VerificationCodeDao extends GenericDao<VerificationCode>{
    void deleteAllCodesByUser(Integer userId) throws HibernateException;
    void deleteAllCodes() throws HibernateException;
    List<VerificationCode> findByUserId(Integer userId) throws HibernateException;
}
