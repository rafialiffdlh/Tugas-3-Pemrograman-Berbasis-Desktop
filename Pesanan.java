import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Pesanan {
    private ArrayList<MenuItem> itemPesanan = new ArrayList<>();

    public void tambahPesanan(MenuItem item) {
        if (item instanceof Diskon) {
            throw new IllegalArgumentException(
                    "Promo tidak dapat dipesan sebagai makanan/minuman.");
        }
        itemPesanan.add(item);
    }

    public boolean isKosong() {
        return itemPesanan.isEmpty();
    }

    public double hitungSubtotal() {
        double subtotal = 0;
        for (MenuItem item : itemPesanan) {
            subtotal += item.getHarga();
        }
        return subtotal;
    }

    public double hitungTotal(Menu menu) {
        double subtotal = hitungSubtotal();
        double potongan = subtotal * menu.getDiskonTerbesar() / 100;
        return subtotal - potongan;
    }

    public String buatStruk(Menu menu) {
        StringBuilder struk = new StringBuilder();
        struk.append("\\n===== STRUK RESTORAN =====\\n");

        for (MenuItem item : itemPesanan) {
            struk.append(String.format("%-20s Rp%.0f%n",
                    item.getNama(), item.getHarga()));
        }

        double subtotal = hitungSubtotal();
        double persen = menu.getDiskonTerbesar();
        double potongan = subtotal * persen / 100;

        struk.append("--------------------------\\n");
        struk.append(String.format("Subtotal       : Rp%.0f%n", subtotal));
        struk.append(String.format("Diskon (%.1f%%) : Rp%.0f%n",
                persen, potongan));
        struk.append(String.format("Total Bayar    : Rp%.0f%n",
                subtotal - potongan));
        struk.append("==========================\\n");
        return struk.toString();
    }

    public void tampilkanStruk(Menu menu) {
        System.out.println(buatStruk(menu));
    }

    public void simpanStruk(Menu menu, String namaFile) throws IOException {
        Files.write(Paths.get(namaFile),
                buatStruk(menu).getBytes(StandardCharsets.UTF_8));
    }
}
