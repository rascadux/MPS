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

        // Crear informe
        await createInforme(page);

        const spanMedico = page.locator('span:has-text("El médico ha anotado:")');
        const spanCreado = page.locator('span:has-text("Creado el:")');
        const spanResultado = page.locator('span:has-text("El resultado de la predicción ha sido:")');

        check(page, {
            'span "El médico ha anotado:" existe': () => spanMedico !== null,
            'span "Creado el:" existe': () => spanCreado !== null,
            'span "El resultado de la predicción ha sido:" existe': () => spanResultado !== null
        });

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
        'redirected to patient page': (p) => p.url().includes('/paciente/')
    });
}

// Función para navegar a la página de detalles de la imagen desde la página del paciente
async function navigateToImageDetailPage(page) {

    await page.waitForSelector('div.detalle-paciente');

    const viewImageButton = page.locator('button[name="view"]:first-of-type');
    await viewImageButton.click();

    check(page, {
        'redirected to image detail page': (p) => p.url().includes('/image-detail/')
    });
}

// Función para crear un informe
async function createInforme(page) {

    const addButton = page.locator('button[name="add"]');

    await addButton.click();

    check(page, {
        'redirected to create informe page': (p) => p.url().includes('/informe/create/')
    });

    // Rellenar el formulario de informe
    const informeTextarea = page.locator('textarea');
    await informeTextarea.type('Informe de prueba end-to-end');

    const saveButton = page.locator('button[name="save"]');
    await Promise.all([page.waitForNavigation(), saveButton.click()]);
}


