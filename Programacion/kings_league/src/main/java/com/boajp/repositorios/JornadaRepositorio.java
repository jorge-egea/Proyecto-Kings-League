package com.boajp.repositorios;

import com.boajp.modelos.JornadaEntidad;
import jakarta.persistence.*;

import java.util.List;

public class JornadaRepositorio {

    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;
    private EntityTransaction transaction;

    public JornadaRepositorio() {
        entityManagerFactory = Persistence.createEntityManagerFactory("default");
        entityManager = entityManagerFactory.createEntityManager();
    }

    public void insertar(JornadaEntidad jornada) throws Exception {
        transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(jornada);
            transaction.commit();
        } catch (Exception exception) {
            transaction.rollback();
            throw new Exception("Error al intentar insertar jornada");
        }
    }

    public void eliminar(JornadaEntidad jornada) throws Exception {
        transaction = entityManager.getTransaction();
        try {
            JornadaEntidad jornadaEncontrada = entityManager.find(JornadaEntidad.class, jornada.getCodJornada());
            transaction.begin();
            if (jornadaEncontrada != null)
                entityManager.remove(jornadaEncontrada);
            transaction.commit();
        } catch (Exception exception) {
            transaction.rollback();
            throw new Exception("Error al intentar eliminar jornada");
        }
    }

    public void modificar(JornadaEntidad jornada) throws Exception {
        transaction = entityManager.getTransaction();
        try {
            JornadaEntidad jornadaEncontrada = entityManager.find(JornadaEntidad.class, jornada.getCodJornada());
            transaction.begin();
            jornadaEncontrada.setNumero(jornada.getNumero());
            jornadaEncontrada.setFecha(jornada.getFecha());
            jornadaEncontrada.setUbicacion(jornada.getUbicacion());
            jornadaEncontrada.setSplit(jornada.getSplit());
            jornadaEncontrada.setListaPartidos(jornada.getListaPartidos());
            if (jornadaEncontrada != null)
                entityManager.persist(jornadaEncontrada);
            transaction.commit();

        } catch (Exception exception) {
            transaction.rollback();
            throw new Exception("Error al intentar modificar jornada");
        }
    }

    public List<JornadaEntidad> buscarTodasJornadas() throws Exception {
        try {
            String jpql = "SELECT j FROM JornadaEntidad j";
            TypedQuery<JornadaEntidad> query = entityManager.createQuery(jpql, JornadaEntidad.class);
            return query.getResultList();
        } catch (Exception exception) {
            throw new Exception("Error al intentar extraer jornadas.", exception);
        }
    }

    public JornadaEntidad buscarUltimaJornada() throws Exception {
        try {
            String query = "SELECT j FROM JornadaEntidad j WHERE j.codJornada = (SELECT MAX(m.codJornada) FROM JornadaEntidad m)";
            TypedQuery<JornadaEntidad> resultado = entityManager.createQuery(query, JornadaEntidad.class);
            return resultado.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception exception) {
            throw new Exception("Error al intentar extraer jornadas.", exception);
        }
    }


}
