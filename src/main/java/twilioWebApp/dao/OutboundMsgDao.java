package twilioWebApp.dao;

import twilioWebApp.model.OutboundMsg;
import java.util.List;
import java.util.Date;
import org.hibernate.HibernateException;
public interface OutboundMsgDao extends GenericDao<OutboundMsg> {   
    List<OutboundMsg> findByFromNumber(String fromNumber) throws HibernateException;
    List<OutboundMsg> findByToNumber(String toNumber) throws HibernateException;
    List<OutboundMsg> findByBody(String body) throws HibernateException;
    List<OutboundMsg> findByUserId(Integer userId) throws HibernateException;
    List<OutboundMsg> findByDate(Date date1, Date date2) throws HibernateException;
}
