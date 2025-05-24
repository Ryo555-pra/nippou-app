// src/pages/NotFound.tsx

import { Link } from 'react-router-dom';

export default function NotFound() {
  return (
    <div>
      <h2>404 - ページが見つかりません</h2>
      <p>
        <Link to="/">ホームに戻る</Link>
      </p>
    </div>
  );
}
