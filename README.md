# fingerprint-auth

Fingerprint authentication using legacy Fingerprint Manager class

## Install

```bash
npm install fingerprint-auth
npx cap sync
```

## API

<docgen-index>

* [`echo(...)`](#echo)
* [`available()`](#available)
* [`authenticate()`](#authenticate)
* [`encrypt(...)`](#encrypt)
* [`decrypt(...)`](#decrypt)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### echo(...)

```typescript
echo(parms: { value: string; }) => Promise<void>
```

Echoes the input value back. Mainly used for testing or debugging purposes.

| Param       | Type                            | Description                                     |
| ----------- | ------------------------------- | ----------------------------------------------- |
| **`parms`** | <code>{ value: string; }</code> | - An object containing the string to be echoed. |

--------------------


### available()

```typescript
available() => Promise<void>
```

Checks if fingerprint authentication is available on the device.

--------------------


### authenticate()

```typescript
authenticate() => Promise<AuthenticationResult>
```

Authenticates the user using fingerprint authentication.

**Returns:** <code>Promise&lt;<a href="#authenticationresult">AuthenticationResult</a>&gt;</code>

--------------------


### encrypt(...)

```typescript
encrypt(parms: { value: string; }) => Promise<{ encryptedMessage: string; }>
```

Encrypts a given string using the user's fingerprint for encryption key management.

| Param       | Type                            | Description                                        |
| ----------- | ------------------------------- | -------------------------------------------------- |
| **`parms`** | <code>{ value: string; }</code> | - An object containing the string to be encrypted. |

**Returns:** <code>Promise&lt;{ encryptedMessage: string; }&gt;</code>

--------------------


### decrypt(...)

```typescript
decrypt(parms: { value: string; }) => Promise<{ decryptedMessage: string; }>
```

Decrypts a previously encrypted string using the user's fingerprint for decryption key management.

| Param       | Type                            | Description                                                  |
| ----------- | ------------------------------- | ------------------------------------------------------------ |
| **`parms`** | <code>{ value: string; }</code> | - An object containing the encrypted string to be decrypted. |

**Returns:** <code>Promise&lt;{ decryptedMessage: string; }&gt;</code>

--------------------


### Interfaces


#### AuthenticationResult

| Prop                | Type                 |
| ------------------- | -------------------- |
| **`method`**        | <code>string</code>  |
| **`authenticated`** | <code>boolean</code> |

</docgen-api>
