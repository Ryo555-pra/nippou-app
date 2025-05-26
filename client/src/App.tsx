// src/App.tsx
import { BrowserRouter, Routes, Route, Outlet } from 'react-router-dom'
import Home         from './pages/Home'
import ReportList   from './pages/ReportList'
import ReportForm   from './pages/ReportForm'
import CalendarView from './pages/CalendarView'
import NotFound     from './pages/NotFound'
import PrivateRoute from './components/PrivateRoute'
import Login from './pages/Login'

// 認証後にだけヘッダーを表示するレイアウト
function AuthenticatedLayout() {
  return (
    <>
      <nav style={{ padding: '10px', display: 'flex', gap: '10px' }}>
        <a href="/"><button>TODO</button></a>
        <a href="/reports"><button>日報一覧</button></a>
        <a href="/reports/new"><button>日報登録</button></a>
        <a href="/calendar"><button>カレンダー</button></a>
      </nav>
      <Outlet />
    </>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* ① /login は公開。element={null} で React は何も描かず、Vite proxy が Spring の /login を返す */}
        <Route path="/login" element={<Login />} />

        {/* ② ここから先は認証必須 */}
        <Route element={<PrivateRoute />}>
          {/* ③ 認証済みレイアウト */}
          <Route element={<AuthenticatedLayout />}>
            <Route path="/"            element={<Home         />} />
            <Route path="/reports"     element={<ReportList   />} />
            <Route path="/reports/new" element={<ReportForm   />} />
            <Route path="/calendar"    element={<CalendarView />} />
          </Route>
        </Route>

        {/* ④ それ以外は404 */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </BrowserRouter>
  )
}
