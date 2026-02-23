import { test, expect } from '@playwright/test';

/**
 * ログイン成功シナリオ
 *
 * 検証内容:
 * - 未認証状態で /cemeteries へアクセスすると /login へリダイレクトされること
 * - 有効な認証情報でログインできること
 * - ログイン後に /cemeteries へ遷移すること
 * - 霊園一覧の主要要素（見出し）が表示されること
 */
test('ログイン成功 — /cemeteries へ遷移し一覧が表示されること', async ({ page }) => {
  // 未認証で /cemeteries へアクセスすると /login へリダイレクト
  await page.goto('/cemeteries');
  await expect(page).toHaveURL(/\/login/);

  // 有効な認証情報でログイン
  await page.locator('#usernameInput').fill('tom');
  await page.locator('#passwordInput').fill('password');
  await page.getByRole('button', { name: 'ログイン' }).click();

  // ログイン後 /cemeteries へリダイレクトされること（Spring Security 6 は ?continue クエリを付与する場合あり）
  await expect(page).toHaveURL(/\/cemeteries/);

  // 霊園一覧の見出しが表示されること
  await expect(page.locator('h2')).toContainText('霊園ページ一覧');
});
