document.addEventListener("DOMContentLoaded", () => {
    loadTableData("stores");

    document.querySelectorAll(".nav-link").forEach(link => {
        link.addEventListener("click", (event) => {
            event.preventDefault();
            document.querySelectorAll(".nav-link").forEach(l => l.classList.remove("active"));
            link.classList.add("active");
            const section = link.getAttribute("data-section");
            if (section) {
                loadTableData(section);
            }
        });
    });
});

async function loadTableData(section) {
    let endpoint = "";
    let tableBody = document.getElementById("table-body");
    let tableHeader = document.getElementById("table-header");
    let summaryContainer = document.getElementById("summary-container");

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
            endpoint = "/store/api/stores";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Название магазина</th>
                    <th>Адрес</th>
                    <th></th>
                </tr>`;
            break;
        case "employees":
            endpoint = "/store/api/employees/all";
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
        let positionsResponse = await fetch("/store/api/positions");
        let positionsData = await positionsResponse.json();

        let storesResponse = await fetch("/store/api/stores");
        let storesData = await storesResponse.json();

        let purchasesTypesResponse = await fetch("/store/api/purchase-types");
        let purchasesTypesData = await purchasesTypesResponse.json();

        let employeesResponse = await fetch("/store/api/employees");
        let employeesData = await employeesResponse.json();

        let electronicsProductsResponse = await fetch("/store/api/electronics");
        let electronicsProductsData = await electronicsProductsResponse.json();

        let electronicsTypesResponse = await fetch("/store/api/electronics/types");
        let electronicsTypesData = await electronicsTypesResponse.json();

        let electronicsTypesMap = {};
        electronicsTypesData.forEach(electronicsType => {
            electronicsTypesMap[electronicsType.electronicsTypeId] = electronicsType.name;
        });

        let electronicsProductsMap = {};
        electronicsProductsData.forEach(electronicsProduct => {
            electronicsProductsMap[electronicsProduct.productId] = electronicsProduct.name;
        });

        let employeesMap = {};
        employeesData.forEach(employee => {
            employeesMap[employee.employeeId] = `${employee.firstName} ${employee.lastName}`;
        });

        let purchasesTypesMap = {};
        purchasesTypesData.forEach(purchasesType => {
            purchasesTypesMap[purchasesType.purchaseTypeId] = purchasesType.name;
        });

        let storesMap = {};
        storesData.forEach(store => {
            storesMap[store.storeId] = store.name;
        });

        let positionMap = {};
        positionsData.forEach(position => {
            positionMap[position.positionId] = position.name;
        });

        const response = await fetch(endpoint);
        if (!response.ok) {
            throw new Error(`Ошибка: ${response.status}`);
        }
        const data = await response.json();

        if (section === "stores") {
            data.forEach(store => {
                let row = `<tr>
                    <td>${store.storeId}</td>
                    <td>${store.name}</td>
                    <td>${store.address}</td>
                    <td><button class="btn btn-sm btn-outline-primary">Просмотреть</button></td>
                </tr>`;
                tableBody.innerHTML += row;
            });
        } else if (section === "employees") {
            data.forEach((emp, index) => {
                let bestLabel = "";
                if (index === 0) bestLabel = `<span class="badge bg-primary">Лучший по количеству продаж</span>`;
                else if (index === 1) bestLabel = `<span class="badge bg-warning">Лучший по сумме продаж</span>`;
                else if (index === 2) bestLabel = `<span class="badge bg-info">Лучший по продаже смарт-часов</span>`;

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
        } else if (section === "purchases") {
            const response = await fetch(endpoint);
            if (!response.ok) {
                console.error("Ошибка при загрузке данных о покупках");
                return;
            }
            const purchases = await response.json();

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

            tableBody.innerHTML = '';
            let sortedPurchases = purchases;
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
                tableBody.innerHTML = '';
                sortedPurchases.forEach(purchase => {
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
        } else if (section === "electronics") {
            data.forEach(product => {
                const electronicsTypeName = electronicsTypesMap[product.electronicsTypeId];
                console.log(product);
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
        } else if (section === "references") {
            data.forEach(ref => {
                let row = `<tr>
                    <td>${ref.id}</td>
                    <td>${ref.name}</td>
                    <td>${ref.description}</td>
                    <td><button class="btn btn-sm btn-outline-primary">Просмотреть</button></td>
                </tr>`;
                tableBody.innerHTML += row;
            });
        }
    } catch (error) {
        console.error("Ошибка загрузки данных:", error);
    }
}
