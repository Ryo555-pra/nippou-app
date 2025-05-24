// src/App.tsx

import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Home from './pages/Home';
import ReportList from './pages/ReportList';
import ReportForm from './pages/ReportForm';
import CalendarView from './pages/CalendarView';
import NotFound from './pages/NotFound';

function App() {
  return (
    <BrowserRouter>
      <div style={{ padding: '10px', display: 'flex', gap: '10px' }}>
        <Link to="/">
          <button>ホーム</button>
        </Link>
        <Link to="/reports">
          <button style={{ padding: '8px 16px', backgroundColor: '#3498db', color: 'white', border: 'none', borderRadius: '5px' }}>
            日報一覧
          </button>
        </Link>
        <Link to="/reports/new">
          <button>日報登録</button>
        </Link>
        <Link to="/calendar">
          <button>カレンダー</button>
        </Link>
      </div>

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/reports" element={<ReportList />} />
        <Route path="/reports/new" element={<ReportForm />} />
        <Route path="/calendar" element={<CalendarView />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
