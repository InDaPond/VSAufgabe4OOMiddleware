package nameservice;

public abstract class NameService { //Schnittstelle zum Namensdienst public


    /**
     * Meldet ein Objekt (servant) beim Namensdienst an.
     * @param servant
     * @param name
     */

    public abstract void rebind(Object servant, String name);

    /** Liefert eine generische Objektreferenz zu einem Namen.
     *
     * @param name
     * @return
     */
    public abstract Object resolve(String name);

}