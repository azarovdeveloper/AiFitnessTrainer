**AiFitnessTrainer** is a simple Spring Boot 4+ application demonstrating integration with  
**Google Gemini AI** using the official **Google GenAI SDK**, without relying on **Spring AI**  
(which is not yet supported in Spring Boot 4+).

The application provides:
- REST-based chat sessions
- AI-powered responses using Gemini
- Session-based conversation history
- Prompt-based AI interactions

---

## 🚀 Tech Stack

- Java 21+
- Spring Boot 4+
- Google Gemini (GenAI SDK)
- PostgreSQL
- Jackson
- JSON Schema validation
- Bean Validation (Jakarta Validation)

---
## ▶️ Run the Application

### 1. Clone the repository
```bash
git clone https://github.com/azarovdeveloper/AiFitnessTrainer.git
cd AiFitnessTrainer
```

### 2. ️Configure required environment variables
Set the following environment variables before running the application:
  ```bash
  export GOOGLE_APPLICATION_CREDENTIALS=/path/to/credentials.json
  export GOOGLE_CLOUD_PROJECT=your-gcp-project-id
  export GOOGLE_CLOUD_LOCATION=global
  export GOOGLE_GENAI_USE_VERTEXAI=true
  ```
Notes:
* **GOOGLE_APPLICATION_CREDENTIALS** must point to a Google Cloud service account JSON file
* **GOOGLE_CLOUD_PROJECT** is your Project ID from Google Cloud Console
* More details: https://cloud.google.com/docs/authentication

### 3. Run application
```bash
 ./mvnw spring-boot:run
 ```
The application will start on:

```arduiono
http://localhost:8080
```


### 4. Create a new chat session
```bash
  curl --location --request POST 'http://localhost:8080/api/chat/session?prompt=default'
```

```json
{
  "sessionId": "b2640064-f90c-4078-9d0a-dc75cde3939b"
}
```

### 5. Send a message to the session
```bash
  curl --location 'http://localhost:8080/api/chat/b2640064-f90c-4078-9d0a-dc75cde3939b' \
    --header 'Content-Type: application/json' \
    --data '{"message": "Hello, my nickname is AzarovDeveloper"}' 
```
