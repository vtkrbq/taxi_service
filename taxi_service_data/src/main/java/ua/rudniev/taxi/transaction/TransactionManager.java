package ua.rudniev.taxi.transaction;

/**
 * This class enables code execution in one transaction
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
