# 📊 Jasper Reports Integration dengan JavaFX

Panduan lengkap untuk mengintegrasikan Jasper Reports dengan aplikasi JavaFX untuk membuat laporan dan dokumen yang profesional.

## 🎯 **Apa yang Telah Ditambahkan**

### **1. Dependencies Baru**
```xml
<!-- Jasper Reports Dependencies -->
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports</artifactId>
    <version>6.21.0</version>
</dependency>
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports-fonts</artifactId>
    <version>6.21.0</version>
</dependency>
<dependency>
    <groupId>net.sf.jasperreports</groupId>
    <artifactId>jasperreports-functions</artifactId>
    <version>6.21.0</version>
</dependency>
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.15.1</version>
</dependency>
```

### **2. File Baru yang Dibuat**
- ✅ **Student.java** - Model class untuk data mahasiswa
- ✅ **JasperReportService.java** - Service untuk generate reports
- ✅ **Updated StudentFormController.java** - Integrasi dengan Jasper Reports
- ✅ **Updated student-form.fxml** - Button untuk generate reports
- ✅ **Updated module-info.java** - Module requirements untuk Jasper Reports

## 🚀 **Fitur Jasper Reports yang Tersedia**

### **1. Kartu Pendaftaran Mahasiswa**
- **Format:** PDF dan HTML
- **Konten:** Data lengkap mahasiswa dalam format kartu
- **Fitur:** 
  - Header universitas
  - Data pribadi lengkap
  - Data akademik
  - Timestamp pembuatan
  - Auto-export ke Desktop

### **2. Daftar Mahasiswa Terdaftar**
- **Format:** PDF dan HTML (Landscape)
- **Konten:** Tabel semua mahasiswa yang terdaftar
- **Fitur:**
  - Header dengan total mahasiswa
  - Kolom: NIM, Nama, Fakultas, Program Studi, Tahun, IPK, Gender
  - Pagination otomatis
  - Professional styling

## 🎨 **Cara Menggunakan**

### **1. Dari StudentFormApp**

#### **Setelah Submit Data:**
1. Isi form pendaftaran mahasiswa
2. Klik **Submit**
3. Pilih opsi yang tersedia:
   - 🖨️ **Cetak Kartu** - Generate kartu pendaftaran
   - 📋 **Lihat Daftar** - Generate daftar semua mahasiswa
   - ➕ **Daftar Lagi** - Clear form untuk input baru
   - ❌ **Tutup** - Tutup dialog

#### **Dari Menu Reports:**
1. Scroll ke bawah ke section "📊 Laporan dan Cetak"
2. Klik **🖨️ Generate Reports**
3. Pilih jenis laporan:
   - 📋 **Daftar Mahasiswa** - Semua mahasiswa terdaftar
   - 🎓 **Kartu Mahasiswa Terakhir** - Kartu mahasiswa terakhir yang didaftarkan

### **2. Output Files**
- **Lokasi:** Desktop (`~/Desktop/`)
- **Format:** PDF (default), HTML (optional)
- **Naming:**
  - Kartu: `student_registration_[NIM].pdf`
  - Daftar: `students_list_[YYYYMMDD_HHMMSS].pdf`

## 🔧 **Arsitektur Teknis**

### **1. JasperReportService.java**
```java
// Main methods:
- generateStudentReport(Student student)     // Kartu mahasiswa
- generateStudentsListReport(List<Student>)  // Daftar mahasiswa
- createStudentReportDesign()               // Design kartu
- createStudentsListReportDesign()          // Design daftar
- showReportViewer()                        // Preview report
- exportToPDF()                             // Export ke PDF
```

### **2. Student.java Model**
```java
// Properties untuk Jasper Reports:
- getFullName()                    // Nama lengkap
- getFormattedBirthDate()         // Tanggal lahir formatted
- getFormattedRegistrationDate()  // Tanggal daftar formatted
- getHobbiesString()              // Hobi sebagai string
- getGpaString()                  // IPK formatted
- getAgreementStatus()            // Status persetujuan
- getAge()                        // Umur calculated
```

### **3. Report Design (Programmatic)**
- **Title Band:** Header universitas dan judul
- **Detail Band:** Data mahasiswa
- **Column Header:** Header tabel (untuk daftar)
- **Page Footer:** Timestamp dan info generator

