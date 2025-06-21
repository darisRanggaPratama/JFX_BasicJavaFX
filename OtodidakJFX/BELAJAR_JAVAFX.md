# 🚀 Belajar JavaFX 21 dari Dasar

Panduan lengkap untuk mempelajari JavaFX 21 dengan contoh-contoh praktis dan progressif.

## 📋 Daftar Isi

1. [Setup dan Persiapan](#setup-dan-persiapan)
2. [Struktur Proyek](#struktur-proyek)
3. [Contoh Aplikasi](#contoh-aplikasi)
4. [Cara Menjalankan](#cara-menjalankan)
5. [Konsep yang Dipelajari](#konsep-yang-dipelajari)
6. [Tips dan Best Practices](#tips-dan-best-practices)

## 🛠️ Setup dan Persiapan

### Requirements
- **Java 21** (LTS)
- **JavaFX 21**
- **Maven 3.9+**
- **IntelliJ IDEA** (recommended)

### Dependencies (pom.xml)
```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21</version>
    </dependency>
</dependencies>
```

## 📁 Struktur Proyek

```
OtodidakJFX/
├── src/main/java/com/tama/otodidakjfx/
│   ├── JavaFXLearningApp.java          # 🏠 Menu utama
│   ├── BasicJavaFXApp.java             # 1️⃣ Aplikasi sederhana
│   ├── LayoutExampleApp.java           # 2️⃣ Layout containers
│   ├── ControlsAndEventsApp.java       # 3️⃣ Controls & events
│   ├── StyledApp.java                  # 4️⃣ CSS styling
│   ├── CalculatorApp.java              # 5️⃣ Calculator (FXML)
│   ├── CalculatorController.java       # 5️⃣ Calculator controller
│   ├── StudentFormApp.java             # 6️⃣ Student form (FXML)
│   ├── StudentFormController.java      # 6️⃣ Form controller
│   └── Person.java                     # Model class
├── src/main/resources/com/tama/otodidakjfx/
│   ├── styles.css                      # CSS umum
│   ├── calculator-styles.css           # CSS calculator
│   ├── calculator-view.fxml            # FXML calculator
│   └── student-form.fxml               # FXML form mahasiswa
└── pom.xml
```

## 🎯 Contoh Aplikasi

### 1. **BasicJavaFXApp** - Aplikasi Sederhana
**Konsep:** Application, Stage, Scene, Layout, Controls
```java
// Aplikasi Hello World tanpa FXML
// Belajar struktur dasar JavaFX
```

### 2. **LayoutExampleApp** - Layout Containers
**Konsep:** VBox, HBox, BorderPane, GridPane, TabPane
```java
// Berbagai jenis layout dengan TabPane
// Memahami positioning dan spacing
```

### 3. **ControlsAndEventsApp** - Controls & Events
**Konsep:** TextField, Button, ComboBox, ListView, Event Handling
```java
// Form input dengan validasi
// Event handling dan user interaction
```

### 4. **StyledApp** - CSS Styling
**Konsep:** External CSS, Style Classes, Modern UI
```java
// Aplikasi dengan styling CSS yang menarik
// Best practices untuk UI design
```

### 5. **CalculatorApp** - FXML Application
**Konsep:** FXML, Controller, MVC Pattern
```java
// Kalkulator lengkap dengan FXML
// Pemisahan UI dari logic
```

### 6. **StudentFormApp** - Complex Form
**Konsep:** Form Validation, Dependent Controls, Real-time Feedback
```java
// Form pendaftaran mahasiswa yang kompleks
// Validasi real-time dan user experience
```

## 🚀 Cara Menjalankan

### Menjalankan Menu Utama
```bash
# Compile dan run
mvn clean javafx:run

# Atau run specific class
mvn clean compile exec:java -Dexec.mainClass="com.tama.otodidakjfx.JavaFXLearningApp"
```

### Menjalankan Aplikasi Tertentu
```bash
# Basic App
mvn exec:java -Dexec.mainClass="com.tama.otodidakjfx.BasicJavaFXApp"

# Calculator
mvn exec:java -Dexec.mainClass="com.tama.otodidakjfx.CalculatorApp"

# Student Form
mvn exec:java -Dexec.mainClass="com.tama.otodidakjfx.StudentFormApp"
```

### Dari IntelliJ IDEA
1. Buka project di IntelliJ IDEA
2. Run `JavaFXLearningApp.main()`
3. Pilih aplikasi yang ingin dipelajari dari menu

## 📚 Konsep yang Dipelajari

### Level 1: Dasar-dasar
- ✅ **Application Class** - Entry point aplikasi JavaFX
- ✅ **Stage** - Window/jendela aplikasi
- ✅ **Scene** - Container untuk semua content
- ✅ **Layout Containers** - VBox, HBox, BorderPane, GridPane
- ✅ **Basic Controls** - Label, Button, TextField

### Level 2: Intermediate
- ✅ **Event Handling** - Button clicks, text changes
- ✅ **Advanced Controls** - ComboBox, ListView, TableView
- ✅ **CSS Styling** - External stylesheets, style classes
- ✅ **Data Binding** - Properties dan ObservableList

### Level 3: Advanced
- ✅ **FXML** - Pemisahan UI dari logic
- ✅ **Controller Pattern** - MVC architecture
- ✅ **Form Validation** - Real-time validation
- ✅ **Complex UI** - Multi-section forms, dependent controls

### Level 4: Expert
- 🔄 **Database Integration** - MySQL connection
- 🔄 **Custom Controls** - Membuat komponen sendiri
- 🔄 **Charts dan Graphs** - Data visualization
- 🔄 **Deployment** - Membuat executable

## 💡 Tips dan Best Practices

### 1. **Struktur Kode**
```java
// Gunakan MVC pattern
// Pisahkan UI (FXML) dari Logic (Controller)
// Buat model classes untuk data
```

### 2. **CSS Styling**
```css
/* Gunakan external CSS files */
/* Buat style classes yang reusable */
/* Konsisten dengan color scheme */
```

### 3. **Event Handling**
```java
// Gunakan lambda expressions untuk event handlers
button.setOnAction(e -> handleButtonClick());

// Atau method references
button.setOnAction(this::handleButtonClick);
```

### 4. **Data Binding**
```java
// Gunakan Properties untuk automatic updates
StringProperty nameProperty = new SimpleStringProperty();
label.textProperty().bind(nameProperty);
```

### 5. **Form Validation**
```java
// Real-time validation dengan listeners
textField.textProperty().addListener((obs, oldVal, newVal) -> validate());
```

## 🎨 Styling Guidelines

### Color Scheme
- **Primary:** #3498db (Blue)
- **Success:** #27ae60 (Green)  
- **Warning:** #f39c12 (Orange)
- **Danger:** #e74c3c (Red)
- **Dark:** #2c3e50
- **Light:** #ecf0f1

### Typography
- **Title:** 24px, Bold
- **Subtitle:** 16px, Bold
- **Body:** 14px, Regular
- **Small:** 12px, Regular

## 🔧 Troubleshooting

### Common Issues

1. **Module Path Error**
   ```bash
   # Pastikan JavaFX ada di module path
   --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
   ```

2. **FXML Loading Error**
   ```java
   // Pastikan file FXML ada di resources folder
   // Check nama file dan path yang benar
   ```

3. **CSS Not Applied**
   ```java
   // Pastikan CSS file ada di resources
   // Check syntax CSS yang benar
   ```

## 📖 Referensi

- [JavaFX Documentation](https://openjfx.io/)
- [JavaFX Tutorial](https://docs.oracle.com/javafx/)
- [Scene Builder](https://gluonhq.com/products/scene-builder/)
- [CSS Reference Guide](https://openjfx.io/javadoc/21/javafx.graphics/javafx/scene/doc-files/cssref.html)

## 🤝 Kontribusi

Jika Anda menemukan bug atau ingin menambahkan contoh baru:
1. Fork repository ini
2. Buat branch baru untuk fitur Anda
3. Commit perubahan Anda
4. Push ke branch
5. Buat Pull Request

---

**Happy Learning! 🎉**

*Dibuat dengan ❤️ untuk pembelajaran JavaFX 21*
