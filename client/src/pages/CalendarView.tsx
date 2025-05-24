// src/pages/CalendarView.tsx

import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

type DateValue = Date | null;

export default function CalendarView() {
  const [value, setValue] = useState<Date>(new Date());
  const navigate = useNavigate();

  // 仮：提出済みの日付（将来はAPIから取得予定）
  const submittedDates = ['2025-05-19', '2025-05-17'];

  // 日付フォーマット YYYY-MM-DD に変換
  const formatDate = (date: Date) =>
    // date.toISOString().split('T')[0];
    date.toLocaleDateString('sv-SE');

const handleDateClick = (date: Date) => {
    setValue(date);
    const formatted = formatDate(date);
    navigate(`/reports/new?date=${formatted}`);
  };

  return (
    <div>
      <h2>日報提出カレンダー</h2>
      <Calendar
        onClickDay={handleDateClick}
        value={value}
        tileContent={({ date }) => {
          const dateStr = formatDate(date);
          if (submittedDates.includes(dateStr)) {
            return <div style={{ fontSize: '0.7em', color: 'green' }}>✅</div>;
          } else {
            return <div style={{ fontSize: '0.7em', color: 'red' }}>❌</div>;
          }
        }}
      />
      <p>選択された日: {value?.toDateString()}</p>
    </div>
  );
}
