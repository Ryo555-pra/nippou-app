// src/pages/Home.tsx
import { useState, useEffect } from 'react'
import axios from 'axios'

type Summary = {
  reportDate: string
  t: string
}

export default function Home() {
  const [items, setItems] = useState<Summary[]>([])
  const [loading, setLoading] = useState(true)
  const [error,   setError]   = useState<string>()

  useEffect(() => {
    axios.get<Summary[]>('/api/reports/last-week', { withCredentials: true })
      .then(res => setItems(res.data))
      .catch(err => setError('取得に失敗しました'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <p>読み込み中…</p>
  if (error)   return <p style={{ color: 'red' }}>{error}</p>

  return (
    <div style={{ padding: 24 }}>
      <h1>TODOリスト</h1>
      <h2>過去一週間の「次にすること」</h2>
      {items.length === 0
        ? <p>まだ日報がありません</p>
        : (
          <ul style={{ listStyle: 'none', padding: 0 }}>
            {items.map(item => (
              <li key={item.reportDate} style={{ marginBottom: 8 }}>
                <strong>{item.reportDate}</strong>：{item.t}
              </li>
            ))}
          </ul>
        )
      }
    </div>
  )
}
