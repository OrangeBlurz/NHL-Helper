package com.example.fokoproject.utils;

import com.example.fokoproject.R;
import com.example.fokoproject.model.Team;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class
 */

public class Utils {

    /**
     * Map teams based on their name to drawable icons of their logos.
     * @param team team to be looked up
     * @return The id of the drawable for the team's logo. Placeholder image returned if team can't be found
     */
    public static int getDrawableForTeam(Team team) {
        String teamName = team.getName().toLowerCase();
        switch (teamName) {
            case "new jersey devils" :
                return R.drawable.new_jersey_devil;
            case "new york islanders" :
                return R.drawable.new_york_islanders;
            case "new york rangers" :
                return R.drawable.new_york_rangers;
            case "philadelphia flyers" :
                return R.drawable.philadelphia_flyers;
            case "pittsburgh penguins" :
                return R.drawable.pittsburgh_penguins;
            case "boston bruins" :
                return R.drawable.boston_bruins;
            case "buffalo sabres" :
                return R.drawable.buffalo_sabres;
            case "montréal canadiens" :
                return R.drawable.montreal_canadians;
            case "ottawa senators" :
                return R.drawable.ottawa_senators;
            case "toronto maple leafs" :
                return R.drawable.toronto_maple_leafs;
            case "carolina hurricanes" :
                return R.drawable.carolina_hurricanes;
            case "florida panthers" :
                return R.drawable.florida_panthers;
            case "tampa bay lightning" :
                return R.drawable.tampa_bay_lightning;
            case "washington capitals" :
                return R.drawable.washington_capitals;
            case "chicago blackhawks" :
                return R.drawable.chicago_blackhawks;
            case "detroit red wings" :
                return R.drawable.detroit_red_wings;
            case "nashville predators" :
                return R.drawable.nashville_predators;
            case "st. lous blues" :
                return R.drawable.st_louis_blues;
            case "calgary flames" :
                return R.drawable.calgary_flames;
            case "colorado avalanche" :
                return R.drawable.colorado_avalanche;
            case "edmonton oilers" :
                return R.drawable.edmonton_oilers;
            case "vancouver canucks" :
                return R.drawable.vancouver_canucks;
            case "anaheim ducks" :
                return R.drawable.anaheim_ducks;
            case "dallas stars" :
                return R.drawable.dallas_stars;
            case "los angeles kings" :
                return R.drawable.los_angeles_kings;
            case "san jose sharks" :
                return R.drawable.san_jose_sharks;
            case "columbus blue jackets" :
                return R.drawable.columbus_blue_jackets;
            case "minnesota wild" :
                return R.drawable.minnesota_wild;
            case "winnipeg jets" :
                return R.drawable.winnipeg_jets;
            case "arizona coyotes" :
                return R.drawable.arizona_coyotes;
            case "vegas golden knights" :
                return R.drawable.las_veags_golden_knights;

                default:
                    return R.drawable.ic_placeholder;
        }
    }

    private static Map<String, Locale> mLocaleMap;
    /**
     * Initialize the country mapping map
     */
    private static void initCountryCodeMapping() {
        String[] countries = Locale.getISOCountries();
        mLocaleMap = new HashMap<>(countries.length);
        for (String country : countries) {
            Locale locale = new Locale("", country);
            mLocaleMap.put(locale.getISO3Country().toUpperCase(), locale);
        }
    }

    /**
     * Utility function to transform ISO 3 country code to ISO 2 country codes
     * @param iso3CountryCode ISO 3 country code
     * @return ISO 2 country code
     */
    public static String iso3CountryCodeToIso2CountryCode(String iso3CountryCode) {
        if (mLocaleMap == null) {
            initCountryCodeMapping();
        }
        return mLocaleMap.get(iso3CountryCode).getCountry();
    }

    /**
     * Utility method to convert ISO2 country code to their display names
     * @param isoCode ISO@ country code
     * @return Name of the country
     */
    public static String countryNameFromIso(String isoCode) {
        return new Locale("", isoCode).getDisplayName();
    }
}
