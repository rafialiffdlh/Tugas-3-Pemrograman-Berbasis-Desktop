public class Minuman extends MenuItem {
    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    public String getJenisMinuman() {
        return jenisMinuman;
    }

    @Override
    public void tampilMenu() {
        System.out.printf("%-20s Rp%-12.0f Jenis: %s%n",
                getNama(), getHarga(), jenisMinuman);
    }
}
