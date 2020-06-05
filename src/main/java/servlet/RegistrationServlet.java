package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {

    private BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        Long money = Long.valueOf(req.getParameter("money"));
        try {
            if(bankClientService.addClient(new BankClient(name,password,money))){
                resp.getWriter().println("Add client successful");
                resp.setStatus(HttpServletResponse.SC_OK);
            }else{
                resp.getWriter().println("Client not add");
            }

        } catch (IOException | SQLException | DBException e) {
            e.printStackTrace();
        }
    }
}
