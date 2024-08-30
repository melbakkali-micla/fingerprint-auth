import { WebPlugin } from '@capacitor/core';

import type { AuthenticationResult, FingerprintAuthPlugin } from './definitions';

export class FingerprintAuthWeb extends WebPlugin implements FingerprintAuthPlugin {
  echo(parms: { value: string; }): Promise<void> {
    console.table(parms);
    throw new Error('Method not implemented.');
  }
  available(): Promise<void> {
    throw new Error('Method not implemented.');
  }
  authenticate(): Promise<AuthenticationResult> {
    throw new Error('Method not implemented.');
  }
  encrypt(parms: { value: string; }): Promise<{ encryptedMessage: string; }> {
    console.table(parms);
    throw new Error('Method not implemented.');
  }
  decrypt(parms: { value: string; }): Promise<{ decryptedMessage: string; }> {
    console.table(parms);
    throw new Error('Method not implemented.');
  }
}