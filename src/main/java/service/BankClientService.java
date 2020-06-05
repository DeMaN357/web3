package service;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;
import org.w3c.dom.ls.LSOutput;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientByName(String name) throws DBException {
        try {
            return getBankClientDAO().getClientByName(name);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public List<BankClient> getAllClient() throws DBException {
        try {
            return getBankClientDAO().getAllBankClient();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public boolean addClient(BankClient client) throws DBException, SQLException {
        if (getBankClientDAO().checkClientHasExist(client.getName())) {
            return false;
        } else {
            try {
                return getBankClientDAO().addClient(client);
            } catch (SQLException e) {
                throw new DBException(e);
            }
        }
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) throws DBException {
        try {
            if (getClientByName(name) != null && getBankClientDAO().validateClient(sender.getName(), sender.getPassword()) &&
                    getBankClientDAO().isClientHasSum(sender.getName(), value) && value >= 0){

                getBankClientDAO().updateClientsMoney(sender.getName(), name, value);
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=root");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    public static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
