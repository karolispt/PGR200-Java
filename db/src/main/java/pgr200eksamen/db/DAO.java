package pgr200eksamen.db;

import javax.sql.*;
import java.sql.*;
import java.util.*;

class Tuple<A, B>
{
    private A fst;
    private B snd;

    public Tuple(A a, B b)
    {
        fst = a;
        snd = b;
    }

    public A getFst() { return fst; }
    public B getSnd() { return snd; }
}

class DAOTuple
{
    Tuple<Connection, PreparedStatement> data;

    public DAOTuple(Connection conn, PreparedStatement stmt)
    {
        data = new Tuple<Connection, PreparedStatement>(conn, stmt);
    }

    public Connection getConnection()
    {
        return data.getFst();
    }

    public PreparedStatement getPreparedStatement()
    {
        return data.getSnd();
    }

    public void close() throws SQLException
    {
        data.getSnd().close();
        data.getFst().close();
    }
}

public abstract class DAO<T> {
    private DataSource source;
    public String table, idName, columns, values;

    DAO(DataSource source)
    {
        this.source = source;
    }

    public Connection Connection() throws SQLException
    {
        return source.getConnection();
    }

    DataSource DataSource()
    {
        return source;
    }

    public DAOTuple Statement(String sql) throws SQLException
    {
        Connection connection = Connection();
        PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        DAOTuple data = new DAOTuple(connection, stmt);

        return data;
    }

    public T selectSingle(int id) throws SQLException
    {
        Connection conn = Connection();
        Statement stmt = conn.createStatement();

        ResultSet result = stmt.executeQuery("SELECT * FROM " + table + " WHERE " + idName + " = " + id);
        result.next();

        T entity = getEntity(result);

        result.close();
        stmt.close();
        conn.close();

        return entity;
    }

    public List<T> selectAll() throws SQLException
    {
        Connection conn = Connection();
        Statement stmt = conn.createStatement();

        ResultSet result = stmt.executeQuery("SELECT * FROM " + table);

        List<T> entities = new ArrayList<T>();

        while (result.next())
        {
            T entity = getEntity(result);
            entities.add(entity);
        }

        result.close();
        stmt.close();
        conn.close();

        return entities;
    }

    public void insert(T entity) throws SQLException
    {
        DAOTuple data = Statement("INSERT INTO " + table + " " + columns + " VALUES " + values);

        executeSave(entity, data.getPreparedStatement());

        data.close();
    }

    public void update(T entity, int id) throws SQLException
    {
        DAOTuple data = Statement("UPDATE " + table + " SET" + columns + " = " + values + " WHERE " + idName + " = " + id);

        executeSave(entity, data.getPreparedStatement());

        data.close();
    }

    public void removeSingle(int id) throws SQLException
    {
        DAOTuple data = Statement("DELETE FROM " + table + " WHERE " + idName + " = " + id);

        data.getPreparedStatement().executeUpdate();

        data.close();
    }

    public void removeAll() throws SQLException
    {
        DAOTuple data = Statement("DELETE FROM " + table);

        data.getPreparedStatement().executeUpdate();

        data.close();
    }

    abstract void executeSave(T entity, PreparedStatement stmt) throws SQLException;
    abstract T getEntity(ResultSet result) throws SQLException;
}
