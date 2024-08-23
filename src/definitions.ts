export interface FingerprintAuthPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
