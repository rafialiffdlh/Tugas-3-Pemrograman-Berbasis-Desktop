public class Diskon extends MenuItem {
    private double diskon;

    public Diskon(String nama, double diskon) {
        super(nama, 0, "Diskon");
        if (diskon < 0 || diskon > 100 || !Double.isFinite(diskon)) {
            throw new IllegalArgumentException(
                    "Diskon harus antara 0 sampai 100 persen.");
        }
        this.diskon = diskon;
    }

    public double getDiskon() {
        return diskon;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s Diskon: %.1f%%%n", getNama(), diskon);
    }
}
