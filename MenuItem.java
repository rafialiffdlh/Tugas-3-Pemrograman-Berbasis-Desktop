public abstract class MenuItem {
    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        if (nama == null || nama.trim().isEmpty() || nama.contains("|")) {
            throw new IllegalArgumentException("Nama menu tidak valid.");
        }
        if (harga < 0 || !Double.isFinite(harga)) {
            throw new IllegalArgumentException("Harga tidak valid.");
        }
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().isEmpty() || nama.contains("|")) {
            throw new IllegalArgumentException("Nama menu tidak valid.");
        }
        this.nama = nama;
    }

    public void setHarga(double harga) {
        if (harga < 0 || !Double.isFinite(harga)) {
            throw new IllegalArgumentException("Harga tidak valid.");
        }
        this.harga = harga;
    }

    public abstract void tampilMenu();
}
