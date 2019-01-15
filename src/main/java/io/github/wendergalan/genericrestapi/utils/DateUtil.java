package io.github.wendergalan.genericrestapi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * generic-rest-api
 * All rights reserved ©
 * *********************************************
 * File name: DateUtil.java
 * Created by : Wender Galan
 * Date created : 12/01/2019
 * *********************************************
 */
public class DateUtil {

    /**
     * The constant MASCARA_DD_MM_YYY.
     */
    public static final String MASCARA_DD_MM_YYY = "dd/MM/yyyy";
    /**
     * The constant MASCARA_DD_MM_YYY_HH_MM_SS_A.
     */
    public static final String MASCARA_DD_MM_YYY_HH_MM_SS_A = "dd/MM/yyyy hh:mm:ss a";
    /**
     * The constant MASCARA_DD_MM_YYY_HH_MM_SS.
     */
    public static final String MASCARA_DD_MM_YYY_HH_MM_SS = "dd/MM/yyyy HH:mm:ss";
    /**
     * The constant MASCARA_HH_MM_SS_A.
     */
    public static final String MASCARA_HH_MM_SS_A = "HH:mm:ss a";
    /**
     * The constant MASCARA_HH_MM_SS.
     */
    public static final String MASCARA_HH_MM_SS = "HH:mm:ss";

    /**
     * Convert data date.
     *
     * @param value   the value
     * @param mascara the mascara
     * @return the date
     */
    public static Date convertData(String value, String mascara){
        try{
            SimpleDateFormat format = new SimpleDateFormat(mascara);
            return format.parse(value);
        }catch (ParseException ex){
            return null;
        }
    }
}
