// // // src/App.tsx

// // import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
// // import Home from './pages/Home';
// // import ReportList from './pages/ReportList';
// // import ReportForm from './pages/ReportForm';
// // import CalendarView from './pages/CalendarView';
// // import NotFound from './pages/NotFound';
// // import PrivateRoute from './components/PrivateRoute';

// // function App() {
// //   return (
// //     <BrowserRouter>
// //       <div style={{ padding: '10px', display: 'flex', gap: '10px' }}>
// //         <Link to="/">
// //           <button>ホーム</button>
// //         </Link>
// //         <Link to="/reports">
// //           <button style={{ padding: '8px 16px', backgroundColor: '#3498db', color: 'white', border: 'none', borderRadius: '5px' }}>
// //             日報一覧
// //           </button>
// //         </Link>
// //         <Link to="/reports/new">
// //           <button>日報登録</button>
// //         </Link>
// //         <Link to="/calendar">
// //           <button>カレンダー</button>
// //         </Link>
// //       </div>

// //       <Routes>
// //          {/* ログインフォームだけは公開 */}
// //         <Route
// //           path="/login"
// //           element={null /* Vite proxy が Spring の /login ページを返す */}
// //         />

// //         {/* ここから下は PrivateRoute でガード */}
// //         <Route element={<PrivateRoute />}>
// //         <Route path="/" element={<Home />} />
// //         <Route path="/reports" element={<ReportList />} />
// //         <Route path="/reports/new" element={<ReportForm />} />
// //         <Route path="/calendar" element={<CalendarView />} />
// //          </Route>
// //         <Route path="*" element={<NotFound />} />
// //       </Routes>
// //     </BrowserRouter>
// //   );
// // }

// // export default App;





// // src/App.tsx
// import { BrowserRouter, Routes, Route, Outlet, Navigate } from 'react-router-dom'
// import Home         from './pages/Home'
// import ReportList   from './pages/ReportList'
// import ReportForm   from './pages/ReportForm'
// import CalendarView from './pages/CalendarView'
// import NotFound     from './pages/NotFound'
// import PrivateRoute from './components/PrivateRoute'

// // ヘッダーだけを切り出したレイアウトコンポーネント
// function AuthenticatedLayout() {
//   return (
//     <>
//       <nav style={{ padding: '10px', display: 'flex', gap: '10px' }}>
//         <a href="/"><button>ホーム</button></a>
//         <a href="/reports"><button>日報一覧</button></a>
//         <a href="/reports/new"><button>日報登録</button></a>
//         <a href="/calendar"><button>カレンダー</button></a>
//       </nav>
//       <Outlet />  {/* ルートに応じたコンテンツをここに表示 */}
//     </>
//   )
// }

// export default function App() {
//   return (
//     <BrowserRouter>
//       <Routes>

//         {/* 1) ログイン画面はここでキャッチ。element={null} で React は何も描かず、
//              Vite proxy が Spring の /login ページを返します */}
//         <Route path="/login" element={null} />

//         {/* 2) 認証が必要な部分は PrivateRoute で一括ガード */}
//         <Route element={<PrivateRoute />}>
//           {/* 2-1) ガードを通ったあとのレイアウト */}
//           <Route element={<AuthenticatedLayout/>}>
//             <Route path="/"            element={<Home         />} />
//             <Route path="/reports"     element={<ReportList   />} />
//             <Route path="/reports/new" element={<ReportForm   />} />
//             <Route path="/calendar"    element={<CalendarView />} />
//           </Route>
//         </Route>

//         {/* 3) 他は NotFound */}
//         <Route path="*" element={<NotFound />} />
//       </Routes>
//     </BrowserRouter>
//   )
// }





// src/App.tsx
import { BrowserRouter, Routes, Route, Outlet } from 'react-router-dom'
import Home         from './pages/Home'
import ReportList   from './pages/ReportList'
import ReportForm   from './pages/ReportForm'
import CalendarView from './pages/CalendarView'
import NotFound     from './pages/NotFound'
import PrivateRoute from './components/PrivateRoute'

// 認証後にだけヘッダーを表示するレイアウト
function AuthenticatedLayout() {
  return (
    <>
      <nav style={{ padding: '10px', display: 'flex', gap: '10px' }}>
        <a href="/"><button>ホーム</button></a>
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
        <Route path="/login" element={null} />

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
