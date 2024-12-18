import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  resolve: {
    alias: {
      // Point to proper paths if `stompjs` and `sockjs-client` cause issues
      'stompjs': resolve(__dirname, 'node_modules/stompjs/lib/stomp.min.js'),
      'sockjs-client': resolve(__dirname, 'node_modules/sockjs-client/dist/sockjs.min.js'),
    },
  },
});
