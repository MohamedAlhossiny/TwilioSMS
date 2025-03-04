package twilioWebApp.dao;

import twilioWebApp.model.InboundMsg;
import java.util.List;
import java.util.Date;
import org.hibernate.HibernateException;

public interface InboundMsgDao extends GenericDao<InboundMsg> {
    List<InboundMsg> findByFromNumber(String fromNumber) throws HibernateException;
    List<InboundMsg> findByToNumber(String toNumber) throws HibernateException;
    List<InboundMsg> findByDate(Date date1, Date date2) throws HibernateException;
    List<InboundMsg> findByBody(String body) throws HibernateException;
    List<InboundMsg> findByUserId(Integer userId) throws HibernateException;  
}
