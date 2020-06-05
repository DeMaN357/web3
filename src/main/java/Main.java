
import dao.BankClientDAO;
import exception.DBException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import service.BankClientService;
import model.BankClient;
import servlet.ApiServlet;
import servlet.MoneyTransactionServlet;
import servlet.RegistrationServlet;

public class Main {
    public static void main(String[] args) throws Exception{
        ApiServlet apiServlet = new ApiServlet();
        RegistrationServlet registrationServlet = new RegistrationServlet();
        MoneyTransactionServlet moneyTransactionServlet = new MoneyTransactionServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(registrationServlet),"/registration");
        context.addServlet(new ServletHolder(moneyTransactionServlet),"/transaction");
        context.addServlet(new ServletHolder(apiServlet), "/api/*");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
