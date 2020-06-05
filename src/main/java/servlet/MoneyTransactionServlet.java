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
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    private BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        Long count = Long.valueOf(req.getParameter("count"));
        String nameTo = req.getParameter("nameTo");
        try {
            if(bankClientService.sendMoneyToClient(new BankClient(senderName,senderPass),nameTo,count)){
                resp.getWriter().write("The transaction was successful");
                resp.setStatus(HttpServletResponse.SC_OK);
            }else{
                resp.getWriter().write("transaction rejected");
            }

        } catch (DBException e) {
            e.printStackTrace();
        }
    }
}
