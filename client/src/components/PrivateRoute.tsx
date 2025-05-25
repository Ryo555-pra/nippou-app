// src/components/PrivateRoute.tsx
import { useState, useEffect } from 'react'
import { Outlet, useLocation, Navigate } from 'react-router-dom'
import axios from 'axios'

export default function PrivateRoute() {
  const [checking, setChecking] = useState(true)
  const [authed, setAuthed]   = useState(false)
  const location = useLocation()

  useEffect(() => {
    setChecking(true)
    axios.get('/api/reports/whoami', { withCredentials: true })
      .then(() => setAuthed(true))
      .catch(() => setAuthed(false))
      .finally(() => setChecking(false))
  }, [location.pathname])

  if (checking) {
    // 認証チェック完了前は何も描かない（フリッカー防止）
    return null
  }
  if (!authed) {
    // 認証されていなければ /login へリダイレクト
    return <Navigate to="/login" replace />
  }
  // 認証済みなら子ルートを描画
  return <Outlet />
}
