package com.clinicmanager.repository;

import com.clinicmanager.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class BaseRepository<T, ID> {

    private final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // ---------- generic CRUD ----------

    public T save(T entity) {
        return inTransaction(em -> {
            em.persist(entity);
            return entity;
        });
    }

    public T update(T entity) {
        return inTransaction(em -> em.merge(entity));
    }

    public Optional<T> findById(ID id) {
        return readOnly(em -> Optional.ofNullable(em.find(entityClass, id)));
    }

    public List<T> findAll() {
        return readOnly(em -> em.createQuery(
                        "SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList());
    }

    public void deleteById(ID id) {
        inTransaction(em -> {
            T found = em.find(entityClass, id);
            if (found != null) {
                em.remove(found);
            }
            return null;
        });
    }

    // ---------- the "execute around" helpers ----------

    protected <R> R readOnly(Function<EntityManager, R> work) {
        EntityManager em = JpaUtil.createEntityManager();
        try {
            return work.apply(em);
        } finally {
            em.close();
        }
    }

    protected <R> R inTransaction(Function<EntityManager, R> work) {
        EntityManager em = JpaUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            R result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}