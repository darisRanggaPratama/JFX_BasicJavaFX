# Test Plan untuk Fitur Pagination, Search, dan Rows Per Page

## Persiapan Test:
1. Pastikan database `test` sudah dibuat dan table `customer` sudah ada dengan sample data
2. Jalankan aplikasi: `mvn javafx:run`
3. Connect ke database melalui dialog koneksi

## Test Cases:

### 1. Test Pagination:
**Expected Behavior:**
- Tombol "Previous" disabled di halaman pertama
- Tombol "Next" disabled di halaman terakhir
- Label menampilkan "Page X of Y (Total records: Z)"
- Klik "Next" pindah ke halaman berikutnya
- Klik "Previous" pindah ke halaman sebelumnya

**Debug Output yang Diharapkan:**
```
DEBUG: Previous button clicked. Current page: 1, Total pages: 2
DEBUG: Already on first page
```

### 2. Test Rows Per Page:
**Expected Behavior:**
- ComboBox menampilkan opsi: 1, 5, 10, 25, 50, 100
- Default value: 10
- Mengubah nilai akan reload data dengan jumlah baris sesuai pilihan
- Pagination akan update sesuai dengan rows per page baru

**Debug Output yang Diharapkan:**
```
DEBUG: Rows per page changed via FXML to: 5
DEBUG: Loading data - Page: 1, RowsPerPage: 5, Offset: 0, Search: ''
```

### 3. Test Search:
**Expected Behavior:**
- Mengetik di search field akan filter data real-time
- Search akan reset ke halaman 1
- Pagination akan update sesuai hasil search
- Search kosong akan menampilkan semua data

**Debug Output yang Diharapkan:**
```
DEBUG: Search field changed from '' to 'John'
DEBUG: Loading data - Page: 1, RowsPerPage: 10, Offset: 0, Search: 'John'
```

## Troubleshooting:

### Jika Pagination Tidak Berfungsi:
1. Periksa console untuk debug output
2. Pastikan totalPages > 1
3. Periksa apakah button event handler terpanggil

### Jika Search Tidak Berfungsi:
1. Periksa apakah listener textProperty terpanggil
2. Periksa query SQL di console
3. Pastikan database connection aktif

### Jika Rows Per Page Tidak Berfungsi:
1. Periksa apakah ComboBox event handler terpanggil
2. Periksa apakah nilai ComboBox tidak null
3. Pastikan setupRowsPerPageComboBox() dipanggil di initialize()
