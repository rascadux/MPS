import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import bank.BankAccount;

class BankAccountTest {
    
    BankAccount bank;

    @BeforeEach
    void setup(){
        bank = new BankAccount(100);
    }

    @Test
    @DisplayName("Test for withdraw")
    void testWithdraw(){
        assertTrue(bank.withdraw(50));
        assertEquals(50, bank.getBalance());
    }

    @Test
    @DisplayName("Test for withdraw without money")
    void testWithdrawWithoutMoney(){
        assertTrue(!bank.withdraw(111));
        assertEquals(100, bank.getBalance());
    }

    @Test
    @DisplayName("Test for deposit")
    void testDeposit(){
        assertEquals(105, bank.deposit(5));
    }

    @Test
    @DisplayName("Test for deposit with negative amount")
    void testDepositNegative(){
        try{
            bank.deposit(-10);
        }catch(IllegalArgumentException e){
            assertEquals("Amount cannot be negative", e.getMessage());
        }
    }

    @Test
    @DisplayName("Test for payment")
    void testPayment(){
        double payment = bank.payment(1000, 0.05, 12);
        payment = Math.round(payment * 100.0) / 100.0;
        assertEquals(112.83, payment);
    }

    @Test
    @DisplayName("Test for pending")
    void testPending(){
        double pending = bank.pending(1000, 0.05, 12, 1);
        pending = Math.round(pending * 100.0) / 100.0;
        assertEquals(937.17, pending);
    }

    @Test
    @DisplayName("Test for pending in month 2")
    void testPendingMonth2(){
        double pending = bank.pending(1000, 0.05, 12, 2);
        pending = Math.round(pending * 100.0) / 100.0;
        assertEquals(871.21, pending);   
    }

}

