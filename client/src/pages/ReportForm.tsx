// src/pages/ReportForm.tsx

import { useSearchParams, useNavigate } from 'react-router-dom'; // クエリパラメータを取得するために使用
import { useEffect, useState } from 'react';
import axios from 'axios';

type ReportStatus = 'NOT_SUBMITTED' | 'SUBMITTED' | 'SAVED_TEMPORARILY';


// レスポンスとして受け取る日報の型定義
// バックエンドの Report エンティティに合わせて hrid は含めない
type Report = {
  // hrid: string;         // ユーザーID（トレーニーID）
  reportDate: string;   // 日付（'YYYY-MM-DD'）
  curriculum: string;  // 入力された日報本文
  y: string;
  w: string;
  t: string;
  status: ReportStatus; // 日報の状態（未提出、提出済み、一時保存）
};

export default function ReportForm() {
  // クエリパラメータから "date" を取得、なければ今日の日付を初期値にする
  const [searchParams] = useSearchParams();
  const date = searchParams.get('date') || new Date().toLocaleDateString('sv-SE');

  // データの読み込み中フラグ（ローディング状態）
  const [loading, setLoading] = useState(true);


  // 既に提出された日報が存在するかどうかを判断するフラグ
  const [isExisting, setIsExisting] = useState(false);


  // フォーム入力用 state
  const [status, setStatus] = useState<ReportStatus>('NOT_SUBMITTED');
  const [curriculum, setCurriculum] = useState('');
  const [y, setY] = useState('');
  const [w, setW] = useState('');
  const [t, setT] = useState('');

  const navigate = useNavigate(); // ← 遷移用フック

  // 初回マウントで GET /api/reports?date=...
  useEffect(() => {
    axios.get<Report>('/api/reports', {
      params: { date },
      withCredentials: true
    })
      .then(res => {
        // 200 OK なら既存レポートをセット
        setCurriculum(res.data.curriculum);
        setY(res.data.y);
        setW(res.data.w);
        setT(res.data.t);
        setStatus(res.data.status);
        setIsExisting(true);
      })
      .catch(err => {
        // 404 Not Found → 未登録なら isExisting=false のまま
        if (err.response?.status !== 404) {
          console.error('Report fetch failed', err);
        }
      })
      .finally(() => {
        setLoading(false);
      });
  }, [date]);

  
    // 2) フォーム送信時に POST /api/reports
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post(
        '/api/reports',
        {
          reportDate: date,
          curriculum,
          y,
          w,
          t,
          status
        },
        { withCredentials: true }
      );
      alert('日報を送信しました');
      navigate('/calendar');
    } catch {
      alert('送信に失敗しました');
    }
  };

  // 読み込み中はフォームを描画せずローディング表示
  if (loading) return <p>読み込み中…</p>;

  return (
    <div>
      <h2>日報{isExisting ? '編集' : '登録'}画面</h2>
      <p>対象日付：{date}</p>

      <form onSubmit={handleSubmit}>
        <label>
          ステータス：
          <select value={status} onChange={e => setStatus(e.target.value as ReportStatus)}>
            <option value="NOT_SUBMITTED">未提出</option>
            <option value="SAVED_TEMPORARILY">一時保存</option>
            <option value="SUBMITTED">提出済み</option>
          </select>
        </label>
        <br /><br />

        <label>カリキュラム記入欄：</label><br />
        <textarea
          value={curriculum}
          onChange={e => setCurriculum(e.target.value)}
          rows={4}
          cols={60}
          required
        /><br /><br />

        <label>Y（やったこと）：</label><br />
        <textarea
          value={y}
          onChange={e => setY(e.target.value)}
          rows={3}
          cols={60}
          required
        /><br /><br />

        <label>W（わかったこと）：</label><br />
        <textarea
          value={w}
          onChange={e => setW(e.target.value)}
          rows={3}
          cols={60}
          required
        /><br /><br />

        <label>T（次にすること）：</label><br />
        <textarea
          value={t}
          onChange={e => setT(e.target.value)}
          rows={3}
          cols={60}
          required
        /><br /><br />

        <button type="submit">{isExisting ? '更新する' : '登録する'}</button>
      </form>
    </div>
  );
}