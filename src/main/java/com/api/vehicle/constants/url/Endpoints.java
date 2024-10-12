package com.api.vehicle.constants.url;

public class Endpoints {

    private Endpoints(){}

    public static final String VEHICLE = "/vehicle";

    //PATH VARIABLES
    public static final String PATH_VARIABLE_ID = "/{id}";

    //REQUEST PARAMS
    public static final String PARAM_PAGE_NUMBER = "pageNumber={pageNumber}";
    public static final String PARAM_PAGE_SIZE = "pageSize={pageSize}";
    public static final String PARAM_TYPE = "type={type}";

    //PARTIAL URL
    public static final String FIND_ALL_VEHICLES = "/findAll";
    public static final String FIND_ALL_VEHICLES_BY_TYPE = "/findAllByType";

    //COMPLETE URL'S
    public static final String URL_FIND_VEHICLE = VEHICLE + PATH_VARIABLE_ID;
    public static final String URL_FIND_ALL_VEHICLES = VEHICLE + FIND_ALL_VEHICLES
            + getFormatedRequestParams(PARAM_PAGE_NUMBER, PARAM_PAGE_SIZE);
    public static final String URL_FIND_ALL_VEHICLES_BY_TYPE = VEHICLE + FIND_ALL_VEHICLES_BY_TYPE
            + getFormatedRequestParams(PARAM_TYPE, PARAM_PAGE_NUMBER, PARAM_PAGE_SIZE);
    public static final String URL_REMOVE_VEHICLE = VEHICLE + PATH_VARIABLE_ID;

    private static String getFormatedRequestParams(String ... requestParams){
        return "?" + String.join("&", requestParams);
    }
}
