package pt.ulisboa.tecnico.cmov.shopist.util;

public class ShopISTUtils {
    private ShopISTUtils() {}

    public static boolean isNonEmpty(String s) {
        return s != null && s.length() > 0;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
