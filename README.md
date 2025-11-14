# 💰 Daily Money Tracker App

A simple Android Digital Money Tracker application built with **Kotlin** and **SQLite Database**.  
This app allows users to **add**, **view**, **delete**, and **track income & expenses**, calculate balance, and visualize data with a PieChart. All sensitive data is **encrypted** for security.

---

## 🚀 Features

✅ **Add Income & Expense** – Record income and expense with reason and amount  
✅ **View Data** – Display all income or expense entries in a RecyclerView  
✅ **Delete Data** – Remove individual income or expense entries  
✅ **SQLite Database** – Store all data locally with SQLite  
✅ **Balance Calculation** – Automatically calculate total income, total expense, and current balance  
✅ **PieChart Visualization** – Show ratio of income vs expense with `ir.mahozad.android.PieChart`  
✅ **Encrypted Storage** – Income and expense amounts and reasons are encrypted using AES  
✅ **Search Transactions** – Filter income or expense data by reason  
✅ **Dynamic UI** – Balance text color changes based on value, simple and modern design  
✅ **Navigation** – Easy navigation between Add Data and Show Data screens  

---

## 🧱 Tech Stack

| Component          | Technology Used                   |
|-------------------|------------------------------------|
| Language           | Kotlin                            |
| Database           | SQLite (Local Storage)            |
| UI                 | LinearLayout, CardView, RecyclerView, PieChart |
| Architecture       | MVVM (basic structure)            |
| Encryption         | AES (Advanced Encryption Standard)|
| Adapter/Binding    | RecyclerView Adapter               |
| Android Version    | API 24+ (Android 7.0 and above)   |

---

## 📲 Screens Included

1. **MainActivity** – Displays current balance, total income, total expense, and PieChart visualization. Buttons to add income/expense or view all data.  
2. **Add_Data Activity** – Add new income or expense with reason and amount. Encrypts data before saving to SQLite.  
3. **Show_Data Activity** – View all saved income or expense entries in a RecyclerView, delete individual entries, and search/filter transactions.  
4. **DatabaseOpenHelper** – SQLite helper for managing income & expense tables, calculation of totals, search, and data deletion.  
5. **PieChart** – Visual representation of income vs expense ratio.  

---

## 🗃️ Database Design

**Tables:** `income` and `expense`  

| Field   | Type           | Description                              |
|---------|----------------|------------------------------------------|
| id      | Int (Auto-generated) | Primary key                          |
| reason  | String         | Description/reason for transaction (encrypted in DB) |
| amount  | String         | Transaction amount (encrypted in DB)     |
| time    | Double         | Timestamp of transaction (millis)       |

---

## 🖼️ Screenshots 

| Main Screen | Add Income/Expense | Show Data |
|:--:|:--:|:--:|
|<img width="300" height="2992" alt="Screenshot_20251114_185945" src="https://github.com/user-attachments/assets/19a70f55-2d3c-48cd-9a9a-4d46c2f77ba6" />|<img width="300" height="2992" alt="Screenshot_20251114_190003" src="https://github.com/user-attachments/assets/bd613717-ec6e-4649-854e-18faf17ca20d" />|<img width="300" height="2992" alt="Screenshot_20251114_190207" src="https://github.com/user-attachments/assets/a2a60c84-342f-4274-bc52-a30e8ef970a2" />|


---



1. Clone this repository:
   ```bash
   git clone https://github.com/ar-sarkar-77/Digital-Money-Tracker-App.git  
---
## 👨‍💻 Author
---

## 👤 Mohammad Anondo Sarkar  
💼 Android App Developer | UI UX Designer | 💻 Computer Science Student  
📧 Email: anondosarkarar77@gmail.com  
🌍 From: Lalmonirhat, Bangladesh  
🌐 Website: https://arsarkar77.blogspot.com  
💬 Built with ❤️, powered by Kotlin, and fueled by late-night coding ☕💡  
