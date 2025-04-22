package model;

public class CurrentUser<T> {
    private static CurrentUser<?> instance;
    private T user;

    private CurrentUser() {}

    // Singleton instance getter (generic)
    public static <T> CurrentUser<T> getInstance() {
        if (instance == null) {
            instance = new CurrentUser<>();
        }
        @SuppressWarnings("unchecked")
        CurrentUser<T> typedInstance = (CurrentUser<T>) instance;
        return typedInstance;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public void clear() {
        user = null;
        instance = null;
    }
}
