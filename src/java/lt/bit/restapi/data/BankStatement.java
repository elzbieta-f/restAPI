package lt.bit.restapi.data;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class BankStatement {

    private String accountNumber;

    private Date operationDate;

    private String beneficiary;

    private String comment;

    private BigDecimal amount;

    private String currency;

    public BankStatement() {
    }

    /**
     * Parses provided line and creates object from parsed data. Line must have
     * following fields separated by comma(",") accountNumber - String,
     * operationDate - Date in format yyyy-MM-dd, beneficiary - String, comment
     * - String, amount - BigDecimal, currency - String
     * If the number of fields after parsing a line is not equal 6, the values 
     * are not assigned to the object properties.
     *
     * @param line
     * @throws NullPointerException when line is null
     * @throws NumberFormatException when when amount is not decimal number;
     * @throws ParseException when date does not conform to specified pattern;
     */
    public BankStatement(String line) throws ParseException {
        String[] parts = line.split(",");
        if (parts.length == 6) {
            this.accountNumber = parts[0];
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.operationDate = sdf.parse(parts[1]);
            this.beneficiary = parts[2];
            this.comment = parts[3];
            this.amount = new BigDecimal(parts[4]);
            this.currency = parts[5];
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.accountNumber);
        hash = 97 * hash + Objects.hashCode(this.operationDate);
        hash = 97 * hash + Objects.hashCode(this.beneficiary);
        hash = 97 * hash + Objects.hashCode(this.comment);
        hash = 97 * hash + Objects.hashCode(this.amount);
        hash = 97 * hash + Objects.hashCode(this.currency);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BankStatement other = (BankStatement) obj;
        if (!Objects.equals(this.accountNumber, other.accountNumber)) {
            return false;
        }
        if (!Objects.equals(this.beneficiary, other.beneficiary)) {
            return false;
        }
        if (!Objects.equals(this.comment, other.comment)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        if (!Objects.equals(this.operationDate, other.operationDate)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BankStatement{" + "accountNumber=" + accountNumber + ", operationDate=" + operationDate + ", beneficiary=" + beneficiary + ", comment=" + comment + ", amount=" + amount + ", currency=" + currency + '}';
    }

}
