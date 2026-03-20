import bcrypt from 'bcryptjs'

/**
 * 使用 bcrypt 对密码进行哈希加密
 * @param password 明文密码
 * @returns 加密后的密码
 */
export const hashPassword = (password: string): string => {
  // 生成 salt（10 rounds）
  const salt = bcrypt.genSaltSync(10)
  // 哈希密码
  return bcrypt.hashSync(password, salt)
}

/**
 * 验证密码是否匹配
 * @param password 明文密码
 * @param hash 哈希后的密码
 * @returns 是否匹配
 */
export const verifyPassword = (password: string, hash: string): boolean => {
  return bcrypt.compareSync(password, hash)
}

/**
 * 检查密码是否已经是 bcrypt 格式
 * bcrypt 格式以 $2a$, $2b$, $2y$ 等开头
 */
export const isBcryptHash = (password: string): boolean => {
  return /^\$2[ayb]\$\d+\$/.test(password)
}
