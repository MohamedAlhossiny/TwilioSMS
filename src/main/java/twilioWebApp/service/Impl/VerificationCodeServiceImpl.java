package twilioWebApp.service.Impl;

import twilioWebApp.model.VerificationCode;
import twilioWebApp.service.VerificationCodeService;
import twilioWebApp.dao.VerificationCodeDao;
import twilioWebApp.dao.Impl.VerificationCodeDaoImpl;

import java.util.List;
import java.util.Random;
import java.util.Date;
import org.hibernate.HibernateException;

public class VerificationCodeServiceImpl implements VerificationCodeService {
    private VerificationCodeDao verificationCodeDao;
    private Random random;

    public VerificationCodeServiceImpl() {
        this.verificationCodeDao = new VerificationCodeDaoImpl();
        this.random = new Random();
    }

    @Override
    public void save(VerificationCode code) throws HibernateException{
            verificationCodeDao.save(code);

    }

    @Override
    public void update(VerificationCode code) throws HibernateException{
        verificationCodeDao.update(code);
    }

    @Override
    public VerificationCode findById(Integer id) throws HibernateException{
        return verificationCodeDao.findById(id);
    }
    //find by user id 
    //Return previous codes for the user sorted by created_at descending.
    @Override
    public List<VerificationCode> findByUserId(Integer userId) throws HibernateException{
        return verificationCodeDao.findByUserId(userId);
    }

    @Override
    public List<VerificationCode> findAll() throws HibernateException{
        return verificationCodeDao.findAll();
    }

    @Override
    public VerificationCode generateCode(Integer userId, Date expiredDate) throws HibernateException{
        Integer code = random.nextInt(100000, 999999);//6 digits code
        VerificationCode verificationCode = new VerificationCode(code, expiredDate, userId);
        return verificationCode;
    }

    @Override
    public boolean verifyCode(Integer userId, Integer code) throws HibernateException{
        try{
            VerificationCode verificationCode = findByUserId(userId).get(0);//get the first code(the list is sorted by created_at descending)
            if (verificationCode.getVerification_code().equals(code) && 
                    verificationCode.getExpired_date().after(new Date())) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {
            throw new HibernateException("No verification code found for user " + userId, e);
        }
        return false;
    }

    @Override
    public void deleteAllCodesByUser(Integer userId) throws HibernateException{
        verificationCodeDao.deleteAllCodesByUser(userId);
    }

    @Override
    public void deleteAllCodes() throws HibernateException{
        verificationCodeDao.deleteAllCodes();
    }

    @Override
    public void deleteCode(Integer id) throws HibernateException{
        verificationCodeDao.delete(id);
    }
}
