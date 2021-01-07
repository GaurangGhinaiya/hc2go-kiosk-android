package com.example.kiosk.Utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * @author Administrator
 * <p/>
 * Class for methods which save settings values
 */
public class Preferences {


    private SharedPreferences pref;
    public static Preferences store;

    public static Preferences getInstance(Context context) {
        if (store == null)
            store = new Preferences(context);
        return store;
    }

    public Preferences(Context context) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setPRE_DeviceMacAddress(String link) {
        pref.edit().putString("DeviceMacAddress", link).commit(); }
    public String getPRE_DeviceMacAddress() {
        return pref.getString("DeviceMacAddress", "");
    }

    public void setPRE_DeviceName(String link) {
        pref.edit().putString("DeviceName", link).commit(); }
    public String getPRE_DeviceName() {
        return pref.getString("DeviceName", "");
    }

    public void setFACILITY(String link) {
        pref.edit().putString("FACILITY", link).commit(); }
    public String getFACILITY() {
        return pref.getString("FACILITY", "");
    }

    public void setTIMEZONE(String link) {
        pref.edit().putString("TIMEZONE", link).commit(); }
    public String getTIMEZONE() {
        return pref.getString("TIMEZONE", "");
    }

    public void setPrinter_MODEL(String link) {
        pref.edit().putString("Printer_MODEL", link).commit(); }
    public String getPrinter_MODEL() {
        return pref.getString("Printer_MODEL", "");
    }



    public void setWIFI_USB_PRINTER_id(String link) {
        pref.edit().putString("WIFI_USB_PRINTER_id", link).commit(); }
    public String getWIFI_USB_PRINTER_id() {
        return pref.getString("WIFI_USB_PRINTER_id", "");
    }

    public void setWIFI_USB_PRINTER_Type(String link) {
        pref.edit().putString("WIFI_USB_PRINTER_Type", link).commit(); }
    public String getWIFI_USB_PRINTER_Type() {
        return pref.getString("WIFI_USB_PRINTER_Type", "");
    }


    public void setBranchid(String link) {
        pref.edit().putString("Branchid", link).commit(); }
    public String getBranchid() {
        return pref.getString("Branchid", "");
    }


    public void setCompany_id(String link) {
        pref.edit().putString("company_id", link).commit(); }
    public String getCompany_id() {
        return pref.getString("company_id", "");
    }

    /*......................................*/

    public void setUser_Id(String link) {
        pref.edit().putString("User_Id", link).commit(); }
    public String getUser_Id() {
        return pref.getString("User_Id", "");
    }

    public void setUser_Name(String link) {
        pref.edit().putString("User_Name", link).commit(); }
    public String getUser_Name() {
        return pref.getString("User_Name", "");
    }


    public void setUser_phone(String link) {
        pref.edit().putString("User_phone", link).commit(); }
    public String getUser_phone() {
        return pref.getString("User_phone", "");
    }


    public void setUser_company(String link) {
        pref.edit().putString("User_company", link).commit(); }
    public String getUser_company() {
        return pref.getString("User_company", "");
    }


    public void setMain_Type(String link) {
        pref.edit().putString("Main_Type", link).commit(); }
    public String getMain_Type() {
        return pref.getString("Main_Type", "");
    }


    public void setpatient_list(String link) {
        pref.edit().putString("patient_list", link).commit(); }
    public String getpatient_list() {
        return pref.getString("patient_list", "");
    }



    public void setvehicle_model(String link) {
        pref.edit().putString("vehicle_model", link).commit(); }
    public String getvehicle_model() {
        return pref.getString("vehicle_model", "");
    }

    public void setvehicle_color(String link) {
        pref.edit().putString("vehicle_color", link).commit(); }
    public String getvehicle_color() {
        return pref.getString("vehicle_color", "");
    }


    public void setvehicle_plate(String link) {
        pref.edit().putString("vehicle_plate", link).commit(); }
    public String getvehicle_plate() {
        return pref.getString("vehicle_plate", "");
    }

    public void setfamily_type(String link) {
        pref.edit().putString("family_type", link).commit(); }
    public String getfamily_type() {
        return pref.getString("family_type", "");
    }


    public void setHomecarePassQList(String link) {
        pref.edit().putString("HomecarePassQList", link).commit(); }
    public String getHomecarePassQList() {
        return pref.getString("HomecarePassQList", "");
    }



}