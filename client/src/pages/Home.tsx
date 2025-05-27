// // src/pages/Home.tsx
// import { useState, useEffect } from 'react'
// import axios from 'axios'

// /** サーバーから返ってくるタスク一覧（改行区切り） */
// type Summary = {
//   reportDate: string   // 'YYYY-MM-DD'
//   t: string            // 改行区切りの「次にすること」
// }

// /** サーバーから返ってくる評価データ */
// type EvalDTO = {
//   reportDate: string   // 'YYYY-MM-DD'
//   taskIndex: number    // 同じ日付の中でのタスク番号（0始まり）
//   score: number        // 1〜5
// }

// /** テーブル描画用にフラット化した行データ */
// type Row = {
//   id: string           // `${reportDate}-${taskIndex}` をキーに
//   reportDate: string
//   task: string
// }

// export default function Home() {
//   const [rows,    setRows]    = useState<Row[]>([])
//   const [scores, setScores]  = useState<Record<string, number>>({})
//   const [loading, setLoading] = useState(true)
//   const [error,   setError]   = useState<string>()

//   useEffect(() => {
//     setLoading(true)
//     // ① タスクと評価を並列で取得
//     Promise.all([
//       axios.get<Summary[]>('/api/reports/last-week/t',{ withCredentials: true }),
//       axios.get<EvalDTO[]>   ('/api/reports/last-week/tasks', { withCredentials: true }),
//     ])
//       .then(([taskRes, evalRes]) => {
//         // タスクをフラット化
//         const flat: Row[] = []
//         taskRes.data.forEach(({ reportDate, t }) => {
//           t.split('\n')
//             .map((s) => s.trim())
//             .filter((s) => s !== '')
//             .forEach((task, idx) => {
//               flat.push({ id: `${reportDate}-${idx}`, reportDate, task })
//             })
//         })
//         setRows(flat)

//         // 評価マップを初期化（デフォルト3点）
//         const map: Record<string, number> = {}
//         flat.forEach((r) => { map[r.id] = 3 })
//         // サーバーから来た評価で上書き
//         evalRes.data.forEach(({ reportDate, taskIndex, score }) => {
//           const key = `${reportDate}-${taskIndex}`
//           map[key] = score
//         })
//         setScores(map)
//       })
//       .catch((e) => {
//         console.error(e)
//         setError('TODOリストの取得に失敗しました')
//       })
//       .finally(() => setLoading(false))
//   }, [])

//   // 評価セレクトを変更したとき
//   const handleScoreChange = (row: Row, newScore: number) => {
//     const key = row.id
//     // 1) ローカル state 更新
//     setScores((prev) => ({ ...prev, [key]: newScore }))

//     // 2) バックエンドへ保存
//     const taskIndex = parseInt(key.split('-')[1], 10)
//     axios
//       .post(
//         `/api/reports/${row.reportDate}/scores`,
//         [{ taskIndex, score: newScore }],
//         { withCredentials: true }
//       )
//       .catch((e) => {
//         console.error('評価の保存に失敗しました', e)
//         // （必要ならエラーフィードバックを追加）
//       })
//   }

//   if (loading) return <p>読み込み中…</p>
//   if (error)   return <p style={{ color: 'red' }}>{error}</p>

//   return (
//     <div style={{ padding: '20px 24px' }}>
//       <h2>TODOリスト</h2>
//       <p>過去一週間の「次にすること」に対する自己評価</p>

//       <table
//         style={{
//           width: '100%',
//           borderCollapse: 'collapse',
//           marginTop: '1em',
//         }}
//       >
//         <thead>
//           <tr>
//             <th style={cellStyle}>日付</th>
//             <th style={cellStyle}>次にすること</th>
//             <th style={cellStyle}>自己評価</th>
//           </tr>
//         </thead>
//         <tbody>
//           {rows.map((row) => (
//             <tr key={row.id}>
//               <td style={cellStyle}>{row.reportDate}</td>
//               <td style={cellStyle}>{row.task}</td>
//               <td style={cellStyle}>
//                 <select
//                   value={scores[row.id]}
//                   onChange={(e) =>
//                     handleScoreChange(row, Number(e.target.value))
//                   }
//                 >
//                   {[1, 2, 3, 4, 5].map((n) => (
//                     <option key={n} value={n}>
//                       {n}
//                     </option>
//                   ))}
//                 </select>
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//     </div>
//   )
// }

// const cellStyle: React.CSSProperties = {
//   border: '1px solid #ccc',
//   padding: '8px',
//   verticalAlign: 'top',
// }
// ↑点数の変更を画面遷移後に保存する

// // src/pages/Home.tsx
// import { useState, useEffect } from 'react'
// import axios from 'axios'

// type TaskScoreDTO = {
//   reportDate: string       // "2025-05-26"
//   taskIndex: number        // 0,1,2...
//   score: number            // 1〜5
// }

// type TaskString = {
//   reportDate: string
//   t: string                // 改行区切りの文字列
// }

