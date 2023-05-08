package ua.rudniev.taxi.transaction;

/**
 * This class has fields and methods that are enable execute code in one transaction
 */
public interface TransactionManager {
    default <T> T doInTransaction(Execution<T> execution) {
        return doInTransaction(execution, true);
    }

    <T> T doInTransaction(Execution<T> execution, boolean readOnly);

    interface Execution<T> {
        T execute();
    }
}
