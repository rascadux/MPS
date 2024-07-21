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
        // Iniciar sesión
        await login(page);

        const addButton = page.locator('button[name="add"]');
        await Promise.all([page.waitForNavigation(), addButton.click()]);

        check(page, {
            'Redirigido a la pagina /paciente/create': (p) => p.url().includes('/paciente/create'),
        });

        await page.locator('input[name="dni"]').type('22334455');
        await page.locator('input[name="nombre"]').type('Marta');
        await page.locator('input[name="edad"]').type('36');
        await page.locator('input[name="cita"]').type('Mamografía');

        const createButton = page.locator('button[type="submit"]');
        await Promise.all([page.waitForNavigation(), createButton.click()]);

        check(page, {
            'Redirigido de vuelta a /home': (p) => p.url().includes('/home'),
        });

        await checkNewPatientAdded(page);
        
    } finally {
        page.close();
    }
}

// Función para iniciar sesión
async function login(page) {
    await page.goto('http://localhost:4200/');

    await page.locator('input[name="nombre"]').type('Manuel'); // Nombre de usuario
    await page.locator('input[name="DNI"]').type('99999999'); // DNI

    const loginButton = page.locator('button[name="login"]');
    await Promise.all([page.waitForNavigation(), loginButton.click()]);
}

// Función para verificar que el nuevo paciente se ha añadido correctamente a la lista
async function checkNewPatientAdded(page) {
    await page.waitForSelector('table.mat-table');

    const newPatientAdded = await page.evaluate(() => {
        const rows = document.querySelectorAll('table.mat-table tbody tr');
        for (const row of rows) {
            const cells = row.querySelectorAll('td');
            let rowText = '';
            for (const cell of cells) {
                rowText += cell.innerText + ' ';
            }
            if (rowText.includes('Marta') && 
                rowText.includes('22334455')){
                return true;
            }
        }
        return false;
    });

    check(page, {
        'Nuevo paciente añadido': () => newPatientAdded,
    });
}
