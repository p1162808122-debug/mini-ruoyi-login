-- 教学账号：admin / admin123
-- 数据库只保存“盐 + 迭代 1024 次 SHA-256”后的结果，不保存明文密码。
INSERT INTO app_user (
    username,
    password_hash,
    salt,
    display_name,
    enabled,
    role_key,
    permission
) VALUES (
    'admin',
    '4570d9d11301d06fb5b6697a2370a47a4db8430fc1a1585cc527374f952cce3a',
    'mini-ruoyi-2026',
    '学习管理员',
    TRUE,
    'admin',
    'dashboard:view'
);
