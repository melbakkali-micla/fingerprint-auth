declare module "@capacitor/core" {
  interface PluginRegistery {
    FingerprintAuth: FingerprintAuthPlugin
  }
}

export interface AuthenticationResult {
  method: string;
  authenticated: boolean;
}

export interface FingerprintAuthPlugin {
  /**
   * Echoes the input value back. Mainly used for testing or debugging purposes.
   *
   * @param parms - An object containing the string to be echoed.
   * @param parms.value - The string value to be echoed.
   * @returns A Promise that resolves when the operation is complete.
   */
  echo(parms: { value: string }): Promise<void>;

  /**
   * Checks if fingerprint authentication is available on the device.
   *
   * @returns A Promise that resolves if fingerprint authentication is available.
   *          The promise is rejected if fingerprint authentication is not available.
   */
  available(): Promise<void>;

  /**
   * Authenticates the user using fingerprint authentication.
   *
   * @returns A Promise that resolves to an AuthenticationResult object, 
   *          indicating the method of authentication and whether it was successful.
   */
  authenticate(): Promise<AuthenticationResult>;

  /**
   * Encrypts a given string using the user's fingerprint for encryption key management.
   *
   * @param parms - An object containing the string to be encrypted.
   * @param parms.value - The string value to be encrypted.
   * @returns A Promise that resolves to an object containing the encrypted message.
   */
  encrypt(parms: { value: string }): Promise<{ encryptedMessage: string }>;

  /**
   * Decrypts a previously encrypted string using the user's fingerprint for decryption key management.
   *
   * @param parms - An object containing the encrypted string to be decrypted.
   * @param parms.value - The encrypted string value to be decrypted.
   * @returns A Promise that resolves to an object containing the decrypted message.
   */
  decrypt(): Promise<{ decryptedMessage: string }>;
}