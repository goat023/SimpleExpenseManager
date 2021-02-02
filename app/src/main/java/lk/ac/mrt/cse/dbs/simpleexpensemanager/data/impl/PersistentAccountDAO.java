package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO implements AccountDAO {
    private final DBHelper dbHelper;

    public PersistentAccountDAO(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return dbHelper.getAccountNumberList();
    }

    @Override
    public List<Account> getAccountsList() {
        return dbHelper.getAccountList();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account ac = dbHelper.getAccount(accountNo);
        if (ac != null) {
            return ac;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        dbHelper.insertAccount(account.getAccountNo(),account.getAccountHolderName(),account.getBankName(),account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        Account ac = dbHelper.getAccount(accountNo);
        if (ac == null) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        dbHelper.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account ac = dbHelper.getAccount(accountNo);
        if (ac == null) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }


        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                ac.setBalance(ac.getBalance() - amount);
                break;
            case INCOME:
                ac.setBalance(ac.getBalance() + amount);
                break;
        }
        dbHelper.updateAccount(accountNo,ac.getBalance());
    }
}
