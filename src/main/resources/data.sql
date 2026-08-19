-- 测试账号：
-- admin / admin123
-- user / user123
-- guest / guest123
-- 数据库只保存“盐 + 迭代 1024 次 SHA-256”后的结果，不保存明文密码。

INSERT INTO app_user (
    username,
    password_hash,
    salt,
    display_name,
    enabled,
    role_key,
    permission
) VALUES
(
    'admin',
    '4570d9d11301d06fb5b6697a2370a47a4db8430fc1a1585cc527374f952cce3a',
    'mini-ruoyi-2026',
    '学习管理员',
    TRUE,
    'admin',
    'dashboard:view'
),
(
    'user',
    '96f6328af1574334b33bdc810058b3849b506c54972d70dce92f8682aae64403',
    'mini-ruoyi-user',
    '普通用户',
    TRUE,
    'user',
    'dashboard:view'
),
(
    'guest',
    'c474650bd124e234a686ea68a847cc5d40ddb1b528caa96e7bd04ff7c2fb4083',
    'mini-ruoyi-guest',
    '访客用户',
    TRUE,
    'guest',
    'profile:view'
)
ON DUPLICATE KEY UPDATE
    password_hash = VALUES(password_hash),
    salt = VALUES(salt),
    display_name = VALUES(display_name),
    enabled = VALUES(enabled),
    role_key = VALUES(role_key),
    permission = VALUES(permission);
