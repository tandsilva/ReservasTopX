package com.reservastopx.util;

public class CNPJUtils {

    public static boolean isCNPJValid(String cnpj) {
        if (cnpj == null) return false;

        // remove tudo que não for dígito (aceita com ou sem máscara)
        cnpj = cnpj.replaceAll("\\D", "");

        if (!cnpj.matches("\\d{14}")) return false;

        // elimina CNPJs inválidos conhecidos (tudo igual)
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            int soma = 0;
            int peso = 2;

            // 1º dígito verificador (usa os 12 primeiros dígitos)
            for (int i = 11; i >= 0; i--) {
                soma += (cnpj.charAt(i) - '0') * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            int digito1 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            // 2º dígito verificador (usa 12 + 1º dígito)
            soma = 0;
            peso = 2;
            for (int i = 12; i >= 0; i--) {
                soma += (cnpj.charAt(i) - '0') * peso;
                peso = (peso == 9) ? 2 : peso + 1;
            }
            int digito2 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            return (cnpj.charAt(12) - '0' == digito1) &&
                    (cnpj.charAt(13) - '0' == digito2);

        } catch (Exception e) {
            return false;
        }
    }
}
