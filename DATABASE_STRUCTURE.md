# OOP Mini Project - Wig Shop Admin Panel

## โครงสร้างระบบ
ระบบจัดการร้านขายตัวหนัง สำหรับด้าน Admin พร้อม Database SQLite

### ฐานข้อมูล (Database Tables)
1. **products** - เก็บข้อมูลหนัง
   - id (TEXT) - ID หนัง
   - title (TEXT) - ชื่อหนัง
   - category (TEXT) - หมวดหมู่
   - price (REAL) - ราคา
   
2. **dates** - วันฉาย
   - id (INTEGER) - ID วัน
   - date_str (TEXT) - รูปแบบ "DD MM YYYY"

3. **schedules** - รอบฉาย
   - id (INTEGER) - ID รอบ
   - date_id (INTEGER) - FK ไปที่ dates
   - time (TEXT) - เวลาเปิดฉาย
   - product_id (TEXT) - FK ไปที่ products
   - theater (TEXT) - โรงภาพยนตร์
   - price (REAL) - ราคาตั๋ว

4. **transactions** - การขายตั๋ว
   - id (INTEGER) - ID การขาย
   - schedule_id (INTEGER) - FK ไปที่ schedules
   - seat (TEXT) - ที่นั่ง
   - price (REAL) - ราคาขาย
   - timestamp (DATETIME) - เวลาขาย

## ไฟล์ที่เกี่ยวข้อง
- DatabaseManager.java - จัดการการเชื่อมต่อ SQLite
- Product.java, ProductManager.java - โมเดลและ CRUD ของหนัง
- DateModel.java, DateManager.java - โมเดลและ CRUD ของวันฉาย
- Schedule.java, ScheduleManager.java - โมเดลและ CRUD ของรอบฉาย
- Transaction.java, TransactionManager.java - โมเดลและ CRUD ของการขาย
- MainDashboard.java - GUI หลัก

## การรัน
```
javac -cp lib\sqlite-jdbc-3.44.0.0.jar -d bin src\*.java
java -cp "bin;lib\sqlite-jdbc-3.44.0.0.jar" MainDashboard
```

หรือ รัน batch file: run.bat

## ฟีเจอร์เปิดใช้งาน (Admin Side)
✓ Movie Manager - เพิ่ม/ลบ หนัง
✓ Schedule Manager - จัดการวันและรอบฉาย
✓ Report Dashboard - รายได้ทั้งหมด ตั๋วที่ขาย หนังขายดี

## ที่ยังไม่ได้ทำ
- User Side - หน้าซื้อตั๋ว
- Authentication - ระบบเข้าสู่ระบบ
- Advanced Reports - รายงานรายละเอียด