## 🎯 **Konsep yang Dipelajari**

### **Level Advanced:**
- ✅ **JasperReports Integration** - Integrasi dengan JavaFX
- ✅ **Dynamic Report Generation** - Membuat report secara programmatic
- ✅ **Report Design API** - Menggunakan JasperDesign API
- ✅ **Data Binding** - Menghubungkan Java objects dengan report
- ✅ **Multi-format Export** - PDF, HTML export
- ✅ **Report Viewer** - Preview report sebelum export

### **Level Expert:**
- ✅ **Professional Layout** - Design report yang profesional
- ✅ **Complex Data Handling** - List, computed properties
- ✅ **User Experience** - Seamless integration dengan UI
- ✅ **Error Handling** - Robust error handling untuk reports
- ✅ **Performance** - Threading untuk non-blocking UI

## 📋 **Contoh Output**

### **Kartu Pendaftaran Mahasiswa:**
```
UNIVERSITAS TEKNOLOGI INDONESIA
KARTU PENDAFTARAN MAHASISWA

NIM:              12345678
Nama Lengkap:     John Doe
Email:            john.doe@email.com
Telepon:          081234567890
Tanggal Lahir:    15 Januari 1995
Jenis Kelamin:    Laki-laki
Fakultas:         Teknik
Program Studi:    Teknik Informatika
Tahun Masuk:      2024
IPK:              3.75
Alamat:           Jl. Contoh No. 123, Jakarta
Hobi:             Programming, Gaming
Tanggal Daftar:   21 Juni 2025

Generated on: 21 Juni 2025 16:45:30
```

### **Daftar Mahasiswa (Table Format):**
```
DAFTAR MAHASISWA TERDAFTAR

| NIM      | Nama Lengkap | Fakultas | Program Studi      | Tahun | IPK  | Gender    |
|----------|--------------|----------|--------------------|-------|------|-----------|
| 12345678 | John Doe     | Teknik   | Teknik Informatika | 2024  | 3.75 | Laki-laki |
| 12345679 | Jane Smith   | Ekonomi  | Manajemen          | 2024  | 3.85 | Perempuan |
```

## 🛠️ **Troubleshooting**

### **Common Issues:**

1. **Report Viewer tidak muncul**
   ```java
   // Solution: Check JavaFX threading
   Platform.runLater(() -> {
       // Report generation code
   });
   ```

2. **Export gagal**
   ```java
   // Check permissions dan path
   String outputPath = System.getProperty("user.home") + "/Desktop/";
   ```

3. **Font issues**
   ```xml
   <!-- Add font dependency -->
   <dependency>
       <groupId>net.sf.jasperreports</groupId>
       <artifactId>jasperreports-fonts</artifactId>
       <version>6.21.0</version>
   </dependency>
   ```

## 🚀 **Cara Menjalankan**

```bash
# Compile dengan dependencies baru
mvn clean compile

# Run StudentFormApp
mvn exec:java -Dexec.mainClass="com.tama.otodidakjfx.StudentFormApp"

# Atau run dari menu utama
mvn javafx:run
```

## 🎉 **Hasil Akhir**

Sekarang StudentFormApp memiliki:
- ✅ **Form input** yang lengkap dengan validasi
- ✅ **Real-time validation** untuk user experience
- ✅ **Professional reports** dengan Jasper Reports
- ✅ **Multiple export formats** (PDF, HTML)
- ✅ **Report viewer** untuk preview
- ✅ **Automatic file management** (save ke Desktop)
- ✅ **Error handling** yang robust
- ✅ **Modern UI** dengan CSS styling

## 📚 **Next Steps**

Untuk pengembangan lebih lanjut, Anda bisa menambahkan:
- 🔄 **Database integration** untuk persistent storage
- 🔄 **Email integration** untuk kirim report via email
- 🔄 **Custom report templates** dengan JRXML files
- 🔄 **Charts dan graphs** dalam reports
- 🔄 **Batch report generation** untuk multiple students
- 🔄 **Report scheduling** untuk automated reports

---

**Selamat! Anda telah berhasil mengintegrasikan Jasper Reports dengan JavaFX! 🎊**
