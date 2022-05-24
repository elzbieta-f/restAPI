package lt.bit.restapi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lt.bit.restapi.data.BankStatement;
import lt.bit.restapi.data.DB;

import org.json.JSONObject;

@WebServlet(name = "BankStatementsServlet", urlPatterns = {"/bankStatements"})
public class BankStatementsServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws ParseException if the objects cannot be parsed from file
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BankStatement> list = DB.readData(request.getServletContext());
        if (list != null) {
            for (BankStatement bs : list) {
                String json = "{\n";
                json += "\"accountNumber\": " + JSONObject.quote(bs.getAccountNumber()) + ",\n";
                json += "\"operationDate\": " + JSONObject.quote(sdf.format(bs.getOperationDate())) + ",\n";
                json += "\"beneficiary\": " + JSONObject.quote(bs.getBeneficiary()) + ",\n";
                json += "\"comment\": " + JSONObject.quote(bs.getComment()) + ",\n";
                json += "\"amount\": " + bs.getAmount() + ",\n";
                json += "\"currency\": " + JSONObject.quote(bs.getCurrency()) + "\n";
                json += "}";
                if (!list.listIterator().hasNext()){
                } else {
                    json+=",\n";
                } 
                response.getOutputStream().println(json);
            }

        } else {
            //If list wasn't found return an empty JSON object.
            response.getOutputStream().println("{}");
        }
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
        processRequest(request, response);
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
