import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    private static Scanner input = new Scanner(System.in);
    private static Menu menu = new Menu();
    private static Pesanan pesanan = new Pesanan();

    private static int inputInt(String pesan) {
        while (true) {
            System.out.print(pesan);
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa bilangan bulat.");
            }
        }
    }

    private static double inputDouble(String pesan) {
        while (true) {
            System.out.print(pesan);
            try {
                double angka = Double.parseDouble(input.nextLine());
                if (Double.isFinite(angka) && angka >= 0) {
                    return angka;
                }
                System.out.println("Masukkan angka positif yang valid.");
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
            }
        }
    }

    private static String inputTeks(String pesan) {
        System.out.print(pesan);
        return input.nextLine().trim();
    }

    private static void tambahMenu() {
        System.out.println("\\n1. Makanan");
        System.out.println("2. Minuman");
        System.out.println("3. Diskon");

        int jenis = inputInt("Pilih jenis menu: ");
        String nama = inputTeks("Nama menu: ");

        if (nama.isEmpty() || nama.contains("|")) {
            System.out.println("Nama menu tidak valid.");
            return;
        }

        try {
            switch (jenis) {
                case 1:
                    double hargaMakanan = inputDouble("Harga makanan: ");
                    String jenisMakanan = inputTeks(
                            "Jenis makanan (utama/camilan): ");
                    menu.tambahItem(new Makanan(
                            nama, hargaMakanan, jenisMakanan));
                    break;
                case 2:
                    double hargaMinuman = inputDouble("Harga minuman: ");
                    String jenisMinuman = inputTeks(
                            "Jenis minuman (dingin/panas): ");
                    menu.tambahItem(new Minuman(
                            nama, hargaMinuman, jenisMinuman));
                    break;
                case 3:
                    double persen = inputDouble("Persentase diskon: ");
                    menu.tambahItem(new Diskon(nama, persen));
                    break;
                default:
                    System.out.println("Pilihan jenis tidak tersedia.");
                    return;
            }
            System.out.println("Menu berhasil ditambahkan.");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal: " + e.getMessage());
        }
    }

    private static void buatPesanan() {
        menu.tampilkanMenu();
        if (menu.isKosong()) {
            return;
        }

        while (true) {
            int nomor = inputInt("Nomor menu (0 untuk selesai): ");
            if (nomor == 0) {
                break;
            }

            try {
                MenuItem item = menu.getItem(nomor);
                pesanan.tambahPesanan(item);
                System.out.println(item.getNama() + " ditambahkan.");
            } catch (IndexOutOfBoundsException
                    | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        if (!pesanan.isKosong()) {
            pesanan.tampilkanStruk(menu);
        }
    }

    private static void bacaStruk() {
        Path path = Paths.get("struk.txt");
        try {
            if (Files.exists(path)) {
                System.out.println(new String(
                        Files.readAllBytes(path), StandardCharsets.UTF_8));
            } else {
                System.out.println("File struk belum tersedia.");
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca struk: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            if (Files.exists(Paths.get("menu.txt"))) {
                menu.muatMenu("menu.txt");
            } else {
                menu.tambahItem(new Makanan("Nasi Goreng", 20000, "Utama"));
                menu.tambahItem(new Makanan("Ayam Bakar", 30000, "Utama"));
                menu.tambahItem(new Minuman("Es Teh", 8000, "Dingin"));
                menu.tambahItem(new Diskon("Promo Restoran", 10));
                menu.simpanMenu("menu.txt");
            }
        } catch (IOException e) {
            System.out.println("Gagal memuat menu: " + e.getMessage());
        }

        int pilihan;
        do {
            System.out.println("\\n=== MANAJEMEN RESTORAN ===");
            System.out.println("1. Tambah item menu");
            System.out.println("2. Tampilkan menu");
            System.out.println("3. Buat pesanan");
            System.out.println("4. Tampilkan dan simpan struk");
            System.out.println("5. Simpan menu ke file");
            System.out.println("6. Muat menu dari file");
            System.out.println("7. Baca struk dari file");
            System.out.println("0. Keluar");

            pilihan = inputInt("Pilih menu: ");

            try {
                switch (pilihan) {
                    case 1:
                        tambahMenu();
                        break;
                    case 2:
                        menu.tampilkanMenu();
                        break;
                    case 3:
                        buatPesanan();
                        break;
                    case 4:
                        if (pesanan.isKosong()) {
                            System.out.println("Belum ada pesanan.");
                        } else {
                            pesanan.simpanStruk(menu, "struk.txt");
                            pesanan.tampilkanStruk(menu);
                            System.out.println("Struk disimpan ke struk.txt.");
                        }
                        break;
                    case 5:
                        menu.simpanMenu("menu.txt");
                        System.out.println("Menu berhasil disimpan.");
                        break;
                    case 6:
                        menu.muatMenu("menu.txt");
                        break;
                    case 7:
                        bacaStruk();
                        break;
                    case 0:
                        System.out.println("Program selesai. Terima kasih.");
                        break;
                    default:
                        System.out.println("Pilihan tidak tersedia.");
                }
            } catch (IOException e) {
                System.out.println("Kesalahan file: " + e.getMessage());
            }
        } while (pilihan != 0);

        input.close();
    }
}
