import java.util.Random;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

class DigitalWallet {
   
    private double balance;
    private String ownerName;
    private String walletID;

        public DigitalWallet(String namaPemilik, double saldoAwal) {
        this.ownerName = namaPemilik;
       
        Random rand = new Random();
        this.walletID = "W-" + (rand.nextInt(9000) + 1000);

        if (saldoAwal >= 0) {
            this.balance = saldoAwal;
        } else {
            this.balance = 0;
            System.out.println("[Sistem] Peringatan: Saldo awal tidak boleh negatif. Otomatis diset ke 0.");
        }

        System.out.println(" Dompet Digital berhasil dibuat untuk: " + this.ownerName);
        System.out.println(" ID Dompet Anda: " + this.walletID);
        System.out.println("------------------------------------------");
    }

    // 2. GETTER: Mengambil saldo dengan format rupiah (IDR)
    public String getBalance() {
        DecimalFormat kursIndonesia = (DecimalFormat) DecimalFormat.getCurrencyInstance();
        DecimalFormatSymbols formatRp = new DecimalFormatSymbols();

        formatRp.setCurrencySymbol("Rp ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');

        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(this.balance).replace(",00", "");
    }

    // 3. METHOD DEPOSIT (Top Up / Masukin Uang)
    public void deposit(double jumlah) {
        if (jumlah > 0) {
            this.balance += jumlah;
            System.out.println("📥 Sukses: " + this.ownerName + " berhasil Top Up sebesar " + formatAngka(jumlah));
        } else {
            System.out.println("❌ Gagal: Jumlah Top Up harus lebih dari 0!");
        }
    }

    // 4. METHOD WITHDRAW (Penarikan Uang)
    public boolean withdraw(double jumlah) {
        System.out.println("--- Proses Penarikan ---");
        
        // Validasi 1: Input tidak boleh nol atau negatif
        if (jumlah <= 0) {
            System.out.println("❌ Gagal: Jumlah penarikan tidak valid!");
            return false;
        } 
        // Validasi 2: Cek apakah saldo mencukupi
        else if (jumlah > this.balance) {
            System.out.println("❌ Gagal: Saldo " + this.ownerName + " tidak mencukupi!");
            System.out.println("   Saldo saat ini: " + this.getBalance());
            return false;
        } 
        // Jika lolos semua validasi
        else {
            this.balance -= jumlah;
            System.out.println("💸 Berhasil: " + this.ownerName + " menarik " + formatAngka(jumlah));
            System.out.println("💰 Sisa saldo sekarang: " + this.getBalance());
            return true;
        }
    }

    // 5. METHOD TRANSFER (Kirim uang ke dompet lain)
    public void transfer(DigitalWallet penerima, double jumlah) {
        System.out.println("🔄 Mencoba transfer dari " + this.ownerName + " ke " + penerima.ownerName + "...");
        
        if (jumlah > 0 && jumlah <= this.balance) {
            this.balance -= jumlah;      // Kurangi saldo pengirim
            penerima.deposit(jumlah);    // Tambah saldo penerima
            
            System.out.println("✨ Transfer BERHASIL sebesar " + formatAngka(jumlah));
            System.out.println("💰 Sisa saldo " + this.ownerName + ": " + this.getBalance());
        } else {
            System.out.println("❌ Transfer Gagal: Saldo tidak cukup atau jumlah tidak valid.");
        }
        System.out.println("------------------------------------------");
    }

    // Fungsi pembantu untuk format angka ke Rupiah agar kode lebih bersih
    private String formatAngka(double jumlah) {
        DecimalFormat df = new DecimalFormat("Rp #,###");
        return df.format(jumlah).replace(",", ".");
    }

    // --- MAIN METHOD: Tempat menjalankan program di Java ---
    public static void main(String[] args) {
        // Membuat dompet baru
        DigitalWallet dompetSekar = new DigitalWallet("Sekar", 500000);
        DigitalWallet dompetTeman = new DigitalWallet("Budi", 100000);

        // 1. Masukin Uang (Top Up)
        dompetSekar.deposit(100000);

        // 2. Tarik Uang (Berhasil)
        dompetSekar.withdraw(50000);

        // 3. Tarik Uang (Gagal karena Saldo Tidak Mencukupi)
        dompetSekar.withdraw(1000000);

        // 4. Transfer ke Budi
        dompetSekar.transfer(dompetTeman, 250000);

        // Laporan Akhir
        System.out.println("\n[LAPORAN AKHIR]");
        System.out.println("Pemilik: Sekar | Saldo Akhir: " + dompetSekar.getBalance());
        System.out.println("Pemilik: Budi  | Saldo Akhir: " + dompetTeman.getBalance());
    }
}