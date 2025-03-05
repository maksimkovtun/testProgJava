async function loadStores() {
    try {
        const response = await fetch('/store/api/stores');
        if (!response.ok) {
            throw new Error(`Ошибка: ${response.status}`);
        }
        const stores = await response.json();
        let tableBody = document.getElementById('stores-table-body');
        tableBody.innerHTML = '';
        stores.forEach(store => {
            let row = `<tr>
                <td>${store.storeId}</td>
                <td>${store.name}</td>
                <td>${store.address}</td>
                <td><button class="btn btn-sm btn-outline-primary">Просмотреть</button></td>
            </tr>`;
            tableBody.innerHTML += row;
        });
    } catch (error) {
        console.error('Ошибка загрузки магазинов:', error);
    }
}
document.addEventListener("DOMContentLoaded", loadStores);
