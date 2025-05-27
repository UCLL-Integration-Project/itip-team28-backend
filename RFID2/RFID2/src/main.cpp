#include <ArduinoJson.h>

#include <WiFi.h>
#include <WiFiUdp.h>
#include <Wire.h>

#include <MFRC522.h>
#include <SPI.h>
#include <HTTPClient.h>
#include <NTPClient.h>

#include <SocketIOclient.h>
#include <WebSocketsClient.h>

#include "config.h"


#define NSS_PIN 15
#define RST_PIN 5
#define GREEN_PIN 13
#define ORANGE_PIN 12



WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org", utcOffsetInSeconds);
String lastCardUID;
MFRC522 mfrc522(NSS_PIN, RST_PIN);
SocketIOclient socketIO;

String getUIDString(byte *buffer, byte bufferSize)
{
  String uid = "";
  for (byte i = 0; i < bufferSize; i++)
  {
    if (buffer[i] < 0x10)
      uid += "0";
    uid += String(buffer[i], HEX);
  }
  uid.toUpperCase();
  return uid;
}

String getMacAddress()
{
  uint8_t mac[6];
  WiFi.macAddress(mac);
  char macStr[18];
  sprintf(macStr, "%02X:%02X:%02X:%02X:%02X:%02X",
          mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
  return String(macStr);
}

String getISO8601Timestamp()
{
  timeClient.update();
  time_t epochTime = timeClient.getEpochTime();
  struct tm *ptm = gmtime((time_t *)&epochTime);
  char buffer[25];
  sprintf(buffer, "%04d-%02d-%02dT%02d:%02d:%02dZ",
          ptm->tm_year + 1900,
          ptm->tm_mon + 1,
          ptm->tm_mday,
          ptm->tm_hour + 1,
          ptm->tm_min,
          ptm->tm_sec);
  return String(buffer);
}

void sendData(String uid, String mac, String timestamp) {
  digitalWrite(ORANGE_PIN, HIGH);
  DynamicJsonDocument doc(256);
  JsonArray array = doc.to<JsonArray>();
  array.add("update");
  JsonObject data = array.createNestedObject();
  data["car_id"] = uid;
  data["reader_id"] = mac;
  data["timestamp"] = timestamp;

  String output;
  serializeJson(doc, output);
  Serial.print("Sending via WebSocket: ");
  Serial.println(output);
  socketIO.sendEVENT(output);
  delay(300);
  digitalWrite(ORANGE_PIN, LOW);
}

void socketIOEvent(socketIOmessageType_t type, uint8_t *payload, size_t length) {
  if (type == sIOtype_CONNECT) {
    Serial.println("[WS] Connected.");
    Serial.printf("[WS] Server event: %.*s\n", length, payload);
    socketIO.send(sIOtype_CONNECT, "/");
  } else if (type == sIOtype_DISCONNECT) {
    Serial.println("[WS] Disconnected. Reconnecting...");
    socketIO.beginSSL(websocket_host, websocket_port, websocket_path);
    // socketIO.begin(websocket_host, websocket_port, websocket_path);
  }
}

void setup()
{
  Serial.begin(115200);
  SPI.begin();
  mfrc522.PCD_Init();
  WiFi.begin(ssid, password);
  Serial.println("Verbinden met WiFi...");
  pinMode(ORANGE_PIN, OUTPUT);
  pinMode(GREEN_PIN, OUTPUT);
  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
  }
  Serial.println("Verbonden met WiFi!");
  Serial.println("IP-adres: " + WiFi.localIP().toString());
  

  timeClient.begin();

  socketIO.beginSSL(websocket_host, websocket_port, websocket_path);
  // socketIO.begin(websocket_host, websocket_port, websocket_path);
  socketIO.onEvent(socketIOEvent);
  digitalWrite(GREEN_PIN, HIGH);
}

void loop()
{
  socketIO.loop();

  static unsigned long lastScanTime = 0;
  const unsigned long scanInterval = 3000; // 3 second

  if (millis() - lastScanTime < scanInterval)
    return;

  if (mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()) {
    String newUID = getUIDString(mfrc522.uid.uidByte, mfrc522.uid.size);
    String mac = getMacAddress();
    String timestamp = getISO8601Timestamp();

    Serial.println("Nieuwe kaart gescand:");
    Serial.println("UID: " + newUID);
    Serial.println("MAC: " + mac);
    Serial.println("Timestamp: " + timestamp);


    sendData(newUID, mac, timestamp);
    mfrc522.PICC_HaltA();
    lastScanTime = millis();
  }
}