// type Row = {
//   reportDate: string
//   tasks: string[]          // t を split('\n') した配列
//   scores: number[]         // tasks と同長のスコア配列 (index ごとに 1〜5)
// }

// export default function Home() {
//   const [rows, setRows] = useState<Row[]>([])
//   const [loading, setLoading] = useState(true)
//   const [error, setError] = useState<string>()

//   useEffect(() => {
//     // 並列で「タスク文字列」と「保存済みスコア」を取ってくる
//     Promise.all([
//       axios.get<TaskString[]>('/api/reports/last-week/t', { withCredentials: true }),
//       axios.get<TaskScoreDTO[]>('/api/reports/last-week/tasks', { withCredentials: true })
//     ])
//       .then(([taskRes, scoreRes]) => {
//         const taskData = taskRes.data
//         const scoreData = scoreRes.data

//         // scoreData を日付・taskIndex で検索しやすいように map に詰め替え
//         const scoreMap: Record<string, Record<number, number>> = {}
//         scoreData.forEach(({ reportDate, taskIndex, score }) => {
//           scoreMap[reportDate] = scoreMap[reportDate] || {}
//           scoreMap[reportDate][taskIndex] = score
//         })

//         // taskData をベースに Row を組み立て
//         const initialRows: Row[] = taskData.map(({ reportDate, t }) => {
//           const tasks = t
//             .split('\n')
//             .map((line) => line.trim())
//             .filter((line) => line !== '')

//           // 保存済みスコアがあればそれを、なければデフォルト 3
//           const scores = tasks.map((_, idx) =>
//             scoreMap[reportDate]?.[idx] ?? 3
//           )

//           return { reportDate, tasks, scores }
//         })

//         setRows(initialRows)
//       })
//       .catch(() => {
//         setError('TODOリストの取得に失敗しました')
//       })
//       .finally(() => {
//         setLoading(false)
//       })
//   }, [])

//   // スコア変更ハンドラ
//   const handleScoreChange = (date: string, idx: number, newScore: number) => {
//     setRows((prev) =>
//       prev.map((row) => {
//         if (row.reportDate !== date) return row
//         const newScores = [...row.scores]
//         newScores[idx] = newScore

//         // 変更が起こったら即サーバに保存
//         axios
//           .post(
//             `/api/reports/${date}/scores`,
//             newScores.map((score, taskIndex) => ({ taskIndex, score })),
//             { withCredentials: true }
//           )
//           .catch(() => {
//             alert('自己評価の保存に失敗しました')
//           })

//         return { ...row, scores: newScores }
//       })
//     )
//   }

//   if (loading) return <p>読み込み中…</p>
//   if (error) return <p style={{ color: 'red' }}>{error}</p>

//   return (
//     <div style={{ padding: '20px 24px' }}>
//       <h2>TODOリスト</h2>
//       <p>過去一週間の「次にすること」と自己評価</p>

//       <table
//         style={{
//           width: '100%',
//           borderCollapse: 'collapse',
//           marginTop: '1em',
//         }}
//       >
//         <thead>
//           <tr>
//             <th style={{ border: '1px solid #ccc', padding: '8px' }}>
//               日付
//             </th>
//             <th style={{ border: '1px solid #ccc', padding: '8px' }}>
//               次にすること
//             </th>
//             <th style={{ border: '1px solid #ccc', padding: '8px' }}>
//               自己評価
//             </th>
//           </tr>
//         </thead>
//         <tbody>
//           {rows.map(({ reportDate, tasks, scores }) => (
//             <tr key={reportDate}>
//               <td
//                 style={{
//                   border: '1px solid #ccc',
//                   padding: '8px',
//                   verticalAlign: 'top',
//                   whiteSpace: 'nowrap',
//                 }}
//               >
//                 {reportDate}
//               </td>
//               <td style={{ border: '1px solid #ccc', padding: '8px' }}>
//                 <ul style={{ margin: 0, paddingLeft: '1em' }}>
//                   {tasks.map((task, i) => (
//                     <li key={i}>{task}</li>
//                   ))}
//                 </ul>
//               </td>
//               <td style={{ border: '1px solid #ccc', padding: '8px' }}>
//                 <ul style={{ margin: 0, paddingLeft: '1em' }}>
//                   {tasks.map((_, i) => (
//                     <li key={i}>
//                       <select
//                         value={scores[i]}
//                         onChange={(e) =>
//                           handleScoreChange(
//                             reportDate,
//                             i,
//                             Number(e.target.value)
//                           )
//                         }
//                       >
//                         {[1, 2, 3, 4, 5].map((n) => (
//                           <option key={n} value={n}>
//                             {n}点
//                           </option>
//                         ))}
//                       </select>
//                     </li>
//                   ))}
//                 </ul>
//               </td>
//             </tr>
//           ))}
//         </tbody>
//       </table>
//     </div>
//   )
// }


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
            [{ taskIndex, score: newScore }],
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

