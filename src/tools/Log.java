package tools;

public class Log {

    private static boolean registerDetails = true;

    public static final void registerDetails(boolean active) {
        registerDetails = active;
    }

    public static final void error(String msg) {
        System.err.println("Error: " + msg);
        System.err.flush();
    }

    public static final void info(String msg) {
        System.out.println("Log:   " + msg);
        System.out.flush();
    }

    public static final void detail(String msg) {
        if(!registerDetails){
            return;
        }

        System.out.println(" (d)  " + msg);
        System.out.flush();
    }
}