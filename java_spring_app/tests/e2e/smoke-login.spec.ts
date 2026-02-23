import { test, expect } from '@playwright/test';

/**
 * スモークテスト: ログイン画面導線
 *
 * 検証内容:
 * - /login にアクセスできること
 * - ページタイトルに「ログイン」が含まれること
 * - ユーザー名入力欄 (#usernameInput) が表示されること
 * - パスワード入力欄 (#passwordInput) が表示されること
 * - 送信ボタンが表示されること
 */
test('ログイン画面 — 主要要素が表示されること', async ({ page }) => {
  await page.goto('/login');

  await expect(page.locator('h2')).toContainText('ログイン');
  await expect(page.locator('#usernameInput')).toBeVisible();
  await expect(page.locator('#passwordInput')).toBeVisible();
  await expect(page.getByRole('button', { name: 'ログイン' })).toBeVisible();
});
