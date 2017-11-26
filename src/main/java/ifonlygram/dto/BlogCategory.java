package ifonlygram.dto;

public enum BlogCategory {
    FASHION,
    TRAVEL,
    FITNESS;

    public static boolean isContainsElement(String element) {
        try {
            BlogCategory.valueOf(element);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
