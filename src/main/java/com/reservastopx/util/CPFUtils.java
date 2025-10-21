package com.reservastopx.util;

public class CPFUtils {

    public static boolean isCPFValid(String cpf) {
        if (cpf == null) return false;

        // remove tudo que não for dígito
        cpf = cpf.replaceAll("\\D", "");

        if (!cpf.matches("\\d{11}")) return false;

        int[] weight1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weight2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++)
                sum += (cpf.charAt(i) - '0') * weight1[i];

            int mod = sum % 11;
            int digit1 = (mod < 2) ? 0 : 11 - mod;

            sum = 0;
            for (int i = 0; i < 9; i++)
                sum += (cpf.charAt(i) - '0') * weight2[i];
            sum += digit1 * weight2[9];

            mod = sum % 11;
            int digit2 = (mod < 2) ? 0 : 11 - mod;

            return cpf.charAt(9) - '0' == digit1 && cpf.charAt(10) - '0' == digit2;
        } catch (Exception e) {
            return false;
        }
    }
}
