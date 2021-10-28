import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.*;

public class Server_app {

    public static Logger lgm;           //static class variables for global access
    public static Handler fhl;
    public static int port_listen;

    private static Logger LOGGER = Logger.getLogger(String.valueOf(Server_app.class));

    static                                //static block for formatter output and logger
    {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n");
        lgm = Logger.getLogger((Server_app.class.getClass().getName()));
    }

    public static void filing_logging() throws Exception{      //handling output of logger into a file

        fhl = new FileHandler("./Server_app.log");
        SimpleFormatter simple = new SimpleFormatter();
        fhl.setFormatter(simple);
        lgm.addHandler(fhl);
    }

    public static void main(String[] args) throws Exception {

        filing_logging();

        if (args.length == 1){                            //accept command line arguments, if none provided take defaults
            port_listen = Integer.parseInt(args[0]);
        }
        else{
            port_listen = 7051;
        }

        try{
            Key_Store_Int msObj = new Key_Store_Int_Imp();
            Registry registry = LocateRegistry.createRegistry(port_listen);
            registry.bind("Key_Store_Int_Imp", msObj);
            lgm.log(Level.INFO, "OBJECT AND REGISTRY BINDING SUCCESSFUL");

        }catch(Exception e){
            lgm.log(Level.SEVERE, "UNABLE TO BIND!");
            System.out.println(e);
        }
    }
}