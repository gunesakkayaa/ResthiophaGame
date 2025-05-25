# 🛡️ Resthiopha Game

**Resthiopha** is a lightweight 2D tile-based action RPG game built with Java Swing and Spring Boot. Players explore the map, fight monsters, collect loot, and manage gear and inventory.

---

## 🚀 Getting Started

### ✅ Requirements

To run or contribute to this project, ensure you have:

- Java 17+
- Gradle 8+
- Postman for API testing
- Git

---

### 🛠️ Clone the Repository

```bash
git clone https://github.com/your-username/ResthiophaGame.git
cd ResthiophaGame
```

---

### ⚙️ Build the Project

You can build the project with:

```bash
./gradlew clean build
```

Or to refresh dependencies:

```bash
./gradlew --refresh-dependencies
```

---

### ▶️ Run the Game

To launch the **Spring Boot backend**:

```bash
./gradlew bootRun
```

To run the **Swing game window**, open `Main.java` in an IDE (like IntelliJ) and run it.

---

## 🧪 API Testing with Postman

You can test available endpoints via Postman once Spring Boot is running:

**Base URL:** `http://localhost:8080`

**Example Endpoints:**
- `GET /api/player/coins`
- `POST /api/player/save`
- `GET /api/items/random`

---

## 🗃️ H2 Database Console

Available at:  
📍 `http://localhost:8080/h2-console`

**JDBC URL:** `jdbc:h2:mem:resthiopha_db`  
**Username:** `sa`  
**Password:** *(empty)*

---

## 💡 Contributing

Feel free to fork the repo and submit pull requests.  
We welcome improvements, new features, bug fixes, and documentation updates!
