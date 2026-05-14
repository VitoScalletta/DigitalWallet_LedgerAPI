# 🏦 Digital Wallet & Ledger API

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)
![Redis](https://img.shields.io/badge/Redis-Alpine-red.svg)
![JWT](https://img.shields.io/badge/Security-JWT-black.svg)
![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)

## 📖 Proje Hakkında
Digital Wallet Ledger API; kullanıcıların dijital cüzdan oluşturabildiği, para yatırma/çekme işlemleri yapabildiği ve cüzdanlar arası güvenli para transferi gerçekleştirebildiği bir finansal altyapı (Backend) projesidir.

Bu proje, basit bir CRUD uygulaması olmanın ötesinde; **finansal yazılımlarda karşılaşılan kritik güvenlik ve veri bütünlüğü sorunlarını çözmek amacıyla** tasarlanmıştır. Çifte harcama (Double Spending), aynı isteğin yanlışlıkla iki kez atılması ve yetkisiz erişim gibi senaryolar kurumsal mimari standartlarına uygun olarak engellenmiştir.

## ✨ Öne Çıkan Mühendislik Çözümleri

### 1. Eşzamanlılık Kontrolü (Concurrency & Optimistic Locking)
Para transferi gibi kritik işlemlerde, sisteme aynı anda (milisaniyeler içinde) gelen çoklu isteklerin bakiyeyi eksiye düşürmemesi veya havadan para üretmemesi gerekir.
* Cüzdan (Wallet) entity'sinde `@Version` anotasyonu kullanılarak **Optimistic Locking** (İyimser Kilitleme) mekanizması kurulmuştur.
* Aynı saniye içinde gelen transfer istekleri Hibernate tarafından `ObjectOptimisticLockingFailureException` fırlatılarak engellenir ve veritabanı bütünlüğü (Data Integrity) korunur.

### 2. Idempotency (Eş Etkililik) Kontrolü (Redis)
Zayıf internet bağlantısı veya kullanıcının butona iki kez basması gibi durumlarda, aynı para transferi isteğinin iki kez işlenmesini önlemek için **Idempotency** deseni uygulanmıştır.
* Her istekte Header üzerinden `Idempotency-Key` talep edilir.
* Gelen key, **Redis** üzerinde saklanır. Aynı key ile gelen ikinci bir istek, Gümrük Görevlisi (`IdempotencyInterceptor`) tarafından Controller'a ulaşmadan anında engellenir.

### 3. Stateless JWT Güvenliği (Spring Security)
Sistemin güvenliği, oturum (Session) tutmayan **Stateless JWT (JSON Web Token)** mimarisi ile sağlanmıştır.
* `SecurityConfig` üzerinden kayıt olma (`/users`) ve giriş yapma (`/auth/login`) dışındaki tüm uç noktalar dış dünyaya kapatılmıştır.
* Gelen her istek `JwtAuthenticationFilter` tarafından denetlenir ve sadece geçerli bileti (Token) olan kullanıcılar içeri alınır.
* Kullanıcı şifreleri veritabanına düz metin olarak değil, **BCrypt** algoritması ile hashlenerek kaydedilir.

### 4. Çok Kanallı Kaos Testi (Concurrency Testing)
Yazılan güvenlik kalkanlarının çalıştığını kanıtlamak için proje içerisinde özel bir `WalletConcurrencyTest` bulunmaktadır.
* Test kapsamında `ExecutorService` kullanılarak tam **50 farklı Thread**, aynı anda aynı cüzdandan para çekmeye (Transfer) çalışır.
* Test sonucunda Optimistic Lock mekanizmasının devreye girdiği, çifte harcamanın önlendiği ve **"Sistemdeki Toplam Para"** miktarının (Paranın Korunumu Kanunu) milimetrik olarak sabit kaldığı otomatik olarak doğrulanır.

---

## 🛠️ Kurulum ve Çalıştırma

### Gereksinimler
* Java 17+
* Maven
* Docker ve Docker Compose

### Adımlar

1. **Projeyi Klonlayın:**
```bash
git clone [https://github.com/kullaniciadiniz/DigitalWallet_LedgerAPI.git](https://github.com/kullaniciadiniz/DigitalWallet_LedgerAPI.git)
cd DigitalWallet_LedgerAPI
2. **Veritabanı ve Redis'i Ayağa Kaldırın:**
Proje dizininde bulunan `docker-compose.yml` dosyasını kullanarak altyapıyı başlatın:
```bash
docker-compose up -d
```

3. **Projeyi Çalıştırın:**
```bash
./mvnw spring-boot:run
```

---

## 🚀 API Kullanımı (Endpoints)

Tüm isteklerde `Content-Type: application/json` kullanılmalıdır. Korunan uç noktalar için Header'a `Authorization: Bearer <TOKEN>` ve transfer/deposit işlemleri için `Idempotency-Key: <UUID>` eklenmelidir.

### Auth & Users (Açık Uç Noktalar)

| HTTP Metodu | Uç Nokta | Açıklama |
| :--- | :--- | :--- |
| `POST` | `/api/v1/users` | Yeni kullanıcı kaydı oluşturur. |
| `POST` | `/api/v1/auth/login` | Giriş yapar ve 24 saatlik JWT Token döner. |

### Wallets & Transactions (Korumalı Uç Noktalar - Token Gerektirir)

| HTTP Metodu | Uç Nokta | Açıklama | Ekstra Header Gereksinimi |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/wallets` | Yeni bir cüzdan oluşturur. | Yok |
| `GET` | `/api/v1/wallets/user/{userId}`| Kullanıcıya ait tüm cüzdanları listeler. | Yok |
| `POST` | `/api/v1/wallets/{walletId}/deposit`| Cüzdana para yatırır. | `Idempotency-Key` |
| `POST` | `/api/v1/wallets/{walletId}/withdraw`| Cüzdandan para çeker. | `Idempotency-Key` |
| `POST` | `/api/v1/wallets/{walletId}/transfer`| Başka bir cüzdana para gönderir. | `Idempotency-Key` |

---
*Bu proje, modern ve ölçeklenebilir finansal backend sistemlerinin mimari zorluklarını (Concurrency, Security, Idempotency) simüle etmek ve çözüm üretmek amacıyla geliştirilmiştir.*