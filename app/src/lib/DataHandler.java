package lib;

import lib.types.PADState;
import lib.types.PADValue;
import lib.net.packages.PADPackage;

import java.sql.*;
import java.util.ArrayList;

import static lib.utils.Logging.log;

/**
 * Handle database connection and provide useful methods for acquiring data
 */
public class DataHandler {

    /**
     * Database connection
     */
    protected Connection connection;

    /**
     *
     * @param connectionString Connection string
     * @param user Database user
     * @param password Database password
     * @throws SQLException Exception thrown on connection error
     */
    public DataHandler(String connectionString, String user, String password) throws SQLException {
        try {
            connection = DriverManager.getConnection(connectionString, user, password);
        } catch (SQLException error) {
            log("Cannot connect.");
            error.printStackTrace();
            throw error;
        }
    }

    /**
     * Preare in statement argument, e.g: [1, 4, 5] -> "(1, 4, 5)"
     * @param values Values to use
     * @return In statement argument
     */
    protected String prepareInStmt(ArrayList<Integer> values) {
        String inStmt = "( ";

        for (int i = 0; i < values.size(); ++i) {
            if (i == values.size() - 1) {
                inStmt += values.get(i).toString() + " ";
            } else {
                inStmt += values.get(i).toString() + ", ";
            }
        }
        inStmt += ")";

        return inStmt;
    }

    /**
     * Get all experiment states
     *
     * @param experimentId Experiment identifier
     * @param methods Methods identifiers
     * @return Matching PAD states
     */
    public ArrayList<PADState> fetchAll(int experimentId, ArrayList<Integer> methods) {
        ArrayList<PADState> states = new ArrayList<PADState>();

        log("experimentId = %s", experimentId);
        log("methods = %s", methods);
        log("prepared = %s", prepareInStmt(methods));

        // 1. Get sessions
        ArrayList<Integer> sessionIds = new ArrayList<Integer>();
        String query = String.format("SELECT id FROM experiment_session WHERE method_id IN %s AND experiment_id = %s", prepareInStmt(methods), experimentId);
        try {
            ResultSet sessions = connection.createStatement().executeQuery(query);
            while (sessions.next()) {
                sessionIds.add(sessions.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        log("sessionIds = %s", sessionIds);
        log("prepared: %s", prepareInStmt(sessionIds));

        query = String.format(
            "SELECT p, a, d, cp, ca, cd, timestamp, method_id FROM pad_value " +
            "JOIN result_set ON result_set.id = pad_value.result_set_id " +
            "JOIN experiment_session ON result_set.session_id = experiment_session.id " +
            "WHERE result_set.session_id IN %s", prepareInStmt(sessionIds));

        log(query);

        try {
            ResultSet results = connection.createStatement().executeQuery(query);

            while (results.next()) {
                states.add(new PADState(new PADValue(results.getFloat(1), results.getFloat(4), results.getLong(7)),
                                        new PADValue(results.getFloat(2), results.getFloat(5), results.getLong(7)),
                                        new PADValue(results.getFloat(3), results.getFloat(6), results.getLong(7)),
                                        results.getLong(7),
                                        results.getInt(8)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return states;
    }

    /**
     * Insert a new PAD value
     *
     * @param data Package
     * @param resultSetId
     * @return true if succeeded, false otherwise
     */
    public boolean insertPadValue(PADPackage data, int resultSetId) {
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement("INSERT INTO pad_value(p, a, d, cp, ca, cd, timestamp, result_set_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setFloat(1, data.getState().getP().getValue());
            stmt.setFloat(2, data.getState().getA().getValue());
            stmt.setFloat(3, data.getState().getD().getValue());
            stmt.setFloat(4, data.getState().getP().getCertainty());
            stmt.setFloat(5, data.getState().getA().getCertainty());
            stmt.setFloat(6, data.getState().getD().getCertainty());
            stmt.setLong(7, data.getState().getTimestamp());
            stmt.setInt(8, resultSetId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log("Cannot insert PadValue record");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * @return Id of last inserted row
     */
    private int getLastInsertedId() {
        ResultSet results = null;

        try {
            results = connection.createStatement().executeQuery("SELECT last_insert_id()");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (results != null) {
            try {
                if (results.next()) {
                    return results.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * Create a new session
     *
     * @param experimentId Experiment id
     * @param methodId Method id
     * @return Created session id
     */
    public int createSession(int experimentId, int methodId) {
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement("INSERT INTO experiment_session(method_id, experiment_id) VALUES(?, ?)");
            stmt.setInt(1, methodId);
            stmt.setInt(2, experimentId);
            stmt.executeUpdate();
            return getLastInsertedId();
        }  catch (SQLException e) {
            log("Cannot insert PadValue record");
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * Create a new result set for session
     *
     * @param sessionId Session id
     * @return Created result set id
     */
    public int createResultSet(int sessionId) {
        try {
            PreparedStatement stmt;
            stmt = connection.prepareStatement("INSERT INTO result_set(session_id) VALUES(?)");
            stmt.setInt(1, sessionId);
            stmt.executeUpdate();
            return getLastInsertedId();
        }  catch (SQLException e) {
            log("Cannot insert ResultSet record");
            e.printStackTrace();
        }

        return -1;
    }
}
