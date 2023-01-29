package com.maorgil.hospitalappointmentsystem;


import com.maorgil.hospitalappointmentsystem.entity.*;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.*;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.util.*;

public class DBHandler {
    private static final String PERSISTENCE_UNIT = "hospitalPU";

    private EntityManager _entityManager;
    private boolean isConnected;

    public DBHandler() {
        isConnected = false;
    }

    /**
     * Connect to the hospital database.
     * @return true if the connection attempt was successful, I.E if connected to the DB afterwards, false otherwise.
     */
    public boolean connect() {
        if (isConnected)
            return true;

        try {
            EntityManagerFactory _entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            _entityManager = _entityManagerFactory.createEntityManager();
            isConnected = true;
        } catch (PersistenceException e) {
            System.out.println("Error connecting to database...");
            isConnected = false;
            e.printStackTrace();
        }
        return isConnected;
    }

    /**
     * Persist a new entry to the database - change or add new
     * @param entity represents the entity to persist to the DB.
     * @return true if the entry has been persisted successfully to the DB, false otherwise.
     */
    public <T> boolean persistEntity(T entity, Class<T> c, Object pk) {
        if (!connect())
            return false;

        UserTransaction ut = null;
        try {
            ut = getTransaction();
            if (_entityManager.find(c, pk) != null) {
                System.out.println("Entity already exists in the database...");
                _entityManager.merge(entity);
            }
            else _entityManager.persist(entity);
            ut.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                assert ut != null;
                ut.rollback();
            } catch (Exception ignore) {}
            return false;
        }
    }

    /**
     * Remove an entry from the database
     * @param entity represents the entity to remove from the DB.
     * @return true if the entry has been removed successfully from the DB, false otherwise.
     */
    public boolean removeEntity(Object entity) {
        if (!connect())
            return false;

        UserTransaction ut = null;
        try {
            ut = getTransaction();
            _entityManager.remove(entity);
            ut.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                assert ut != null;
                ut.rollback();
            } catch (Exception ignore) {}
            return false;
        }
    }

    /**
     * Execute a SELECT query in the MySQL dialect.
     * <p>
     * Example query:
     * <p><b>
     *      =====SQL=====<p></p>
     *      SELECT u
     *      FROM Users u
     *      WHERE year(CURRENT_DATE) - year(u.birth_date) >= 21
     *      <p>=====SQL======
     *      </b></p>
     * @param query represents the SQL query to execute.
     * @param c represents the class of the returned entity's list.
     *          See function declaration and EntityManager.createNamedQuery() for more detail.
     * @return the result of the query, a list of entities of the given type.
     */
    public <T> List<T> executeSelectQuery(String query, Class<T> c) {
        return executeSelectQuery(query, c, new ArrayList<>());
    }

    /**
     * Execute a prepared SELECT query in the MySQL dialect.
     * <p>
     * Example query:
     * <p><b>
     *      =====SQL=====<p></p>
     *      SELECT u
     *      FROM Users u
     *      WHERE year(CURRENT_DATE) - year(u.birth_date) >= 21
     *      <p>=====SQL======
     *      </b></p>
     * @param query represents the SQL query to execute.
     * @param c represents the class of the returned entity's list.
     *          See function declaration and EntityManager.createNamedQuery() for more detail.
     * @param parameters represents a list of parameters for the prepared query.
     * @return the result of the query, a list of entities of the given type.
     */
    public <T> List<T> executeSelectQuery(String query, Class<T> c, List<Object> parameters) {
        if (!connect()) // first, connect to the database
            return Collections.emptyList(); // if failed to connect return an empty list

        try {
            TypedQuery<T> tq = _entityManager.createQuery(query, c);

            // set parameters in prepared query
            for (int i = 0; i < parameters.size(); i++)
                tq.setParameter(i + 1, parameters.get(i)); // parameters start from 1

            return tq.getResultList();
        } catch (PersistenceException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Execute an ALTER query in the MySQL dialect. Should not be used to alter the tables structure,
     * but only the data they contain.
     * <p>
     * Example query:
     * <p><b>
     *     =====SQL=====<p>
     *     INSERT INTO Users (id, first_name, last_name, birth_date, password, phone_number, email, is_admin)
     *     VALUES ('123456789', 'John', 'Doe', '1980-01-01', 'password123', '5555555555', 'johndoe@example.com', 0)
     *     <p>=====SQL======
     *     </b>
     * </p>
     * @param query represents the SQL query to execute.
     * @return true if the query has been executed successfully, false otherwise.
     */
    public boolean executeAlterQuery(String query) {
        return executeAlterQuery(query, new ArrayList<>()); // prepared statement with no args
    }

    /**
     * Execute a prepared ALTER query in the MySQL dialect. Should not be used to alter the tables structure,
     * but only the data they contain.
     * <p>
     * Example query:
     * <p><b>
     *     =====SQL=====<p>
     *     INSERT INTO Users (id, first_name, last_name, birth_date, password, phone_number, email, is_admin)
     *     VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8)
     *     <p>=====SQL======
     *     </b>
     * </p>
     * @param query represents the SQL query to execute.
     * @param parameters represents a list of parameters for the prepared query.
     * @return true if the query has been executed successfully, false otherwise.
     */
    public boolean executeAlterQuery(String query, List<Object> parameters) {
        if (!connect()) // first, connect to the database
            return false; // if failed to connect return  false

        UserTransaction ut = null;
        try {
            ut = getTransaction();
            Query q = _entityManager.createQuery(query);

            // set the parameters of the prepared statement
            for (int i = 0; i < parameters.size(); i++)
                q.setParameter(i + 1, parameters.get(i));

            q.executeUpdate(); // returns the number of rows affected, prob. not needed
            ut.commit();
            return true;
        } catch (Exception e) {
            try {
                assert ut != null;
                ut.rollback();
            } catch (Exception ignore) {}
            return false;
        }
    }

    private UserTransaction getTransaction() throws NamingException, SystemException, NotSupportedException {
        UserTransaction ut = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        ut.begin();
        _entityManager.joinTransaction();
        return ut;
    }

    public UsersEntity getUserById(String id) {
        if (!connect())
            return null;
        return _entityManager.find(UsersEntity.class, id);
    }

    public boolean validateLogin(String id, String password) {
        if (!connect())
            return false;
        UsersEntity user = getUserById(id);
        return user != null && user.getPassword().equals(password);
    }

    public DoctorsEntity getDoctorById(String id) {
        if (!connect())
            return null;
        return _entityManager.find(DoctorsEntity.class, id);
    }

    /**
     * Browse the database for doctors that may fit the given search.
     * The search will be done by looking at first name prefix, last name prefix
     * and full name prefix.
     * The result list will be sorted by the full name.
     * @param text represents the text to search for.
     * @return a list of doctors entities who match the search text.
     */
    public List<DoctorsEntity> getDoctorsBySearch(String text) {
        List<Object> params = new ArrayList<>();
        params.add(text + "%"); // regex to match starts with text
        params.add(text + "%"); // Two params for the first and last name
        params.add(text + "%"); // Match entire name

        String query = "SELECT d FROM DoctorsEntity d JOIN UsersEntity u ON d.id = u.id " +
                "WHERE u.firstName LIKE ?1 OR u.lastName LIKE ?2 OR u.firstName + u.lastName LIKE ?3 "
                + "ORDER BY u.firstName, u.lastName";

        return executeSelectQuery(query, DoctorsEntity.class, params);
    }

    public WorkingHoursEntity getWorkingHoursByPK(String doctor_id, int week_day) {
        if (!connect())
            return null;
        WorkingHoursEntityPK pk = new WorkingHoursEntityPK();
        pk.setDoctorId(doctor_id);
        pk.setWeekDay(week_day);
        return _entityManager.find(WorkingHoursEntity.class, pk);
    }

    public List<WorkingHoursEntity> getWorkingHoursById(String doctor_id) {
        List<Object> params = new ArrayList<>();
        params.add(doctor_id);

        String query = "SELECT wh FROM WorkingHoursEntity wh WHERE wh.doctor_id = ?1";
        return executeSelectQuery(query, WorkingHoursEntity.class, params);
    }

    /**
     * Get all the appointments for the given patient (past and future).
     * @param id represents the id of the given user.
     * @return a list of appointment entities of the given user.
     */
    public List<AppointmentsEntity> getAppointmentsByPatientId(String id) {
        List<Object> params = new ArrayList<>();
        params.add(id);

        String query = "SELECT a FROM AppointmentsEntity a WHERE a.patient_id = ?1";
        return executeSelectQuery(query, AppointmentsEntity.class, params);
    }

    public List<DoctorsEntity> getDoctors() {
        return getDoctorsBySearch("");
    }

    /**
     * Get all the working hours for the given doctor.
     * @param id represents the id of the given doctor.
     * @return a list of working hours entities of the given doctor.
     */
    public List<WorkingHoursEntity> getDoctorHours(String id) {
        List<Object> params = new ArrayList<>();
        params.add(id);

        String query = "SELECT wh FROM WorkingHoursEntity wh WHERE wh.doctorId = ?1";
        return executeSelectQuery(query, WorkingHoursEntity.class, params);
    }
}
