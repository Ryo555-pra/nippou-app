// src/pages/CalendarView.tsx

import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

type StatusMap = Record<
  string,             // '2025-05-19' のような YYYY-MM-DD
  'SUBMITTED'|'SAVED_TEMPORARILY'|'NOT_SUBMITTED'
>

export default function CalendarView() {
  const [value, setValue] = useState(new Date());
  const [statusMap, setStatusMap] = useState<StatusMap>({})
  const navigate = useNavigate();

  // １．過去１週間分を取りにいく
  useEffect(() => {
    axios.get< { reportDate:string; t:string }[]>('/api/reports/last-week', {
      withCredentials: true
    })
    .then(res => {
      const m: StatusMap = {}
      res.data.forEach(r => {
        // 今回は「GET /last-week」は T と日付しか返さないので
        // status は持ってない。status付けたいならコントローラ側を拡張
        // 例: ReportSummaryDTO に status フィールドを足す
        m[r.reportDate] = 'SUBMITTED'
      })
      setStatusMap(m)
    })
    .catch(() => {
      // エラー時は全部 NOT_SUBMITTED のまま
    })
  }, [])

  // tile ごとにマークを描画
  const tileContent = ({ date }: { date: Date }) => {
    const key = date.toLocaleDateString('sv-SE')
    const st = statusMap[key] ?? 'NOT_SUBMITTED'
    return (
      <div style={{ fontSize:'0.7em' }}>
        { st==='SUBMITTED'           ? '✅'
        : st==='SAVED_TEMPORARILY'   ? '🟡'
        :           '❌' }
      </div>
    )
  }

  // クリックで ReportForm へ遷移
  const handleDateClick = (date: Date) => {
    setValue(date)
    const d = date.toLocaleDateString('sv-SE')
    navigate(`/reports/new?date=${d}`)
  }

  return (
    <div>
      <h2>日報提出カレンダー</h2>
      <Calendar
        onClickDay={handleDateClick}
        value={value}
        tileContent={tileContent}
      />
      <p>選択された日: {value.toDateString()}</p>
    </div>
  )
}
