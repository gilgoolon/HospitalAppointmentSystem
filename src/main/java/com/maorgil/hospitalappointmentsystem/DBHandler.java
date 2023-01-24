package com.maorgil.hospitalappointmentsystem;


import com.maorgil.hospitalappointmentsystem.entity.AppointmentsEntity;
import com.maorgil.hospitalappointmentsystem.entity.DoctorsEntity;
import com.maorgil.hospitalappointmentsystem.entity.UsersEntity;
import com.maorgil.hospitalappointmentsystem.entity.WorkingHoursEntity;

import javax.persistence.*;
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
     * Persist (add) a new entry to the database
     * @param entity represents the entity to persist to the DB.
     * @return true if the entry has been persisted successfully to the DB, false otherwise.
     */
    public boolean persistEntity(Object entity) {
        if (!connect())
            return false;

        try {
            _entityManager.persist(entity);
            return true;
        } catch (PersistenceException e) {
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
     * @param c represents the class of the returned entity's stream.
     *          See function declaration and EntityManager.createNamedQuery() for more detail.
     * @return the result of the query, a stream of entities of the given type.
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
     * @param c represents the class of the returned entity's stream.
     *          See function declaration and EntityManager.createNamedQuery() for more detail.
     * @param parameters represents a list of parameters for the prepared query.
     * @return the result of the query, a stream of entities of the given type.
     */
    public <T> List<T> executeSelectQuery(String query, Class<T> c, List<Object> parameters) {
        if (!connect()) // first, connect to the database
            return Collections.emptyList(); // if failed to connect return an empty stream

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

        try {
            Query q = _entityManager.createNamedQuery(query);

            // set the parameters of the prepared statement
            for (int i = 0; i < parameters.size(); i++)
                q.setParameter(i + 1, parameters.get(i));

            q.executeUpdate(); // returns the number of rows affected, prob. not needed
            return true;
        } catch (PersistenceException e) {
            return false;
        }
    }

    public UsersEntity getUserById(String id) {
        List<Object> params = new ArrayList<>();
        params.add(id);

        String query = "SELECT u FROM UsersEntity u WHERE u.id = ?1";
        List<UsersEntity> list = executeSelectQuery(query, UsersEntity.class, params);

        return list.size() > 0 ? list.get(0) : null;
    }

    public DoctorsEntity getDoctorById(String id) {
        List<Object> params = new ArrayList<>();
        params.add(id);

        String query = "SELECT d FROM DoctorsEntity d WHERE d.id = ?1";
        List<DoctorsEntity> list = executeSelectQuery(query, DoctorsEntity.class, params);

        return list.size() > 0 ? list.get(0) : null;
    }

    /**
     * Browse the database for doctors that may fit the given search.
     * The search will be done by looking at first name prefix and last name prefix.
     * @param text represents the text to search for.
     * @return a stream of doctors entities who match the search text.
     */
    public List<DoctorsEntity> getDoctorsBySearch(String text) {
        List<Object> params = new ArrayList<>();
        params.add(text + "%"); // regex to match starts with text
        params.add(text + "%"); // Two params for the first and last name

        String query = "SELECT d FROM DoctorsEntity d JOIN UsersEntity u ON d.id = u.id WHERE u.firstName LIKE ?1 OR u.lastName LIKE ?2";

        return executeSelectQuery(query, DoctorsEntity.class, params);
    }

    public WorkingHoursEntity getWorkingHoursByPK(String doctor_id, int week_day) {
        List<Object> params = new ArrayList<>();
        params.add(doctor_id);
        params.add(week_day);

        String query = "SELECT wh FROM WorkingHoursEntity wh WHERE wh.doctor_id = ?1 AND wh.week_day = ?2";
        List<WorkingHoursEntity> list = executeSelectQuery(query, WorkingHoursEntity.class, params);

        return list.size() > 0 ? list.get(0) : null;
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
     * @return a stream of appointment entities of the given user.
     */
    public List<AppointmentsEntity> getAppointmentsByPatientId(String id) {
        List<Object> params = new ArrayList<>();
        params.add(id);

        String query = "SELECT a FROM AppointmentsEntity a WHERE a.patient_id = ?1";
        return executeSelectQuery(query, AppointmentsEntity.class, params);
    }
}