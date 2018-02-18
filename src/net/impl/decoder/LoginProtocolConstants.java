package net.impl.decoder;

import java.util.function.Predicate;

/**
 * The type Login protocol constants.
 */
public class LoginProtocolConstants {


    /**
     * The constant LOGIN_REQUEST.
     */
    public static final int LOGIN_REQUEST = 14;
    /**
     * The constant UPDATE.
     */
    public static final int UPDATE = 15;
    /**
     * The constant NEW_SESSION.
     */
    public static final int NEW_SESSION = 16;
    /**
     * The constant RECONNECT.
     */
    public static final int RECONNECT = 18;
    /**
     * The constant PROTOCOL_REVISION.
     */
    public static final int PROTOCOL_REVISION = 317;
    /**
     * The constant MAX_USERNAME_CHARS.
     */
    public static final int MAX_USERNAME_CHARS = 12;
    /**
     * The constant MAX_PASSWORD_CHARS.
     */
    public static final int MAX_PASSWORD_CHARS = 12;
    /**
     * The constant LOGIN_BLOCK_KEY.
     */
    public static int LOGIN_BLOCK_KEY = (0x24 + 0x1 + 0x1 + 0x2);
    /**
     * The constant LOGIN_MAGIC_NUMBER.
     */
    public static int LOGIN_MAGIC_NUMBER = 255;
    /**
     * The constant MIN_PASSWORD_CHARS.
     */
    public static int MIN_PASSWORD_CHARS = 6;
    /**
     * The constant VALID_PASSWORD_PREDICATE.
     */
    public static final Predicate<String> VALID_PASSWORD_PREDICATE = (password) -> password != null &&
            password.length() >= LoginProtocolConstants.MIN_PASSWORD_CHARS && password.length() <=
            LoginProtocolConstants.MAX_PASSWORD_CHARS;
    /**
     * The constant MIN_USERNAME_CHARS.
     */
    public static int MIN_USERNAME_CHARS = 1;
    /**
     * The constant VALID_USERNAME_PREDICATE.
     */
    public static Predicate<String> VALID_USERNAME_PREDICATE = (username) ->
            username != null && username.length() >= LoginProtocolConstants.MIN_USERNAME_CHARS
                    && username.length() <= LoginProtocolConstants.MAX_USERNAME_CHARS;


    /**
     * The constant EXCHANGE_SESSION_KEYS.
     */
    public static boolean EXCHANGE_SESSION_KEYS = false;
    /**
     * The constant TRY_AGAIN.
     */
    public static int TRY_AGAIN = 1;
    /**
     * The constant LOGIN_SUCCESS.
     */
    public static int LOGIN_SUCCESS = 2;
    /**
     * The constant INVALID_USERNAME_OR_PASSWORD.
     */
    public static int INVALID_USERNAME_OR_PASSWORD = 3;
    /**
     * The constant ACCOUNT_DISABLED.
     */
    public static int ACCOUNT_DISABLED = 4;
    /**
     * The constant WORLD_UPDATE.
     */
    public static int WORLD_UPDATE = 5;
    /**
     * The constant ALREADY_LOGGED_IN.
     */
    public static int ALREADY_LOGGED_IN = 6;
    /**
     * The constant WORLD_FULL.
     */
    public static int WORLD_FULL = 7;
    /**
     * The constant LOGIN_SERVER_OFFLINE.
     */
    public static int LOGIN_SERVER_OFFLINE = 8;
    /**
     * The constant LOGIN_LIMIT_EXCEEDED.
     */
    public static int LOGIN_LIMIT_EXCEEDED = 9;
    /**
     * The constant BAD_SESSION_ID.
     */
    public static int BAD_SESSION_ID = 10;
    /**
     * The constant REJECTED_SESSION.
     */
    public static int REJECTED_SESSION = 11;
    /**
     * The constant MEMBERS_REQUIRED.
     */
    public static int MEMBERS_REQUIRED = 12;
    /**
     * The constant TRY_DIFFERENT_WORLD.
     */
    public static int TRY_DIFFERENT_WORLD = 13;
    /**
     * The constant SERVER_UPDATE_IN_PROGRESS.
     */
    public static int SERVER_UPDATE_IN_PROGRESS = 14;
    /**
     * The constant LOGIN_ATTEMPTS_EXCEEDED.
     */
    public static int LOGIN_ATTEMPTS_EXCEEDED = 16;
    /**
     * The constant MEMBERS_ONLY_AREA.
     */
    public static int MEMBERS_ONLY_AREA = 17;
    /**
     * The constant INVALID_LOGIN_SERVER.
     */
    public static int INVALID_LOGIN_SERVER = 20;
    /**
     * The constant WAIT_TO_TRANSFER_WORLD.
     */
    public static int WAIT_TO_TRANSFER_WORLD = 21;
}
