import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.*;


class Store {

    private ConcurrentHashMap<String, String> hmap;
    private static Store instance = new Store();

    private Store(){
        hmap = new ConcurrentHashMap<>();
    }

//    private Store(){
//        hmap = new HashMap<>();
//    }

    public static Store getInstance() {
        return instance;
    }

    public void setMap(ConcurrentHashMap<String, String> val) {
        hmap = val;
    }

    public ConcurrentHashMap<String, String> getMap() {
        return hmap;
    }
}

public class Key_Store_Int_Imp extends UnicastRemoteObject implements Key_Store_Int, Runnable {

    public static String IP_client = null;
    public static Logger lgm;           //static class variables for global access
    public static Handler fhl;
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

    private ArrayList request_field;
    private Thread th;
    Store store_obj = Store.getInstance();
    public ArrayList res;

    public Key_Store_Int_Imp() throws Exception {
        super();
        filing_logging();
    }

    public void start() throws InterruptedException {
//        lgm.log(Level.INFO, "Thread started...");
        if (th == null) {
            th = new Thread(this);
            th.start();
            th.join();
        }
    }

    public Key_Store_Int_Imp(ArrayList request_field) throws RemoteException {
        this.request_field = request_field;
    }

    public ArrayList remote_put_pair(ArrayList request) throws Exception {
        IP_client = getClientHost();
        Key_Store_Int_Imp serverTh = new Key_Store_Int_Imp(request);
        serverTh.start();

        return serverTh.setter();
    }

    public ArrayList remote_get_pair(ArrayList request) throws Exception {
        IP_client = getClientHost();
        Key_Store_Int_Imp serverTh = new Key_Store_Int_Imp(request);
        serverTh.start();

        return serverTh.setter();
    }

    public ArrayList remote_del_pair(ArrayList request) throws Exception {
        IP_client = getClientHost();
        Key_Store_Int_Imp serverTh = new Key_Store_Int_Imp(request);
        serverTh.start();

        return serverTh.setter();
    }

    @Override
    public void run() {

        try {
            String operation = (String) request_field.get(0);
            String key = (String) request_field.get(1);

            if ((operation.equalsIgnoreCase("put")) && (key != "") && ((String) request_field.get(2) != "")) {
                if (request_field.size() == 3)
                    res = put_pair(request_field);
            } else if ((operation.equalsIgnoreCase("get")) && (key != "")) {
                if (request_field.size() == 2)
                    res = get_pair(request_field);
            } else if ((operation.equalsIgnoreCase("del")) && (key != "")) {
                if (request_field.size() == 2)
                    res = delete_pair(request_field);
            } else {
                //log into logger and send the status back to the client
                res = new ArrayList();
                lgm.log(Level.SEVERE, "ERR- Datagram " + request_field + " received from client "+ IP_client +" is invalid!");
                res.add("ERR- Datagram " + request_field + " is invalid!");
                System.out.println("ERR- Datagram " + request_field + " is invalid!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized ArrayList put_pair(ArrayList messages){

        ConcurrentHashMap<String, String> retrieved = store_obj.getMap();  //class store
        ArrayList<String> response = new ArrayList<>();

        if (!retrieved.containsKey((String) messages.get(1))) {
            retrieved.put((String) messages.get(1), (String) messages.get(2));
            lgm.log(Level.INFO,"Request "+messages+" from client "+IP_client+" performed on Hashmap");

            store_obj.setMap(retrieved);
            response.add("ACK-Added pair! "+ messages.get(1)+":"+messages.get(2));
            response.add("Data-Store contents: "+store_obj.getMap());
            System.out.println("ACK-Added pair!");
            System.out.println(store_obj.getMap());
        }
        else{
            System.out.println("ERR-Could not add Key, already exists!");
            response.add("ERR-Could not add Key, already exists!");
            lgm.log(Level.WARNING,"Key "+messages.get(1)+ " already exists");
        }
        return response;
    }

    public synchronized ArrayList get_pair(ArrayList messages) {

        ConcurrentHashMap<String, String> retrieved = store_obj.getMap();
        ArrayList<String> response = new ArrayList<>();

        String k = (String) messages.get(1);                  //send to the client, else return and log an error
        String v;

        if (retrieved.containsKey(k)){
            v = (String) retrieved.get(k);

            response.add("ACK-Fetched pair! " + k + ":" + v);
            response.add("Data-Store contents: "+store_obj.getMap());
            System.out.println("ACK-Fetched pair! " + k + ":" + v);
            lgm.log(Level.INFO,"Request "+messages+" from client "+IP_client+" performed on Hashmap");

        }
        else{
            System.out.println("ERR-Could not find Key to get, DNE!");
            response.add("ERR-Could not find Key to get, DNE!");
            lgm.log(Level.WARNING,"Key: "+messages.get(1)+ " DNE in Hashmap!");
        }
        return response;
    }

    public synchronized ArrayList delete_pair(ArrayList messages) {

        ConcurrentHashMap<String, String> retrieved = store_obj.getMap();
        ArrayList<String> response = new ArrayList<>();

        //send response to the client, else return and log an error
        if (retrieved.containsKey((String) messages.get(1))){
            retrieved.remove((String) messages.get(1));

            store_obj.setMap(retrieved);
            response.add("ACK-Deleted pair with key! "+messages.get(1));
            response.add("Data-Store contents: "+store_obj.getMap());
            System.out.println("ACK-Deleted pair! ");
            System.out.println(store_obj.getMap());
            lgm.log(Level.INFO,"Request "+messages+" from client "+IP_client+" performed on Hashmap");

        }
        else{
            System.out.println("ERR-Could not find Key to delete, DNE!");
            response.add("ERR-Could not find Key to delete, DNE!");
            lgm.log(Level.WARNING,"Key: "+messages.get(1)+ " to be deleted DNE in Hashmap");
        }
        return response;
    }

    public synchronized ArrayList setter(){                       //for accessing the result from the main() function
        return this.res;
    }
}