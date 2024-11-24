package org.wifijava.mailproject.persistence.repository;

import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.entity.MailMessageEntity;

import java.util.List;

@AllArgsConstructor
public class MailMessageRepository {
    private final SessionFactory sessionFactory;

    public void persistMessage(MailMessageEntity entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    public MailMessageEntity getMessageById(long id){
        MailMessageEntity result;
        try (Session session = sessionFactory.openSession()) {
            result = session.get(MailMessageEntity.class, id);
        }
        return result;
    }

    public MailMessageEntity[] getMessagesByOwnerAndLabel(MailAccountEntity account, String label) {
        List<MailMessageEntity> result;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String queryString = "from MailMessageEntity where belongsTo = :belongsToId and label = :label order by filePath desc";
            TypedQuery<MailMessageEntity> query = session.createQuery(queryString, MailMessageEntity.class);
            query.setParameter("belongsToId", account);
            query.setParameter("label",label);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result.toArray(MailMessageEntity[]::new);
    }

    public MailMessageEntity[] getMessagesByOwner(MailAccountEntity account) {
        List<MailMessageEntity> result;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String queryString = "from MailMessageEntity where belongsTo = :belongsToId order by filePath desc";
            TypedQuery<MailMessageEntity> query = session.createQuery(queryString, MailMessageEntity.class);
            query.setParameter("belongsToId", account);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result.toArray(MailMessageEntity[]::new);
    }
}
