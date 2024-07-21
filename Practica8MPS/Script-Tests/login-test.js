import { browser } from 'k6/experimental/browser';
import { check } from 'k6';

export const options = {
    scenarios: {
        ui: {
            executor: 'shared-iterations',
            options: {
                browser: {
                    type: 'chromium',
                }
            },
            vus: 1,
            iterations: 1,
        },
    },
    thresholds: {
        checks: ["rate==1.0"]
    }
};

export default async function () {
    const page = browser.newPage();
    try {
        await page.goto('http://localhost:4200/');

        page.locator('input[name="nombre"]').type('Manuel');

        page.locator('input[name="DNI"]').type('99999999');

        const loginButton = page.locator('button[name="login"]');

        await Promise.all([page.waitForNavigation(), loginButton.click()]);

        check(page, {
            'Login correcto y redirigido a /home': (p) => p.url().includes('/home'),
        });

    } finally {
        page.close();
    }
}
