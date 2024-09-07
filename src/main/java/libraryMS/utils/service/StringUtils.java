package libraryMS.utils.service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StringUtils {

    public static String toUTF8(String str) {
        byte[] bytes;
        bytes = str.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);

    }

    public static String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    public static String getArabicString(String englishString) {
        String arabicString = "";
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < englishString.length(); j++) {
            if (Character.isDigit(englishString.charAt(j))) {
                builder.append(arabicChars[(int) (englishString.charAt(j)) - 48]);
            } else {
                builder.append(englishString.charAt(j));
            }
        }
        arabicString = builder.toString();
        String[] arabicStringParts = arabicString.split("/");
        for (int i = 0; i < arabicStringParts.length / 2; i++) {
            String temp = arabicStringParts[i];
            arabicStringParts[i] = arabicStringParts[arabicStringParts.length
                    - i - 1];
            arabicStringParts[arabicStringParts.length - i - 1] = temp;
        }
        arabicString = arabicStringParts[0] + "/";
        for (int i = 1; i < arabicStringParts.length; i++) {
            arabicString += arabicStringParts[i];
            if (i != arabicStringParts.length - 1)
                arabicString += "/";
        }
        return arabicString;
    }

    public static String normalizeVSId(String toBeNormalized) {

        String normalized = toBeNormalized.replaceAll("[}{]*", "");
        return normalized;
    }

}
