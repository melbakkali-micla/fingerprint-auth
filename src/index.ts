import { registerPlugin } from '@capacitor/core';

import type { FingerprintAuthPlugin } from './definitions';

const FingerprintAuth = registerPlugin<FingerprintAuthPlugin>('FingerprintAuth', {
  web: () => import('./web').then(m => new m.FingerprintAuthWeb()),
});

export * from './definitions';
export { FingerprintAuth };
