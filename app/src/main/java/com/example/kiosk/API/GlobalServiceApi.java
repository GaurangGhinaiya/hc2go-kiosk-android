package com.example.kiosk.API;

public class GlobalServiceApi {

//    Homecare2go socket url
//    Https : 6464
//    http : 8484


  public static String PRINT_URL = "https://paladion.dolphin-products.com/paladion/api/v1/get-last-data/";


  // public static String MAIN_URL = "http://192.168.29.162:2345";
      public static String MAIN_URL = "http://kiosk.homecare2go.com";


    public static String BASEURL = MAIN_URL + "/Api/v1/";
    public static String Image_url = MAIN_URL + "/";


    public static String API_get_company_branches = BASEURL + "get-company-branches";
    public static String API_get_covid19_questions = BASEURL + "get-covid19-questions";
    public static String API_getKioskHomecareCovidQuestions = BASEURL + "get-kiosk-homecare-covid-questions";


    public static String API_check_staff_employee_code = BASEURL + "check-staff-employee-code";

    public static String API_check_volunteer_phonenumber = BASEURL + "check-volunteer-phonenumber";
    public static String API_add_volunteer_info = BASEURL + "add-volunteer-info";

    public static String API_check_provider_phonenumber = BASEURL + "check-provider-phonenumber";
    public static String API_add_provider_info = BASEURL + "add-provider-info";

    public static String API_update_family_guest_info = BASEURL + "update-family-guest-info";
    public static String API_update_volunteer_info = BASEURL + "update-volunteer-info";
    public static String API_update_provider_info = BASEURL + "update-provider-info";

    public static String API_get_resident = BASEURL + "get-resident";


    public static String API_sigin_staff = BASEURL + "sigin-staff";
    public static String API_siginProvider = BASEURL + "sigin-provider";
    public static String API_sigin_resident = BASEURL + "sigin-resident";
    public static String API_sigin_family_guest = BASEURL + "sigin-family-guest";
    public static String API_sigin_volunteer = BASEURL + "sigin-volunteer";


    public static String API_staff_signout = BASEURL + "signout-staff";
    public static String API_visitor_signout = BASEURL + "signout-visitor";
    public static String API_resident_signout = BASEURL + "signout-resident";


    public static String API_add_family_guest_info = BASEURL + "add-family-guest-info";
    public static String API_check_family_guest_phonenumber = BASEURL + "check-family-guest-phonenumber";
    public static String API_get_homecare_pass = BASEURL + "get-homecare-pass";
    public static String API_add_branch_wise_device = BASEURL + "add-branch-wise-device";
    public static String API_get_branch_wise_device = BASEURL + "get-branch-wise-device/";


//      define('USER_TYPE_APP_HEALTHCARE_PROVIDER', 1);
//      define('USER_TYPE_APP_FAMILY_GUEST', 2);
//      define('USER_TYPE_APP_VOLUNTEER', 3);
//      define('USER_TYPE_APP_RESIDENT', 4);
//      define('USER_TYPE_APP_COMMUNITY_STAFF', 5);

}
