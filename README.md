## 動作確認

### 日報登録 API

```bash
curl -X POST http://localhost:5173/api/reports \
  -H "Content-Type: application/json" \
  --cookie "JSESSIONID=xxxx" \
  -d '{
    "reportDate":"2025-05-25",
    "curriculum":"●●研修受講",
    "y":"Y:～",
    "w":"W:～",
    "t":"T:～",
    "status":"SUBMITTED"
  }'
