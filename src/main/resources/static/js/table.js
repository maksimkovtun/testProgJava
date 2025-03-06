let currentSection = "stores";

let data = {
    stores: [],
    employees: [],
    purchases: [],
    electronics: [],
    references: []
};

document.addEventListener("DOMContentLoaded", () => {
    loadTableData(currentSection, 1);
    document.querySelectorAll(".nav-link").forEach(link => {
        link.addEventListener("click", (event) => {
            event.preventDefault();
            document.querySelectorAll(".nav-link").forEach(l => l.classList.remove("active"));
            link.classList.add("active");
            const section = link.getAttribute("data-section");
            if (section) {
                currentSection = section;
                currentPage = 1;
                loadTableData(section, currentPage);
            }
        });
    });
});

let currentPage = 1;
const recordsPerPage = 10;
let electronicsTypesMap = {};
let electronicsProductsMap = {};
let employeesMap = {};
let storesMap = {};
let positionMap = {};
let purchasesTypesMap = {};
let summaryContainer = document.getElementById("summary-container");

async function loadTableData(section) {
    let endpoint = "";
    let tableBody = document.getElementById("table-body");
    let tableHeader = document.getElementById("table-header");
    let currentPageElement = document.getElementById("current-page");
    let totalPagesElement = document.getElementById("total-pages");

    const sectionTitles = {
        "stores": "Магазины",
        "employees": "Сотрудники",
        "purchases": "Продажи",
        "electronics": "Электротовары",
        "references": "Справочники"
    };
    document.querySelector("h2").textContent = sectionTitles[section] || "Неизвестный раздел";

    tableBody.innerHTML = "";
    tableHeader.innerHTML = "";
    summaryContainer.innerHTML = "";

    switch (section) {
        case "stores":
            endpoint = `/store/api/stores`;
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Название магазина</th>
                    <th>Адрес</th>
                    <th></th>
                </tr>`;
            break;
        case "employees":
            endpoint = `/store/api/employees/all`;
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Фамилия</th>
                    <th>Отчество</th>
                    <th>Дата рождения</th>
                    <th>Должность</th>
                    <th>Магазин</th>
                    <th>Пол</th>
                    <th></th>
                </tr>`;
            break;
        case "purchases":
            endpoint = "/store/api/purchases/summary";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Продукт</th>
                    <th>Сотрудник</th>
                    <th><span class="sortable" data-sort="purchaseDate">Дата</span></th>
                    <th>Тип оплаты</th>
                    <th>Магазин</th>
                    <th></th>
                </tr>`;
            break;
        case "electronics":
            endpoint = "/store/api/electronics";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Название</th>
                    <th>Тип товара</th>
                    <th>Цена</th>
                    <th>Количество</th>
                    <th>Наличие</th>
                    <th>Описание</th>
                    <th></th>
                </tr>`;
            break;
        case "references":
            endpoint = "/store/api/references";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Название</th>
                    <th>Описание</th>
                    <th></th>
                </tr>`;
            break;
        default:
            console.error("Неизвестный раздел:", section);
            return;
    }

    try {
        const responses = await Promise.all([
            fetch("/store/api/positions"),
            fetch("/store/api/stores"),
            fetch("/store/api/purchase-types"),
            fetch("/store/api/employees"),
            fetch("/store/api/electronics"),
            fetch("/store/api/electronics/types")
        ]);
        const [positionsResponse, storesResponse, purchasesTypesResponse, employeesResponse, electronicsProductsResponse, electronicsTypesResponse] = await responses;
        const positionsData = await positionsResponse.json();
        const storesData = await storesResponse.json();
        const purchasesTypesData = await purchasesTypesResponse.json();
        const employeesData = await employeesResponse.json();
        const electronicsProductsData = await electronicsProductsResponse.json();
        const electronicsTypesData = await electronicsTypesResponse.json();

        purchasesTypesData.forEach(purchasesType => {
            purchasesTypesMap[purchasesType.purchaseTypeId] = purchasesType.name;
        });

        electronicsTypesData.forEach(electronicsType => {
            electronicsTypesMap[electronicsType.electronicsTypeId] = electronicsType.name;
        });

        electronicsProductsData.forEach(electronicsProduct => {
            electronicsProductsMap[electronicsProduct.productId] = electronicsProduct.name;
        });

        employeesData.forEach(employee => {
            employeesMap[employee.employeeId] = `${employee.firstName} ${employee.lastName}`;
        });

        storesData.forEach(store => {
            storesMap[store.storeId] = store.name;
        });

        positionsData.forEach(position => {
            positionMap[position.positionId] = position.name;
        });

        const sectionData = await fetch(endpoint);
        if (!sectionData.ok) {
            throw new Error(`Ошибка: ${sectionData.status}`);
        }
        const sectionDataJson = await sectionData.json();
        data[section] = sectionDataJson;

        const totalPages = Math.ceil(data[section].length / recordsPerPage);
        totalPagesElement.textContent = totalPages || 1;

        renderPage(section, currentPage);

        currentPageElement.textContent = currentPage;
        document.getElementById("prev-page").disabled = currentPage === 1;
        document.getElementById("next-page").disabled = currentPage === totalPages;
    } catch (error) {
        console.error("Ошибка загрузки данных:", error);
    }
}

async function renderPage(section, page) {
    const start = (page - 1) * recordsPerPage;
    const end = start + recordsPerPage;

    const currentData = data[section].slice(start, end);

    let tableBody = document.getElementById("table-body");
    tableBody.innerHTML = "";

    if (section === "stores") {
        currentData.forEach(store => {
            let row = `<tr>
                <td>${store.storeId}</td>
                <td>${store.name}</td>
                <td>${store.address}</td>
                <td><button class="btn btn-sm btn-outline-primary">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    } else if (section === "employees") {
        let bestLabel = "";
        currentData.forEach((emp, index) => {
            if (index === 0 && page === 1) {
                bestLabel = `<span class="badge bg-primary">Лучший по количеству продаж</span>`;
            } else if (index === 1 && page === 1) {
                bestLabel = `<span class="badge bg-warning">Лучший по сумме продаж</span>`;
            } else if (index === 2 && page === 1) {
                bestLabel = `<span class="badge bg-info">Лучший по продаже смарт-часов</span>`;
            } else {
                bestLabel = '';
            }

            let positionName = positionMap[emp.positionId] || "Неизвестная должность";
            let storeName = storesMap[emp.storeId] || "Неизвестный магазин";
            let genderText = emp.gender ? "Муж." : "Жен.";

            let row = `<tr>
                <td>${emp.employeeId}</td>
                <td>${emp.firstName} ${bestLabel}</td>
                <td>${emp.lastName}</td>
                <td>${emp.middleName}</td>
                <td>${emp.birthDate}</td>
                <td>${positionName}</td>
                <td>${storeName}</td>
                <td>${genderText}</td>
                <td><button class="btn btn-sm btn-outline-primary">Детали</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    }

    else if (section === "purchases") {
        const totalCashResponse = await fetch('/store/api/purchases/total-cash');
        const totalCash = await totalCashResponse.json();

        const totalSalesResponse = await fetch('/store/api/purchases/total-sales');
        const totalSales = await totalSalesResponse.json();

        summaryContainer.innerHTML = `
        <div class="alert alert-info">
            <strong>Общая сумма покупок за наличные: </strong>${totalCash} рублей<br>
            <strong>Общая сумма всех покупок: </strong>${totalSales} рублей
        </div>
    `;

        let sortedPurchases = data.purchases.slice();
        let isAscending = true;

        const sortByDate = () => {
            sortedPurchases = sortedPurchases.sort((a, b) => {
                const dateA = new Date(a.purchaseDate);
                const dateB = new Date(b.purchaseDate);
                return isAscending ? dateA - dateB : dateB - dateA;
            });
            renderPurchases();
            isAscending = !isAscending;
        };

        const renderPurchases = () => {
            const start = (currentPage - 1) * recordsPerPage;
            const end = start + recordsPerPage;

            const currentPurchases = sortedPurchases.slice(start, end);

            tableBody.innerHTML = '';

            currentPurchases.forEach(purchase => {
                const purchaseTypeName = purchasesTypesMap[purchase.purchaseTypeId];
                const storeName = storesMap[purchase.storeId];
                const employeeName = employeesMap[purchase.employeeId];
                const electronicsProductName = electronicsProductsMap[purchase.productId];

                let row = `<tr>
                <td>${purchase.purchaseId}</td>
                <td>${electronicsProductName}</td>
                <td>${employeeName}</td>
                <td>${purchase.purchaseDate}</td>
                <td>${purchaseTypeName}</td>
                <td>${storeName}</td>
                <td><button class="btn btn-sm btn-outline-primary">Детали</button></td>
            </tr>`;
                tableBody.innerHTML += row;
            });
        };
        renderPurchases();
        document.querySelector('[data-sort="purchaseDate"]').addEventListener("click", () => {
            sortByDate();
        });
        const totalPages = Math.ceil(sortedPurchases.length / recordsPerPage);
        totalPagesElement.textContent = totalPages || 1;
        document.getElementById("prev-page").disabled = currentPage === 1;
        document.getElementById("next-page").disabled = currentPage === totalPages;
    }

    else if (section === "electronics") {
        currentData.forEach(product => {
            const electronicsTypeName = electronicsTypesMap[product.electronicsTypeId];
            let archivedText = (product.archived === true || product.archived === 1) ? "Нет" : "Есть";

            let row = `<tr>
                <td>${product.productId}</td>
                <td>${product.name}</td>
                <td>${electronicsTypeName}</td>
                <td>${product.price}</td>
                <td>${product.quantity}</td>
                <td>${archivedText}</td>
                <td>${product.description}</td>
                <td><button class="btn btn-sm btn-outline-primary">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    }

    else if (section === "references") {
        currentData.forEach(ref => {
            let row = `<tr>
                <td>${ref.id}</td>
                <td>${ref.name}</td>
                <td>${ref.description}</td>
                <td><button class="btn btn-sm btn-outline-primary">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    }
}

document.getElementById("prev-page").addEventListener("click", () => {
    if (currentPage > 1) {
        currentPage--;
        renderPage(currentSection, currentPage);
        document.getElementById("current-page").textContent = currentPage;
        updatePaginationButtons();
    }
});

document.getElementById("next-page").addEventListener("click", () => {
    const totalPages = Math.ceil(data[currentSection].length / recordsPerPage);
    if (currentPage < totalPages) {
        currentPage++;
        renderPage(currentSection, currentPage);
        document.getElementById("current-page").textContent = currentPage;
        updatePaginationButtons();
    }
});

function updatePaginationButtons() {
    const totalPages = Math.ceil(data[currentSection].length / recordsPerPage);
    document.getElementById("prev-page").disabled = currentPage === 1;
    document.getElementById("next-page").disabled = currentPage === totalPages;
}