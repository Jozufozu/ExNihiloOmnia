package exnihiloomnia.registries;

import java.util.HashMap;

public interface IRegistry<T> {

    /**
     * Load all entries into some HashMap to be retrieved with {@link #getEntries()}
     */
    void initialize();

    /**
     * @return A HashMap containing all entries set in {@link #initialize()}
     */
    HashMap<String, T> getEntries();

    /**
     * Empties the registry
     */
    void clear();
}
