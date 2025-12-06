const API_BASE_URL = "http://localhost:8080/api/dishes";

let allDishes = [];
let selectedRowId = null;

function renderDishesTable(dishes) {
    const tbody = document.querySelector("#dishes-table tbody");
    const msg = document.getElementById("list-message");

    tbody.innerHTML = "";

    if (!dishes || dishes.length === 0) {
        msg.textContent = "Блюда не найдены. Попробуйте изменить запрос.";
        return;
    }

    msg.textContent = "Нажми на блюдо в таблице, чтобы увидеть детали.";

    dishes.forEach(dish => {
        const tr = document.createElement("tr");
        tr.dataset.id = dish.id;

        tr.innerHTML = `
            <td>${dish.id}</td>
            <td>${dish.name}</td>
            <td>${dish.type}</td>
            <td>${dish.totalCalories.toFixed(1)}</td>
            <td>${dish.totalCost.toFixed(2)}</td>
        `;

        tr.addEventListener("click", () => {
            highlightRow(dish.id);
            loadDishDetails(dish.id);
        });

        tbody.appendChild(tr);
    });
}

function highlightRow(id) {
    selectedRowId = id;
    const rows = document.querySelectorAll("#dishes-table tbody tr");
    rows.forEach(row => {
        if (Number(row.dataset.id) === id) {
            row.classList.add("selected");
        } else {
            row.classList.remove("selected");
        }
    });
}

async function loadDishes() {
    const msg = document.getElementById("list-message");
    msg.textContent = "Загружаю список блюд...";

    try {
        const response = await fetch(API_BASE_URL);
        if (!response.ok) {
            throw new Error(`Ошибка загрузки: ${response.status}`);
        }
        const dishes = await response.json();
        allDishes = dishes;
        renderDishesTable(allDishes);
    } catch (e) {
        console.error(e);
        msg.textContent = "Ошибка: " + e.message;
    }
}

async function loadDishDetails(id) {
    const detailsDiv = document.getElementById("dish-details");
    detailsDiv.innerHTML = "<p>Загружаю детали блюда...</p>";

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`);
        if (response.status === 404) {
            detailsDiv.innerHTML = "<p>Блюдо не найдено.</p>";
            return;
        }
        if (!response.ok) {
            throw new Error("Ошибка загрузки деталей: " + response.status);
        }

        const dish = await response.json();

        let html = `
            <h3>${dish.name}</h3>
            <p><strong>Тип:</strong> ${dish.type}</p>
            <div class="details-quick">
                <span class="pill pill-strong">Калорийность: ${dish.totalCalories.toFixed(1)} ккал</span>
                <span class="pill pill-success">Стоимость: ${dish.totalCost.toFixed(2)} тг</span>
                <span class="pill">ID: ${dish.id}</span>
            </div>
        `;

        if (dish.ingredients && dish.ingredients.length > 0) {
            html += `
                <h4>Состав блюда</h4>
                <table>
                    <thead>
                        <tr>
                            <th>Ингредиент</th>
                            <th>Масса, г</th>
                            <th>Калории, ккал</th>
                            <th>Стоимость, тг</th>
                        </tr>
                    </thead>
                    <tbody>
            `;

            dish.ingredients.forEach(line => {
                html += `
                    <tr>
                        <td>${line.ingredientName}</td>
                        <td>${line.grams.toFixed(1)}</td>
                        <td>${line.calories.toFixed(1)}</td>
                        <td>${line.cost.toFixed(2)}</td>
                    </tr>
                `;
            });

            html += `
                    </tbody>
                </table>
            `;
        } else {
            html += `<p>Состав для этого блюда не указан.</p>`;
        }

        detailsDiv.innerHTML = html;
    } catch (e) {
        console.error(e);
        detailsDiv.innerHTML = "<p>Ошибка: " + e.message + "</p>";
    }
}

function setupSearch() {
    const input = document.getElementById("search-input");
    input.addEventListener("input", () => {
        const query = input.value.toLowerCase().trim();
        if (!query) {
            renderDishesTable(allDishes);
            if (selectedRowId != null) {
                highlightRow(selectedRowId);
            }
            return;
        }

        const filtered = allDishes.filter(d =>
            d.name.toLowerCase().includes(query)
        );
        renderDishesTable(filtered);
    });
}

document.addEventListener("DOMContentLoaded", () => {
    loadDishes();
    setupSearch();
});
