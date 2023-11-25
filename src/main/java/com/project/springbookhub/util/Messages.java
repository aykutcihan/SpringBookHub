package com.project.springbookhub.util;

public class Messages {
    private Messages(){}

    public static final String ALREADY_REGISTER_MESSAGE_USERNAME = "Error: User with username %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_EMAIL = "Error: User with email %s is already registered";
    public static final String ALREADY_REGISTER_MESSAGE_PHONE = "Error: User with phone number %s is already registered";

    public static final String USERNAME_NOT_FOUND = "Error: Username with USERNAME %s not found";
    public static final String USER_NOT_FOUND = "Error: User with ID %d not found";
    public static final String BOOK_NOT_FOUND = "Error: Book with ID %d not found";
    public static final String ORDER_NOT_FOUND = "Error: Order with ID %d not found";
    public static final String REVIEW_NOT_FOUND = "Error: Review with ID %d not found";

    public static final String INVALID_PASSWORD = "Error: Invalid password";

    public static final String ROLE_NOT_FOUND = "There is no role like that, check the database";
    public static final String ROLE_ALREADY_EXIST = "Role already exist in DB";
    public static final String USER_DELETE = "%s successfully deleted";
    public static final String USER_UPDATE = "%s successfully updated";
    public static final String USER_SAVED = "%s successfully saved";
    public static final String USER_AUTHENTICATED = "%s successfully authenticated";


    public static final String ADMIN_DELETED_SUCCESSFULLY = "Admin is deleted successfully";
    public static final String NOT_FOUND_USER_MESSAGE = "User with ID %d not found";
    public static final String NOT_PERMITTED_METHOD_MESSAGE = "This method is not permitted";



}
