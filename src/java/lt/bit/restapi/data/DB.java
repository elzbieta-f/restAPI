package lt.bit.restapi.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletContext;

public class DB {

    public static List<BankStatement> readData(ServletContext app) throws IOException {
        List<BankStatement> list = new ArrayList();
        try (
                InputStream is = app.getResourceAsStream("/WEB-INF/bankStatements.csv");
                Reader r = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(r);) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    list.add(new BankStatement(line));
                } catch (IllegalArgumentException | ParseException ex){
                    //if line can't be parsed the object will be skipped
                }               

            }
        }
        return list;
    }

    public static void saveData(List<BankStatement> list, String filename) throws IOException {
        try (
                OutputStream os = new FileOutputStream("/WEB-INF/"+filename + ".csv");
                Writer w = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter bw = new BufferedWriter(w);) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (BankStatement bs : list) {
                String line
                        = bs.getAccountNumber() + ","
                        + sdf.format(bs.getOperationDate()) + ","
                        + bs.getBeneficiary() + ","
                        + (bs.getComment() != null ? bs.getComment() : "") + ","
                        + bs.getAmount() + ","
                        + bs.getCurrency();
                bw.write(line);
                bw.write("\r\n");
            }
            bw.flush();
            w.flush();
            os.flush();
        }
    }

    public static List<BankStatement> filterByDate(List<BankStatement> list, Date from, Date to) {
        List<BankStatement> filteredList = list.stream().
                filter(bs -> bs.getOperationDate().getTime() >= from.getTime()).
                filter(bs -> bs.getOperationDate().getTime() <= to.getTime()).
                toList();
        return filteredList;
    }
}
