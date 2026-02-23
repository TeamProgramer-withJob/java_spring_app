import { test, expect } from '@playwright/test';

/**
 * 霊園作成シナリオ
 *
 * 検証内容:
 * - ログイン済み状態で霊園作成フォームへ遷移できること
 * - 必須項目（霊園名）を入力して作成できること
 * - 作成後に詳細ページへ遷移し、作成した霊園名が表示されること
 *
 * 前提: data.sql で用意された初期ユーザー tom / password を使用
 */
test.describe('霊園作成', () => {

  /** 各テスト前にログイン済み状態にする */
  test.beforeEach(async ({ page }) => {
    await page.goto('/login');
    await page.locator('#usernameInput').fill('tom');
    await page.locator('#passwordInput').fill('password');
    await page.getByRole('button', { name: 'ログイン' }).click();
    // ログイン完了（Spring Security のデフォルト success URL = /）
    await page.waitForURL(url => !url.toString().includes('/login'));
  });

  test('作成フォームへ遷移できること', async ({ page }) => {
    await page.goto('/cemeteries/new');

    await expect(page.locator('h2')).toContainText('霊園ページを作成する');
    await expect(page.locator('#name')).toBeVisible();
    await expect(page.getByRole('button', { name: '作成する' })).toBeVisible();
  });

  test('必須項目を入力して作成し、詳細ページで確認できること', async ({ page }) => {
    // タイムスタンプで重複しない霊園名を生成
    const cemeteryName = `E2Eテスト霊園_${Date.now()}`;

    await page.goto('/cemeteries/new');
    await page.locator('#name').fill(cemeteryName);
    await page.getByRole('button', { name: '作成する' }).click();

    // 作成後は詳細ページ /cemeteries/{id} へリダイレクト
    await expect(page).toHaveURL(/\/cemeteries\/\d+$/);

    // 詳細ページに作成した霊園名が表示されること
    await expect(page.locator('h2')).toContainText(cemeteryName);
  });
});
