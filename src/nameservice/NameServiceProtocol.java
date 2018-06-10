package nameservice;


/**
 * protocol how to communicate with a NameService.
 */
public class NameServiceProtocol {


    public static final String REBIND = "rebind";
    public static final String RESOLVE = "resolve";
    public static final String UNKNOWN = "unknown";
    public static final String SUCCESS = "ok";
    public static final String FAILURE = "nok";


    public static String createRequest(String type, String objectName, String hostname, int hostport) {
        return String.format("%s,%s,%s,%d", type, objectName, hostname, hostport);
    }


    public static String getRequestType(String request) {
        switch (request.split(",")[0]) {
            case REBIND:
                return REBIND;
            case RESOLVE:
                return RESOLVE;
            default:
                return UNKNOWN;
        }
    }

    public static String getRequestObjectName(String request) {
        return request.split(",")[1];
    }

    public static String getRequestHost(String request) {
        return request.split(",")[2];
    }

    public static String getRequestPort(String request) {
        return request.split(",")[3];
    }

    public static String createResolveResponse(String objectName, String locationHost, String locationPort){
            return String.format("%s,%s,%s,%s",SUCCESS,objectName,locationHost,locationPort);
    }

    public static String extractObjectHost(String[] response){
        return response[0];
    }

    public static String extractObjectPort(String[] response){
        return response[1];
    }

    public static String resolveFailed(){
        return FAILURE;
    }

    public static String createUnknownProtocol(){
        return String.format("%s,%s,%s,%s",UNKNOWN,"","","");
    }


}
