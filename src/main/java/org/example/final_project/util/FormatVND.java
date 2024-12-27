package org.example.final_project.util;

import java.text.NumberFormat;
import java.util.Locale;

public class FormatVND {
    public static String formatCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = currencyFormatter.format(amount);

        return formattedAmount.replace("₫", "").trim();
    }
    
}
