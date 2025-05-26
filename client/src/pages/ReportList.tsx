// src/pages/ReportList.tsx

import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';

type ReportSummary = {
  /** yyyy-MM-dd 形式 */
  reportDate: string;
  /** T（次にすること）などサマリ項目 */
  curriculum: string;
};

export default function ReportList() {
  const [reports, setReports] = useState<ReportSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string| null>(null);

  useEffect(() => {
    axios
      .get<ReportSummary[]>('/api/reports/last-week', {
        withCredentials: true,
      })
      .then((res) => {
        setReports(res.data);
      })
      .catch((e) => {
        console.error(e);
        setError('日報の一覧取得に失敗しました');
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <p>読み込み中…</p>;
  }
  if (error) {
    return <p style={{ color: 'red' }}>{error}</p>;
  }

  return (
    <div style={{ padding: '0 20px' }}>
      <h2>日報一覧（過去１週間）</h2>
      {reports.length === 0 ? (
        <p>日報がまだ登録されていません。</p>
      ) : (
        <table
          style={{
            width: '100%',
            borderCollapse: 'collapse',
            marginTop: 16,
          }}
        >
          <thead>
            <tr>
              <th style={{ border: '1px solid #ccc', padding: '8px' }}>日付</th>
              <th style={{ border: '1px solid #ccc', padding: '8px' }}>カリキュラム</th>
            </tr>
          </thead>
          <tbody>
            {reports.map((r) => (
              <tr key={r.reportDate}>
                <td style={{ border: '1px solid #eee', padding: '8px' }}>
                  {/* クリックで編集画面へ */}
                  <Link to={`/reports/new?date=${r.reportDate}`}>
                    {r.reportDate}
                  </Link>
                </td>
                <td style={{ border: '1px solid #eee', padding: '8px' }}>
                  {r.curriculum}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

