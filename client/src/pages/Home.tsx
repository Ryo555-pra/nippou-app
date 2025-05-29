// src/pages/Home.tsx
import { useState, useEffect } from 'react'
import axios from 'axios'

// サーバーから返ってくるタスク文字列 DTO
type TaskString = {
  reportDate: string  // 'YYYY-MM-DD'
  t: string           // 改行区切りの「次にすること」
}

// サーバーから返ってくる評価 DTO
type TaskScoreDTO = {
  reportDate: string
  taskIndex: number   // 同じ日付内でのタスク番号
  score: number       // 1～5
}

// 表示用にフラット化した行データ
type Row = {
  reportDate: string
  tasks: string[]     // 改行で分割したタスクの配列
  scores: number[]    // tasks と同じ長さのスコア配列
}

export default function Home() {
  const [rows,    setRows]    = useState<Row[]>([])
  const [loading, setLoading] = useState(true)
  const [error,   setError]   = useState<string>()

  useEffect(() => {
    setLoading(true)
    Promise.all([
      axios.get<TaskString[]>('/api/reports/last-week/t',    { withCredentials: true }),
      axios.get<TaskScoreDTO[]>('/api/reports/last-week/tasks', { withCredentials: true })
    ])
      .then(([taskRes, scoreRes]) => {
        // ① 既存スコアを lookup しやすい map に変換
        const scoreMap: Record<string, Record<number, number>> = {}
        scoreRes.data.forEach(({ reportDate, taskIndex, score }) => {
          if (!scoreMap[reportDate]) scoreMap[reportDate] = {}
          scoreMap[reportDate][taskIndex] = score
        })

        // ② タスク文字列を Row[] に変換
        const newRows: Row[] = taskRes.data.map(({ reportDate, t }) => {
          const tasks = t
            .split('\n')
            .map((s) => s.trim())
            .filter((s) => s !== '')

          // スコアがあればそれを、なければデフォルト 3
          const scores = tasks.map((_, idx) =>
            scoreMap[reportDate]?.[idx] ?? 3
          )

          return { reportDate, tasks, scores }
        })

        setRows(newRows)
      })
      .catch(() => {
        setError('TODOリストの取得に失敗しました')
      })
      .finally(() => {
        setLoading(false)
      })
  }, [])

  // 全体達成率を算出
  const { overallPercent } = (() => {
    const sumTotal = rows
      .flatMap((r) => r.scores)
      .reduce((acc, v) => acc + v, 0)
    const maxTotal = rows
      .reduce((acc, r) => acc + r.scores.length * 5, 0)
    const percent = maxTotal > 0 ? Math.ceil((sumTotal / maxTotal) * 100) : 0
    return { overallPercent: percent }
  })()

  // ハンドラ：スコア変更時に即保存＆ステート更新
  const handleScoreChange = (reportDate: string, taskIndex: number, newScore: number) => {
    setRows((prev) =>
      prev.map((r) => {
        if (r.reportDate !== reportDate) return r
        const newScores = [...r.scores]
        newScores[taskIndex] = newScore

        // バックエンドへ保存
        axios
          .post(
            `/api/reports/${reportDate}/scores`,
            [{ reportDate, taskIndex, score: newScore }], // reportDate を追加
            { withCredentials: true }
          )
          .catch(() => {
            alert('自己評価の保存に失敗しました')
          })

        return { ...r, scores: newScores }
      })
    )
  }

  if (loading) return <p>読み込み中…</p>
  if (error) return <p style={{ color: 'red' }}>{error}</p>

  return (
    <div style={{ padding: '20px 24px' }}>
      {/* ヘッダー: 左にタイトル、右に全体達成率 */}
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <h2>TODOリスト</h2>
        <div style={{ fontSize: '1.2em', fontWeight: 'bold' }}>
          全体達成率：{overallPercent}%
        </div>
      </div>
      <p>過去一週間の「次にすること」と自己評価</p>

      <table
        style={{
          width: '100%',
          borderCollapse: 'collapse',
          marginTop: '1em',
        }}
      >
        <thead>
          <tr>
            <th style={cellStyle}>日付</th>
            <th style={cellStyle}>次にすること</th>
            <th style={cellStyle}>自己評価</th>
          </tr>
        </thead>
        <tbody>
          {rows.map(({ reportDate, tasks, scores }) => (
            <tr key={reportDate}>
              <td style={cellStyle}>{reportDate}</td>
              <td style={cellStyle}>
                <ul style={{ margin: 0, paddingLeft: '1em' }}>
                  {tasks.map((task, i) => (
                    <li key={i}>{task}</li>
                  ))}
                </ul>
              </td>
              <td style={cellStyle}>
                <ul style={{ margin: 0, paddingLeft: '1em' }}>
                  {tasks.map((_, i) => (
                    <li key={i}>
                      <select
                        value={scores[i]}
                        onChange={(e) =>
                          handleScoreChange(
                            reportDate,
                            i,
                            Number(e.target.value)
                          )
                        }
                      >
                        {[1, 2, 3, 4, 5].map((n) => (
                          <option key={n} value={n}>
                            {n}点
                          </option>
                        ))}
                      </select>
                    </li>
                  ))}
                </ul>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}

const cellStyle: React.CSSProperties = {
  border: '1px solid #ccc',
  padding: '8px',
  verticalAlign: 'top',
}

