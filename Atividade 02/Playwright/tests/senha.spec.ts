import { test, expect } from '@playwright/test';

const casosValidos = [
  { senha: 'Abcdef1!', classe: 'limite mínimo de 8 caracteres' },
  { senha: 'Abcdefghijklmnopq12!', classe: 'limite máximo de 20 caracteres' },
];

for (const caso of casosValidos) {
  test(`senha válida — ${caso.classe}`, async ({ page }) => {
    await page.goto('/senha');
    await page.getByLabel('Nova senha').fill(caso.senha);
    await page.getByLabel('Confirmar senha').fill(caso.senha);
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText('Senha cadastrada');
    await expect(resultado).toHaveAttribute('role', 'status');
    await expect(page.getByLabel('Nova senha')).toHaveValue('');
    await expect(page.getByLabel('Confirmar senha')).toHaveValue('');
  });
}

const casosForaDoPadrao = [
  { senha: 'Abcde1!', classe: 'abaixo do limite mínimo' },
  { senha: 'Abcdefghijklmnopq123!', classe: 'acima do limite máximo' },
  { senha: 'abcdef1!', classe: 'sem letra maiúscula' },
  { senha: 'ABCDEF1!', classe: 'sem letra minúscula' },
  { senha: 'Abcdefgh!', classe: 'sem número' },
  { senha: 'Abc def1!', classe: 'com espaço' },
  { senha: '', classe: 'vazia' },
];

for (const caso of casosForaDoPadrao) {
  test(`senha inválida ${caso.classe}`, async ({ page }) => {
    await page.goto('/senha');
    await page.getByLabel('Nova senha').fill(caso.senha);
    await page.getByLabel('Confirmar senha').fill(caso.senha);
    await page.getByRole('button', { name: 'Cadastrar senha' }).click();

    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText('Senha fora do padrão');
    await expect(resultado).toHaveAttribute('role', 'alert');
  });
}

test('senha inválida confirmação', async ({ page }) => {
  await page.goto('/senha');
  await page.getByLabel('Nova senha').fill('Abcdef1!');
  await page.getByLabel('Confirmar senha').fill('Abcdef2!');
  await page.getByRole('button', { name: 'Cadastrar senha' }).click();

  const resultado = page.locator('#resultado');
  await expect(resultado).toHaveText('As senhas não coincidem');
  await expect(resultado).toHaveAttribute('role', 'alert');
});
