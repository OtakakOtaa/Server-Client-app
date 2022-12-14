package Server;

public class ServerContract
{

    public static class Operations
    {
        public final static String SIGN_IN = "SIGN_IN";
        public final static String SIGN_UP = "SIGN_UP";
        public final static String CREATE_NEW_COMPANY = "CREATE_NEW_COMPANY";
        public final static String ACCOUNT_VALIDATE_INFO = "ACCOUNT_VALIDATE_INFO";
        public final static String UN_BLOCK_ACCOUNT = "UN_BLOCK_ACCOUNT";
        public final static String BLOCK_ACCOUNT = "BLOCK_ACCOUNT";
        public final static String UPDATE_ACCOUNT_INFO = "UPDATE_ACCOUNT_INFO";
        public final static String GET_ALL_EMPLOYEES = "GET_ALL_EMPLOYEES";
        public final static String ACCOUNT_ACK = "ACK";

        public static final String ADD_TRANSACTION  = "ADD_TRANSACTION";
        public static final String ALL_TRANSACTION  = "ALL_TRANSACTION";

        public final static String EXIT = "EXIT";
        public final static String RETURN = "RETURN";

    }

    public static class Result
    {
        public final static String SUCCESS_KEY = "SUCCESS_KEY";
        public final static String FAILED_KEY = "FAILED_KEY";

    }

    public static class ResultDetails
    {
        public final static String COMPANY_FAILED = "COMPANY_FAILED";
        public final static String USER_FAILED = "USER_FAILED";
    }

}

