package lt.bit.restapi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lt.bit.restapi.data.BankStatement;
import lt.bit.restapi.data.DB;

import org.json.JSONObject;

@WebServlet(name = "BankStatementsServlet", urlPatterns = {"/bankStatements/*"})
public class BankStatementsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs *
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reqUrl = request.getPathInfo();
        String jsonData = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (null == reqUrl) {
            jsonData = "{ }";
        } else {
            switch (reqUrl) {
                case "/":
                    List<BankStatement> list = DB.readData(request.getServletContext());
                    if (list != null) {
                        String json = "[\n";
                        for (BankStatement bs : list) {
                            json += "{\n";
                            json += "\"accountNumber\": " + JSONObject.quote(bs.getAccountNumber()) + ",\n";
                            json += "\"operationDate\": " + JSONObject.quote(sdf.format(bs.getOperationDate())) + ",\n";
                            json += "\"beneficiary\": " + JSONObject.quote(bs.getBeneficiary()) + ",\n";
                            json += "\"comment\": " + JSONObject.quote(bs.getComment()) + ",\n";
                            json += "\"amount\": " + bs.getAmount() + ",\n";
                            json += "\"currency\": " + JSONObject.quote(bs.getCurrency()) + "\n";
                            json += "},";
                        }

                        if (json.endsWith(",")) {
                            jsonData = json.substring(0, json.length() - 1) + "\n]";
                        }
                    } else {
                        jsonData = "{ }";
                    }
                    break;
                case "/accounts":
                    Set<String> set = DB.getAccounts(request.getServletContext());
                    System.out.println(set);
                    if (!set.isEmpty()) {
                        String json = "[\n";
                        json = set.stream().map(s -> "\"" + s + "\"" + ", ").reduce(json, String::concat);
                        System.out.println(json);
                        if (json.endsWith(", ")) {
                            jsonData = json.substring(0, json.length() - 2) + "\n]";
                        }
                        System.out.println(jsonData);
                    }
                    break;
                default:
                    jsonData = "{ }";
                    break;
            }
        }
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonData);
        out.flush();
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String reqUrl = request.getRequestURI();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromStr = request.getParameter("from");
        String toStr = request.getParameter("to");
        Date from = null;
        Date to = null;
        try {
            from = sdf.parse(fromStr);
        } catch (ParseException ex) {
            from = new Date(0);
        }
        try {
            to = sdf.parse(toStr);
        } catch (ParseException ex) {
            to = new Date();
        }
        switch (reqUrl) {
            case "/restapi/bankStatements":
        try {
                List<BankStatement> filteredByDate = DB.filterByDate(DB.readData(request.getServletContext()), from, to);
                if (filteredByDate != null) {
                    DB.saveData(request.getServletContext(), filteredByDate);
                }
                response.sendRedirect("./");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                response.setStatus(404);
                response.sendRedirect("./");
            }
            break;
            case "/restapi/bankStatements/accounts":
                String account = request.getParameter("account");
                if (account == null) {
                    response.setStatus(404);
                    response.sendRedirect("../");
                }
                List<BankStatement> bs = DB.filterByAccountNumber(DB.readData(request.getServletContext()), account);
                String jsonData = "";
                if (!bs.isEmpty()) {
                    String currency = bs.get(0).getCurrency();
                    BigDecimal sum = DB.filterByDate(bs, from, to).
                            stream().map(bsa -> bsa.getAmount()).
                            reduce(BigDecimal.ZERO, BigDecimal::add);
                    System.out.println(sum);
                    jsonData += "{\n\"accountNumber\": " + JSONObject.quote(account) + ",\n"
                            + "\"balance\": " + sum + ",\n"
                            + "\"currency\": " + JSONObject.quote(currency) + "\n}";
                } else {
                    jsonData = "{ }";
                }
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(jsonData);
                out.flush();

                break;
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
