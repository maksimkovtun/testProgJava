let currentSection = "stores";

let data = {
    stores: [],
    employees: [],
    purchases: [],
    electronics: [],
    references: [],
    positions: [],
    electronicsTypes: [],
    purchasesTypes: []
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
        "purchases": "Покупки",
        "electronics": "Электротовары",
        "positions": "Должности",
        "electronicsTypes": "Типы электроники",
        "purchasesTypes": "Типы покупок"
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
            addButton(section);
            break;
        case "employees":
            endpoint = `/store/api/employees/sorted-employees`;
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
            addButton(section);
            break;
        case "purchases":
            endpoint = "/store/api/purchases";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Товар</th>
                    <th>Сотрудник</th>
                    <th><span class="sortable" data-sort="purchaseDate">Дата</span></th>
                    <th>Тип оплаты</th>
                    <th>Магазин</th>
                    <th></th>
                </tr>`;
            addButton(section);
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
            addButton(section);
            break;
        case "positions":
            endpoint = "/store/api/positions";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Должность</th>
                    <th></th>
                </tr>`;
            addButton(section);
            break;
        case "electronicsTypes":
            endpoint = "/store/api/electronics/types";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Тип</th>
                    <th></th>
                </tr>`;
            addButton(section);
            break;
        case "purchasesTypes":
            endpoint = "/store/api/purchases/types";
            tableHeader.innerHTML = `
                <tr>
                    <th>ID</th>
                    <th>Тип</th>
                    <th></th>
                </tr>`;
            addButton(section);
            break;
        default:
            console.error("Неизвестный раздел:", section);
            return;
    }
    try {
        const responses = await Promise.all([
            fetch("/store/api/positions"),
            fetch("/store/api/stores"),
            fetch("/store/api/purchases/types"),
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
            employeesMap[employee.employeeId] = `${employee.lastName} ${employee.firstName} ${employee.middleName}`;
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

        data[section] = await sectionData.json();

        const totalPages = Math.ceil(data[section].length / recordsPerPage);
        totalPagesElement.textContent = totalPages || 1;

        currentPageElement.textContent = currentPage;

        renderPage(section, currentPage);

    } catch (error) {
        console.error("Ошибка загрузки данных:", error);
    }
}

async function renderPage(section, page) {
    const start = (page - 1) * recordsPerPage;
    const end = start + recordsPerPage;
    currentSection = section;

    const currentData = data[section].slice(start, end);

    let tableBody = document.getElementById("table-body");
    tableBody.innerHTML = "";

    if (section === "stores") {
        currentData.forEach(store => {
            let row = `<tr>
                <td>${store.storeId}</td>
                <td>${store.name}</td>
                <td>${store.address}</td>
                <td><button class="btn btn-sm btn-outline-primary view-details" data-id="${store.storeId}">Просмотреть</button></td>
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
                <td><button class="btn btn-sm btn-outline-primary view-details" data-id="${emp.employeeId}">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    } else if (section === "purchases") {
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
                <td><button class="btn btn-sm btn-outline-primary view-details" data-id="${purchase.purchaseId}">Просмотреть</button></td>
            </tr>`;
                tableBody.innerHTML += row;
            });
        };
        renderPurchases();
        document.querySelector('[data-sort="purchaseDate"]').addEventListener("click", () => {
            sortByDate();
        });
    } else if (section === "electronics") {
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
                <td><button class="btn btn-sm btn-outline-primary view-details" data-id="${product.productId}">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    } else if (section === "positions") {
        currentData.forEach(position => {
            let row = `<tr>
                <td>${position.positionId}</td>
                <td>${position.name}</td>
                <td><button class="btn btn-sm btn-outline-primary view-details" data-id="${position.positionId}">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    } else if (section === "electronicsTypes") {
        currentData.forEach(et => {
            let row = `<tr>
                <td>${et.electronicsTypeId}</td>
                <td>${et.name}</td>
                <td><button class="btn btn-sm btn-outline-primary view-details" data-id="${et.electronicsTypeId}">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    } else if (section === "purchasesTypes") {
        currentData.forEach(pt => {
            let row = `<tr>
                <td>${pt.purchaseTypeId}</td>
                <td>${pt.name}</td>
                <td><button class="btn btn-sm btn-outline-primary view-details" data-id="${pt.purchaseTypeId}">Просмотреть</button></td>
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

function addButton(section) {
    const addContainer = document.getElementById('add-container');
    const existingButton = addContainer.querySelector('button');
    if (existingButton) {
        existingButton.remove();
    }
    const addButton = document.createElement('button');
    addButton.classList.add("btn", "btn-sm", "btn-outline-primary");
    addButton.textContent = 'Добавить';
    addButton.addEventListener('click', () => {
        openAddForm(section);
    });
    addContainer.appendChild(addButton);
}

function openUpdateForm(title, content, id) {
    let modalContainer = document.getElementById("update-container");

    if (!modalContainer) {
        console.error("Не найден контейнер для модального окна!");
        return;
    }

    modalContainer.innerHTML = `
        <div class="modal fade" id="updateModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${title}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                    </div>
                    <div class="modal-body">${content}</div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" id="update-btn" data-id="${id}">Обновить</button>
                        <button type="button" class="btn btn-danger" id="delete-btn" data-id="${id}">Удалить</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    let modal = new bootstrap.Modal(document.getElementById("updateModal"));
    modal.show();

    document.getElementById("update-btn").addEventListener("click", updateRecord);
    document.getElementById("delete-btn").addEventListener("click", function(event) {
        const id = event.target.dataset.id;
        deleteRecord(id);
    });

}

document.addEventListener("click", async (event) => {
    if (event.target.classList.contains("view-details")) {
        const id = event.target.dataset.id;
        let details = "";

        if (currentSection === "stores") {
            const store = data.stores.find(s => s.storeId == id);
            details = `<p><strong>Название:</strong> <input type="text" id="edit-name" value="${store.name}"></p>
                       <p><strong>Адрес:</strong> <input type="text" id="edit-address" value="${store.address}"></p>`;
        } else if (currentSection === "employees") {
            const emp = data.employees.find(e => e.employeeId == id);
            details = `
                      <p><strong>ФИО:</strong> <input type="text" id="edit-fio" value="${emp.lastName} ${emp.firstName} ${emp.middleName}"></p>
                      <p><strong>Должность:</strong> <input type="text" id="edit-position" value="${positionMap[emp.positionId]}"></p>
                      <p><strong>Магазин:</strong> <input type="text" id="edit-store" value="${storesMap[emp.storeId]}"></p>
                      <p><strong>Дата рождения:</strong> <input type="date" id="edit-birthdate" value="${emp.birthDate}"></p>
                      <p><strong>Пол:</strong>
                          <select id="edit-gender">
                              <option value="1" ${emp.gender ? 'selected' : ''}>Муж.</option>
                              <option value="0" ${!emp.gender ? 'selected' : ''}>Жен.</option>
                          </select>
                      </p>`;

        } else if (currentSection === "purchases") {
            const purchase = data.purchases.find(p => p.purchaseId == id);
            details = `<p><strong>Товар:</strong> <input type="text" id="edit-product" value="${electronicsProductsMap[purchase.productId]}"></p>
                       <p><strong>Сотрудник:</strong> <input type="text" id="edit-employee" value="${employeesMap[purchase.employeeId]}"></p>
                       <p><strong>Дата:</strong> <input type="date" id="edit-date" value="${purchase.purchaseDate}"></p>
                       <p><strong>Тип:</strong> <input type="text" id="edit-type" value="${purchasesTypesMap[purchase.purchaseTypeId]}"></p>
                       <p><strong>Магазин:</strong> <input type="text" id="edit-store" value="${storesMap[purchase.storeId]}"></p>`;
        } else if (currentSection === "electronics") {
            const electronic = data.electronics.find(e => e.productId == id);
            details = `<p><strong>Товар:</strong> <input type="text" id="edit-product" value="${electronic.name}"></p>
                       <p><strong>Тип:</strong> <input type="text" id="edit-type" value="${electronicsTypesMap[electronic.electronicsTypeId]}"></p>
                       <p><strong>Цена:</strong> <input type="number" id="edit-price" value="${electronic.price}"></p>
                       <p><strong>Количество:</strong> <input type="number" id="edit-quantity" value="${electronic.quantity}"></p>
                       <p><strong>Архивирован:</strong> <input type="text" id="edit-archived" value="${electronic.archived ? "Нет" : "Есть"}"></p>
                       <p><strong>Описание:</strong> <input type="text" id="edit-description" value="${electronic.description}"></p>`;
        } else if (currentSection === "positions") {
            const position = data.positions.find(p => p.positionId == id);
            details = `<p><strong>Должность:</strong> <input type="text" id="edit-position" value="${position.name}"></p>`;
        } else if (currentSection === "electronicsTypes") {
            const et = data.electronicsTypes.find(e => e.electronicsTypeId == id);
            details = `<p><strong>Тип электроники:</strong> <input type="text" id="edit-type" value="${et.name}"></p>`;
        } else if (currentSection === "purchasesTypes") {
            const pt = data.purchasesTypes.find(p => p.purchaseTypeId == id);
            details = `<p><strong>Тип покупки:</strong> <input type="text" id="edit-type" value="${pt.name}"></p>`;
        }

        openUpdateForm("Редактирование записи", details, id);
    }
});


async function updateRecord() {
    const id = this.dataset.id;
    let updatedData = {};

    if (currentSection === "stores") {
        updatedData = {
            name: document.getElementById("edit-name").value,
            address: document.getElementById("edit-address").value
        };
        await updateFetch(`store/api/stores/update/${id}`, updatedData);
    } else if (currentSection === "employees") {
        const fioParts = document.getElementById("edit-fio").value.split(" ");
        const positionName = document.getElementById("edit-position").value.trim();
        const storeName = document.getElementById("edit-store").value.trim();
        const gender = document.getElementById("edit-gender").value === "1";

        updatedData = {
            lastName: fioParts[0],
            firstName: fioParts[1] || "",
            middleName: fioParts[2] || "",
            positionId: Number(Object.keys(positionMap).find(key => positionMap[key] === positionName)),
            storeId: Number(Object.keys(storesMap).find(key => storesMap[key] === storeName)),
            birthDate: document.getElementById("edit-birthdate").value,
            gender: gender
        };
        console.log("Отправка данных:", updatedData);

        await updateFetch(`store/api/employees/update/${id}`, updatedData);
    } else if (currentSection === "purchases") {
        updatedData = {
            productId: Number(Object.keys(electronicsProductsMap).find(key => electronicsProductsMap[key] === document.getElementById("edit-product").value)),
            employeeId: Number(Object.keys(employeesMap).find(key => employeesMap[key] === document.getElementById("edit-employee").value)),
            purchaseDate: document.getElementById("edit-date").value,
            purchaseTypeId: Number(Object.keys(purchasesTypesMap).find(key => purchasesTypesMap[key] === document.getElementById("edit-type").value)),
            storeId: Number(Object.keys(storesMap).find(key => storesMap[key] === document.getElementById("edit-store").value))
        };
        await updateFetch(`store/api/purchases/update/${id}`, updatedData);
    } else if (currentSection === "electronics") {
        updatedData = {
            name: document.getElementById("edit-product").value,
            electronicsTypeId: Number(Object.keys(electronicsTypesMap).find(key => electronicsTypesMap[key] === document.getElementById("edit-type").value)),
            price: document.getElementById("edit-price").value,
            quantity: document.getElementById("edit-quantity").value,
            archived: document.getElementById("edit-archived").value !== "Нет",
            description: document.getElementById("edit-description").value
        };
        await updateFetch(`store/api/electronics/update/${id}`, updatedData);
    } else if (currentSection === "positions") {
        updatedData = {
            name: document.getElementById("edit-position").value
        };
        await updateFetch(`store/api/positions/update/${id}`, updatedData);
    } else if (currentSection === "electronicsTypes") {
        updatedData = {
            name: document.getElementById("edit-type").value
        };
        await updateFetch(`store/api/electronics/types/update/${id}`, updatedData);
    } else if (currentSection === "purchasesTypes") {
        updatedData = {
            name: document.getElementById("edit-type").value
        };
        await updateFetch(`store/api/purchases/types/update/${id}`, updatedData);
    }
}

async function updateFetch(link, data) {
    try {
        const response = await fetch(link, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });
        if (response.ok) {
            alert("Запись обновлена!");
            const modal = bootstrap.Modal.getInstance(document.getElementById("updateModal"));
            modal.hide();
            document.getElementById("updateModal").remove();
            renderPage(currentSection, 1);
        } else {
            const errorResponse = await response.json();
            if (errorResponse && errorResponse.productId === undefined) {
                alert("Ошибка при обновлении!");
            } else {
                alert("Ошибка: товар отсутствует в наличии или не найден.");
            }
        }
    } catch (error) {
        console.error("Ошибка:", error);
        alert("Ошибка при отправке запроса.");
    }
}

async function deleteRecord(id) {
    try {
        let response;

        if (currentSection === "stores") {
            response = await fetch(`store/api/stores/delete/${id}`, {
                method: "DELETE"
            });
        } else if (currentSection === "employees") {
            response = await fetch(`store/api/employees/delete/${id}`, {
                method: "DELETE"
            });
        } else if (currentSection === "purchases") {
            response = await fetch(`store/api/purchases/delete/${id}`, {
                method: "DELETE"
            });
        } else if (currentSection === "electronics") {
            response = await fetch(`store/api/electronics/delete/${id}`, {
                method: "DELETE"
            });
        } else if (currentSection === "positions") {
            response = await fetch(`store/api/positions/delete/${id}`, {
                method: "DELETE"
            });
        } else if (currentSection === "electronicsTypes") {
            response = await fetch(`store/api/electronics/types/delete/${id}`, {
                method: "DELETE"
            });
        } else if (currentSection === "purchasesTypes") {
            response = await fetch(`store/api/purchases/types/delete/${id}`, {
                method: "DELETE"
            });
        }

        if (response.ok) {
            alert("Запись удалена!");
            const modal = bootstrap.Modal.getInstance(document.getElementById("updateModal"));
            modal.hide();
            document.getElementById("updateModal").remove();

            renderPage(currentSection, 1);
        } else {
            alert("Ошибка при удалении!");
        }
    } catch (error) {
        console.error("Ошибка:", error);
    }
}

function openAddForm(section) {
    let formHtml = '';
    const closeButton = `<button id="closeModal" class="btn btn-sm btn-outline-danger" style="margin-left: 10px;">&times;</button>`;

    switch (section) {
        case 'stores':
            formHtml = `
                <div>
                    <label for="storeName">Название магазина:</label>
                    <input type="text" id="storeName" class="form-control">
                </div>
                <div>
                    <label for="storeAddress">Адрес магазина:</label>
                    <input type="text" id="storeAddress" class="form-control">
                </div>
                <button id="saveStore" class="btn btn-sm btn-outline-primary">Сохранить</button>
            `;
            break;
        case 'employees':
            formHtml = `
                <div>
                    <label for="lastName">Фамилия:</label>
                    <input type="text" id="lastName" class="form-control">
                </div>
                <div>
                    <label for="firstName">Имя:</label>
                    <input type="text" id="firstName" class="form-control">
                </div>
                <div>
                    <label for="middleName">Отчество:</label>
                    <input type="text" id="middleName" class="form-control">
                </div>
                <div>
                    <label for="birthDay">День рождения:</label>
                    <input type="text" id="birthDay" class="form-control">
                </div>
                <div>
                    <label for="positionName">Должность:</label>
                    <input type="text" id="positionName" class="form-control">
                </div>
                <div>
                    <label for="storeName">Магазин:</label>
                    <input type="text" id="storeName" class="form-control">
                </div>
                <div>
                    <label for="male">Мужчина</label>
                    <input type="radio" id="male" name="gender" value="male">
                    <label for="female">Женщина</label>
                    <input type="radio" id="female" name="gender" value="female">
                </div>
                <button id="saveEmployee" class="btn btn-sm btn-outline-primary">Сохранить</button>
            `;
            break;
        case "purchases":
            formHtml = `
                <div>
                    <label for="electronicName">Товар:</label>
                    <input type="text" id="electronicName" class="form-control">
                </div>
                <div>
                    <label for="employeeName">Сотрудник:</label>
                    <input type="text" id="employeeName" class="form-control">
                </div>
                <div>
                    <label for="purchaseDate">Дата:</label>
                    <input type="text" id="purchaseDate" class="form-control">
                </div>
                <div>
                    <label for="purchaseType">Тип оплаты:</label>
                    <input type="text" id="purchaseType" class="form-control">
                </div>
                <div>
                    <label for="storeName">Магазин:</label>
                    <input type="text" id="storeName" class="form-control">
                </div>
                <button id="savePurchase" class="btn btn-sm btn-outline-primary">Сохранить</button>
            `;
            break;
        case "electronics":
            formHtml = `
                <div>
                    <label for="electronicName">Название:</label>
                    <input type="text" id="electronicName" class="form-control">
                </div>
                <div>
                    <label for="electronicType">Тип товара:</label>
                    <input type="text" id="electronicType" class="form-control">
                </div>
                <div>
                    <label for="electronicPrice">Цена:</label>
                    <input type="text" id="electronicPrice" class="form-control">
                </div>
                <div>
                    <label for="quantity">Количество:</label>
                    <input type="text" id="quantity" class="form-control">
                </div>
                <div>
                    <label for="isArchived">Наличие:</label>
                    <input type="text" id="isArchived" class="form-control">
                </div>
                <div>
                    <label for="description">Описание:</label>
                    <input type="text" id="description" class="form-control">
                </div>
                <button id="saveElectronic" class="btn btn-sm btn-outline-primary">Сохранить</button>
            `;
            break;
        case "positions":
            formHtml = `
                <div>
                    <label for="position">Должность:</label>
                    <input type="text" id="position" class="form-control">
                </div>
                <button id="savePosition" class="btn btn-sm btn-outline-primary">Сохранить</button>
            `;
            break;
        case "electronicsTypes":
            formHtml = `
                <div>
                    <label for="electronicsType">Тип:</label>
                    <input type="text" id="electronicsType" class="form-control">
                </div>
                <button id="saveElectronicsType" class="btn btn-sm btn-outline-primary">Сохранить</button>
            `;
            break;
        case "purchasesTypes":
            formHtml = `
                <div>
                    <label for="purchasesType">Тип:</label>
                    <input type="text" id="purchasesType" class="form-control">
                </div>
                <button id="savePurchasesType" class="btn btn-sm btn-outline-primary">Сохранить</button>
            `;
            break;
        default:
            console.error('Неизвестный раздел:', section);
            return;
    }

    const formContainer = document.getElementById('add-container');
    const addButton = document.querySelector('#add-container > button');
    formContainer.innerHTML = formHtml;

    formContainer.innerHTML = `<div style="position: relative;">${closeButton}${formHtml}</div>`;

    formContainer.querySelector('#closeModal')?.addEventListener('click', () => {
        const formElements = formContainer.querySelectorAll('div, input, button');
        formElements.forEach(element => {
            if (element.id !== 'addFormButton' && element.id !== 'closeModal') {
                element.remove();
            }
        });
        formContainer.appendChild(addButton);
    });

    if (section === 'stores') {
        addStoreEventListener();
    }else if(section === 'employees'){
        addEmployeeEventListener();
    }else if(section === 'purchases'){
        addPurchaseEventListener();
    }else if(section === 'electronics'){
        addElectronicEventListener();
    }else if(section === 'positions'){
        addPositionEventListener();
    }else if(section === 'electronicsTypes'){
        addElectronicsTypeEventListener();
    }else if(section === 'purchasesTypes'){
        addPurchasesTypeEventListener()
    }
}

function addStoreEventListener() {
    document.getElementById('saveStore').addEventListener('click', () => {
        const storeName = document.getElementById('storeName').value;
        const storeAddress = document.getElementById('storeAddress').value;
        saveData('/store/api/stores/add', { name: storeName, address: storeAddress });
    });
}

function addEmployeeEventListener() {
    document.getElementById('saveEmployee').addEventListener('click', () => {
        const genderRadio = document.querySelector('input[name="gender"]:checked');
        const gender = genderRadio ? genderRadio.value === 'male' : null;
        const positionsName = document.getElementById('positionName').value;
        const storesName = document.getElementById('storeName').value;
        let positionId = Number(Object.keys(positionMap).find(key => positionMap[key] === positionsName.trim()));
        let storeId = Number(Object.keys(storesMap).find(key => storesMap[key] === storesName.trim()));

        if (positionId === undefined) {
            console.error(`Должность "${positionsName}" не найдена в базе.`);
            alert(`Должность "${positionsName}" не найдена в базе.`);
            return;
        }
        if (storeId === undefined) {
            console.error(`Магазин "${storesName}" не найден в базе.`);
            alert(`Магазин "${storesName}" не найден в базе.`);
            return;
        }

        const employeeData = {
            lastName: document.getElementById('lastName').value,
            firstName: document.getElementById('firstName').value,
            middleName: document.getElementById('middleName').value,
            birthDay: document.getElementById('birthDay').value,
            positionId: positionId,
            storeId: storeId,
            gender: gender
        };
        console.log(employeeData);

        saveData('/store/api/employees/add', employeeData);
    });
}

function addPurchaseEventListener() {
    document.getElementById('savePurchase').addEventListener('click', () => {
        const productName = document.getElementById('electronicName').value;
        const employeeName = document.getElementById('employeeName').value;
        const storeName = document.getElementById('storeName').value;
        const purchaseType = document.getElementById('purchaseType').value;

        let productId = Number(Object.keys(electronicsProductsMap).find(key => electronicsProductsMap[key] === productName.trim()));
        let employeeId = Number(Object.keys(employeesMap).find(key => employeesMap[key] === employeeName.trim()));
        let storeId = Number(Object.keys(storesMap).find(key => storesMap[key] === storeName.trim()));
        let purchaseTypeId = Number(Object.keys(purchasesTypesMap).find(key => purchasesTypesMap[key] === purchaseType.trim()));

        if (productId === undefined) {
            console.error(`Электроника "${productName}" не найдена в базе.`);
            alert(`Электроника "${productName}" не найдена в базе.`);
            return;
        }
        if (employeeId === undefined) {
            console.error(`Сотрудник "${employeeName}" не найден в базе.`);
            alert(`Сотрудник "${employeeName}" не найден в базе.`);
            return;
        }
        if (storeId === undefined) {
            console.error(`Магазин "${storeName}" не найден в базе.`);
            alert(`Магазин "${storeName}" не найден в базе.`);
            return;
        }
        if (purchaseTypeId === undefined) {
            console.error(`Тип оплаты "${purchaseType}" не найден в базе.`);
            alert(`Тип оплаты "${purchaseType}" не найден в базе.`);
            return;
        }

        const purchaseData = {
            productId: productId,
            employeeId: employeeId,
            purchaseDate: document.getElementById('purchaseDate').value,
            purchaseTypeId: purchaseTypeId,
            storeId: storeId
        };

        saveData('/store/api/purchases/add', purchaseData)
            .then(response => {
                if (response && response.ok) {
                    alert('Покупка успешно добавлена!');
                } else {
                    alert('Ошибка при добавлении покупки. Товар отсутствует.');
                }
            })
            .catch(error => {
                const errorMessage = error.response ? error.response.data : error.message;
                alert(errorMessage);
            });

    });
}

function addElectronicEventListener() {
    document.getElementById('saveElectronic').addEventListener('click', () => {
        const electronicTypeName = document.getElementById('electronicType').value;
        const isArchived = document.getElementById('isArchived').value === 'true';
        const price = Number(document.getElementById('electronicPrice').value);
        const quantity = Number(document.getElementById('quantity').value);

        let electronicsTypeId = Number(Object.keys(electronicsTypesMap).find(key => electronicsTypesMap[key] === electronicTypeName.trim()));
        if (electronicsTypeId === undefined) {
            console.error(`Тип электроники "${electronicTypeName}" не найден в базе.`);
            alert(`Тип электроники "${electronicTypeName}" не найден в базе.`);
            return;
        }

        const electronicData = {
            name: document.getElementById('electronicName').value,
            electronicsTypeId: electronicsTypeId,
            price: price,
            quantity: quantity,
            archived: isArchived,
            description: document.getElementById('description').value
        };

        saveData('/store/api/electronics/add', electronicData);
    });
}


function addPositionEventListener() {
    document.getElementById('savePosition').addEventListener('click', () => {
        const positionData = {
            name: document.getElementById('position').value
        };
        saveData('/store/api/positions/add', positionData);
    });
}

function addElectronicsTypeEventListener() {
    document.getElementById('saveElectronicsType').addEventListener('click', () => {
        const electronicsTypeData = {
            name: document.getElementById('electronicsType').value
        };
        saveData('/store/api/electronics/types/add', electronicsTypeData);
    });
}

function addPurchasesTypeEventListener() {
    document.getElementById('savePurchasesType').addEventListener('click', () => {
        const purchasesTypeData = {
            name: document.getElementById('purchasesType').value
        };
        saveData('/store/api/purchases/types/add', purchasesTypeData);
    });
}


async function saveData(url, data) {
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('Данные успешно добавлены!');
            await renderPage(currentSection, 1);
        } else {
            console.error('Ошибка сохранения данных:', response.statusText);
        }
    } catch (error) {
        console.error('Ошибка при отправке данных:', error);
    }
}