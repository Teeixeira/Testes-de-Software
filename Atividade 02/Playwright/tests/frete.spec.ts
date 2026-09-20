import { test, expect } from '@playwright/test';

const casosValidos = [
  { cep: '80000000', valor: '199,99', esperado: 'Frete: R$ 15,00', classe: 'CEP iniciado por 8' },
  { cep: '10000000', valor: '100,00', esperado: 'Frete: R$ 25,00', classe: 'demais CEPs' },
  { cep: '10000000', valor: '199,99', esperado: 'Frete: R$ 25,00', classe: 'abaixo do limite de frete grátis' },
  { cep: '10000000', valor: '200,00', esperado: 'Frete grátis', classe: 'limite de frete grátis' },
  { cep: '80000000', valor: '200,01', esperado: 'Frete grátis', classe: 'acima do limite de frete grátis' },
];

for (const caso of casosValidos) {
  test(`frete válido ${caso.classe}`, async ({ page }) => {
    await page.goto('/frete');
    await page.getByLabel('CEP').fill(caso.cep);
    await page.getByLabel('Valor do pedido').fill(caso.valor);
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText(caso.esperado);
    await expect(resultado).toHaveAttribute('role', 'status');
  });
}

const casosInvalidos = [
  { cep: '8000000', valor: '100', classe: 'CEP com menos de 8 dígitos' },
  { cep: '8000000a', valor: '100', classe: 'CEP com caractere não numérico' },
  { cep: '', valor: '100', classe: 'CEP vazio' },
  { cep: '80000000', valor: '', classe: 'valor vazio' },
  { cep: '80000000', valor: '0', classe: 'valor igual a zero' },
  { cep: '80000000', valor: '-1', classe: 'valor negativo' },
  { cep: '80000000', valor: '10,999', classe: 'valor com mais de duas casas decimais' },
];

for (const caso of casosInvalidos) {
  test(`frete inválido ${caso.classe}`, async ({ page }) => {
    await page.goto('/frete');
    await page.getByLabel('CEP').fill(caso.cep);
    await page.getByLabel('Valor do pedido').fill(caso.valor);
    await page.getByRole('button', { name: 'Calcular frete' }).click();

    const resultado = page.locator('#resultado');
    await expect(resultado).toHaveText('Dados inválidos');
    await expect(resultado).toHaveAttribute('role', 'alert');
  });
}
