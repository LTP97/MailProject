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

    public void deleteAccountByEmail(String mailAddress) throws HibernateException {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "FROM MailAccountEntity WHERE mailAddress = :mailAddress";
            Query<MailAccountEntity> query = session.createQuery(hql, MailAccountEntity.class);
            query.setParameter("mailAddress", mailAddress);
            MailAccountEntity accountEntity = query.uniqueResult();
            if (accountEntity != null) {
                session.remove(accountEntity);
            } else {
                System.out.println("No account found for the email: " + mailAddress);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new HibernateException("Error while deleting the account: " + mailAddress, e);
        }
    }

    public MailAccountEntity getEntityById(long id) throws HibernateException {
        MailAccountEntity result;
        try (Session session = sessionFactory.openSession()) {
            result = session.get(MailAccountEntity.class, id);
        }
        return result;
    }

    public MailAccountEntity getEntityByAddress(String mailAddress) throws HibernateException {
        MailAccountEntity result;
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM MailAccountEntity WHERE mailAddress = :mailAddress";
            Query<MailAccountEntity> query = session.createQuery(hql, MailAccountEntity.class);
            query.setParameter("mailAddress", mailAddress);
            result = query.uniqueResult();
        }
        return result;
    }

    public void updateEntityPassword(MailAccountEntity entity, String newPassword) throws HibernateException {
        long id = entity.getId();
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String hql = "UPDATE MailAccountEntity SET password = :password WHERE id = :id";
            Query<Void> query = session.createQuery(hql,null);
            query.setParameter("password", newPassword);
            query.setParameter("id", id);
            query.executeUpdate();
            session.getTransaction().commit();
            System.out.println("password updated");
        }
    }

    public void persistNewAccountEnitity(MailAccountEntity entity) throws HibernateException {
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
        }catch(Exception e){
            return null;
        }
        return result;
    }
}
