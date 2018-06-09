package mware_lib;

public class NameServiceProtocol {

    public static final String REBIND = "rebind";
    public static final String RESOLVE = "resolve";


    public static String createRequest(String type,String hostname,int hostport) {
        return String.format("%s,%s,%d",type,hostname,hostport);
//        switch (type) {
//            case REBIND:
//                return createRebindRequest(hostname,hostport);
//            case RESOLVE:
//                return createResolveRequest(hostname,hostport);
//            default:
//                return null;
//            //ToDo
//        }
    }

//    private static String createResolveRequest(String hostname, int hostport) {
//        return String.format("%s,%s,%d",REBIND,hostname,hostport);
//    }
//
//    private static String createRebindRequest(String hostname, int hostport) {
//        return null;
//    }

}
