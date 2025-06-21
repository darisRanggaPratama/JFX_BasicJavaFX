# Customer Management System - JavaFX CRUD Application

Aplikasi desktop modern untuk manajemen data customer menggunakan JavaFX 21, MySQL 8, dan arsitektur MVC.

## 🚀 Fitur Utama

### ✨ CRUD Operations
- **Create**: Tambah customer baru dengan validasi form
- **Read**: Tampilkan data customer dengan pagination
- **Update**: Edit data customer yang sudah ada
- **Delete**: Hapus data customer dengan konfirmasi

### 📊 Data Management
- **Pagination**: Tampilkan data dengan pagination (1, 5, 10, 25, 50, 100 rows per page)
- **Search**: Pencarian data berdasarkan NIK, Name, Born, Active, Salary
- **Sorting**: Data terurut berdasarkan ID

### 📁 File Operations
- **CSV Import/Export**: Upload dan download file CSV dengan separator semicolon (;)
- **Excel Import/Export**: Upload dan download file Excel (.xlsx)
- **Batch Processing**: Import multiple records sekaligus dengan laporan sukses/gagal

### 🎨 User Interface
- **Modern Design**: Tampilan modern dan profesional dengan CSS styling
- **Responsive**: Form yang responsive dan user-friendly
- **Validation**: Validasi input real-time
- **Confirmation Dialogs**: Konfirmasi untuk operasi penting

## 🛠️ Teknologi yang Digunakan

- **JavaFX 21**: Framework UI modern untuk Java
- **Maven 3.9**: Build tool dan dependency management
- **MySQL 8**: Database relational
- **Apache POI**: Library untuk operasi Excel
- **OpenCSV**: Library untuk operasi CSV
- **MVC Architecture**: Pemisahan concern yang jelas

## 📋 Persyaratan Sistem

- **Java 21** atau lebih tinggi
- **Maven 3.9** atau lebih tinggi
- **MySQL 8** atau lebih tinggi
- **IntelliJ IDEA** (recommended)

## 🗄️ Database Schema

```sql
CREATE TABLE customer (
    idx int(11) NOT NULL AUTO_INCREMENT,
    nik varchar(6) NOT NULL,
    name varchar(50) NOT NULL,
    born date DEFAULT NULL,
    active tinyint(4) DEFAULT NULL,
    salary int(11) DEFAULT NULL,
    PRIMARY KEY (idx)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

## 🚀 Cara Menjalankan

### 1. Setup Database
```bash
# Login ke MySQL
mysql -u root -p

# Buat database dan user
CREATE DATABASE test;
CREATE USER 'rangga'@'localhost' IDENTIFIED BY 'rangga';
GRANT ALL PRIVILEGES ON test.* TO 'rangga'@'localhost';
FLUSH PRIVILEGES;

# Import sample data
mysql -u rangga -p test < sample_data.sql
```

### 2. Clone dan Build Project
```bash
# Clone repository
git clone <repository-url>
cd CrudXMLJavaFX

# Build dengan Maven
mvn clean compile

# Jalankan aplikasi
mvn javafx:run
```

### 3. Atau Jalankan dari IDE
1. Buka project di IntelliJ IDEA
2. Pastikan Java 21 sudah dikonfigurasi
3. Run `HelloApplication.java`

## 📁 Struktur Project

```
src/main/java/com/tama/crudxmljavafx/
├── controller/
│   ├── MainController.java          # Controller utama
│   └── CustomerFormController.java  # Controller form customer
├── model/
│   └── Customer.java               # Model data customer
├── repository/
│   └── CustomerRepository.java     # Data access layer
├── service/
│   └── CustomerService.java        # Business logic layer
├── util/
│   ├── DatabaseConnection.java     # Koneksi database
│   ├── AlertUtil.java             # Utility untuk dialog
│   └── FileUtil.java              # Utility untuk file operations
└── HelloApplication.java          # Main application class

src/main/resources/com/tama/crudxmljavafx/
├── main-view.fxml                 # Layout utama
├── customer-form.fxml             # Layout form customer
└── styles.css                     # CSS styling
```

## 🎯 Cara Penggunaan

### Menambah Customer Baru
1. Klik tombol "Add Customer"
2. Isi form dengan data yang valid
3. Klik "Save"

### Mengedit Customer
1. Double-click pada row customer di table
2. Edit data yang diperlukan
3. Klik "Update"

### Menghapus Customer
1. Double-click pada row customer di table
2. Klik "Delete"
3. Konfirmasi penghapusan

### Import/Export Data
- **CSV**: Gunakan separator semicolon (;)
- **Excel**: Format .xlsx dengan header di baris pertama
- File akan diproses dengan laporan sukses/gagal

### Pencarian dan Pagination
- Gunakan search box untuk mencari data
- Pilih jumlah rows per page dari dropdown
- Navigasi dengan tombol pagination

## 📊 Format File Import

### CSV Format
```
idx;nik;name;born;active;salary
1;845723;Eren Yeager;2000-03-30;1;7500000
2;319604;Mikasa Ackerman;2000-02-10;1;8000000
```

### Excel Format
- Header di baris pertama
- Format tanggal: YYYY-MM-DD
- Active: 1 (aktif) atau 0 (tidak aktif)
- Salary: angka tanpa separator

## 🔧 Konfigurasi Database

Edit file `DatabaseConnection.java` untuk mengubah konfigurasi database:

```java
private static final String URL = "jdbc:mysql://localhost:3306/test";
private static final String USERNAME = "rangga";
private static final String PASSWORD = "rangga";
```

## 🐛 Troubleshooting

### Database Connection Error
- Pastikan MySQL server berjalan
- Periksa username, password, dan nama database
- Pastikan user memiliki privilege yang cukup

### JavaFX Runtime Error
- Pastikan menggunakan Java 21
- Periksa module-info.java sudah benar
- Pastikan semua dependencies ter-download

### File Import Error
- Periksa format file sesuai dengan template
- Pastikan encoding file UTF-8
- Periksa tidak ada karakter khusus yang tidak didukung

## 📝 Lisensi

Project ini dibuat untuk keperluan pembelajaran dan pengembangan aplikasi JavaFX dengan arsitektur MVC.

## 👨‍💻 Developer

Dikembangkan menggunakan:
- JavaFX 21
- Maven 3.9
- MySQL 8
- Apache POI
- OpenCSV
- MVC Architecture Pattern
