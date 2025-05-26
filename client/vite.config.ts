// // vite.config.ts
// import { defineConfig } from 'vite'
// import react from '@vitejs/plugin-react'

// export default defineConfig({
//   plugins: [react()],
//   server: {
//     // 開発サーバーでのプロキシ設定
//     proxy: {
//       // API 呼び出しを Spring Boot (http://localhost:8080) へ転送
//       '/api': {
//         target: 'http://localhost:8080',
//         changeOrigin: true,
//         secure: false,
//       },
//       // Spring のフォームログイン画面 (/login) を透過
//       '/login': {
//         target: 'http://localhost:8080',
//         changeOrigin: true,
//         secure: false,
//       },
//       // ログアウト処理を透過
//       '/logout': {
//         target: 'http://localhost:8080',
//         changeOrigin: true,
//         secure: false,
//       },
//     }
//   }
// })
// vite.config.ts
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      /**
       * /api/** で始まるリクエストはすべて
       * バックエンドの http://localhost:8080/api/** に転送します。
       * withCredentials: true を axios で指定していれば
       * Cookie（JSESSIONID）も正しく送られます。
       */
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        // (必要なら) rewrite でパス書き換え例
        // rewrite: path => path.replace(/^\/api/, '/api')
      }
    }
  }
})
