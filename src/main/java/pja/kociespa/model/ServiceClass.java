package pja.kociespa.model;

public enum ServiceClass {
    BASIC(20),
    CARE_PLUS(50),
    VIP(80);

    public final int pricePerDay;

    ServiceClass(int pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
