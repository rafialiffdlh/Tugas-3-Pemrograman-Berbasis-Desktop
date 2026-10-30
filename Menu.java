import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menu {
    private ArrayList<MenuItem> daftarMenu = new ArrayList<>();

    public void tambahItem(MenuItem item) {
        daftarMenu.add(item);
    }

    // Mengembalikan salinan daftar agar data internal tidak diubah langsung.
    public List<MenuItem> getDaftarMenu() {
        return Collections.unmodifiableList(daftarMenu);
    }

    public boolean isKosong() {
        return daftarMenu.isEmpty();
    }

    public MenuItem getItem(int nomor) {
        if (nomor < 1 || nomor > daftarMenu.size()) {
            throw new IndexOutOfBoundsException("Nomor menu tidak ditemukan.");
        }
        return daftarMenu.get(nomor - 1);
    }

    public void tampilkanMenu() {
        if (daftarMenu.isEmpty()) {
            System.out.println("Menu restoran masih kosong.");
            return;
        }

        System.out.println("\\n===== MENU RESTORAN =====");
        for (int i = 0; i < daftarMenu.size(); i++) {
            System.out.print((i + 1) + ". ");
            daftarMenu.get(i).tampilMenu(); // Polymorphism
        }
    }

    public double getDiskonTerbesar() {
        double persen = 0;
        for (MenuItem item : daftarMenu) {
            if (item instanceof Diskon) {
                Diskon promo = (Diskon) item;
                persen = Math.max(persen, promo.getDiskon());
            }
        }
        return persen;
    }

    public void simpanMenu(String namaFile) throws IOException {
        ArrayList<String> baris = new ArrayList<>();

        for (MenuItem item : daftarMenu) {
            if (item instanceof Makanan) {
                Makanan m = (Makanan) item;
                baris.add("Makanan|" + m.getNama() + "|" + m.getHarga()
                        + "|" + m.getJenisMakanan());
            } else if (item instanceof Minuman) {
                Minuman m = (Minuman) item;
                baris.add("Minuman|" + m.getNama() + "|" + m.getHarga()
                        + "|" + m.getJenisMinuman());
            } else if (item instanceof Diskon) {
                Diskon d = (Diskon) item;
                baris.add("Diskon|" + d.getNama() + "|" + d.getDiskon()
                        + "|Promo");
            }
        }

        Files.write(Paths.get(namaFile), baris, StandardCharsets.UTF_8);
    }

    public void muatMenu(String namaFile) throws IOException {
        Path path = Paths.get(namaFile);
        if (!Files.exists(path)) {
            System.out.println("File menu belum tersedia.");
            return;
        }

        ArrayList<MenuItem> menuBaru = new ArrayList<>();
        List<String> baris = Files.readAllLines(path, StandardCharsets.UTF_8);

        for (String data : baris) {
            try {
                String[] bagian = data.split("\\\\|", -1);
                if (bagian.length != 4) {
                    throw new IllegalArgumentException("Format data tidak valid.");
                }

                String tipe = bagian[0];
                String nama = bagian[1];
                double nilai = Double.parseDouble(bagian[2]);

                switch (tipe) {
                    case "Makanan":
                        menuBaru.add(new Makanan(nama, nilai, bagian[3]));
                        break;
                    case "Minuman":
                        menuBaru.add(new Minuman(nama, nilai, bagian[3]));
                        break;
                    case "Diskon":
                        menuBaru.add(new Diskon(nama, nilai));
                        break;
                    default:
                        throw new IllegalArgumentException("Jenis menu tidak dikenal.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Data dilewati: " + data
                        + " (" + e.getMessage() + ")");
            }
        }

        daftarMenu.clear();
        daftarMenu.addAll(menuBaru);
        System.out.println("Data menu berhasil dimuat.");
    }
}
