package twilioWebApp.service;

import twilioWebApp.model.VerificationCode;
import java.util.List;
import java.util.Date;
import org.hibernate.HibernateException;

public interface VerificationCodeService {
    void save(VerificationCode verificationCode) throws HibernateException;
    void update(VerificationCode verificationCode) throws HibernateException;
    VerificationCode findById(Integer id) throws HibernateException;
    List<VerificationCode> findByUserId(Integer userId) throws HibernateException;
    List<VerificationCode> findAll() throws HibernateException;
    VerificationCode generateCode(Integer userId, Date expiredDate) throws HibernateException;
    boolean verifyCode(Integer userId, Integer code) throws HibernateException;
    void deleteAllCodesByUser(Integer userId) throws HibernateException;
    void deleteAllCodes() throws HibernateException;
    void deleteCode(Integer id) throws HibernateException;
}
