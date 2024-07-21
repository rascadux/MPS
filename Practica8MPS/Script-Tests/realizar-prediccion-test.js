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

        // Navegar a la página del paciente desde /home
        await navigateToPatientPage(page);

        // Navegar a la página de detalles de la imagen desde la página del paciente
        await navigateToImageDetailPage(page);

        const predictButton = page.locator('button[name="predict"]');
        await predictButton.click();

        // Esperar a que aparezca el span con name="predict" 10 segundos
        const predictionSpan =  page.waitForSelector('span[name="predict"]', { timeout: 10000 });

        if (predictionSpan) {
            const predictionText =  predictionSpan.textContent();

            check(page, {
                'Predicción visible': () => predictionText.includes('Probabilidad de cáncer:')
            });
        } else {
            console.error("El selector 'span[name=\"predict\"]' no se encontró dentro del tiempo de espera");
        }
    } finally {
        page.close();
    }
}


// Función para iniciar sesión
async function login(page) {

    await page.goto('http://localhost:4200/');

    page.locator('input[name="nombre"]').type('Manuel'); 
    page.locator('input[name="DNI"]').type('99999999');

    const loginButton = page.locator('button[name="login"]');
    await Promise.all([page.waitForNavigation(), loginButton.click()]);
}

// Función para navegar a la página del paciente desde /home
async function navigateToPatientPage(page) {

    await page.waitForSelector('table.mat-table');

    const lastPatientRow = page.locator('table.mat-table tbody tr:last-child');
    await lastPatientRow.click();

    check(page, {
        'Redirigido a la pagina /paciente/': (p) => p.url().includes('/paciente/')
    });
}

// Función para navegar a la página de detalles de la imagen desde la página del paciente
async function navigateToImageDetailPage(page) {

    await page.waitForSelector('div.detalle-paciente');

    const viewImageButton = page.locator('button[name="view"]:first-of-type');
    await viewImageButton.click();

    check(page, {
        'Redirigido a la pagina /image-detail/': (p) => p.url().includes('/image-detail/')
    });
}
