import java.rmi.Remote;
import java.util.ArrayList;

public interface Key_Store_Int extends Remote {

    ArrayList remote_put_pair (ArrayList request) throws Exception;
    ArrayList remote_get_pair(ArrayList request) throws Exception;
    ArrayList remote_del_pair(ArrayList request) throws Exception;

}