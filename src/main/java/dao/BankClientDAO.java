package dao;

import com.sun.deploy.util.SessionState;
import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() throws SQLException {
        String query = "SELECT * FROM bank_client";
        List<BankClient> bankClients = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                long idSet = result.getLong("id");
                String name = result.getString("name");
                String password = result.getString("password");
                Long money = result.getLong("money");
                bankClients.add(new BankClient(idSet, name, password, money));
            }
        }
        return bankClients;
    }

    public boolean validateClient(String name, String password) throws SQLException {
        String query = "SELECT * from bank_client where name = ? AND password = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setNString(1, name);
            ps.setNString(2, password);
            ResultSet result = ps.executeQuery();
            return result.next();
        }
    }

    public void updateClientsMoney(String name, String nameTo, Long transactValue) throws SQLException {
        String query = "UPDATE bank_client SET money = ? where name = ? ";

        connection.setAutoCommit(false);

        try(PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, getClientByName(name).getMoney() - transactValue);
            ps.setString(2, name);
            ps.executeUpdate();

            ps.setLong(1,getClientByName(nameTo).getMoney() + transactValue);
            ps.setString(2,nameTo);
            ps.executeUpdate();

            connection.commit();

        } catch (SQLException e) {
            try{
                connection.rollback();
                connection.setAutoCommit(true);
            }catch (SQLException ignore){

            }
        }
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        String query = "SELECT * from bank_client where name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setNString(1, name);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                return expectedSum <= result.getLong("money");
            }
            return false;
        }
    }

    public BankClient getClientByName(String name) throws SQLException {
        String query = "SELECT * FROM bank_client where name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                long id = result.getLong("id");
                String name1 = result.getString("name");
                String password = result.getString("password");
                Long money = result.getLong("money");
                return new BankClient(id, name1, password, money);
            } else {
                return null;
            }
        }
    }

    public boolean addClient(BankClient client) throws SQLException {
        String query = "INSERT INTO bank_client (name, password, money) values (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getPassword());
            ps.setLong(3, client.getMoney());
            return ps.executeUpdate() > 0;
        }
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }

    public boolean checkClientHasExist(String name) throws SQLException {
        String query = "SELECT * from bank_client where name = ?";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setNString(1, name);
            ResultSet result = ps.executeQuery();
            return result.next();
        }
    }
}
