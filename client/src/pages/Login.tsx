// src/pages/Login.tsx
import { useState } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'

export default function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError]       = useState<string>()
  const navigate = useNavigate()

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      // form-urlencoded で送る
      const params = new URLSearchParams()
      params.append('username', username)
      params.append('password', password)

      await axios.post('/api/auth/login', params, {
        withCredentials: true,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      })
      // ログイン成功 → TODO: トップページへリダイレクト
      navigate('/')
    } catch {
      setError('認証に失敗しました')
    }
  }

  return (
    <div style={{ maxWidth: 400, margin: 'auto', padding: 20 }}>
      <h2>ログイン</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>ユーザー名</label><br/>
          <input
            value={username}
            onChange={e => setUsername(e.target.value)}
            required
          />
        </div>
        <div style={{ marginTop: 10 }}>
          <label>パスワード</label><br/>
          <input
            type="password"
            value={password}
            onChange={e => setPassword(e.target.value)}
            required
          />
        </div>
        {error && <p style={{ color: 'red' }}>{error}</p>}
        <button style={{ marginTop: 20 }} type="submit">ログイン</button>
      </form>
    </div>
  )
}
