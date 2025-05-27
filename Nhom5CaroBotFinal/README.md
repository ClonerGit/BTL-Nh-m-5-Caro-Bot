# Dự án Caro Bot (Cờ Caro với AI)

## 🧠 Giới thiệu
Đây là dự án game Cờ Caro được xây dựng bằng JavaFX. Có 3 mức độ AI:
  - Dễ (chỉ chặn hoặc tấn công thắng ngay)
  - Trung bình (đánh giá mọi nước đi có thể và chọn nước có điểm số cao nhất)
  - Khó (Sử dụng thuật toán Minimax và alpha-beta pruning)

---
Khi bắt đầu, có thể chọn 3 chế đô:
  - Người vs Máy: Sẽ được chọn 3 cấp độ AI để đấu với người
  - Người vs Người: 2 người đấu với nhau
  - Máy vs Máy: Sẽ có 3 cặp đấu của 3 cấp độ AI

---

## 🚀 Cách chạy dự án

### Yêu cầu:
- JDK 17 hoặc mới hơn
- JavaFX SDK (cấu hình biến môi trường `PATH_TO_FX` nếu cần)
- IDE hỗ trợ Java (Eclipse, IntelliJ, VS Code...)

### Chạy từ IDE:
1. Clone repo về máy:
    ```bash
    git clone https://github.com/ClonerGit/BTL-Nh-m-5-Caro-Bot.git
    ```
2. Mở bằng IDE
3. Chạy class `src/main/Main.java`

### Chạy từ terminal:
```bash
javac --module-path %PATH_TO_FX% --add-modules javafx.controls -d bin src/main/Main.java
java --module-path %PATH_TO_FX% --add-modules javafx.controls -cp bin main.Main
```

---

## 🧬 Bắt Đầu

### Yêu Cầu
Đảm bảo đã cài đặt:
- Git
- JavaFX
- Selenium
- Chromedriver (nếu bạn làm việc với Twitter KOL crawler hoặc tương tự)

---

## 📦 Sao Chép Kho Lưu Trữ

```bash
git clone https://github.com/ClonerGit/BTL-Nh-m-5-Caro-Bot.git
cd BTL-Nh-m-5-Caro-Bot
```

---

## 🌿 Thực Hiện Thay Đổi Trên Nhánh Mới

### 1. Chuyển sang nhánh chính và pull bản mới nhất:
```bash
git checkout main
git pull origin main
```

### 2. Tạo và chuyển sang nhánh mới để làm việc:
```bash
git checkout -b <ten-nhanh-moi>
```

### 3. Thực hiện thay đổi, sau đó đưa vào staging area:
```bash
git add .
```

### 4. Commit với thông điệp rõ ràng:
```bash
git commit -m "Mô tả ngắn về thay đổi"
```

---

## ⬆️ Đẩy Nhánh Lên Remote
```bash
git push origin <ten-nhanh-moi>
```
