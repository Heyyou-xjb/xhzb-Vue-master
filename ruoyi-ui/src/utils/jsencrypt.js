import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair
// 密钥通过环境变量配置（参考根目录与前端目录下的 .env.example），
// 不要将真实密钥提交到仓库。未配置密钥时相关功能自动降级。

const publicKey = import.meta.env.VITE_APP_RSA_PUBLIC_KEY
const privateKey = import.meta.env.VITE_APP_RSA_PRIVATE_KEY

// 加密
export function encrypt(txt) {
  if (!publicKey || !txt) {
    return undefined
  }
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) || undefined // 对数据进行加密
}

// 解密
export function decrypt(txt) {
  if (!privateKey || !txt) {
    return undefined
  }
  const encryptor = new JSEncrypt()
  encryptor.setPrivateKey(privateKey) // 设置私钥
  return encryptor.decrypt(txt) || undefined // 对数据进行解密
}

