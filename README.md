# AiFitnessTrainer
Simple application to present Google Gemini AI integration with Spring Boot 4+ using Google GenAI SDK intead of not yet supported Spring AI

# Run application
1. git clone https://github.com/azarovdeveloper/AiFitnessTrainer.git
2. add reqired enviroment variables
  * GOOGLE_APPLICATION_CREDENTIALS= environment variable to provide the location of a credential JSON file. You can get more details in the Google Cloud docs.
  * GOOGLE_CLOUD_LOCATION=global
  * GOOGLE_CLOUD_PROJECT= this is Project ID from your Google Cloud Console.
  * GOOGLE_GENAI_USE_VERTEXAI=true
3. ./mvnw spring-boot:run
4. create new session: <br/>
  curl --location --request POST 'http://localhost:8080/api/chat/session?prompt=default'
5. using session id from response send message:<br/>
  curl --location 'http://localhost:8080/api/chat/{session-id}' \
    --header 'Content-Type: application/json' \
    --data '{"message": "Hello, my nickname is AzarovDeveloper"}' 
