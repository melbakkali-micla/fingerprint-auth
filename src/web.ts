import { WebPlugin } from '@capacitor/core';

import type { FingerprintAuthPlugin } from './definitions';

export class FingerprintAuthWeb extends WebPlugin implements FingerprintAuthPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
