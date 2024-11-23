package org.wifijava.mailproject.persistence.repository;

import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;

import java.util.List;


@AllArgsConstructor
public class MailAccountRepository {
    private final SessionFactory sessionFactory;

    public MailAccountEntity getEntityById(long id) throws HibernateException {
        MailAccountEntity result;
        try (Session session = sessionFactory.openSession()) {
            result = session.get(MailAccountEntity.class, id);
        }
        return result;
    }

    public void updateEntityPassword(long id, String newPassword) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "UPDATE MailAccountEntity SET password = :password WHERE id = :id";
            TypedQuery<MailAccountEntity> query = session.createQuery(hql, MailAccountEntity.class);
            query.setParameter("password", newPassword);
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    public void persistNewAccountEnitity(MailAccount entity) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    public List<MailAccountEntity> getAllAccounts() {
        List<MailAccountEntity> result;
        try (Session session = sessionFactory.openSession()) {
            Query<MailAccountEntity> query = session.createQuery("FROM MailAccountEntity", MailAccountEntity.class);
            result = query.getResultList();
        }
        return result;
    }

    public MailAccountEntity getFirstMailAccount() {
        MailAccountEntity result;
        try (Session session = sessionFactory.openSession()) {
            Query<MailAccountEntity> query = session.createQuery("FROM MailAccountEntity", MailAccountEntity.class);
            query.setMaxResults(1);
            result = query.uniqueResult();
        }
        return result;
    }
}
