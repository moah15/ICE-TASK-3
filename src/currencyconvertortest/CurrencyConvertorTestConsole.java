package currencyconvertortest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConvertorTestConsole {
// Replace with your actual API key
    static final String API_ENDPOINT = "https://v6.exchangerate-api.com/v6/0f3e4039c071545b8878133d/latest/USD";
    static String[] CURRENCIES = {
            "USD", "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG",
            "AZN", "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB",
            "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP",
            "CNY", "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD",
            "EGP", "ERN", "ETB", "EUR", "FJD", "FKP", "FOK", "GBP", "GEL", "GGP",
            "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HRK", "HTG",
            "HUF", "IDR", "ILS", "IMP", "INR", "IQD", "IRR", "ISK", "JEP", "JMD",
            "JOD", "JPY", "KES", "KGS", "KHR", "KID", "KMF", "KRW", "KWD", "KYD",
            "KZT", "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA",
            "MKD", "MMK", "MNT", "MOP", "MRU", "MUR", "MVR", "MWK", "MXN", "MYR",
            "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN",
            "PGK", "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF",
            "SAR", "SBD", "SCR", "SDG", "SEK", "SGD", "SHP", "SLL", "SOS", "SRD",
            "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY",
            "TTD", "TVD", "TWD", "TZS", "UAH", "UGX", "UYU", "UZS", "VEF", "VND",
            "VUV", "WST", "XAF", "XCD", "XDR", "XOF", "XPF", "YER", "ZAR", "ZMW",
            "ZWL"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Available Currencies:");
        for (int i = 0; i < CURRENCIES.length; i++) {
            System.out.print(CURRENCIES[i] + " ");
            // Print a new line after every 10th currency
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        System.out.println("\n");

        System.out.print("Enter amount to convert: ");
        double amount = scanner.nextDouble();

        System.out.print("Enter currency to convert from (e.g., USD): ");
        String fromCurrency = scanner.next().toUpperCase();

        System.out.print("Enter currency to convert to (e.g., EUR): ");
        String toCurrency = scanner.next().toUpperCase();

        try {
            double result = convert(amount, fromCurrency, toCurrency);
            System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, result, toCurrency);
        } catch (IOException e) {
            System.err.println("Error while converting: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid currency code: " + e.getMessage());
        }
    }

    public static double convert(double amount, String fromCurrency, String toCurrency) throws IOException {
        // Make Request
        URL url = new URL(API_ENDPOINT);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(request.getInputStream()));
        JsonObject jsonobj = root.getAsJsonObject();

        // Accessing object
        JsonObject conversionRates = jsonobj.getAsJsonObject("conversion_rates");

        // Ensure that the currencies exist in the conversion_rates object
        if (!conversionRates.has(fromCurrency) || !conversionRates.has(toCurrency)) {
            throw new IllegalArgumentException("Invalid currency codes");
        }

        double rateFrom = conversionRates.get(fromCurrency).getAsDouble();
        double rateTo = conversionRates.get(toCurrency).getAsDouble();

        return amount * rateTo / rateFrom;
    }
}